package com.mesalabs.on.update.ota.receivers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.mesalabs.on.update.ota.ROMUpdate;
import com.mesalabs.on.update.ota.utils.Constants;
import com.mesalabs.on.update.ota.utils.GeneralUtils;
import com.mesalabs.on.update.ota.utils.PreferencesUtils;
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

public class AppReceiver extends BroadcastReceiver {
    public final String TAG = "AppReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        long mRomDownloadID = PreferencesUtils.Download.getDownloadID();

        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);

            LogUtils.v(TAG, "Receiving " + mRomDownloadID);

            if (id != mRomDownloadID) {
                LogUtils.v(TAG, "Ignoring unrelated non-ROM download " + id);
                return;
            }

            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor cursor = downloadManager.query(query);

            // it shouldn't be empty, but just in case
            if (!cursor.moveToFirst()) {
                LogUtils.e(TAG, "Rom download Empty row");
                return;
            }

            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL != cursor.getInt(statusIndex)) {
                LogUtils.w(TAG, "Download Failed");
                PreferencesUtils.Download.setDownloadFinished(false);
                return;
            } else {
                LogUtils.v(TAG, "Download Succeeded");
                PreferencesUtils.Download.setDownloadFinished(true);
                return;
            }
        }

        if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            long[] ids = extras.getLongArray(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);

            for (long id : ids) {
                if (id != mRomDownloadID) {
                    LogUtils.v(TAG, "mDownloadID is " + mRomDownloadID + " and ID is " + id);
                    return;
                } else {
                    //Intent i = new Intent(context, AvailableActivity.class);
                    //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //context.startActivity(i);
                }
            }
        }

        if (action.equals(Constants.MANIFEST_CHECK_BACKGROUND)) {
            LogUtils.d(TAG, "Receiving background check confirmation");

            boolean updateAvailable = PreferencesUtils.Download.getUpdateAvailability();

            if (updateAvailable) {
                GeneralUtils.setupUpdateAvailableNotification(context);
                GeneralUtils.scheduleNotification(context, !PreferencesUtils.getBgServiceEnabled());
            }
        }

        if (action.equals(Constants.START_UPDATE_CHECK)) {
            LogUtils.d(TAG, "Update check started");
            ROMUpdate update = new ROMUpdate(context, null);
            update.checkUpdates(false);
        }

        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            LogUtils.d(TAG, "Boot received");
            boolean backgroundCheck = PreferencesUtils.getBgServiceEnabled();
            if (backgroundCheck) {
                LogUtils.d(TAG, "Starting background check alarm");
                GeneralUtils.scheduleNotification(context, true);
            }
        }
    }
}

