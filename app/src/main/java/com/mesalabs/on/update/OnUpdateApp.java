package com.mesalabs.on.update;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import com.mesalabs.on.update.ota.utils.PreferencesUtils;
import com.mesalabs.on.update.utils.LogUtils;

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
        if (PreferencesUtils.getMainNotiChannelName().equals("")) {
            createNotificationChannel();
        }
    }

    public static void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = mAppContext.getSystemService(NotificationManager.class);

            int randomId = (int) (Math.random() * 100 + 1);
            while (PreferencesUtils.getMainNotiChannelName().contains(Integer.toString(randomId))) {
                randomId = (int) (Math.random() * 100 + 1);
            }

            notificationManager.deleteNotificationChannel(PreferencesUtils.getMainNotiChannelName());

            PreferencesUtils.setMainNotiChannelName("mesa_onupdate_notichannel_main" + "_" + randomId);
            NotificationChannel notiMainChannel = new NotificationChannel(PreferencesUtils.getMainNotiChannelName(), mAppContext.getString(R.string.mesa_onupdate_notichannel_main_name), NotificationManager.IMPORTANCE_HIGH);
            notiMainChannel.setLightColor(Color.GREEN);
            notiMainChannel.enableLights(true);
            notiMainChannel.setSound(Uri.parse(PreferencesUtils.getBgServiceNotificationSound()), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build());

            NotificationChannel notiBgChannel = new NotificationChannel("mesa_onupdate_notichannel_bg", mAppContext.getString(R.string.mesa_onupdate_notichannel_bg_name), NotificationManager.IMPORTANCE_LOW);

            notificationManager.createNotificationChannel(notiMainChannel);
            notificationManager.createNotificationChannel(notiBgChannel);
        }
    }

}
