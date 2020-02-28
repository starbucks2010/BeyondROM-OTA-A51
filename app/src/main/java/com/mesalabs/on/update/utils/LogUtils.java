package com.mesalabs.on.update.utils;

import android.util.Log;

import com.mesalabs.on.update.OnUpdateApp;

/*
 * On Update
 *
 * Coded by BlackMesa @2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */

public class LogUtils {
    // Verbose
    public static void v(String tag, String msg) {
        if (OnUpdateApp.isDebugBuild())
            Log.v("OnUpdate: " + tag, msg);
    }

    // Debug
    public static void d(String tag, String msg) {
        if (OnUpdateApp.isDebugBuild())
            Log.d("OnUpdate: " + tag, msg);
    }

    public static void d(String tag, String msg, Exception e) {
        if (OnUpdateApp.isDebugBuild())
            Log.d("OnUpdate: " + tag, msg, e);
    }

    public static void d(String tag, String msg, Throwable t) {
        if (OnUpdateApp.isDebugBuild())
            Log.d("OnUpdate: " + tag, msg, t);
    }

    // Info
    public static void i(String tag, String msg) {
        if (OnUpdateApp.isDebugBuild())
            Log.i("OnUpdate: " + tag, msg);
    }

    public static void i(String tag, String msg, Exception e) {
        if (OnUpdateApp.isDebugBuild())
            Log.i("OnUpdate: " + tag, msg, e);
    }

    // Warn
    public static void w(String tag, String msg) {
        if (OnUpdateApp.isDebugBuild())
            Log.w("OnUpdate: " + tag, msg);
    }

    public static void w(String tag, String msg, Exception e) {
        if (OnUpdateApp.isDebugBuild())
            Log.w("OnUpdate: " + tag, msg, e);
    }

    // Error
    public static void e(String tag, String msg) {
        if (OnUpdateApp.isDebugBuild())
            Log.e("OnUpdate: " + tag, msg);
    }
}
