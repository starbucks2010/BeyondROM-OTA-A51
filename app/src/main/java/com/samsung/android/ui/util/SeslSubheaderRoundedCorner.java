package com.samsung.android.ui.util;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

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

public class SeslSubheaderRoundedCorner extends SeslRoundedCorner {
    public SeslSubheaderRoundedCorner(Context context) {
        super(context);
    }

    public SeslSubheaderRoundedCorner(Context context, boolean isStrokeRoundedCorner) {
        super(context, isStrokeRoundedCorner);
    }

    public void drawRoundedCorner(int left, int top, int right, int bottom, Canvas canvas) {
        mRoundedCornerBounds.set(left, top, right, bottom);
        if ((mRoundedCornerMode & 1) != 0) {
            if (mIsStrokeRoundedCorner) {
                mRoundStrokeBottom.setBounds(0, mRoundedCornerBounds.bottom, mRoundedCornerBounds.right, mRoundedCornerBounds.bottom + mRoundStrokeHeight);
                mRoundStrokeBottom.draw(canvas);
            }
            mTopLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.bottom, mRoundedCornerBounds.left + mRoundRadius, mRoundedCornerBounds.bottom + mRoundRadius);
            mTopLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 2) != 0) {
            mTopRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.bottom, mRoundedCornerBounds.right, mRoundedCornerBounds.bottom + mRoundRadius);
            mTopRightRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 4) != 0) {
            if (mIsStrokeRoundedCorner) {
                mRoundStrokeTop.setBounds(0, mRoundedCornerBounds.top - mRoundStrokeHeight, mRoundedCornerBounds.right, mRoundedCornerBounds.top);
                mRoundStrokeTop.draw(canvas);
            }
            mBottomLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.top - mRoundRadius, mRoundedCornerBounds.left + mRoundRadius, mRoundedCornerBounds.top);
            mBottomLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 8) != 0) {
            mBottomRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.top - mRoundRadius, mRoundedCornerBounds.right, mRoundedCornerBounds.top);
            mBottomRightRound.draw(canvas);
        }
    }

    @Override
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
                mRoundStrokeBottom.setBounds(0, mRoundedCornerBounds.bottom, mRoundedCornerBounds.right, mRoundedCornerBounds.bottom + mRoundStrokeHeight);
                mRoundStrokeBottom.draw(canvas);
            }
            mTopLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.bottom, mRoundedCornerBounds.left + mRoundRadius, mRoundedCornerBounds.bottom + mRoundRadius);
            mTopLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 2) != 0) {
            mTopRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.bottom, mRoundedCornerBounds.right, mRoundedCornerBounds.bottom + mRoundRadius);
            mTopRightRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 4) != 0) {
            if (mIsStrokeRoundedCorner) {
                mRoundStrokeTop.setBounds(0, mRoundedCornerBounds.top - mRoundStrokeHeight, mRoundedCornerBounds.right, mRoundedCornerBounds.top);
                mRoundStrokeTop.draw(canvas);
            }
            mBottomLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.top - mRoundRadius, mRoundedCornerBounds.left + mRoundRadius, mRoundedCornerBounds.top);
            mBottomLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 8) != 0) {
            mBottomRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.top - mRoundRadius, mRoundedCornerBounds.right, mRoundedCornerBounds.top);
            mBottomRightRound.draw(canvas);
        }
    }
}
