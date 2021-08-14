package com.samsung.android.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;

import androidx.core.content.res.TypedArrayUtils;

import com.mesalabs.ten.update.R;
import com.samsung.android.ui.widget.SeslSwitch;

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

public class SeslSwitchPreferenceCompat extends TwoStatePreference {
    private final DummyClickListener mClickListener = new DummyClickListener();
    private final Listener mListener = new Listener();
    private CharSequence mSwitchOff;
    private CharSequence mSwitchOn;

    public SeslSwitchPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeslSwitchPreferenceCompat, defStyleAttr, defStyleRes);
        setSummaryOn(TypedArrayUtils.getString(a, R.styleable.SeslSwitchPreferenceCompat_summaryOn, R.styleable.SeslSwitchPreferenceCompat_summaryOn));
        setSummaryOff(TypedArrayUtils.getString(a, R.styleable.SeslSwitchPreferenceCompat_summaryOff, R.styleable.SeslSwitchPreferenceCompat_summaryOff));
        setSwitchTextOn(TypedArrayUtils.getString(a, R.styleable.SeslSwitchPreferenceCompat_switchTextOn, R.styleable.SeslSwitchPreferenceCompat_switchTextOn));
        setSwitchTextOff(TypedArrayUtils.getString(a, R.styleable.SeslSwitchPreferenceCompat_switchTextOff, R.styleable.SeslSwitchPreferenceCompat_switchTextOff));
        setDisableDependentsState(TypedArrayUtils.getBoolean(a, R.styleable.SeslSwitchPreferenceCompat_disableDependentsState, R.styleable.SeslSwitchPreferenceCompat_disableDependentsState, false));
        a.recycle();
    }

    public SeslSwitchPreferenceCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslSwitchPreferenceCompat(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.switchPreferenceCompatStyle);
    }

    public SeslSwitchPreferenceCompat(Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        View switchView = holder.findViewById(android.R.id.switch_widget);
        syncSwitchView(switchView);
        syncSummaryView(holder);
    }

    public void setSwitchTextOn(CharSequence onText) {
        mSwitchOn = onText;
        notifyChanged();
    }

    public void setSwitchTextOff(CharSequence offText) {
        mSwitchOff = offText;
        notifyChanged();
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

        View switchView = view.findViewById(android.R.id.switch_widget);
        syncSwitchView(switchView);

        View summaryView = view.findViewById(android.R.id.summary);
        syncSummaryView(summaryView);
    }

    private void syncSwitchView(View view) {
        if (view instanceof SeslSwitch) {
            final SeslSwitch switchView = (SeslSwitch) view;
            switchView.setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(mChecked);
        }
        if (view instanceof SeslSwitch) {
            final SeslSwitch switchView = (SeslSwitch) view;
            switchView.setTextOn(mSwitchOn);
            switchView.setTextOff(mSwitchOff);
            switchView.setOnCheckedChangeListener(mListener);
            if (switchView.isClickable()) {
                switchView.setOnClickListener(mClickListener);
            }
            if (isTalkBackIsRunning() && !(this instanceof SeslSwitchPreferenceScreen)) {
                switchView.setBackground(null);
                switchView.setClickable(false);
            }
        }
    }


    private class DummyClickListener implements OnClickListener {
        public void onClick(View v) {
            callClickListener();
        }
    }

    private class Listener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!callChangeListener(isChecked)) {
                buttonView.setChecked(!isChecked);
                return;
            }

            SeslSwitchPreferenceCompat.this.setChecked(isChecked);
        }
    }
}
