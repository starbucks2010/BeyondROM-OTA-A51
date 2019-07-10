package com.mesalabs.cerberus.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.cerberus.utils.ViewUtils;

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

    private AppBarLayout abAppBarLayout;
    private AppCompatImageButton abHomeButton;
    private float mAppBarHeightDp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.mesa_baseactivity_layout);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        resetAppBarHeight();
    }

    protected void inflateLayout(int layoutId) {
        ViewStub contentViewStub = findViewById(R.id.mesa_viewstub_baseactivity);

        contentViewStub.setLayoutResource(layoutId);

        contentViewStub.inflate();
    }

    // AppBar related methods

    /**
     * createAppBar (expanded, no homeAsUp, title)
     */
    protected void createAppBar(int titleTextId) {
        createAppBar(true, 0, false, titleTextId);
    }

    /**
     * createAppBar (expanded, no homeAsUp, title)
     */
    protected void createAppBar(boolean isExpanded, int titleTextId) {
        createAppBar(isExpanded, 0, false,  titleTextId);
    }

    /**
     * createAppBar (expanded, homeAsUp, title)
     */
    protected void createAppBar(boolean isExpanded, boolean showHomeAsUp, int titleTextId) {
        createAppBar(isExpanded, 0, false,  titleTextId);
    }

    /**
     * createAppBar (expanded, custom header, homeAsUp, title)
     */
    protected void createAppBar(boolean isExpanded, int customHeaderId, boolean showHomeAsUp, int titleTextId) {
        Toolbar abToolbar = findViewById(R.id.mesa_toolbar_appbarlayout);
        setSupportActionBar(abToolbar);
        if (abToolbar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            abAppBarLayout = findViewById(R.id.mesa_appbar_appbarlayout);
            final CollapsingToolbarLayout abCollapsingToolbarLayout = findViewById(R.id.mesa_colltoolbar_appbarlayout);
            
            ViewStub abStubAppBarHeader = findViewById(R.id.mesa_viewstub_appbarlayout);

            if (customHeaderId <= 0)
                abStubAppBarHeader.setLayoutResource(R.layout.mesa_seslheader_appbarlayout);
            else
                abStubAppBarHeader.setLayoutResource(customHeaderId);

            abStubAppBarHeader.inflate();

            final LinearLayout abExpandedTitleLayout = findViewById(R.id.mesa_abheaderlayout_appbarlayout);
            TextView abExpandedTitle = findViewById(R.id.mesa_abexpatitle_appbarlayout);
            abHomeButton = findViewById(R.id.mesa_toolbarhome_appbarlayout);
            final TextView abCollapsedTitle = findViewById(R.id.mesa_toolbartitle_appbarlayout);

            resetAppBarHeight();

            int statusBarHeight = ViewUtils.getStatusbarHeight(this);
            if (statusBarHeight > 0)
                abExpandedTitleLayout.setPadding(0, 0, 0, statusBarHeight / 2);


            abAppBarLayout.setExpanded(isExpanded);

            if (showHomeAsUp) {
                abHomeButton.setVisibility(View.VISIBLE);

                abCollapsedTitle.setPadding(0, 0, 0, 0);

                TooltipCompat.setTooltipText(abHomeButton, getString(R.string.sesl_action_bar_up_description));

                setToolbarHomeIconOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }

            if (titleTextId != 0) {
                if (customHeaderId <= 0)
                    abExpandedTitle.setText(getString(titleTextId));

                abCollapsedTitle.setText(getString(titleTextId));
            }

            abAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @SuppressLint("Range")
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    int layoutPosition = Math.abs(appBarLayout.getTop());
                    float alphaRange = ((float) abCollapsingToolbarLayout.getHeight()) * 0.17999999f;
                    float toolbarTitleAlphaStart = ((float) abCollapsingToolbarLayout.getHeight()) * 0.35f;

                    abExpandedTitleLayout.setTranslationY((float) ((-verticalOffset) / 3));

                    float expaTitleAlpha = 255.0f - ((100.0f / alphaRange) * (((float) layoutPosition) - 0.0f));
                    if (expaTitleAlpha < 0.0f) {
                        expaTitleAlpha = 0.0f;
                    } else if (expaTitleAlpha > 255.0f) {
                        expaTitleAlpha = 255.0f;
                    }
                    expaTitleAlpha /= 255.0f;

                    abExpandedTitleLayout.setAlpha(expaTitleAlpha);

                    if (appBarLayout.getHeight() <= ((int) mAppBarHeightDp)) {
                        abExpandedTitleLayout.setAlpha(0.0f);
                        abCollapsedTitle.setAlpha(1.0f);
                    } else {
                        double collapsedTitleAlpha = (double) ((150.0f / alphaRange) * (((float) layoutPosition) - toolbarTitleAlphaStart));

                        if (collapsedTitleAlpha >= 0.0d && collapsedTitleAlpha <= 255.0d) {
                            collapsedTitleAlpha /= 255.0d;
                            abCollapsedTitle.setAlpha((float) collapsedTitleAlpha);
                        }
                        else if (collapsedTitleAlpha < 0.0d)
                            abCollapsedTitle.setAlpha(0.0f);
                        else
                            abCollapsedTitle.setAlpha(1.0f);
                    }

                }
            });

        }
    }

    private void customizeToolbarHomeIcon(int homeButtonIcId, int homeButtonDescId) {
        if (homeButtonIcId <= 0 || homeButtonDescId <= 0) return;

        final Drawable abHomeButtonIcon = getResources().getDrawable(homeButtonIcId, mContext.getTheme());
        int color = getResources().getColor(Utils.isNightMode(this) ? R.color.sesl_primary_text_color_dark : R.color.sesl_primary_text_color_light, mContext.getTheme());
        abHomeButtonIcon.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        abHomeButton.setImageDrawable(abHomeButtonIcon);

        TooltipCompat.setTooltipText(abHomeButton, getString(homeButtonDescId));
    }

    protected void resetAppBarHeight() {
        if (abAppBarLayout == null) return;

        ViewGroup.LayoutParams params = abAppBarLayout.getLayoutParams();
        int windowHeight = ViewUtils.getWindowHeight(this);

        if (ViewUtils.isLandscape() || Utils.isInMultiWindowMode(this)) {
            abAppBarLayout.setExpanded(false, false);
            abAppBarLayout.setActivated(false);

            mAppBarHeightDp = getResources().getDimension(R.dimen.sesl_action_bar_default_height_padding);

            params.height = (int) mAppBarHeightDp;
        } else {
            abAppBarLayout.setExpanded(true, false);
            abAppBarLayout.setActivated(true);

            TypedValue outValue = new TypedValue();
            getResources().getValue(R.dimen.sesl_abl_height_proportion, outValue, true);

            mAppBarHeightDp = getResources().getDimension(R.dimen.sesl_action_bar_default_height);

            params.height = (int) ((float) windowHeight * outValue.getFloat());
        }

        abAppBarLayout.setLayoutParams(params);

    }

    protected void setToolbarHomeIconOnClickListener(View.OnClickListener ocl) {
        if (abHomeButton == null) return;

        if (ocl != null)
            abHomeButton.setOnClickListener(ocl);
    }

}
