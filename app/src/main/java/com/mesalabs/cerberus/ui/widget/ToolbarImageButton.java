package com.mesalabs.cerberus.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatImageButton;

import com.samsung.android.ui.widget.SeslTooltip;

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

public class ToolbarImageButton extends AppCompatImageButton {
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
        SeslTooltip.seslSetTooltipNull(false);
        if (mIcon) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_HOVER_MOVE:
                case MotionEvent.ACTION_HOVER_ENTER:
                    SeslTooltip.setTooltipText(this, mToolTipText);
                    SeslTooltip.seslSetTooltipForceBelow(true);
                    SeslTooltip.seslSetTooltipForceActionBarPosX(true);
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    SeslTooltip.seslSetTooltipNull(true);
                    SeslTooltip.seslSetTooltipForceBelow(false);
                    SeslTooltip.seslSetTooltipForceActionBarPosX(false);
                    break;
            }
        }
        return super.dispatchGenericMotionEvent(event);
    }

    @Override
    public void onHoverChanged(boolean hovered) {
        SeslTooltip.seslSetTooltipNull(!hovered);
        super.onHoverChanged(hovered);
    }

    @Override
    public boolean performLongClick() {
        if (mIcon) {
            return super.performLongClick();
        } else {
            SeslTooltip.seslSetTooltipNull(true);
            return true;
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mIcon = true;
    }

    public void setTooltipText(String text) {
        SeslTooltip.setTooltipText(this, text);
        mToolTipText = text;
    }

}
