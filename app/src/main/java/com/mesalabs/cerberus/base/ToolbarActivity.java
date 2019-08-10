package com.mesalabs.cerberus.base;

import android.content.Context;
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

public class ToolbarActivity extends AppCompatActivity {
    protected Context mContext;
    protected ActionBarUtils toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(R.layout.mesa_baselayout_toolbaractivity);

        toolBar = new ActionBarUtils(this);
        toolBar.initToolBar();

        if (view != null) {
            ViewGroup root = findViewById(R.id.mesa_container_baseactivity);
            if (root != null) {
                root.addView(view);
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.mesa_baselayout_toolbaractivity);

        toolBar = new ActionBarUtils(this);
        toolBar.initToolBar();

        View layout = getLayoutInflater().inflate(layoutResID, null);
        ViewGroup root = findViewById(R.id.mesa_container_baseactivity);
        if (root != null) {
            root.addView(layout);
        }
    }

    public void setBaseContentView(View view) {
        super.setContentView(view);
    }

    public void setBaseContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

}