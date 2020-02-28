package com.mesalabs.on.update.activity.settings;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.mesalabs.cerberus.base.BaseSwitchBarActivity;
import com.mesalabs.cerberus.ui.widget.SwitchBar;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.fragment.settings.ORSInnerSettingsFragment;
import com.mesalabs.on.update.ota.utils.PreferencesUtils;
import com.samsung.android.ui.preference.SeslPreferenceFragmentCompat;
import com.samsung.android.ui.preference.SeslPreferenceGroup;

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

public class ORSInnerSettingsActivity extends BaseSwitchBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBar.setTitleText(getString(R.string.mesa_ors_pref_title));
        appBar.setHomeAsUpButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected Fragment getFragment() {
        return new ORSInnerSettingsFragment();
    }

    @Override
    protected SwitchBar.SwitchBarPressListener getSwitchBarListener() {
        return new SwitchBar.SwitchBarPressListener() {
            @Override
            public void setChecked(boolean z) {
                PreferencesUtils.setIsOpenRecoveryScriptEnabled(z);
                final SeslPreferenceGroup parent = getParent(((SeslPreferenceFragmentCompat) mFragment).getPreferenceScreen(), ((SeslPreferenceFragmentCompat) mFragment).findPreference("mesa_ors_innerscreencateg"));
                parent.setEnabled(z);
            }
        };
    }

    @Override
    public boolean getSwitchBarDefaultStatus() {
        return PreferencesUtils.getIsOpenRecoveryScriptEnabled();
    }

}
