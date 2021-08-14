package com.samsung.android.ui.preference.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.mesalabs.ten.update.R;

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

@SuppressLint("AppCompatCustomView")
public class SeslPreferenceImageView extends ImageView {
    private int mMaxWidth = Integer.MAX_VALUE;
    private int mMaxHeight = Integer.MAX_VALUE;

    public SeslPreferenceImageView(Context context) {
        this(context, null);
    }

    public SeslPreferenceImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeslPreferenceImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeslPreferenceImageView, defStyleAttr, 0);
        setMaxWidth(a.getDimensionPixelSize(R.styleable.SeslPreferenceImageView_maxWidth, Integer.MAX_VALUE));
        setMaxHeight(a.getDimensionPixelSize(R.styleable.SeslPreferenceImageView_maxHeight, Integer.MAX_VALUE));
        a.recycle();
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        mMaxWidth = maxWidth;
        super.setMaxWidth(maxWidth);
    }

    @Override
    public int getMaxWidth() {
        return mMaxWidth;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        mMaxHeight = maxHeight;
        super.setMaxHeight(maxHeight);
    }

    @Override
    public int getMaxHeight() {
        return mMaxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            final int maxWidth = getMaxWidth();
            if (maxWidth != Integer.MAX_VALUE && (maxWidth < widthSize || widthMode == MeasureSpec.UNSPECIFIED)) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST);
            }
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            final int maxHeight = getMaxHeight();
            if (maxHeight != Integer.MAX_VALUE && (maxHeight < heightSize || heightMode == MeasureSpec.UNSPECIFIED)) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
