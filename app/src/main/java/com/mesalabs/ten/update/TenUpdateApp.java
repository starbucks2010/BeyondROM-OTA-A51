package com.mesalabs.ten.update;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.mesalabs.ten.update.ota.utils.PreferencesUtils;

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

public class TenUpdateApp extends Application implements LifecycleObserver {
    private static TenUpdateApp mAppInstance;
    private static Context mAppContext;
    // 십 Update
    private static boolean mIsInBackground;
    // 십 Update

    public static Context getAppContext() {
        return mAppContext;
    }

    public static TenUpdateApp getAppInstance() {
        return mAppInstance;
    }

    public static String getAppName() {
        return mAppInstance.getString(R.string.mesa_tenupdate);
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

    // 십 Update
    public static boolean isAppInBackground()  {
        return mIsInBackground;
    }
    // 십 Update

    public static boolean isDebugBuild()  {
        return BuildConfig.DEBUG;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppInstance = this;
        mAppContext = this.getApplicationContext();
        // 십 Update
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        if (PreferencesUtils.getMainNotiChannelName().equals("")) {
            createMainNotificationChannel();
            createMinorNotificationChannel();
        }
        // 십 Update
    }

    // 십 Update
    public static void createMainNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = mAppContext.getSystemService(NotificationManager.class);

            int randomId = (int) (Math.random() * 100 + 1);
            while (PreferencesUtils.getMainNotiChannelName().contains(Integer.toString(randomId))) {
                randomId = (int) (Math.random() * 100 + 1);
            }

            notificationManager.deleteNotificationChannel(PreferencesUtils.getMainNotiChannelName());

            PreferencesUtils.setMainNotiChannelName("mesa_tenupdate_notichannel_main" + "_" + randomId);
            NotificationChannel notiMainChannel = new NotificationChannel(PreferencesUtils.getMainNotiChannelName(), mAppContext.getString(R.string.mesa_tenupdate_notichannel_main_name), NotificationManager.IMPORTANCE_HIGH);
            notiMainChannel.setLightColor(mAppContext.getColor(R.color.mesa_ota_control_activated_color));
            notiMainChannel.enableLights(true);
            notiMainChannel.setSound(Uri.parse(PreferencesUtils.getBgServiceNotificationSound()), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build());
            notiMainChannel.enableVibration(PreferencesUtils.getBgServiceNotificationVibrate());

            notificationManager.createNotificationChannel(notiMainChannel);
        }
    }

    private void createMinorNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = mAppContext.getSystemService(NotificationManager.class);

            NotificationChannel notiDwnlChannel = new NotificationChannel("mesa_tenupdate_notichannel_dwnl", mAppContext.getString(R.string.mesa_tenupdate_notichannel_dwnl_name), NotificationManager.IMPORTANCE_DEFAULT);
            notiDwnlChannel.setSound(null, null);

            notificationManager.createNotificationChannel(notiDwnlChannel);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        mIsInBackground = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        mIsInBackground = true;
    }
    // 십 Update

}
