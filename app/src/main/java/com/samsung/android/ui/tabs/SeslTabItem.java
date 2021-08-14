package com.samsung.android.ui.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.TintTypedArray;

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

public class SeslTabItem extends View {
    public final CharSequence text;
    public final Drawable icon;
    public final int customLayout;

    public SeslTabItem(Context context) {
        this(context, null);
    }

    @SuppressLint("RestrictedApi")
    public SeslTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.TabItem);
        text = a.getText(R.styleable.TabItem_android_text);
        icon = a.getDrawable(R.styleable.TabItem_android_icon);
        customLayout = a.getResourceId(R.styleable.TabItem_android_layout, 0);
        a.recycle();
    }
}