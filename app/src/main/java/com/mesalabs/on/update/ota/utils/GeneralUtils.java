package com.mesalabs.on.update.ota.utils;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DecimalFormat;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import java.io.File;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.mesalabs.cerberus.utils.PropUtils;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.activity.home.MainActivity;
import com.mesalabs.on.update.ota.receivers.AppReceiver;
import com.mesalabs.on.update.utils.LogUtils;

/*
 * On Update
 *
 * Coded by BlackMesa @2020
 * Code snippets by MatthewBooth.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */

public class GeneralUtils {
    public final static String TAG = "GeneralUtils";

    private static final int KILOBYTE = 1024;
    private static int KB = KILOBYTE;
    private static int MB = KB * KB;
    private static int GB = MB * KB;

    private static DecimalFormat decimalFormat = new DecimalFormat("##0.#");
    static {
        decimalFormat.setMaximumIntegerDigits(3);
        decimalFormat.setMaximumFractionDigits(1);
    }

    public static String formatDataFromBytes(long size) {
        String symbol;
        KB = KILOBYTE;
        symbol = "B";
        if (size < KB) {
            return decimalFormat.format(size) + symbol;
        } else if (size < MB) {
            return decimalFormat.format(size / (float) KB) + 'k' + symbol;
        } else if (size < GB) {
            return decimalFormat.format(size / (float) MB) + 'M' + symbol;
        }
        return decimalFormat.format(size / (float)GB) + 'G' + symbol;
    }

    public static void deleteFile(File file) {
        SystemUtils.shell("rm -f " + file.getAbsolutePath(), false);
    }

    public static boolean isNotificationAlarmSet(Context context) {
        Intent intent = new Intent(context, AppReceiver.class);
        intent.setAction(Constants.START_UPDATE_CHECK);
        return PendingIntent.getBroadcast(context, 1673, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }

    public static void setHasFileDownloaded(Context context) {
        File file = PreferencesUtils.ROM.getFullFile();
        int filesize = PreferencesUtils.ROM.getFileSize();
        boolean downloadIsRunning = PreferencesUtils.Download.getIsDownloadOnGoing();

        boolean status = false;

        LogUtils.d(TAG, "Local file " + file.getAbsolutePath());
        LogUtils.d(TAG, "Local filesize " + file.length());
        LogUtils.d(TAG, "Remote filesize " + filesize);

        if (file.length() != 0 && file.length() == filesize && !downloadIsRunning) {
            status = true;
        }
        PreferencesUtils.Download.setDownloadFinished(status);
    }

    public static void setBackgroundCheck(Context context, boolean set) {
        scheduleNotification(context, set);
    }

    public static void scheduleNotification(Context context, boolean schedule) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AppReceiver.class);
        intent.setAction(Constants.START_UPDATE_CHECK);
        int intentId = 1673;
        int intentFlag = PendingIntent.FLAG_UPDATE_CURRENT;

        if (schedule) {
            int requestedInterval = PreferencesUtils.getBgServiceCheckFrequency();

            LogUtils.d(TAG, "Setting alarm for " + requestedInterval + " seconds");
            Calendar calendar = Calendar.getInstance();
            long time = calendar.getTimeInMillis() + requestedInterval * 1000;
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, intentId, intent, intentFlag));
        } else {
            if (alarmManager != null) {
                LogUtils.d(TAG, "Cancelling alarm");
                alarmManager.cancel(PendingIntent.getBroadcast(context, intentId, intent, intentFlag));
            }
        }
    }

    public static boolean isConnected(Context context) {
        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork != null) {
                isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            }
        }
        return isConnected;
    }

    public static boolean isMobileNetwork(Context context) {
        boolean isMobileNetwork = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork != null) {
                isMobileNetwork = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return isMobileNetwork;
    }

    private static boolean versionBiggerThan(String current, String manifest) {
        // returns true if current > manifest, false otherwise
        if (current.length() > manifest.length()) {
            for (int i = 0; i < current.length() - manifest.length(); i++) {
                manifest += "0";
            }
        } else if (manifest.length() > current.length()) {
            for (int i = 0; i < manifest.length() - current.length(); i++) {
                current += "0";
            }
        }

        LogUtils.d(TAG, "Current: " + current + " Manifest: " + manifest);

        return Integer.parseInt(current) < Integer.parseInt(manifest);
    }

    public static void setUpdateAvailability(Context context) {
        int otaVersion = PreferencesUtils.ROM.getVersionNumber();
        String currentVer = PropUtils.get(Constants.OTA_VERSION, "");
        String manifestVer = Integer.toString(otaVersion);

        boolean available = versionBiggerThan(currentVer, manifestVer);

        PreferencesUtils.Download.setUpdateAvailability(available);
        LogUtils.d(TAG, "Update Availability is " + available);
    }

    public static void setupCheckingUpdatesNotification(Context context) {
        LogUtils.d(TAG, "Showing checking updates notification");

        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "mesa_onupdate_notichannel_bg");

        mBuilder.setContentTitle(context.getString(R.string.mesa_checking_updates))
                .setSmallIcon(R.drawable.mesa_fota_postpone_noti)
                .setColor(context.getColor(R.color.mesa_ota_control_activated_color))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        mNotifyManager.notify(Constants.NOTIFICATION_ID, mBuilder.build());
    }

    public static void setupUpdateAvailableNotification(Context context) {
        LogUtils.d(TAG, "Showing update available notification");

        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, PreferencesUtils.getMainNotiChannelName());
        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentTitle(context.getString(R.string.mesa_new_update_noti))
                .setContentText(context.getString(R.string.mesa_new_update_noti_desc))
                .setSmallIcon(R.drawable.mesa_fota_message_phone_noti)
                .setColor(context.getColor(R.color.mesa_ota_control_activated_color))
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(Uri.parse(PreferencesUtils.getBgServiceNotificationSound()));

        if (PreferencesUtils.getBgServiceNotificationVibrate()) {
            mBuilder.setDefaults(NotificationCompat.DEFAULT_VIBRATE);
        }

        mNotifyManager.notify(Constants.NOTIFICATION_ID, mBuilder.build());
    }

    public static void dismissNotifications(Context context) {
        LogUtils.d(TAG, "Dismissing notifications");

        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.cancel(Constants.NOTIFICATION_ID);
    }

    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static String getRemovableMediaPath() {
        return SystemUtils.shell("echo ${SECONDARY_STORAGE%%:*}", false);
    }
}
