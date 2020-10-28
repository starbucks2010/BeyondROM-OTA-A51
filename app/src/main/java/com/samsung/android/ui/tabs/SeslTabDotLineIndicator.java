package com.samsung.android.ui.tabs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.samsung.android.ui.tabs.SeslAbsIndicatorView;

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

public class SeslTabDotLineIndicator extends SeslAbsIndicatorView {
    private int mDiameter;
    private int mInterval;
    private Paint mPaint;
    private float mScaleFrom;
    private final float mScaleFromDiff;
    private int mWidth;

    public SeslTabDotLineIndicator(Context context) {
        this(context, null);
    }

    public SeslTabDotLineIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeslTabDotLineIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslTabDotLineIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mDiameter = (int) TypedValue.applyDimension(1, 2.5f, context.getResources().getDisplayMetrics());
        mInterval = (int) TypedValue.applyDimension(1, 2.5f, context.getResources().getDisplayMetrics());

        mScaleFromDiff = TypedValue.applyDimension(1, 5.0f, context.getResources().getDisplayMetrics());

        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    void onHide() {
        setAlpha(0.0f);
    }

    @Override
    void onShow() {
        startReleaseEffect();
    }

    @Override
    void startPressEffect() {
        setAlpha(1.0f);
        invalidate();
    }

    @Override
    void startReleaseEffect() {
        setAlpha(1.0f);
    }

    private void updateDotLineScaleFrom() {
        if (mWidth != getWidth() || mWidth == 0) {
            mWidth = getWidth();
            if (mWidth <= 0) {
                mScaleFrom = 0.9f;
            } else {
                mScaleFrom = (((float) mWidth) - mScaleFromDiff) / ((float) mWidth);
            }
        }
    }

    @Override
    void onSetSelectedIndicatorColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateDotLineScaleFrom();
        if ((isPressed() || isSelected()) && (getBackground() instanceof ColorDrawable)) {
            int width = (getWidth() - getPaddingStart()) - getPaddingEnd();
            float height = ((float) getHeight()) / 2.0f;
            canvas.drawRoundRect(0.0f, height - (((float) mDiameter) / 2.0f), (float) width, height + (((float) mDiameter) / 2.0f), (float) mDiameter, (float) mDiameter, this.mPaint);
        }
    }
}
