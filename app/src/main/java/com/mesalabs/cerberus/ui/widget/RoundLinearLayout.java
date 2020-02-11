package com.mesalabs.cerberus.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.utils.Utils;
import com.samsung.android.ui.util.SeslRoundedCorner;

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

public class RoundLinearLayout extends LinearLayout {
    private Context mContext;
    private boolean mIsNightMode;
    SeslRoundedCorner mSeslRoundedCorner;

    public RoundLinearLayout(Context context) {
        super(context);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mIsNightMode = Utils.isNightMode(mContext);

        TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.RoundLinearLayout);

        boolean cornersStroke = obtainStyledAttributes.getBoolean(R.styleable.RoundLinearLayout_cornersStroke, true);
        int roundedCorners = obtainStyledAttributes.getInt(R.styleable.RoundLinearLayout_roundedCorners, 15);

        mSeslRoundedCorner = new SeslRoundedCorner(mContext, cornersStroke);
        mSeslRoundedCorner.setRoundedCorners(roundedCorners);
        if (!cornersStroke) {
            TypedValue value = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.round_and_bgColor, value, true);
            mSeslRoundedCorner.setRoundedCornerColor(roundedCorners, value.data);
        }

        obtainStyledAttributes.recycle();
    }

    public RoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mSeslRoundedCorner.drawRoundedCorner(canvas);
    }
}
