package com.mesalabs.on.update.ota.utils;

import android.os.Environment;

import com.mesalabs.cerberus.utils.PropUtils;

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

public class Constants {
    // Props
    public static final String OTA_ROMNAME 								= "ro.on.ota.romname";
    public static final String OTA_VERSION 								= "ro.on.ota.version";
    public static final String OTA_MANIFEST 							= "ro.on.ota.manifest";
    public static final String OTA_DOWNLOAD_LOC							= "ro.on.ota.download_loc";

    // Storage
    public static final String SD_CARD 									= Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String OTA_DOWNLOAD_DIR 						= PropUtils.get(OTA_DOWNLOAD_LOC, "OTAUpdates");
    public static final String INSTALL_AFTER_FLASH_DIR 					= "InstallAfterFlash";

    // Broadcast intents
    public static final String MANIFEST_LOADED 							= "com.mesalabs.on.ota.MANIFEST_LOADED";
    public static final String MANIFEST_CHECK_BACKGROUND 				= "com.mesalabs.on.ota.MANIFEST_CHECK_BACKGROUND";
    public static final String START_UPDATE_CHECK 						= "com.mesalabs.on.ota.START_UPDATE_CHECK";

    //Notification
    public static final int NOTIFICATION_ID 							= 101;
}
