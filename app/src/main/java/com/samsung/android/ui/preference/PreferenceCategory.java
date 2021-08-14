package com.samsung.android.ui.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.core.content.res.TypedArrayUtils;

import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.utils.LogUtils;

/*
 * Cerberus Core App
 *
 * Coded by Samsung. All rights reserved to their respective owners.
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

public class PreferenceCategory extends SeslPreferenceGroup {
    private static final String TAG = "PreferenceCategory";
    private String mHeader;

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mHeader = "Header";
    }

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PreferenceCategory(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.preferenceCategoryStyle, android.R.attr.preferenceCategoryStyle));
        try {
            mHeader = context.getString(R.string.sesl_preferencecategory_added_title);
        } catch (Exception e) {
            LogUtils.d(TAG, "Can not find the string. Please updates latest sesl-appcompat library, ", e);
        }
    }

    @Override
    protected boolean onPrepareAddPreference(SeslPreference preference) {
        if (preference instanceof PreferenceCategory) {
            throw new IllegalArgumentException("Cannot add a " + TAG + " directly to a " + TAG);
        }

        return super.onPrepareAddPreference(preference);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean shouldDisableDependents() {
        return !super.isEnabled();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        if (titleView != null) {
            titleView.setContentDescription(titleView.getText().toString() + ", " + mHeader);
        }
        if (mIsSolidRoundedCorner && titleView != null) {
            titleView.setBackgroundColor(mSubheaderColor);
        }
    }
}
