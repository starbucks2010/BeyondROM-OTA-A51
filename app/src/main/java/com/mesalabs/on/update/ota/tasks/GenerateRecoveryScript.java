package com.mesalabs.on.update.ota.tasks;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import java.io.File;

import com.mesalabs.on.update.ota.utils.Constants;
import com.mesalabs.on.update.ota.utils.SystemUtils;
import com.mesalabs.on.update.utils.LogUtils;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.ota.utils.PreferencesUtils;

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

public class GenerateRecoveryScript extends AsyncTask<Void, String, Boolean> {
    public final String TAG = "GenerateRecoveryScript";

    private Context mContext;
    private Dialog mProgressCircle;
    private StringBuilder mScript = new StringBuilder();
    private static String SCRIPT_FILE = "/cache/recovery/openrecoveryscript";
    private static String NEW_LINE = "\n";
    private String mFilename;
    private String mScriptOutput;

    public GenerateRecoveryScript(Context context) {
        mContext = context;
        mFilename = PreferencesUtils.ROM.getFilename() + ".zip";
    }

    protected void onPreExecute() {
        // Show dialog
        mProgressCircle = new Dialog(mContext, Utils.isNightMode(mContext) ? R.style.mesa_ProgressCircleDialogStyle : R.style.mesa_ProgressCircleDialogStyle_Light);
        mProgressCircle.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressCircle.getWindow().setGravity(Gravity.CENTER);
        mProgressCircle.setCanceledOnTouchOutside(false);
        mProgressCircle.setContentView(LayoutInflater.from(mContext).inflate(R.layout.mesa_progress_circle_dialog_layout, null));
        mProgressCircle.show();

        if (PreferencesUtils.getORSIsWipeDataEnabled()) {
            mScript.append("wipe data" + NEW_LINE);
        }
        if (PreferencesUtils.getORSIsWipeCacheEnabled()) {
            mScript.append("wipe cache" + NEW_LINE);
        }
        if (PreferencesUtils.getORSIsWipeDalvikEnabled()) {
            mScript.append("wipe dalvik" + NEW_LINE);
        }

        mScript.append("install " + "/sdcard"
                + File.separator
                + Constants.OTA_DOWNLOAD_DIR
                + File.separator
                + mFilename
                + NEW_LINE);

        File installAfterFlashDir = new File("/sdcard"
                + File.separator
                + Constants.OTA_DOWNLOAD_DIR
                + File.separator
                + Constants.INSTALL_AFTER_FLASH_DIR);

        File[] filesArr = installAfterFlashDir.listFiles();
        if(filesArr != null && filesArr.length > 0) {
            for(int i = 0; i < filesArr.length; i++) {
                mScript.append(NEW_LINE
                        + "install "
                        + "/sdcard"
                        + File.separator
                        + Constants.OTA_DOWNLOAD_DIR
                        + File.separator
                        + Constants.INSTALL_AFTER_FLASH_DIR
                        + File.separator
                        + filesArr[i].getName());

                LogUtils.d(TAG, "install "
                        + "/sdcard"
                        + File.separator
                        + Constants.OTA_DOWNLOAD_DIR
                        + File.separator
                        + Constants.INSTALL_AFTER_FLASH_DIR
                        + File.separator
                        + filesArr[i].getName());
            }
        }

        if (PreferencesUtils.getORSIsDeleteAfterInstallEnabled()) {
            mScript.append(NEW_LINE
                    + "cmd rm -rf "
                    + "/sdcard"
                    + File.separator
                    + Constants.OTA_DOWNLOAD_DIR
                    + File.separator
                    + Constants.INSTALL_AFTER_FLASH_DIR
                    + File.separator
                    + mFilename
                    + NEW_LINE);
        }

        mScriptOutput = mScript.toString();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // Try create a dir in the cache folder
        // Without root
        String check = SystemUtils.shell("mkdir -p /cache/recovery/; echo $?", false);

        // If not 0, then permission was denied
        if(!check.equals("0")) {
            // Run as root
            SystemUtils.shell("mkdir -p /cache/recovery/; echo $?", true);
            SystemUtils.shell("echo \"" + mScriptOutput + "\" > " + SCRIPT_FILE + "\n", true);
        } else {
            // Permission was enabled, run without root
            SystemUtils.shell("echo \"" + mScriptOutput + "\" > " + SCRIPT_FILE + "\n", false);
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean value) {
        mProgressCircle.cancel();
        SystemUtils.rebootToRecovery(mContext);
    }
}
