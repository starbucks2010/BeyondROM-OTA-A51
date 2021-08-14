package com.samsung.android.ui.picker.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import com.mesalabs.ten.update.R;
import com.samsung.android.ui.widget.SeslSeekBar;

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

class SeslOpacitySeekBar extends SeslSeekBar {
    private static final int SEEKBAR_MAX_VALUE = 255;
    private static final String TAG = "SeslOpacitySeekBar";
    private int[] mColors = new int[]{-1, -16777216};
    private GradientDrawable mProgressDrawable;

    public SeslOpacitySeekBar(Context var1, AttributeSet var2) throws Throwable {
        super(var1, var2);
    }

    private void initColor(int var1) throws Throwable {
        float[] var2 = new float[3];
        Color.colorToHSV(var1, var2);
        var1 = Color.alpha(var1);
        this.mColors[0] = Color.HSVToColor(0, var2);
        this.mColors[1] = Color.HSVToColor(255, var2);
        this.setProgress(var1);
    }

    void changeColorBase(int var1) throws Throwable {
        GradientDrawable var2 = this.mProgressDrawable;
        if (var2 != null) {
            int[] var3 = this.mColors;
            var3[1] = var1;
            var2.setColors(var3);
            this.setProgressDrawable(this.mProgressDrawable);
            this.setProgress(this.getMax());
        }

    }

    void init(Integer var1) throws Throwable {
        this.setMax(255);
        if (var1 != null) {
            this.initColor(var1);
        }

        this.mProgressDrawable = (GradientDrawable)this.getContext().getDrawable(R.drawable.sesl_color_picker_opacity_seekbar);
        this.setProgressDrawable(this.mProgressDrawable);
        this.setThumb(this.getContext().getResources().getDrawable(R.drawable.sesl_color_picker_seekbar_cursor, getContext().getTheme()));
        this.setThumbOffset(0);
    }

    void restoreColor(int var1) throws Throwable {
        this.initColor(var1);
        this.mProgressDrawable.setColors(this.mColors);
        this.setProgressDrawable(this.mProgressDrawable);
    }
}
