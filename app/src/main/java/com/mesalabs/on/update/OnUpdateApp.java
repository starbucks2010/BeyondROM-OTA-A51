package com.mesalabs.on.update;

import android.app.Application;
import android.content.Context;

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

public class OnUpdateApp extends Application {
    private static OnUpdateApp mAppInstance;
    private static Context mAppContext;

    public static Context getAppContext() {
        return mAppContext;
    }

    public static OnUpdateApp getAppInstance() {
        return mAppInstance;
    }

    public static String getAppName() {
        return mAppInstance.getString(R.string.mesa_cerberuscore);
    }

    public static String getAppPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    public static String getAppVersionString() {
        return BuildConfig.VERSION_NAME;
    }

    public static int getAppVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static boolean isDebugBuild()  {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppInstance = this;
        mAppContext = this.getApplicationContext();
    }

}
