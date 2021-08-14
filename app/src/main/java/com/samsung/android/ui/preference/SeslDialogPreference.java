package com.samsung.android.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.core.content.res.TypedArrayUtils;

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

public abstract class SeslDialogPreference extends SeslPreference {
    private Drawable mDialogIcon;
    private int mDialogLayoutResId;
    private CharSequence mDialogMessage;
    private CharSequence mDialogTitle;
    private CharSequence mNegativeButtonText;
    private CharSequence mPositiveButtonText;

    public SeslDialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeslDialogPreference, defStyleAttr, defStyleRes);
        mDialogTitle = TypedArrayUtils.getString(a, R.styleable.SeslDialogPreference_dialogTitle, R.styleable.SeslDialogPreference_android_dialogTitle);
        if (mDialogTitle == null) {
            mDialogTitle = getTitle();
        }
        mDialogMessage = TypedArrayUtils.getString(a, R.styleable.SeslDialogPreference_dialogMessage, R.styleable.SeslDialogPreference_android_dialogMessage);
        mDialogIcon = TypedArrayUtils.getDrawable(a, R.styleable.SeslDialogPreference_dialogIcon, R.styleable.SeslDialogPreference_android_dialogIcon);
        mPositiveButtonText = TypedArrayUtils.getString(a, R.styleable.SeslDialogPreference_positiveButtonText, R.styleable.SeslDialogPreference_android_positiveButtonText);
        mNegativeButtonText = TypedArrayUtils.getString(a, R.styleable.SeslDialogPreference_negativeButtonText, R.styleable.SeslDialogPreference_android_negativeButtonText);
        mDialogLayoutResId = TypedArrayUtils.getResourceId(a, R.styleable.SeslDialogPreference_dialogLayout, R.styleable.SeslDialogPreference_android_dialogLayout, 0);
        a.recycle();
    }

    public SeslDialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslDialogPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle));
    }

    public CharSequence getDialogTitle() {
        return mDialogTitle;
    }

    public CharSequence getDialogMessage() {
        return mDialogMessage;
    }

    public Drawable getDialogIcon() {
        return mDialogIcon;
    }

    public CharSequence getPositiveButtonText() {
        return mPositiveButtonText;
    }

    public CharSequence getNegativeButtonText() {
        return mNegativeButtonText;
    }

    public int getDialogLayoutResource() {
        return mDialogLayoutResId;
    }

    @Override
    protected void onClick() {
        getPreferenceManager().showDialog(this);
    }


    public interface TargetFragment {
        SeslPreference findPreference(CharSequence charSequence);
    }
}
