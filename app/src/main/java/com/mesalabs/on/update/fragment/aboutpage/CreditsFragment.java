package com.mesalabs.on.update.fragment.aboutpage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.mesalabs.cerberus.ui.preference.CreditsPreference;
import com.mesalabs.on.update.R;
import com.samsung.android.ui.preference.SeslPreference;
import com.samsung.android.ui.preference.SeslPreferenceFragmentCompat;

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

public class CreditsFragment extends SeslPreferenceFragmentCompat  implements SeslPreference.OnPreferenceClickListener {
    @Override
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R.xml.mesa_prefs_creditsactivity);
        seslSetRoundedCornerType(SESL_ROUNDED_CORNER_TYPE_STROKE);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        CreditsPreference spinkitPref = (CreditsPreference) findPreference("mesa_credits_spinkit");
        spinkitPref.setOnPreferenceClickListener(this);
        CreditsPreference fetchPref = (CreditsPreference) findPreference("mesa_credits_fetch");
        fetchPref.setOnPreferenceClickListener(this);
        CreditsPreference wdPref = (CreditsPreference) findPreference("mesa_credits_waitingdots");
        wdPref.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(SeslPreference preference) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = null;
        switch (preference.getKey()) {
            case "mesa_credits_spinkit":
                url = "https://github.com/ybq/Android-SpinKit";
                break;
            case "mesa_credits_fetch":
                url = "https://github.com/tonyofrancis/Fetch";
                break;
            case "mesa_credits_waitingdots":
                url = "https://github.com/tajchert/WaitingDots";
                break;
        }
        intent.setData(Uri.parse(url));
        startActivity(intent);
        return true;
    }
}