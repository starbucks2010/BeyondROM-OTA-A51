package com.mesalabs.cerberus.base;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mesalabs.ten.update.R;
import com.mesalabs.cerberus.ui.widget.SwitchBar;
import com.samsung.android.ui.preference.SeslPreference;
import com.samsung.android.ui.preference.SeslPreferenceGroup;

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

public class BaseSwitchBarActivity extends BaseAppBarActivity {
    protected FragmentManager mFragmentManager;
    protected SwitchBar mSwitchBar;
    protected Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mesa_baselayout_baseswitchbaractivity);

        inflateFragment();

        mSwitchBar = findViewById(R.id.mesa_switchbar_baseswitchbaractivity);
        mSwitchBar.setSwitchBarPressListener(getSwitchBarListener());
        mSwitchBar.setChecked(getSwitchBarDefaultStatus());
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
            mFragment = fragment;
            transaction.show(fragment);
        } else {
            mFragment = getFragment();
            transaction.add(R.id.mesa_fragmentcontainer_baseswitchbaractivity, mFragment, "root");
        }
        transaction.commit();
        mFragmentManager.executePendingTransactions();
    }

    protected Fragment getFragment() {
        return null;
    }

    protected SwitchBar.SwitchBarPressListener getSwitchBarListener() {
        return null;
    }

    public boolean getSwitchBarDefaultStatus() {
        return false;
    }

    protected SeslPreferenceGroup getParent(SeslPreferenceGroup groupToSearchIn, SeslPreference preference) {
        for (int i = 0; i < groupToSearchIn.getPreferenceCount(); i++) {
            SeslPreference child = groupToSearchIn.getPreference(i);

            if (child == preference)
                return groupToSearchIn;

            if (child instanceof SeslPreferenceGroup) {
                SeslPreferenceGroup childGroup = (SeslPreferenceGroup)child;
                SeslPreferenceGroup result = getParent(childGroup, preference);
                if (result != null)
                    return result;
            }
        }

        return null;
    }
}
