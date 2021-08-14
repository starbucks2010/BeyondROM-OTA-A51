package com.mesalabs.cerberus.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mesalabs.ten.update.R;
import com.samsung.android.ui.preference.PreferenceCategory;
import com.samsung.android.ui.preference.PreferenceViewHolder;

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

public class InsetPreferenceCategory extends PreferenceCategory {
    private int mHeight;

    public InsetPreferenceCategory(Context context) {
        this(context, null);
    }

    public InsetPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHeight = (int) context.getResources().getDimension(R.dimen.mesa_widget_inset_category_height);
        if (attrs != null) {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.InsetPreferenceCategory);
            mHeight = styledAttrs.getDimensionPixelSize(R.styleable.InsetPreferenceCategory_height, mHeight);
            styledAttrs.recycle();

            TypedArray categoryAttrs = context.obtainStyledAttributes(attrs, R.styleable.SeslPreferenceCategory);
            seslSetSubheaderRoundedBg(categoryAttrs.getInt(R.styleable.SeslPreferenceCategory_roundStroke, 15));
            categoryAttrs.recycle();
        }
    }
    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = mHeight;
        holder.itemView.setLayoutParams(params);
    }

    public void setHeight(int height) {
        if (height >= 0) {
            mHeight = height;
        }
    }
}