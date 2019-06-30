package com.mesalabs.cerberus.base;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.ViewStub;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.utils.SharedPreferencesUtils;
import com.mesalabs.cerberus.utils.Utils;

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

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

    }

    // AppBar related methods

    /**
     * createAppBar (expanded, no homeAsUp, title)
     */
    protected void createAppBar(int mTitleTextId) {
        createAppBar(true, false, 0, mTitleTextId);
    }

    /**
     * createAppBar (expanded, no homeAsUp, title)
     */
    protected void createAppBar(boolean mIsExpanded, int mTitleTextId) {
        createAppBar(mIsExpanded, false, 0, mTitleTextId);
    }

    /**
     * createAppBar (expanded, homeAsUp, title)
     */
    protected void createAppBar(boolean mIsExpanded, boolean mShowHomeAsUp, int mTitleTextId) {
        createAppBar(mIsExpanded, mShowHomeAsUp, 0, mTitleTextId);
    }

    /**
     * createAppBar (expanded, homeAsUp with custom icon, title)
     */
    protected void createAppBar(boolean mIsExpanded, boolean mShowHomeAsUp, int mCustomHAUicId, int mTitleTextId) {
        Toolbar toolbar = findViewById(R.id.mesa_toolbar_appbarlayout);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("");

            if (mShowHomeAsUp) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                if (mCustomHAUicId != 0) {
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                    getSupportActionBar().setHomeAsUpIndicator(abGenCustomHomeAsUpIcon(mCustomHAUicId));
                } else
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
            } else
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            ViewStub stubAbHeader = findViewById(R.id.mesa_viewstub_appbarlayout);
            stubAbHeader.setLayoutResource(R.layout.mesa_header_appbarlayout);

            stubAbHeader.inflate();

            final AppBarLayout appBarLayout = findViewById(R.id.mesa_appbar_appbarlayout);
            final LinearLayout abExpaTitleLayout = findViewById(R.id.mesa_abheaderlayout_appbarlayout);
            TextView abExpaTitle = findViewById(R.id.mesa_abexpatitle_appbarlayout);
            final TextView abCollTitle = findViewById(R.id.mesa_toolbartitle_appbarlayout);

            appBarLayout.setExpanded(mIsExpanded);

            if (mTitleTextId != 0) {
                abExpaTitle.setText(getString(mTitleTextId));
                abCollTitle.setText(getString(mTitleTextId));
            }

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    float range = (((float) Math.abs(verticalOffset)) / ((float) appBarLayout.getTotalScrollRange())) * 2.0f;
                    abExpaTitleLayout.setAlpha(1.5f - range);
                    abCollTitle.setAlpha(range - 1.0f);
                }
            });

        }
    }

    private Drawable abGenCustomHomeAsUpIcon(int mCustomHAUicId) {
        final Drawable tbCustomIcon = getResources().getDrawable(mCustomHAUicId, mContext.getTheme());
        int c = getResources().getColor(Utils.isNightMode(this) ? R.color.sesl_primary_text_color_dark : R.color.sesl_primary_text_color_light, mContext.getTheme());
        tbCustomIcon.setColorFilter(c, PorterDuff.Mode.SRC_ATOP);
        return tbCustomIcon;
    }

}
