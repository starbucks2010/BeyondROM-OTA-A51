package com.mesalabs.cerberus.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.mesalabs.cerberus.R;

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

// kang com.sec.android.app.clockpackage.common.view.ClockAddButton

public class CustomButton extends ConstraintLayout {
    public ConstraintLayout mCustomButton;
    public TextView mButtonText;
    public final Context mContext;
    public int mViewWidth;

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mesa_baselayout_custombutton, this, true);
        mCustomButton = findViewById(R.id.mesa_layout_custombutton);
        mButtonText = findViewById(R.id.mesa_text_custombutton);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton);
        String text = a.getString(R.styleable.CustomButton_text);
        setText(text);
        a.recycle();
    }

    public ConstraintLayout getCustomButton() {
        return mCustomButton;
    }

    public TextView getTextView() {
        return mButtonText;
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCustomButton.setMaxWidth((int) ((double) mViewWidth * 0.75D));
        mCustomButton.setMinWidth((int) ((double) mViewWidth * 0.61D));
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
    }

    public void setText(String text) {
        mButtonText.setText(text);
    }

}

