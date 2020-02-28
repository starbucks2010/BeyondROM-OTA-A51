package com.mesalabs.on.update.activity.home;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mesalabs.cerberus.base.BaseAppBarActivity;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.fragment.home.FirmwareInfoFragment;
import com.mesalabs.on.update.fragment.settings.SettingsFragment;
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

public class FirmwareInfoActivity extends BaseAppBarActivity {
    private SeslPreferenceFragmentCompat mFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mesa_ota_base_fragment_layout);

        appBar.setTitleText(getString(R.string.mesa_firmware_info));
        appBar.setHomeAsUpButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inflateFragment();
    }

    @Override
    protected boolean getIsAppBarExpanded() {
        return false;
    }

    private void inflateFragment() {
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag("root");
        if (mFragment != null) {
            transaction.hide(mFragment);
        }
        if (fragment != null) {
            mFragment = (SeslPreferenceFragmentCompat) fragment;
            transaction.show(fragment);
        } else {
            mFragment = new FirmwareInfoFragment();
            transaction.add(R.id.mesa_fragmentcontainer_base, mFragment, "root");
        }
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }
}
