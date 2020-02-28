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

public class FirmwareInfoFragment extends SeslPreferenceFragmentCompat implements SeslPreference.OnPreferenceClickListener {
    private static String TAG = "FirmwareInfoFragment";

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
        Utils.setFwInfoPrefSummary(rom, Utils.getROMVersion());
        // On ensō Version
        SeslPreference enso = findPreference("mesa_fwinfo_enso_version");
        Utils.setFwInfoPrefSummary(enso, Utils.getEnsoVersion());
        // OneUI Version
        SeslPreference oneui = findPreference("mesa_fwinfo_oneui_version");
        Utils.setFwInfoPrefSummary(oneui, Utils.getOneUIVersion());
        // Android Version
        SeslPreference android = findPreference("mesa_fwinfo_android_version");
        android.setSummary(Build.VERSION.RELEASE);
        // Android Version
        SeslPreference kernel = findPreference("mesa_fwinfo_kernel_version");
        Utils.setFwInfoPrefSummary(kernel, Utils.getKernelVersion());
        // Build Number
        SeslPreference bn = findPreference("mesa_fwinfo_build_number");
        bn.setSummary(BidiFormatter.getInstance().unicodeWrap(Build.DISPLAY));
        // Security Patch
        SeslPreference sp = findPreference("mesa_fwinfo_sp_version");
        Utils.setFwInfoPrefSummary(sp, Utils.getSecurityPatchVersion());
    }

    @Override
    public boolean onPreferenceClick(SeslPreference preference) {
        /*switch (preference.getKey()) {
            case "mesa_fwinfo_android_version":
                Intent intent = new Intent("android.intent.action.MAIN").setClassName("com.mesalabs.on.romcontrol", "com.mesalabs.on.romcontrol.activity.rc.MainActivity");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    LogUtils.e(TAG, e.toString());
                }
        }*/
        return false;
    }


    private static class Utils {
        private static void setFwInfoPrefSummary(SeslPreference pref, String summary) {
            if (summary != null) {
                pref.setSummary(summary);
            }
        }

        private static String getEnsoVersion() {
            int prop = PropUtils.getInt("ro.on.enso.version", 0);

            if (prop != 0) {
                return String.valueOf(prop / 10000) + "." + String.valueOf((prop % 10000) / 100) + "." + String.valueOf((prop % 1000) / 100);
            } else
                return null;
        }

        private static String getKernelVersion() {
            StructUtsname uname = Os.uname();

            if (uname == null) {
                return null;
            }

            Matcher m = Pattern.compile("(#\\d+) (?:.*?)?((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)").matcher(uname.version);
            if (!m.matches()) {
                LogUtils.e(TAG, "Regex did not match on uname version " + uname.version);
                return null;
            }
            return uname.release + "\n" + m.group(1) + " " + m.group(2);
        }

        private static String getOneUIVersion() {
            int prop = PropUtils.getInt("ro.build.version.sep", 0);

            if (prop != 0) {
                int oneUIversion = prop - 90000;
                return String.valueOf(oneUIversion / 10000) + "." + String.valueOf((oneUIversion % 10000) / 100);
            } else
                return null;
        }


        private static String getSecurityPatchVersion() {
            String patch = Build.VERSION.SECURITY_PATCH;
            if ("".equals(patch)) {
                return null;
            }
            try {
                return DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dMMMMyyyy"), new SimpleDateFormat("yyyy-MM-dd").parse(patch)).toString();
            } catch (ParseException e) {
                return patch;
            }
        }

        private static String getROMVersion() {
            int prop = PropUtils.getInt("ro.on.rom.version", 0);

            if (prop != 0) {
                return String.valueOf(prop / 10000) + "." + String.valueOf((prop % 10000) / 100) + "." + String.valueOf((prop % 1000) / 100);
            } else
                return null;
        }
    }
}