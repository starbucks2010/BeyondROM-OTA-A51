package com.mesalabs.cerberus.base;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.app.AppCompatActivity;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.ui.utils.ActionBarUtils;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2019
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

public class AppBarActivity extends AppCompatActivity {
    protected Context mContext;
    protected ActionBarUtils appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (appBar != null)
            appBar.resetAppBarHeight();
    }

    public void setContentView(View view) {
        super.setContentView(R.layout.mesa_baselayout_appbaractivity);

        appBar = new ActionBarUtils(this);
        appBar.initAppBar(getIsAppBarExpanded(), getAppBarCustomHeaderId());

        if (view != null) {
            ViewGroup root = findViewById(R.id.mesa_container_baseactivity);
            if (root != null) {
                root.addView(view);
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.mesa_baselayout_appbaractivity);

        appBar = new ActionBarUtils(this);
        appBar.initAppBar(getIsAppBarExpanded(), getAppBarCustomHeaderId());

        View layout = getLayoutInflater().inflate(layoutResID, null);
        ViewGroup root = findViewById(R.id.mesa_container_baseactivity);
        if (root != null) {
            root.addView(layout);
        }
    }

    protected int getAppBarCustomHeaderId() {
        return 0;
    }

    protected boolean getIsAppBarExpanded() {
        return true;
    }

}
