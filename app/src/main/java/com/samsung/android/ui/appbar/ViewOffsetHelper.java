package com.samsung.android.ui.appbar;

import android.view.View;

import androidx.core.view.ViewCompat;

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

public class ViewOffsetHelper {
    public boolean horizontalOffsetEnabled = true;
    public int layoutLeft;
    public int layoutTop;
    public int offsetLeft;
    public int offsetTop;
    public boolean verticalOffsetEnabled = true;
    public final View view;

    public ViewOffsetHelper(View var1) {
        this.view = var1;
    }

    public int getLayoutTop() {
        return this.layoutTop;
    }

    public int getTopAndBottomOffset() {
        return this.offsetTop;
    }

    public void onViewLayout() {
        this.layoutTop = this.view.getTop();
        this.layoutLeft = this.view.getLeft();
        this.updateOffsets();
    }

    public boolean setLeftAndRightOffset(int var1) {
        if (this.horizontalOffsetEnabled && this.offsetLeft != var1) {
            this.offsetLeft = var1;
            this.updateOffsets();
            return true;
        } else {
            return false;
        }
    }

    public boolean setTopAndBottomOffset(int var1) {
        if (this.verticalOffsetEnabled && this.offsetTop != var1) {
            this.offsetTop = var1;
            this.updateOffsets();
            return true;
        } else {
            return false;
        }
    }

    public final void updateOffsets() {
        View var1 = this.view;
        ViewCompat.offsetTopAndBottom(var1, this.offsetTop - (var1.getTop() - this.layoutTop));
        var1 = this.view;
        ViewCompat.offsetLeftAndRight(var1, this.offsetLeft - (var1.getLeft() - this.layoutLeft));
    }
}
