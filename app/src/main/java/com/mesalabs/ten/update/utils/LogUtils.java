package com.mesalabs.ten.update.utils;

import android.util.Log;

import com.mesalabs.ten.update.TenUpdateApp;

/*
 * 십 Update
 *
 * Coded by BlackMesa123 @2021
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
        if (TenUpdateApp.isDebugBuild())
            Log.v("십 Update: " + tag, msg);
    }

    // Debug
    public static void d(String tag, String msg) {
        if (TenUpdateApp.isDebugBuild())
            Log.d("십 Update: " + tag, msg);
    }

    public static void d(String tag, String msg, Exception e) {
        if (TenUpdateApp.isDebugBuild())
            Log.d("십 Update: " + tag, msg, e);
    }

    public static void d(String tag, String msg, Throwable t) {
        if (TenUpdateApp.isDebugBuild())
            Log.d("십 Update: " + tag, msg, t);
    }

    // Info
    public static void i(String tag, String msg) {
        if (TenUpdateApp.isDebugBuild())
            Log.i("십 Update: " + tag, msg);
    }

    public static void i(String tag, String msg, Exception e) {
        if (TenUpdateApp.isDebugBuild())
            Log.i("십 Update: " + tag, msg, e);
    }

    // Warn
    public static void w(String tag, String msg) {
        if (TenUpdateApp.isDebugBuild())
            Log.w("십 Update: " + tag, msg);
    }

    public static void w(String tag, String msg, Exception e) {
        if (TenUpdateApp.isDebugBuild())
            Log.w("십 Update: " + tag, msg, e);
    }

    // Error
    public static void e(String tag, String msg) {
        if (TenUpdateApp.isDebugBuild())
            Log.e("십 Update: " + tag, msg);
    }
}
