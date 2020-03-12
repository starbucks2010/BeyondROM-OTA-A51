package com.mesalabs.on.update.fragment.home;

import android.content.ComponentName;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.system.Os;
import android.system.StructUtsname;
import android.text.BidiFormatter;
import android.text.format.DateFormat;

import com.mesalabs.cerberus.utils.PropUtils;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.utils.FirmwareInfoUtils;
import com.mesalabs.on.update.utils.LogUtils;
import com.samsung.android.ui.preference.SeslPreference;
import com.samsung.android.ui.preference.SeslPreferenceFragmentCompat;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class FirmwareInfoFragment extends SeslPreferenceFragmentCompat {
    private static String TAG = "FirmwareInfoFragment";

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getListView().seslSetLastOutlineStrokeEnabled(false);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.mesa_prefs_firmwareinfoactivity);
        seslSetRoundedCornerType(SESL_ROUNDED_CORNER_TYPE_STROKE);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        // ROM Version
        SeslPreference rom = findPreference("mesa_fwinfo_rom_version");
        setFwInfoPrefSummary(rom, FirmwareInfoUtils.getROMVersion());
        // On ensō Version
        SeslPreference enso = findPreference("mesa_fwinfo_enso_version");
        setFwInfoPrefSummary(enso, FirmwareInfoUtils.getEnsoVersion());
        // OneUI Version
        SeslPreference oneui = findPreference("mesa_fwinfo_oneui_version");
        setFwInfoPrefSummary(oneui, FirmwareInfoUtils.getOneUIVersion());
        // Android Version
        SeslPreference android = findPreference("mesa_fwinfo_android_version");
        android.setSummary(Build.VERSION.RELEASE);
        // Android Version
        SeslPreference kernel = findPreference("mesa_fwinfo_kernel_version");
        setFwInfoPrefSummary(kernel, FirmwareInfoUtils.getKernelVersion());
        // Build Number
        SeslPreference bn = findPreference("mesa_fwinfo_build_number");
        bn.setSummary(BidiFormatter.getInstance().unicodeWrap(Build.DISPLAY));
        // Security Patch
        SeslPreference sp = findPreference("mesa_fwinfo_sp_version");
        setFwInfoPrefSummary(sp, FirmwareInfoUtils.getSecurityPatchVersion(getContext()));
    }

    private static void setFwInfoPrefSummary(SeslPreference pref, String summary) {
        if (summary != null) {
            pref.setSummary(summary);
        }
    }

}