package com.samsung.android.ui.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;

import com.mesalabs.cerberus.R;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2019
 * Originally coded by Samsung. All rights reserved to their respective owners.
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

public class SeslRoundedCornerWoStroke extends SeslRoundedCorner {
    public SeslRoundedCornerWoStroke(Context context) {
        super(context, false);
    }

    public SeslRoundedCornerWoStroke(Context context, boolean isStrokeRoundedCorner) {
        super(context, false);
    }

    public SeslRoundedCornerWoStroke(Context context, boolean isStrokeRoundedCorner, boolean isMutate) {
        super(context, false, isMutate);
    }

    // We don't want stroke, so we don't enable it neither if light theme is set
    @Override
    private void initRoundedCorner() {
        mRoundRadius = (int) TypedValue.applyDimension(1, (float) 26, mRes.getDisplayMetrics());
        boolean darkTheme = true;
        TypedValue outValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.isLightTheme, outValue, true);
        if (outValue.data != 0) {
            darkTheme = false;
        }
        mIsStrokeRoundedCorner = false;
        Log.d("SeslRoundedCornerWoStroke", "initRoundedCorner, rounded corner without stroke, dark theme = " + darkTheme + ", mutate " + mIsMutate);
        int color;
        if (!darkTheme) {
            color = mRes.getColor(R.color.sesl_round_and_bgcolor_light, mContext.getTheme());
            mBottomRightRoundColor = color;
            mBottomLeftRoundColor = color;
            mTopRightRoundColor = color;
            mTopLeftRoundColor = color;
            mTopLeftRound = mRes.getDrawable(R.drawable.sesl_top_left_round, mContext.getTheme());
            mTopRightRound = mRes.getDrawable(R.drawable.sesl_top_right_round, mContext.getTheme());
            mBottomLeftRound = mRes.getDrawable(R.drawable.sesl_bottom_left_round, mContext.getTheme());
            mBottomRightRound = mRes.getDrawable(R.drawable.sesl_bottom_right_round, mContext.getTheme());
        } else if (mIsMutate) {
            color = mRes.getColor(R.color.sesl_round_and_bgcolor_dark, mContext.getTheme());
            mBottomRightRoundColor = color;
            mBottomLeftRoundColor = color;
            mTopRightRoundColor = color;
            mTopLeftRoundColor = color;
            mTopLeftRound = mRes.getDrawable(R.drawable.sesl_top_left_round, mContext.getTheme()).mutate();
            mTopRightRound = mRes.getDrawable(R.drawable.sesl_top_right_round, mContext.getTheme()).mutate();
            mBottomLeftRound = mRes.getDrawable(R.drawable.sesl_bottom_left_round, mContext.getTheme()).mutate();
            mBottomRightRound = mRes.getDrawable(R.drawable.sesl_bottom_right_round, mContext.getTheme()).mutate();
        } else {
            color = mRes.getColor(R.color.sesl_round_and_bgcolor_dark, mContext.getTheme());
            mBottomRightRoundColor = color;
            mBottomLeftRoundColor = color;
            mTopRightRoundColor = color;
            mTopLeftRoundColor = color;
            mTopLeftRound = mRes.getDrawable(R.drawable.sesl_top_left_round, mContext.getTheme());
            mTopRightRound = mRes.getDrawable(R.drawable.sesl_top_right_round, mContext.getTheme());
            mBottomLeftRound = mRes.getDrawable(R.drawable.sesl_bottom_left_round, mContext.getTheme());
            mBottomRightRound = mRes.getDrawable(R.drawable.sesl_bottom_right_round, mContext.getTheme());
        }
        Drawable drawable = mRes.getDrawable(R.drawable.sesl_round_stroke, mContext.getTheme());
        mRoundStrokeBottom = drawable;
        mRoundStrokeTop = drawable;
        mRoundStrokeHeight = mRes.getDimensionPixelSize(R.dimen.sesl_round_stroke_height);
    }
}
