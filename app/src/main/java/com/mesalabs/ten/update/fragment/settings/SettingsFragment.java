package com.mesalabs.ten.update.fragment.settings;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.TypedValue;

import com.mesalabs.ten.update.TenUpdateApp;
import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.activity.aboutpage.AboutActivity;
import com.mesalabs.ten.update.ota.utils.GeneralUtils;
import com.mesalabs.ten.update.ota.utils.PreferencesUtils;
import com.samsung.android.ui.preference.PreferenceCategory;
import com.samsung.android.ui.preference.SeslListPreference;
import com.samsung.android.ui.preference.SeslPreference;
import com.samsung.android.ui.preference.SeslPreferenceFragmentCompat;
import com.samsung.android.ui.preference.SeslSwitchPreferenceCompat;

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

public class SettingsFragment extends SeslPreferenceFragmentCompat implements
        SeslPreference.OnPreferenceChangeListener,
        SeslPreference.OnPreferenceClickListener {
    private long mLastClickTime = 0L;

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getListView().seslSetLastOutlineStrokeEnabled(false);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.mesa_ota_prefs_settingsactivity);
        seslSetRoundedCornerType(SESL_ROUNDED_CORNER_TYPE_STROKE);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        PreferenceCategory bgServicePrefParent = (PreferenceCategory) findPreference("mesa_bgservice");

        SeslSwitchPreferenceCompat bgServicePref = (SeslSwitchPreferenceCompat) findPreference("mesa_bgservice_pref");
        bgServicePref.setOnPreferenceChangeListener(this);

        SeslListPreference bgServiceFreqPref = (SeslListPreference) findPreference("mesa_bgservice_freq_pref");
        bgServiceFreqPref.seslSetSummaryColor(getColoredSummaryColor());
        bgServiceFreqPref.setOnPreferenceChangeListener(this);

        SeslPreference bgServiceNotiSoundPref = findPreference("mesa_bgservice_noti_sound_pref");
        bgServiceNotiSoundPref.seslSetSummaryColor(getColoredSummaryColor());
        bgServiceNotiSoundPref.setOnPreferenceClickListener(this);

        SeslSwitchPreferenceCompat bgServiceNotiVibratePref = (SeslSwitchPreferenceCompat) findPreference("mesa_bgservice_noti_vibrate_pref");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bgServicePrefParent.removePreference(bgServiceNotiVibratePref);
        } else {
            bgServiceNotiVibratePref.setOnPreferenceChangeListener(this);
        }

        SeslListPreference networkTypePref = (SeslListPreference) findPreference("mesa_networktype_pref");
        networkTypePref.seslSetSummaryColor(getColoredSummaryColor());
        networkTypePref.setEnabled(!PreferencesUtils.Download.getIsDownloadOnGoing());

        SeslPreference aboutActivityPref = findPreference("mesa_aboutactivity_pref");
        if (PreferencesUtils.getIsAppUpdateAvailable()) {
            aboutActivityPref.setWidgetLayoutResource(R.layout.mesa_preference_prefs_badge_layout);
        }
        aboutActivityPref.setOnPreferenceClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mainNotiChannel = getContext().getSystemService(NotificationManager.class).getNotificationChannel(PreferencesUtils.getMainNotiChannelName());
            Uri value = mainNotiChannel.getSound();
            PreferencesUtils.setBgServiceNotificationSound(value == null ? "" : value.toString());
        }

        setRingtoneSummary();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            Uri ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (ringtone != null) {
                PreferencesUtils.setBgServiceNotificationSound(ringtone.toString());
            } else {
                PreferencesUtils.setBgServiceNotificationSound("");
            }
            setRingtoneSummary();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onPreferenceChange(SeslPreference preference, Object newValue) {
        switch (preference.getKey()) {
            case "mesa_bgservice_pref":
                PreferencesUtils.setBgServiceEnabled((boolean) newValue);
                GeneralUtils.setBackgroundCheck(getContext(), (boolean) newValue);
                return true;
            case "mesa_bgservice_freq_pref":
                PreferencesUtils.setBgServiceCheckFrequency((String) newValue);
                GeneralUtils.scheduleNotification(getContext(), PreferencesUtils.getBgServiceEnabled());
                return true;
            case "mesa_bgservice_noti_vibrate_pref":
                PreferencesUtils.setBgServiceNotificationVibrate((boolean) newValue);
                return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceClick(SeslPreference preference) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 600L) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        switch (preference.getKey()) {
            case "mesa_bgservice_noti_sound_pref":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, TenUpdateApp.getAppPackageName());
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, PreferencesUtils.getMainNotiChannelName());
                    startActivity(intent);
                } else  {
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, Settings.System.DEFAULT_NOTIFICATION_URI);

                    String existingValue = PreferencesUtils.getBgServiceNotificationSound();
                    if (existingValue.length() == 0) {
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                    } else {
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(existingValue));
                    }

                    startActivityForResult(intent, 1);
                }

                return true;
            case "mesa_aboutactivity_pref":
                startActivity(new Intent(getContext(), AboutActivity.class));
                return true;
        }

        return false;
    }

    private ColorStateList getColoredSummaryColor() {
        TypedValue colorPrimaryDark = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimaryDark, colorPrimaryDark, true);

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled},
                new int[] {-android.R.attr.state_enabled}
        };
        int[] colors = new int[] {
                Color.argb(0xff, Color.red(colorPrimaryDark.data), Color.green(colorPrimaryDark.data), Color.blue(colorPrimaryDark.data)),
                Color.argb(0x4d, Color.red(colorPrimaryDark.data), Color.green(colorPrimaryDark.data), Color.blue(colorPrimaryDark.data))
        };
        return new ColorStateList(states, colors);
    }

    private void setRingtoneSummary() {
        String title = getString(R.string.mesa_bgservice_noti_sound_silent_sum);
        String value = PreferencesUtils.getBgServiceNotificationSound();
        if (!value.equals("")) {
            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(value));
            title = ringtone.getTitle(getContext());
        }

        SeslPreference bgServiceNotiSoundPref = findPreference("mesa_bgservice_noti_sound_pref");
        bgServiceNotiSoundPref.setSummary(title);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            SeslSwitchPreferenceCompat bgServiceNotiVibratePref = (SeslSwitchPreferenceCompat) findPreference("mesa_bgservice_noti_vibrate_pref");
            bgServiceNotiVibratePref.setChecked(PreferencesUtils.getBgServiceNotificationVibrate());
        }
    }

}