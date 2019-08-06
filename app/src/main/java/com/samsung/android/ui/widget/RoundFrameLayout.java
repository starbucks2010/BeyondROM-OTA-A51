package com.samsung.android.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.utils.Utils;
import com.samsung.android.ui.util.SeslRoundedCorner;

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

public class RoundFrameLayout extends FrameLayout {
    private Context mContext;
    SeslRoundedCorner mSeslRoundedCorner;

    public RoundFrameLayout(Context context) {
        super(context);
    }

    public RoundFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.RoundFrameLayout);

        boolean cornersStroke = obtainStyledAttributes.getBoolean(R.styleable.RoundFrameLayout_cornersStroke, true);
        String roundedCorners = obtainStyledAttributes.getString(R.styleable.RoundFrameLayout_roundedCorners);

        mSeslRoundedCorner = new SeslRoundedCorner(mContext, cornersStroke);
        mSeslRoundedCorner.setRoundedCorners(getCornersInt(roundedCorners));
        if (!cornersStroke)
            mSeslRoundedCorner.setRoundedCornerColor(getCornersInt(roundedCorners), getResources().getColor(Utils.isNightMode(mContext) ? R.color.sesl_round_and_bgcolor_dark : R.color.sesl_round_and_bgcolor_light, mContext.getTheme()));

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

    private int getCornersInt(String cornersS) {
        int corners = 15;

        if (cornersS != null && !cornersS.isEmpty()) {
            if (cornersS.equals("none"))
                corners = 0;
            else {
                if (!cornersS.contains("topleft"))
                    corners -= 1;
                if (!cornersS.contains("topright"))
                    corners -= 2;
                if (!cornersS.contains("bottomleft"))
                    corners -= 4;
                if (!cornersS.contains("bottomright"))
                    corners -= 8;
            }
        }

        return corners;
    }
}
