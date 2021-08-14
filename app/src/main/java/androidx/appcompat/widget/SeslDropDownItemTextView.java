package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.utils.LogUtils;
import com.samsung.android.ui.widget.SeslCheckedTextView;

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

public class SeslDropDownItemTextView extends SeslCheckedTextView {
    public static final String TAG = "SeslDropDownItemTextView";

    public SeslDropDownItemTextView(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public SeslDropDownItemTextView(Context var1, AttributeSet var2) {
        this(var1, var2, 16842884);
    }

    public SeslDropDownItemTextView(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
    }

    @SuppressLint("WrongConstant")
    public void setChecked(boolean var1) {
        super.setChecked(var1);
        int i = var1 ? 1 : 0;
        this.setTypeface(Typeface.create("sec-roboto-light", i));
        if (i != 0 && this.getCurrentTextColor() == -65281) {
            LogUtils.w("SeslDropDownItemTextView", "SeslDropDownItemTextView text color reload!!");
            boolean var2 = !Utils.isNightMode(this.getContext());
            Context var3 = this.getContext();
            int var4;
            if (var2) {
                var4 = R.color.sesl_spinner_dropdown_text_color_light;
            } else {
                var4 = R.color.sesl_spinner_dropdown_text_color_dark;
            }

            if (var3 != null) {
                ColorStateList var5 = var3.getResources().getColorStateList(var4, var3.getTheme());
                if (var5 != null) {
                    this.setTextColor(var5);
                } else {
                    LogUtils.w("SeslDropDownItemTextView", "Didn't set SeslDropDownItemTextView text color!!");
                }
            }
        }

    }
}