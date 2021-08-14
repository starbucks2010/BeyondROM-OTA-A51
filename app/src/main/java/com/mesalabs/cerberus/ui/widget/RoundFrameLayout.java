package com.mesalabs.cerberus.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.mesalabs.ten.update.R;
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

public class RoundFrameLayout extends FrameLayout {
    private Context mContext;
    private boolean mIsNightMode;
    SeslRoundedCorner mSeslRoundedCorner;

    public RoundFrameLayout(Context context) {
        super(context);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        mIsNightMode = Utils.isNightMode(mContext);

        TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout);

        int roundedCorners = obtainStyledAttributes.getInt(R.styleable.RoundFrameLayout_roundedCorners, 15);

        mSeslRoundedCorner = new SeslRoundedCorner(mContext);
        mSeslRoundedCorner.setRoundedCorners(roundedCorners);

        obtainStyledAttributes.recycle();
    }

    public RoundFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mSeslRoundedCorner.drawRoundedCorner(canvas);
    }

}
