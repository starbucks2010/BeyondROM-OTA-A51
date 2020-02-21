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

import com.mesalabs.on.update.R;
import com.mesalabs.cerberus.utils.LogUtils;
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
    boolean mIsStrokeRoundedCorner = true;
    Resources mRes;
    int mRoundRadius = -1;
    Drawable mRoundStrokeBottom;
    int mRoundStrokeHeight;
    Drawable mRoundStrokeTop;
    Rect mRoundedCornerBounds = new Rect();
    int mRoundedCornerMode;
    Drawable mTopLeftRound;
    int mTopLeftRoundColor;
    Drawable mTopRightRound;
    int mTopRightRoundColor;
    int mX;
    int mY;

    public SeslRoundedCorner(Context context) {
        this(context, true);
    }

    public SeslRoundedCorner(Context context, boolean isStrokeRoundedCorner) {
        mContext = context;
        mRes = context.getResources();
        mIsStrokeRoundedCorner = isStrokeRoundedCorner;
        initRoundedCorner();
    }

    public SeslRoundedCorner(Context context, boolean isStrokeRoundedCorner, boolean isMutate) {
        mContext = context;
        mRes = context.getResources();
        mIsStrokeRoundedCorner = isStrokeRoundedCorner;
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

    public void setRoundedCornerColor(int corners, int color) {
        if (mIsStrokeRoundedCorner) {
            LogUtils.d("SeslRoundedCorner", "can not change round color on stroke rounded corners");
        } else if (corners == 0) {
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
        if (darkTheme) {
            mIsStrokeRoundedCorner = false;
        }
        LogUtils.d("SeslRoundedCorner", "initRoundedCorner, rounded corner with stroke = " + mIsStrokeRoundedCorner + ", dark theme = " + darkTheme + ", mutate " + mIsMutate);
        int color;
        if (mIsStrokeRoundedCorner) {
            color = mRes.getColor(R.color.sesl_round_and_bgcolor_light, mContext.getTheme());
            mBottomRightRoundColor = color;
            mBottomLeftRoundColor = color;
            mTopRightRoundColor = color;
            mTopLeftRoundColor = color;
            mTopLeftRound = mRes.getDrawable(R.drawable.sesl_top_left_round_stroke, mContext.getTheme());
            mTopRightRound = mRes.getDrawable(R.drawable.sesl_top_right_round_stroke, mContext.getTheme());
            mBottomLeftRound = mRes.getDrawable(R.drawable.sesl_bottom_left_round_stroke, mContext.getTheme());
            mBottomRightRound = mRes.getDrawable(R.drawable.sesl_bottom_right_round_stroke, mContext.getTheme());
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

    public int getRoundedCornerRadius() {
        return mRoundRadius;
    }

    public void drawRoundedCorner(Canvas canvas) {
        canvas.getClipBounds(mRoundedCornerBounds);
        if ((mRoundedCornerMode & 1) != 0) {
            if (mIsStrokeRoundedCorner) {
                mRoundStrokeTop.setBounds(0, mRoundedCornerBounds.top, mRoundedCornerBounds.right, mRoundedCornerBounds.top + mRoundStrokeHeight);
                mRoundStrokeTop.draw(canvas);
            }
            mTopLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.top, mRoundedCornerBounds.left + mRoundRadius, mRoundedCornerBounds.top + mRoundRadius);
            mTopLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 2) != 0) {
            mTopRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.top, mRoundedCornerBounds.right, mRoundedCornerBounds.top + mRoundRadius);
            mTopRightRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 4) != 0) {
            if (mIsStrokeRoundedCorner) {
                mRoundStrokeBottom.setBounds(0, mRoundedCornerBounds.bottom - mRoundStrokeHeight, mRoundedCornerBounds.right, mRoundedCornerBounds.bottom);
                mRoundStrokeBottom.draw(canvas);
            }
            mBottomLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.bottom - mRoundRadius, mRoundedCornerBounds.left + mRoundRadius, mRoundedCornerBounds.bottom);
            mBottomLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 8) != 0) {
            mBottomRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.bottom - mRoundRadius, mRoundedCornerBounds.right, mRoundedCornerBounds.bottom);
            mBottomRightRound.draw(canvas);
        }
    }

    public void drawRoundedCorner(View view, Canvas canvas) {
        if (view.getTranslationY() != 0.0f) {
            mX = Math.round(view.getX());
            mY = Math.round(view.getY());
        } else {
            mX = view.getLeft();
            mY = view.getTop();
        }
        mRoundedCornerBounds.set(mX, mY, mX + view.getWidth(), mY + view.getHeight());
        if ((mRoundedCornerMode & 1) != 0) {
            if (mIsStrokeRoundedCorner) {
                mRoundStrokeTop.setBounds(0, mRoundedCornerBounds.top, mRoundedCornerBounds.right, mRoundedCornerBounds.top + mRoundStrokeHeight);
                mRoundStrokeTop.draw(canvas);
            }
            mTopLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.top, mRoundedCornerBounds.left + mRoundRadius, mRoundedCornerBounds.top + mRoundRadius);
            mTopLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 2) != 0) {
            mTopRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.top, mRoundedCornerBounds.right, mRoundedCornerBounds.top + mRoundRadius);
            mTopRightRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 4) != 0) {
            if (mIsStrokeRoundedCorner) {
                mRoundStrokeBottom.setBounds(0, mRoundedCornerBounds.bottom - mRoundStrokeHeight, mRoundedCornerBounds.right, mRoundedCornerBounds.bottom);
                mRoundStrokeBottom.draw(canvas);
            }
            mBottomLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.bottom - mRoundRadius, mRoundedCornerBounds.left + mRoundRadius, mRoundedCornerBounds.bottom);
            mBottomLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 8) != 0) {
            mBottomRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.bottom - mRoundRadius, mRoundedCornerBounds.right, mRoundedCornerBounds.bottom);
            mBottomRightRound.draw(canvas);
        }
    }
}
