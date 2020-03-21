package com.mesalabs.on.update.activity.aboutpage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mesalabs.cerberus.base.BaseAppBarActivity;
import com.mesalabs.cerberus.ui.callback.OnSingleClickListener;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.fragment.aboutpage.CreditsFragment;

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

public class CreditsActivity extends BaseAppBarActivity {
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mesa_layout_creditsactivity);

        appBar.setTitleText(getString(R.string.mesa_credits));
        appBar.setHomeAsUpButton(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                onBackPressed();
            }
        });

        inflateFragment();

        TextView desc = findViewById(R.id.mesa_textview_creditsactivity);
        desc.setPadding(desc.getPaddingLeft(), desc.getPaddingTop() - appBar.getAppBarLayout().getPaddingBottom(), desc.getPaddingRight(), desc.getPaddingBottom());

    }

    private void inflateFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("root");
        if (mFragment != null) {
            transaction.hide(mFragment);
        }
        if (fragment != null) {
            mFragment = fragment;
            transaction.show(fragment);
        } else {
            mFragment = new CreditsFragment();
            transaction.add(R.id.mesa_fragmentcontainer_creditsactivity, mFragment, "root");
        }
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    protected boolean getIsAppBarExpanded() {
        return false;
    }

}
