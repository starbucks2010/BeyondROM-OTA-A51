package com.mesalabs.cerberus.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.mesalabs.cerberus.R;
import com.samsung.android.ui.widget.SeslTooltip;

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

public class ToolbarImageButton extends AppCompatImageButton {
    private int mNavigationBarHeight;
    private boolean mIcon;
    private String mToolTipText;

    public ToolbarImageButton(Context context) {
        super(context);
    }

    public ToolbarImageButton(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public ToolbarImageButton(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        SeslTooltip.setTooltipNull(false);
        if (mIcon) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_HOVER_ENTER:
                    SeslTooltip.setTooltipText(this, mToolTipText);
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    SeslTooltip.setTooltipNull(true);
                    break;
            }
            setTooltipOffset();
        }
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public void onHoverChanged(boolean hovered) {
        SeslTooltip.setTooltipNull(!hovered);
        setTooltipOffset();
        super.onHoverChanged(hovered);
    }

    @Override
    public boolean performLongClick() {
        if (mIcon) {
            setTooltipOffset();
            return super.performLongClick();
        } else {
            SeslTooltip.setTooltipNull(true);
            return true;
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mIcon = true;
    }

    private boolean checkNaviBarForLandscape() {
        Context context = getContext();
        Resources res = context.getResources();

        Rect displayFrame = new Rect();
        getWindowVisibleDisplayFrame(displayFrame);

        Point displaySize = new Point();
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        display.getRealSize(displaySize);

        int rotate = display.getRotation();
        int navigationBarHeight = (int) res.getDimension(R.dimen.sesl_navigation_bar_height);
        if (rotate == 1 && displayFrame.right + navigationBarHeight >= displaySize.x) {
            setNavigationBarHeight(displaySize.x - displayFrame.right);
            return true;
        } else if (rotate != 3 || displayFrame.left > navigationBarHeight) {
            return false;
        } else {
            setNavigationBarHeight(displayFrame.left);
            return true;
        }
    }

    private int getNavigationBarHeight() {
        return mNavigationBarHeight;
    }

    private void setNavigationBarHeight(int height) {
        mNavigationBarHeight = height;
    }

    public void setTooltipText(String text) {
        SeslTooltip.setTooltipText(this, text);
        mToolTipText = text;
    }

    protected void setTooltipOffset() {
        if (mToolTipText != null) {
            Context context = getContext();
            Resources res = context.getResources();

            int[] screenPos = new int[2];
            getLocationOnScreen(screenPos);

            int width = getWidth();
            int height = getHeight();
            int paddingStart = getPaddingStart();
            int paddingEnd = getPaddingEnd();

            int[] windowPos = new int[2];
            getLocationInWindow(windowPos);

            Rect displayFrame = new Rect();
            getWindowVisibleDisplayFrame(displayFrame);

            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            display.getRealMetrics(realDisplayMetrics);

            int diff = 0;
            View toolbar = (View) getParent();
            if ((toolbar instanceof Toolbar) && toolbar.getWidth() < displayFrame.right - displayFrame.left) {
                diff = (screenPos[0] - windowPos[0]) - displayFrame.left;
            }

            int xOffset;
            int yOffset = windowPos[1] + height;
            int layoutDirection = getLayoutDirection();
            if (layoutDirection == 0) {
                xOffset = (((displayFrame.right - displayFrame.left) - (windowPos[0] + width)) + (((width - paddingStart) - paddingEnd) / 2)) - diff;
                if (checkNaviBarForLandscape()) {
                    xOffset += (int) ((((float) getNavigationBarHeight()) / res.getDisplayMetrics().density) * realDisplayMetrics.density);
                }
            } else {
                xOffset = windowPos[0] + paddingStart + ((paddingEnd - paddingStart) / 2);
            }
            SeslTooltip.setTooltipPosition(xOffset, yOffset, layoutDirection);
        } else
            SeslTooltip.setTooltipNull(true);
    }

}
