# cordova-plugin-transparent-navigationbar

This plugin allows you to make the navigation bar transparent and overlay the Cordova's webview. Works on Android only.

## Installation

    cordova plugin add cordova-plugin-transparent-navigationbar


## Usage

You can use this plugin by either the `preference` tag in your `config.xml` file or by calling it programmatically during runtime.

Please note that when setting the navigation bar transparent, it will overlap Cordova's webview, which may cause your app to be displayed partly underneath the navigation bar.

To avoid any issues where UI elements are blocked by the navigation bar, it is recommended to set some padding to the bottom of your app.

-----------


Preferences
-------

Add the following in your `config.xml` file

__TransparentNavigationBar__<br>
Makes the navigation bar transparent<br>
Boolean, default value: `false`

```xml
<preference name="TransparentNavigationBar" value="true" />
```
        

__NavigationBarLight__<br>
Makes the navigation bar buttons either dark or light<br>
String, either `dark` or `light`, default value: `dark`

```xml
<preference name="TransparentNavigationBarButtonsColor" value="dark" />
```

Programatically
-------
When running in Cordova, this plugins exposes the global object `TransparentNavigationBar` in the `window` object

Please note that this object will only be available after the `deviceready` event has been fired

```js
function handleDeviceReady() {
  console.log(window.NavigationBar);
}

document.addEventListener('deviceready', handleDeviceReady, false);
```

### TransparentNavigationBar.setNavigationBarTransparent

Makes the navigation bar transparent<br>

```js
TransparentNavigationBar.setNavigationBarTransparent();
```

### TransparentNavigationBar.setNavigationBarButtonsColor

Makes the navigation bar buttons either dark or light<br>
Accepts either `dark` or `light`

```js
TransparentNavigationBar.setNavigationBarButtonsColor('dark');
```

