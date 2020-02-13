package com.mesalabs.cerberus.activity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.base.BaseAppBarActivity;
import com.samsung.android.ui.preference.SeslPreference;
import com.samsung.android.ui.preference.SeslPreferenceFragmentCompat;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * ULTRA-MEGA-PRIVATE SOURCE CODE. SHARING TO DEVKINGS TEAM
 * EXTERNALS IS PROHIBITED AND WILL BE PUNISHED WITH ANAL ABUSE.
 */

public class CreditsActivity extends BaseAppBarActivity {
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mesa_layout_creditsactivity);

        appBar.setTitleText(getString(R.string.mesa_credits));
        appBar.setHomeAsUpButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.mesa_fragmentcontainer_creditsactivity, new CreditsFragment(), "root");
        transaction.commit();

        mFragmentManager.executePendingTransactions();

    }

    @Override
    protected boolean getIsAppBarExpanded() {
        return false;
    }


    public static class CreditsFragment extends SeslPreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle bundle, String str) {
            addPreferencesFromResource(R.xml.mesa_creditspref_creditsactivity);
            seslSetRoundedCornerType(SESL_ROUNDED_CORNER_TYPE_STROKE);
        }
    }

}
