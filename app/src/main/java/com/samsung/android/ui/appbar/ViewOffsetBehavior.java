package com.samsung.android.ui.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.samsung.android.ui.coordinatorlayout.widget.SeslCoordinatorLayout;

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

public class ViewOffsetBehavior<V extends View> extends SeslCoordinatorLayout.Behavior<V> {
    public int tempLeftRightOffset = 0;
    public int tempTopBottomOffset = 0;
    public ViewOffsetHelper viewOffsetHelper;

    public ViewOffsetBehavior() {
    }

    public ViewOffsetBehavior(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public int getTopAndBottomOffset() {
        ViewOffsetHelper var1 = this.viewOffsetHelper;
        int var2;
        if (var1 != null) {
            var2 = var1.getTopAndBottomOffset();
        } else {
            var2 = 0;
        }

        return var2;
    }

    public void layoutChild(SeslCoordinatorLayout var1, V var2, int var3) {
        var1.onLayoutChild(var2, var3);
    }

    public boolean onLayoutChild(SeslCoordinatorLayout var1, V var2, int var3) {
        this.layoutChild(var1, var2, var3);
        if (this.viewOffsetHelper == null) {
            this.viewOffsetHelper = new ViewOffsetHelper(var2);
        }

        this.viewOffsetHelper.onViewLayout();
        var3 = this.tempTopBottomOffset;
        if (var3 != 0) {
            this.viewOffsetHelper.setTopAndBottomOffset(var3);
            this.tempTopBottomOffset = 0;
        }

        var3 = this.tempLeftRightOffset;
        if (var3 != 0) {
            this.viewOffsetHelper.setLeftAndRightOffset(var3);
            this.tempLeftRightOffset = 0;
        }

        return true;
    }

    public boolean setTopAndBottomOffset(int var1) {
        ViewOffsetHelper var2 = this.viewOffsetHelper;
        if (var2 != null) {
            return var2.setTopAndBottomOffset(var1);
        } else {
            this.tempTopBottomOffset = var1;
            return false;
        }
    }
}
