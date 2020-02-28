package com.mesalabs.on.update.ota;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.mesalabs.cerberus.utils.PropUtils;
import com.mesalabs.on.update.ota.tasks.ROMXMLParser;
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

public class ROMUpdate {
    public static final int STATE_NO_UPDATES = 1;
    public static final int STATE_NEW_VERSION_AVAILABLE = 2;
    public static final int STATE_ERROR = 3;
    public static final int STATE_CHECKING = 4;
    public static final int STATE_DOWNLOADING = 5;

    private Context mContext;
    private ROMUpdate.StubListener mStubListener;
    private boolean mIsRunningInApp = true;
    private boolean mNewUpdateAvailable = false;

    public ROMUpdate(Context context, ROMUpdate.StubListener stubListener) {
        mContext = context;
        mStubListener = stubListener;
    }

    public void checkUpdates(boolean inApp) {
        PreferencesUtils.Download.clean();
        PreferencesUtils.ROM.clean();
        mIsRunningInApp = inApp;
        new LoadUpdateManifest(mContext).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void postCheckUpdates() {
        int newStatus = STATE_ERROR;

        if (!PreferencesUtils.ROM.getRomName().equals("null")) {
            int currentVer = PropUtils.getInt("ro.on.version", 201900223);
            int onlineVer = PreferencesUtils.ROM.getVersionNumber();

            mNewUpdateAvailable = currentVer < onlineVer;
            newStatus = mNewUpdateAvailable ? STATE_NEW_VERSION_AVAILABLE : STATE_NO_UPDATES;

            GeneralUtils.dismissNotifications(mContext);

            if (!mIsRunningInApp) {
                if (mNewUpdateAvailable) {
                    GeneralUtils.setupUpdateAvailableNotification(mContext);
                }
                GeneralUtils.scheduleNotification(mContext, PreferencesUtils.getBgServiceEnabled());
            }
        }

        if (mStubListener != null)
            mStubListener.onUpdateCheckCompleted(newStatus);
    }


    public interface StubListener {
        void onUpdateCheckCompleted(int status);
    }

    class LoadUpdateManifest extends AsyncTask<Void, Void, Void> {
        private final String TAG = "LoadUpdateManifest";
        private static final String MANIFEST = "update_manifest.xml";

        private Context mContext;


        public LoadUpdateManifest(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            if (!mIsRunningInApp) {
                GeneralUtils.dismissNotifications(mContext);
                GeneralUtils.setupCheckingUpdatesNotification(mContext);
            }

            File manifest = new File(mContext.getFilesDir().getPath(), MANIFEST);
            if (manifest.exists()) {
                manifest.delete();
            }
        }

        @Override
        protected Void doInBackground(Void... v) {
            try {
                Thread.sleep(1500);

                InputStream input = null;

                //URL url = new URL(PropUtils.get(Constants.OTA_MANIFEST, "").trim());
                URL url = new URL("https://gitlab.com/BlackMesa123/otatest/raw/master/testrommanifest.xml");
                URLConnection connection = url.openConnection();
                connection.connect();
                // download the file
                input = new BufferedInputStream(url.openStream());

                OutputStream output = mContext.openFileOutput(MANIFEST, Context.MODE_PRIVATE);

                byte[] data = new byte[1024];
                int count;
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                // file finished downloading, parse it!
                ROMXMLParser parser = new ROMXMLParser();
                parser.parse(new File(mContext.getFilesDir(), MANIFEST), mContext);
            } catch (Exception e) {
                LogUtils.d(TAG, "Exception: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent intent;
            if (!mIsRunningInApp) {
                intent = new Intent(Constants.MANIFEST_CHECK_BACKGROUND);
            } else {
                intent = new Intent(Constants.MANIFEST_LOADED);

            }

            mContext.sendBroadcast(intent);
            super.onPostExecute(result);

            postCheckUpdates();
        }
    }

}
