package com.mesalabs.cerberus.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.mesalabs.ten.update.R;
import com.mesalabs.cerberus.utils.ViewUtils;
import com.samsung.android.ui.util.SeslRoundedCorner;
import com.samsung.android.ui.widget.SeslNestedScrollView;

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

public class RoundNestedScrollView extends SeslNestedScrollView {
    private Context mContext;
    SeslRoundedCorner mSeslRoundedCorner;

    public RoundNestedScrollView(Context context) {
        super(context);
    }

    public RoundNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.RoundNestedScrollView);

        int roundedCorners = obtainStyledAttributes.getInt(R.styleable.RoundNestedScrollView_roundedCorners, 15);

        mSeslRoundedCorner = new SeslRoundedCorner(mContext, false);
        mSeslRoundedCorner.setRoundedCorners(roundedCorners);
        mSeslRoundedCorner.setRoundedCornerColor(roundedCorners, ViewUtils.getRoundAndBgColor(mContext));

        obtainStyledAttributes.recycle();
    }

    public RoundNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mSeslRoundedCorner.drawRoundedCorner(canvas);
    }
}
