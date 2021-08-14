package com.samsung.android.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;

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

public class SeslCheckBoxPreference extends TwoStatePreference {
    private final Listener mListener = new Listener();

    public SeslCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeslCheckBoxPreference, defStyleAttr, defStyleRes);
        setSummaryOn(TypedArrayUtils.getString(a, R.styleable.SeslCheckBoxPreference_summaryOn, R.styleable.SeslCheckBoxPreference_android_summaryOn));
        setSummaryOff(TypedArrayUtils.getString(a, R.styleable.SeslCheckBoxPreference_summaryOff, R.styleable.SeslCheckBoxPreference_android_summaryOff));
        setDisableDependentsState(TypedArrayUtils.getBoolean(a, R.styleable.SeslCheckBoxPreference_disableDependentsState, R.styleable.SeslCheckBoxPreference_android_disableDependentsState, false));
        a.recycle();
    }

    public SeslCheckBoxPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.checkBoxPreferenceStyle, android.R.attr.checkBoxPreferenceStyle));
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        syncCheckboxView(holder.findViewById(android.R.id.checkbox));

        syncSummaryView(holder);
    }

    @Override
    protected void performClick(View view) {
        super.performClick(view);
        syncViewIfAccessibilityEnabled(view);
    }


    private void syncViewIfAccessibilityEnabled(View view) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            return;
        }

        View checkboxView = view.findViewById(android.R.id.checkbox);
        syncCheckboxView(checkboxView);

        View summaryView = view.findViewById(android.R.id.summary);
        syncSummaryView(summaryView);
    }

    private void syncCheckboxView(View view) {
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(mChecked);
        }
        if (view instanceof CompoundButton) {
            ((CompoundButton) view).setOnCheckedChangeListener(mListener);
        }
    }


    private class Listener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!callChangeListener(isChecked)) {
                buttonView.setChecked(!isChecked);
                return;
            }
            SeslCheckBoxPreference.this.setChecked(isChecked);
        }
    }
}
