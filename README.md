![Banner](img/banner.png?raw=true)
=====

This repo contains Cerberus Base UX which is a collection of reversed & reworked Sesl (Samsung Experience Software Library) & general OneUI View, Widgets, Drawables etc.

Please notice the app needs external edits to the following files in order to enable the "parentIsDeviceDefault" bool in frameworks (this one enables some little UI elements like ToolbarPopup & EdgeEffect):

> /res/values/styles.xml
> /res/values-v25/styles.xml


## App contains as per sources:
* minSdk 24, targetSdk 29
* ProGuard & Minify enabled
* Android Jetpack (androidx.*) & Google Material libs
* Samsung Manifest flags (SamsungBasicInteraction, Icon Container etc)
* Base Activity classes ready to use
* Android 10 Sesl xml elements (anim, drawables, styles, colors, dimens)
* Android 10 Sesl AppBar/Toolbar (managed with ActionBarUtils class)
* Android 10 Sesl Color Picker (+ custom Preference)
* Android 9 Sesl Preference (+ Android 10 layout & custom implementations)
* Android 9 Sesl RecyclerView
* Android 9 Sesl TabLayout
* Android 9/10 Sesl Views (SeslProgressBar, SwitchCompat, SeekBar etc)

