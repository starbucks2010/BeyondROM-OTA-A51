package com.mesalabs.cerberus.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.mesalabs.ten.update.R;
import com.mesalabs.cerberus.ui.utils.ActionBarUtils;
import com.mesalabs.cerberus.utils.ViewUtils;

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

public class BaseToolbarActivity extends AppCompatActivity {
    protected Context mContext;
    protected ActionBarUtils toolBar;
    protected ViewGroup root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ViewUtils.hideStatusBarForLandscape(this, getResources().getConfiguration().orientation);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        ViewUtils.hideStatusBarForLandscape(this, newConfig.orientation);

        if (root != null)
            ViewUtils.updateListBothSideMargin(this, root);
    }


    @Override
    public void setContentView(View view) {
        super.setContentView(R.layout.mesa_baselayout_basetoolbaractivity);

        toolBar = new ActionBarUtils(this);
        toolBar.initToolBar();

        if (view != null) {
            root = findViewById(R.id.mesa_container_baseactivity);
            if (root != null) {
                root.addView(view);
                ViewUtils.updateListBothSideMargin(this, root);
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.mesa_baselayout_basetoolbaractivity);

        toolBar = new ActionBarUtils(this);
        toolBar.initToolBar();

        View layout = getLayoutInflater().inflate(layoutResID, null);
        root = findViewById(R.id.mesa_container_baseactivity);
        if (root != null) {
            root.addView(layout);
            ViewUtils.updateListBothSideMargin(this, root);
        }
    }

    public void setBaseContentView(View view) {
        super.setContentView(view);
    }

    public void setBaseContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    protected void removeViewRoundedCorners() {
        ViewUtils.semSetRoundedCorners(getWindow().getDecorView(), 0);
    }

}