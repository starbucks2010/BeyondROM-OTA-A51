package com.samsung.android.ui.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.SeekBar;

import com.mesalabs.ten.update.R;

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

public class SeslSeekBar extends SeslAbsSeekBar {
    public int mOldValue;
    public SeslSeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;
    public SeslSeekBar.OnSeekBarHoverListener mOnSeekBarHoverListener;

    public SeslSeekBar(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public SeslSeekBar(Context var1, AttributeSet var2) {
        this(var1, var2, R.attr.seekBarStyle);
    }

    public SeslSeekBar(Context var1, AttributeSet var2, int var3) {
        this(var1, var2, var3, 0);
    }

    public SeslSeekBar(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
    }

    public CharSequence getAccessibilityClassName() {
        return SeekBar.class.getName();
    }

    public void onHoverChanged(int var1, int var2, int var3) {
        SeslSeekBar.OnSeekBarHoverListener var4 = this.mOnSeekBarHoverListener;
        if (var4 != null) {
            var4.onHoverChanged(this, var1, true);
        }

        super.onHoverChanged(var1, var2, var3);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
        super.onInitializeAccessibilityNodeInfo(var1);
        if (Build.VERSION.SDK_INT >= 24 && this.canUserSetProgress()) {
            var1.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
        }

    }

    public void onProgressRefresh(float var1, boolean var2, int var3) {
        super.onProgressRefresh(var1, var2, var3);
        SeslSeekBar.OnSeekBarChangeListener var4;
        if (!super.mIsSeamless) {
            var4 = this.mOnSeekBarChangeListener;
            if (var4 != null) {
                var4.onProgressChanged(this, var3, var2);
            }
        } else {
            var3 = Math.round((float)var3 / 1000.0F);
            if (this.mOldValue != var3) {
                this.mOldValue = var3;
                var4 = this.mOnSeekBarChangeListener;
                if (var4 != null) {
                    var4.onProgressChanged(this, var3, var2);
                }
            }
        }

    }

    public void onStartTrackingHover(int var1, int var2, int var3) {
        SeslSeekBar.OnSeekBarHoverListener var4 = this.mOnSeekBarHoverListener;
        if (var4 != null) {
            var4.onStartTrackingHover(this, var1);
        }

        super.onStartTrackingHover(var1, var2, var3);
    }

    public void onStartTrackingTouch() {
        super.onStartTrackingTouch();
        SeslSeekBar.OnSeekBarChangeListener var1 = this.mOnSeekBarChangeListener;
        if (var1 != null) {
            var1.onStartTrackingTouch(this);
        }

    }

    public void onStopTrackingHover() {
        SeslSeekBar.OnSeekBarHoverListener var1 = this.mOnSeekBarHoverListener;
        if (var1 != null) {
            var1.onStopTrackingHover(this);
        }

        super.onStopTrackingHover();
    }

    public void onStopTrackingTouch() {
        super.onStopTrackingTouch();
        SeslSeekBar.OnSeekBarChangeListener var1 = this.mOnSeekBarChangeListener;
        if (var1 != null) {
            var1.onStopTrackingTouch(this);
        }

    }

    public void setOnSeekBarChangeListener(SeslSeekBar.OnSeekBarChangeListener var1) {
        this.mOnSeekBarChangeListener = var1;
    }

    public void setOnSeekBarHoverListener(SeslSeekBar.OnSeekBarHoverListener var1) {
        this.mOnSeekBarHoverListener = var1;
    }

    public interface OnSeekBarChangeListener {
        void onProgressChanged(SeslSeekBar var1, int var2, boolean var3);

        void onStartTrackingTouch(SeslSeekBar var1);

        void onStopTrackingTouch(SeslSeekBar var1);
    }

    public interface OnSeekBarHoverListener {
        void onHoverChanged(SeslSeekBar var1, int var2, boolean var3);

        void onStartTrackingHover(SeslSeekBar var1, int var2);

        void onStopTrackingHover(SeslSeekBar var1);
    }
}
