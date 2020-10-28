package com.samsung.android.ui.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
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

    public void drawRoundedCorner(int left, int top, int right, int bottom, Canvas canvas) {
        mRoundedCornerBounds.set(left, top, right, bottom);
        drawRoundedCornerInternal(canvas);
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
        mRoundedCornerBounds.set(mX, this.mY, view.getWidth() + mX, mY + view.getHeight());
        drawRoundedCornerInternal(canvas);
    }

    private void drawRoundedCornerInternal(Canvas canvas) {
        if ((mRoundedCornerMode & 1) != 0) {
            mTopLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.bottom, mRoundedCornerBounds.left + mRoundRadius, mRoundRadius + mRoundedCornerBounds.bottom);
            mTopLeftRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 2) != 0) {
            mTopRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.bottom, mRoundedCornerBounds.right, mRoundRadius + mRoundedCornerBounds.bottom);
            mTopRightRound.draw(canvas);
        }
        if ((mRoundedCornerMode & 4) != 0) {
            mBottomLeftRound.setBounds(mRoundedCornerBounds.left, mRoundedCornerBounds.top - mRoundRadius, mRoundRadius + mRoundedCornerBounds.left, mRoundedCornerBounds.top);
            mBottomLeftRound.draw(canvas);
        }
        if ((this.mRoundedCornerMode & 8) != 0) {
            mBottomRightRound.setBounds(mRoundedCornerBounds.right - mRoundRadius, mRoundedCornerBounds.top - mRoundRadius, mRoundedCornerBounds.right, mRoundedCornerBounds.top);
            mBottomRightRound.draw(canvas);
        }
    }
}
