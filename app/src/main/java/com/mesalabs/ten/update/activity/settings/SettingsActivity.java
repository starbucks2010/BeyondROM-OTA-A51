package com.mesalabs.ten.update.activity.settings;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mesalabs.cerberus.base.BaseAppBarActivity;
import com.mesalabs.cerberus.ui.callback.OnSingleClickListener;
import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.fragment.settings.SettingsFragment;
import com.samsung.android.ui.preference.SeslPreferenceFragmentCompat;

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

public class SettingsActivity extends BaseAppBarActivity {
    private SeslPreferenceFragmentCompat mFragment;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mesa_ota_base_fragment_layout);

        appBar.setTitleText(getString(R.string.mesa_app_settings));
        appBar.setHomeAsUpButton(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
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
            mFragment = new SettingsFragment();
            transaction.add(R.id.mesa_fragmentcontainer_base, mFragment, "root");
        }
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }
}
