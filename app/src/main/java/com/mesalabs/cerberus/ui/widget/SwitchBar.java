package com.mesalabs.cerberus.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.utils.LogUtils;
import com.mesalabs.cerberus.utils.Utils;
import com.samsung.android.ui.widget.SeslProgressBar;
import com.samsung.android.ui.widget.SeslSwitch;
import com.samsung.android.ui.widget.SeslToggleSwitch;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2019
 * Originally coded by Samsung. All rights reserved to their respective owners.
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

public class SwitchBar extends LinearLayout implements View.OnClickListener, Checkable, CompoundButton.OnCheckedChangeListener {
    private static final int SWITCH_OFF_STRING_RESOURCE_ID = R.string.sesl_switch_off;
    private static final int SWITCH_ON_STRING_RESOURCE_ID = R.string.sesl_switch_on;

    private boolean mChecked = false;
    private boolean mIsNightMode = false;
    private Context mContext;
    private SwitchBar.SwitchBarPressListener mSwitchBarPressListener;
    private LinearLayout mSwitchBarLayout;
    private TextView mSwitchBarText;
    private SeslProgressBar mProgressBar;
    private SeslToggleSwitch mSwitchBarSwitch;
    private int mBackgroundColor;
    private int mBackgroundActivatedColor;
    private int mOffTextColor;
    private int mOffTextId;
    private int mOnTextColor;
    private int mOnTextId;

    public SwitchBar(Context context) {
        this(context, null);
    }

    public SwitchBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seslSwitchBarStyle);
    }

    public SwitchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SwitchBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs);

        mContext = context;
        mIsNightMode = Utils.isNightMode(mContext);

        inflate(mContext, R.layout.mesa_view_switchbar_layout, this);

        setFocusable(true);
        setClickable(true);

        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SwitchBar, defStyleAttr, defStyleRes);
        mBackgroundColor = a.getColor(R.styleable.SwitchBar_seslSwitchBarBackgroundColor, getResources().getColor(R.color.sesl_switchbar_off_background_color_light, mContext.getTheme()));
        int bgActivatedColor = a.getColor(R.styleable.SwitchBar_seslSwitchBarBackgroundActivatedColor, getResources().getColor(R.color.sesl_switchbar_on_background_color, mContext.getTheme()));
        mOnTextColor = a.getColor(R.styleable.SwitchBar_seslSwitchBarTextActivatedColor, getResources().getColor(R.color.sesl_switchbar_on_text_color_light, mContext.getTheme()));
        mOffTextColor = a.getColor(R.styleable.SwitchBar_seslSwitchBarTextColor, getResources().getColor(R.color.sesl_switchbar_off_text_color_light, mContext.getTheme()));
        a.recycle();

        int color;
        int alpha;
        if (mIsNightMode) {
            color = 0xFFFFFF & bgActivatedColor;
            alpha = 0x66000000;
        } else {
            color = 0xFFFFFF & bgActivatedColor;
            alpha = 0x55000000;
        }
        mBackgroundActivatedColor = color | alpha;

        mProgressBar = findViewById(R.id.mesa_switchbar_progress);
        mSwitchBarLayout = findViewById(R.id.mesa_switchbar_layout);
        mSwitchBarLayout.setOnClickListener(this);

        mOnTextId = SWITCH_ON_STRING_RESOURCE_ID;
        mOffTextId = SWITCH_OFF_STRING_RESOURCE_ID;

        mSwitchBarText = findViewById(R.id.mesa_switchbar_textview);
        ((MarginLayoutParams) mSwitchBarText.getLayoutParams()).setMarginStart((int) getResources().getDimension(R.dimen.sesl_switchbar_margin_start));
        mSwitchBarSwitch = findViewById(R.id.mesa_switchbar_switch);
        mSwitchBarSwitch.setSaveEnabled(false);
        mSwitchBarSwitch.setOnCheckedChangeListener(this);
        ((MarginLayoutParams) mSwitchBarSwitch.getLayoutParams()).setMarginEnd((int) getResources().getDimension(R.dimen.sesl_switchbar_margin_end));

        SwitchBar.SwitchBarDelegate delegate = new SwitchBar.SwitchBarDelegate(this);
        ViewCompat.setAccessibilityDelegate(mSwitchBarLayout, delegate);
        ViewCompat.setAccessibilityDelegate(mSwitchBarSwitch, delegate);
        delegate.setSessionName(getActivityTitle());
    }

    public void show() {
        if (!this.isShowing()) {
            this.setVisibility(View.VISIBLE);
        }

    }

    public void hide() {
        if (this.isShowing()) {
            this.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    public boolean isShowing() {
        return this.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setChecked(isChecked);
        mSwitchBarPressListener.setChecked(isChecked);
    }

    @Override
    public void onClick(View v) {
        toggle();
        mSwitchBarPressListener.setChecked(mChecked);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mSwitchBarText.setEnabled(enabled);
        mSwitchBarSwitch.setEnabled(enabled);
        setTextViewLabelAndBackground();
    }

    @Override
    public void setChecked(boolean isChecked) {
        mChecked = isChecked;
        mSwitchBarSwitch.setChecked(mChecked);
        setTextViewLabelAndBackground();
        mSwitchBarPressListener.setChecked(mChecked);
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        setChecked(mChecked);
    }

    private String getActivityTitle() {
        Context context = getContext();

        while (true) {
            if (! (context instanceof ContextWrapper)) {
                return "";
            }

            if (context instanceof Activity) {
                CharSequence title = ((Activity) context).getTitle();
                return title != null ? title.toString() : "";
            }

            context = ((ContextWrapper) context).getBaseContext();
        }
    }

    public void setProgressBarVisible(boolean visible) {
        try {
            mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
        } catch (IndexOutOfBoundsException e) {
            LogUtils.i("SetProgressBarVisible", "Invalid argument" + e);
        }
    }

    public void setSwitchBarPressListener(SwitchBar.SwitchBarPressListener listener) {
        mSwitchBarPressListener = listener;
    }

    public void setSwitchBarText(int onTextId, int offTextId) {
        mOnTextId = onTextId;
        mOffTextId = offTextId;
        setTextViewLabelAndBackground();
    }

    public void setTextViewLabel() {
        String label = getResources().getString(mChecked ? R.string.sesl_switch_on : R.string.sesl_switch_off);
        mSwitchBarText.setText(label);
    }

    public void setTextViewLabelAndBackground() {
        String label = getResources().getString(mChecked ? mOnTextId : mOffTextId);

        DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchBarLayout.getBackground()).mutate(), ColorStateList.valueOf(mChecked ? mBackgroundActivatedColor : mBackgroundColor));

        mSwitchBarText.setTextColor(mChecked ? mOnTextColor : mOffTextColor);

        if (isEnabled()) {
            mSwitchBarText.setAlpha(1.0F);
            mSwitchBarLayout.getBackground().setAlpha(255);
        } else {
            mSwitchBarText.setAlpha(0.4F);
            mSwitchBarLayout.getBackground().setAlpha(102);
        }

        if (label != null && !label.contentEquals(mSwitchBarText.getText())) {
            mSwitchBarText.setText(label);
        }
    }


    public interface SwitchBarPressListener {
        void setChecked(boolean z);
    }

    private static class SwitchBarDelegate extends AccessibilityDelegateCompat {
        public String mSessionName = "";
        public SeslToggleSwitch mSwitch;
        public TextView mText;

        public SwitchBarDelegate(View host) {
            mText = host.findViewById(R.id.mesa_switchbar_textview);
            mSwitch = host.findViewById(R.id.mesa_switchbar_switch);
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(host, info);

            String switchStatus = host.getContext().getResources().getString(mSwitch.isChecked() ? SWITCH_ON_STRING_RESOURCE_ID : SWITCH_OFF_STRING_RESOURCE_ID);

            StringBuilder description = new StringBuilder();

            info.setClassName(SeslSwitch.class.getName());
            if (!TextUtils.isEmpty(mSessionName)) {
                description.append(mSessionName);
                description.append(", ");
            }

            if (!TextUtils.equals(switchStatus, mText.getText()) && !TextUtils.isEmpty(mText.getText())) {
                description.append(mText.getText());
                description.append(", ");
            }

            if (host instanceof SeslToggleSwitch) {
                info.setText(description.toString());
            } else if (host instanceof LinearLayout) {
                description.append(switchStatus);
                description.append(", ");
                description.append(host.getContext().getResources().getString(R.string.sesl_switch));
                host.setContentDescription(description.toString());
            }

        }

        public void setSessionName(String var1) {
            this.mSessionName = var1;
        }
    }
}
