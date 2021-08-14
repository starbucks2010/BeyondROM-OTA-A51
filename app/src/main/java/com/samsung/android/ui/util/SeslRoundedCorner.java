package com.samsung.android.ui.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.utils.LogUtils;
import com.mesalabs.cerberus.utils.Utils;

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

public class SeslRoundedCorner {
    Drawable mBottomLeftRound;
    int mBottomLeftRoundColor;
    Drawable mBottomRightRound;
    int mBottomRightRoundColor;
    Context mContext;
    boolean mIsMutate = false;
    Resources mRes;
    int mRoundRadius = -1;
    Rect mRoundedCornerBounds = new Rect();
    int mRoundedCornerMode;
    Drawable mTopLeftRound;
    int mTopLeftRoundColor;
    Drawable mTopRightRound;
    int mTopRightRoundColor;
    int mX;
    int mY;

    public SeslRoundedCorner(Context context) {
        mContext = context;
        mRes = context.getResources();
        initRoundedCorner();
    }

    public SeslRoundedCorner(Context context, boolean isMutate) {
        mContext = context;
        mRes = context.getResources();
        mIsMutate = isMutate;
        initRoundedCorner();
    }

    public void setRoundedCorners(int corners) {
        if ((corners & -16) == 0) {
            mRoundedCornerMode = corners;
            if (mTopLeftRound == null || mTopRightRound == null || mBottomLeftRound == null || mBottomRightRound == null)
                initRoundedCorner();
        } else
            throw new IllegalArgumentException("Use wrong rounded corners to the param, corners = " + corners);
    }

    public int getRoundedCorners() {
        return mRoundedCornerMode;
    }

    public void setRoundedCornerColor(int corners, int color) {
        if (corners == 0) {
            throw new IllegalArgumentException("There is no rounded corner on = " + this);
        } else if ((corners & -16) == 0) {
            if (!(color == mTopLeftRoundColor && color == mBottomLeftRoundColor)) {
                LogUtils.d("SeslRoundedCorner", "change color = " + color + ", on =" + corners + ", top color = " + mTopLeftRoundColor + ", bottom color = " + mBottomLeftRoundColor);
            }
            if (mTopLeftRound == null || mTopRightRound == null || mBottomLeftRound == null || mBottomRightRound == null)
                initRoundedCorner();
            PorterDuffColorFilter pdcf = new PorterDuffColorFilter(color, Mode.SRC_IN);
            if ((corners & 1) != 0) {
                mTopLeftRoundColor = color;
                mTopLeftRound.setColorFilter(pdcf);
            }
            if ((corners & 2) != 0) {
                mTopRightRoundColor = color;
                mTopRightRound.setColorFilter(pdcf);
            }
            if ((corners & 4) != 0) {
                mBottomLeftRoundColor = color;
                mBottomLeftRound.setColorFilter(pdcf);
            }
            if ((corners & 8) != 0) {
                mBottomRightRoundColor = color;
                mBottomRightRound.setColorFilter(pdcf);
            }
        } else {
            throw new IllegalArgumentException("Use wrong rounded corners to the param, corners = " + corners);
        }
    }

    private void initRoundedCorner() {
        mRoundRadius = (int) TypedValue.applyDimension(1, (float) 26, mRes.getDisplayMetrics());
        boolean darkTheme = Utils.isNightMode(mContext);

        if (mIsMutate) {
            mTopLeftRound = mRes.getDrawable(R.drawable.sesl_top_left_round, mContext.getTheme()).mutate();
            mTopRightRound = mRes.getDrawable(R.drawable.sesl_top_right_round, mContext.getTheme()).mutate();
            mBottomLeftRound = mRes.getDrawable(R.drawable.sesl_bottom_left_round, mContext.getTheme()).mutate();
            mBottomRightRound = mRes.getDrawable(R.drawable.sesl_bottom_right_round, mContext.getTheme()).mutate();
        } else {
            mTopLeftRound = mRes.getDrawable(R.drawable.sesl_top_left_round, mContext.getTheme());
            mTopRightRound = mRes.getDrawable(R.drawable.sesl_top_right_round, mContext.getTheme());
            mBottomLeftRound = mRes.getDrawable(R.drawable.sesl_bottom_left_round, mContext.getTheme());
            mBottomRightRound = mRes.getDrawable(R.drawable.sesl_bottom_right_round, mContext.getTheme());
        }

        int color = mRes.getColor(darkTheme ? R.color.sesl_round_and_bgcolor_dark : R.color.sesl_round_and_bgcolor_light, null);

        mBottomRightRoundColor = color;
        mBottomLeftRoundColor = color;
        mTopRightRoundColor = color;
        mTopLeftRoundColor = color;
    }

    public void drawRoundedCorner(Canvas canvas) {
        canvas.getClipBounds(mRoundedCornerBounds);
        drawRoundedCornerInternal(canvas);
    }

    public void drawRoundedCorner(View view, Canvas canvas) {
        if (view.getTranslationY() != 0.0f) {
            mX = Math.round(view.getX());
            mY = Math.round(view.getY());
        } else {
            mX = view.getLeft();
            mY = view.getTop();
        }
        Rect rect = mRoundedCornerBounds;
        rect.set(mX, mY, view.getWidth() + mX, mY + view.getHeight());
        drawRoundedCornerInternal(canvas);
    }

    private void drawRoundedCornerInternal(Canvas canvas) {
        if ((mRoundedCornerMode & 1) != 0) {
            mTopLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.top, mRoundedCornerBounds.left + mRoundRadius, mRoundRadius + mRoundedCornerBounds.top);
            mTopLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 2) != 0) {
            mTopRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.top, mRoundedCornerBounds.right, mRoundRadius + mRoundedCornerBounds.top);
            mTopRightRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 4) != 0) {
            mBottomLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.bottom - mRoundRadius, mRoundRadius + mRoundedCornerBounds.left, mRoundedCornerBounds.bottom);
            mBottomLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 8) != 0) {
            mBottomRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.bottom - mRoundRadius, mRoundedCornerBounds.right, mRoundedCornerBounds.bottom);
            mBottomRightRound.draw(canvas);
        }
    }
}
