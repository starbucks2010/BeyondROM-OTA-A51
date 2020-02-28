package com.mesalabs.on.update.fragment.settings;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;

import com.mesalabs.on.update.OnUpdateApp;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.activity.aboutpage.AboutActivity;
import com.mesalabs.on.update.activity.settings.ORSInnerSettingsActivity;
import com.mesalabs.on.update.ota.utils.GeneralUtils;
import com.mesalabs.on.update.ota.utils.PreferencesUtils;
import com.samsung.android.ui.preference.SeslListPreference;
import com.samsung.android.ui.preference.SeslPreference;
import com.samsung.android.ui.preference.SeslPreferenceFragmentCompat;
import com.samsung.android.ui.preference.SeslSwitchPreferenceCompat;
import com.samsung.android.ui.preference.SeslSwitchPreferenceScreen;

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

public class SettingsFragment extends SeslPreferenceFragmentCompat implements
        SeslPreference.OnPreferenceChangeListener,
        SeslPreference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.mesa_prefs_settingsactivity);
        seslSetRoundedCornerType(SESL_ROUNDED_CORNER_TYPE_STROKE);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        SeslSwitchPreferenceScreen orsPref = (SeslSwitchPreferenceScreen) findPreference("mesa_ors_pref");
        if (!PreferencesUtils.getIsRootAvailable()) {
            orsPref.setEnabled(false);
        }
        orsPref.setOnPreferenceClickListener(this);

        SeslSwitchPreferenceCompat bgServicePref = (SeslSwitchPreferenceCompat) findPreference("mesa_bgservice_pref");
        bgServicePref.setOnPreferenceChangeListener(this);

        SeslListPreference bgServiceFreqPref = (SeslListPreference) findPreference("mesa_bgservice_freq_pref");
        bgServiceFreqPref.seslSetSummaryColor(getColoredSummaryColor());
        bgServiceFreqPref.setOnPreferenceChangeListener(this);

        SeslPreference bgServiceNotiSoundPref = findPreference("mesa_bgservice_noti_sound_pref");
        bgServiceNotiSoundPref.seslSetSummaryColor(getColoredSummaryColor());
        bgServiceNotiSoundPref.setOnPreferenceChangeListener(this);
        bgServiceNotiSoundPref.setOnPreferenceClickListener(this);

        SeslListPreference networkTypePref = (SeslListPreference) findPreference("mesa_networktype_pref");
        networkTypePref.seslSetSummaryColor(getColoredSummaryColor());

        SeslPreference aboutActivityPref = findPreference("mesa_aboutactivity_pref");
        if (PreferencesUtils.getIsAppUpdateAvailable()) {
            aboutActivityPref.setWidgetLayoutResource(R.layout.mesa_prefs_badge_layout);
        }
        aboutActivityPref.setOnPreferenceClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        SeslSwitchPreferenceScreen orsPref = (SeslSwitchPreferenceScreen) findPreference("mesa_ors_pref");
        orsPref.setChecked(PreferencesUtils.getIsOpenRecoveryScriptEnabled());

        SeslPreference bgServiceNotiSoundPref = findPreference("mesa_bgservice_noti_sound_pref");
        String title = getString(R.string.mesa_bgservice_noti_sound_silent_sum);
        String value = PreferencesUtils.getBgServiceNotificationSound();
        if (!value.equals("")) {
            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), Uri.parse(value));
            title = ringtone.getTitle(getContext());
        }
        bgServiceNotiSoundPref.setSummary(title);
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
            OnUpdateApp.createNotificationChannel();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onPreferenceChange(SeslPreference preference, Object newValue) {
        if (preference.getKey().equals("mesa_bgservice_pref")) {
            GeneralUtils.setBackgroundCheck(getContext(), (boolean) newValue);
            return true;
        } else if (preference.getKey().equals("mesa_bgservice_freq_pref")) {
            PreferencesUtils.setBgServiceCheckFrequency((String) newValue);
            GeneralUtils.scheduleNotification(getContext(), PreferencesUtils.getBgServiceEnabled());
            return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceClick(SeslPreference preference) {
        switch (preference.getKey()) {
            case "mesa_ors_pref":
                startActivity(new Intent(getContext(), ORSInnerSettingsActivity.class));
                return true;
            case "mesa_bgservice_noti_sound_pref":
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
                return true;
            case "mesa_aboutactivity_pref":
                startActivity(new Intent(getContext(), AboutActivity.class));
                return true;
        }

        return false;
    }

    private int getColoredSummaryColor() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorPrimaryDark, outValue, true);
        return outValue.data;
    }

}