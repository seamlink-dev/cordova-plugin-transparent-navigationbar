/* 
    Copyright 2020 Rodrigo Cavanha

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.rdcavanha.cordova.plugin.transparentnavigationbar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONException;

import java.io.InvalidObjectException;

public class TransparentNavigationBar extends CordovaPlugin {
    private static final String TAG = "TransparentNavigationBar";

    private final String PREFERENCES_TRANSPARENT_NAVIGATION_BAR = "TransparentNavigationBar";
    private final String PREFERENCES_NAVIGATION_BAR_BUTTONS_COLOR = "TransparentNavigationBarButtonsColor";

    @Override
    public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
        LOG.v(TAG, "Initializing TransparentNavigationBar");
        super.initialize(cordova, webView);

        this.cordova.getActivity().runOnUiThread(() -> {
            if (preferences.getBoolean(PREFERENCES_TRANSPARENT_NAVIGATION_BAR, false)) {
                setNavigationBarTransparent();
            }
            if (preferences.contains(PREFERENCES_NAVIGATION_BAR_BUTTONS_COLOR)) {
                String color = preferences.getString(PREFERENCES_NAVIGATION_BAR_BUTTONS_COLOR, "dark");
                try {
                    setNavigationBarButtonsColor(color);
                } catch (InvalidObjectException e) {
                    LOG.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean execute(final String action, final CordovaArgs args, final CallbackContext callbackContext) {
        LOG.v(TAG, "Executing: " + action);
        final Activity activity = this.cordova.getActivity();
        final Window window = activity.getWindow();

        if ("_ready".equals(action)) {
            boolean navigationBarVisible = (window.getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0;
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, navigationBarVisible));
            return true;
        }

        if ("setNavigationBarTransparent".equals(action)) {
            this.cordova.getActivity().runOnUiThread(this::setNavigationBarTransparent);
            return true;
        }

        if ("setNavigationBarButtonsColor".equals(action)) {
            this.cordova.getActivity().runOnUiThread(() -> {
                try {
                    setNavigationBarButtonsColor(args.getString(0));
                } catch (JSONException | InvalidObjectException e) {
                    LOG.e(TAG, e.getMessage());
                }
            });
            return true;
        }

        return false;
    }

    private Window getWindow() {
        return cordova.getActivity().getWindow();
    }

    private void setNavigationBarButtonsColor(String color) throws InvalidObjectException {
        final int RETRO_COMPATIBLE_SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 0x10;

        View decorView = this.getWindow().getDecorView();
        int uiOptions = decorView.getSystemUiVisibility();

        if (color.equals("dark")) {
            decorView.setSystemUiVisibility(uiOptions | RETRO_COMPATIBLE_SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else if (color.equals("light")) {
            decorView.setSystemUiVisibility(uiOptions & ~RETRO_COMPATIBLE_SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            throw new InvalidObjectException("NavigationBarButtonsColor accepts either 'dark' or 'light'");
        }
    }

    private void setNavigationBarTransparent() {
        final int RETRO_COMPATIBLE_FLAG_TRANSLUCENT_NAVIGATION = 0x08000000;
        final int RETRO_COMPATIBLE_FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = 0x80000000;

        Window window = this.getWindow();
        window.addFlags(RETRO_COMPATIBLE_FLAG_TRANSLUCENT_NAVIGATION);
        window.addFlags(RETRO_COMPATIBLE_FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        try {
            window.getClass().getDeclaredMethod("setNavigationBarColor", int.class).invoke(window, Color.TRANSPARENT);
        } catch (Exception ignore) {
            LOG.e(TAG, "Failed to execute window.setNavigationBarColor on SDK: " + Build.VERSION.SDK_INT);
        }
    }
}
