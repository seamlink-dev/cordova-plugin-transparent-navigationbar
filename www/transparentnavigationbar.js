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

/** global cordova */

var exec = require('cordova/exec');

var TransparentNavigationBar = {
    setNavigationBarTransparent: function () {
        exec(null, null, "TransparentNavigationBar", "setNavigationBarTransparent");
    },
    setNavigationBarButtonsColor: function (color) {
        exec(null, null, 'TransparentNavigationBar', 'setNavigationBarButtonsColor', [color]);
    }
};

function prepareProxy() {
    window.setTimeout(function () {
        exec(function (res) {
            if (typeof res == 'object') {
                if (res.type == 'tap') {
                    cordova.fireWindowEvent('navigationTap');
                }
            } else {
                NavigationBar.isVisible = res;
            }
        }, null, "NavigationBar", "_ready", []);
    }, 0);
}

prepareProxy();

module.exports = TransparentNavigationBar;