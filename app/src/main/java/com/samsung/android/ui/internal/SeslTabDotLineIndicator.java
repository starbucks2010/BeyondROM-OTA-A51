package com.samsung.android.ui.internal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;

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
    private int mDiameter = 1;
    private int mInterval = 2;
    private Paint mPaint = new Paint();
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

        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mScaleFromDiff = TypedValue.applyDimension(1, 5.0f, context.getResources().getDisplayMetrics());
    }

    @Override
    void onHide() {
        setAlpha(0.0f);
    }

    @Override
    public void setHideImmediatly() {
        setAlpha(0.0f);
    }

    @Override
    void onShow() {
        onReleased();
    }

    @Override
    void onPressed() {
        setAlpha(1.0f);
        invalidate();
    }

    @Override
    void onReleased() {
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
            int width = getWidth();
            int height = getHeight();
            int circleCount = ((width - mDiameter) / (mInterval + mDiameter)) + 1;
            int spaceCount = circleCount - 1;
            int startInterval = (int) ((((float) mDiameter) / 2.0f) + 0.5f);
            int leftSpace = (width - mDiameter) - ((mInterval + mDiameter) * (circleCount - 1));
            if (mDiameter % 2 != 0) {
                leftSpace--;
            }
            int offSet = 0;
            int offSetOnePixel = 0;
            if (spaceCount > 0) {
                offSet = leftSpace / spaceCount;
                offSetOnePixel = leftSpace % spaceCount;
            }
            int dis = 0;
            for (int i = 0; i < circleCount; i++) {
                canvas.drawCircle((float) (startInterval + dis), (float) (height / 2), ((float) mDiameter) / 2.0f, mPaint);
                dis += mDiameter + mInterval + offSet;
                if (i < offSetOnePixel) {
                    dis++;
                }
            }
        }
    }
}
