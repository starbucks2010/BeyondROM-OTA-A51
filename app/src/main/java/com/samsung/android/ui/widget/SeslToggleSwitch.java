package com.samsung.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

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

public class SeslToggleSwitch extends SeslSwitch {
    public SeslToggleSwitch.OnBeforeCheckedChangeListener mOnBeforeListener;

    public SeslToggleSwitch(Context var1) {
        super(var1);
    }

    public SeslToggleSwitch(Context var1, AttributeSet var2) {
        super(var1, var2);
    }

    public SeslToggleSwitch(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    public void setChecked(boolean var1) {
        SeslToggleSwitch.OnBeforeCheckedChangeListener var2 = this.mOnBeforeListener;
        if (var2 == null || !var2.onBeforeCheckedChanged(this, var1)) {
            super.setChecked(var1);
        }
    }

    public void setCheckedInternal(boolean var1) {
        super.setChecked(var1);
    }

    public void setOnBeforeCheckedChangeListener(SeslToggleSwitch.OnBeforeCheckedChangeListener var1) {
        this.mOnBeforeListener = var1;
    }

    public interface OnBeforeCheckedChangeListener {
        boolean onBeforeCheckedChanged(SeslToggleSwitch var1, boolean var2);
    }
}
