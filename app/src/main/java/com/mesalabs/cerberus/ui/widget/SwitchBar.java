package com.mesalabs.cerberus.ui.widget;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.utils.Utils;

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
    private boolean mChecked = false;
    private boolean mIsNightMode = false;

    private Context mContext;
    private SwitchBar.SwitchBarPressListener mSwitchBarPressListener;

    private LinearLayout mSwitchBarLayout;
    private TextView mSwitchBarText;
    private Switch mSwitchBarSwitch;

    private int mBackgroundColor;
    private int mBackgroundActivatedColor;
    private int mTextColor;
    private int mTextActivatedColor;


    public SwitchBar(Context context) {
        super(context);
    }

    public SwitchBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        mIsNightMode = Utils.isNightMode(mContext);

        inflate(mContext, R.layout.mesa_switchbar_layout, this);

        setOrientation(LinearLayout.VERTICAL);

        mSwitchBarLayout = findViewById(R.id.mesa_switchbar_layout);
        mSwitchBarLayout.setOnClickListener(this);

        mSwitchBarText = findViewById(R.id.mesa_switchbar_textview);

        mSwitchBarSwitch = findViewById(R.id.mesa_switchbar_switch);
        mSwitchBarSwitch.setOnCheckedChangeListener(this);

        TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.SwitchBar);

        mBackgroundColor = getResources().getColor(mIsNightMode ? R.color.sesl_switchbar_background_dark : R.color.sesl_switchbar_background_light, mContext.getTheme());
        mBackgroundActivatedColor = obtainStyledAttributes.getColor(R.styleable.SwitchBar_activatedColor, getResources().getColor(mIsNightMode ? R.color.sesl_switchbar_checked_background_dark :R.color.sesl_switchbar_checked_background_light, mContext.getTheme()));
        mTextColor = getResources().getColor(mIsNightMode ? R.color.sesl_primary_text_color_dark : R.color.sesl_primary_text_color_light, mContext.getTheme());
        mTextActivatedColor = getResources().getColor(R.color.sesl_white, mContext.getTheme());

        obtainStyledAttributes.recycle();
    }

    public SwitchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
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
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        setChecked(mChecked);
    }

    private String getLabel(Context context) {
        if (context instanceof AppCompatActivity) {
            ActivityInfo activityInfo = null;
            PackageManager packageManager = context.getPackageManager();
            try {
                activityInfo = packageManager.getActivityInfo(((AppCompatActivity) context).getComponentName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (activityInfo != null) {
                return activityInfo.loadLabel(packageManager).toString();
            }
        }
        return "";
    }

    public void setSwitchBarPressListener(SwitchBar.SwitchBarPressListener listener) {
        mSwitchBarPressListener = listener;
    }

    public void setTextViewLabelAndBackground() {
        mSwitchBarText.setText(getResources().getString(mChecked ? R.string.sesl_switch_on : R.string.sesl_switch_off));
        mSwitchBarSwitch.setContentDescription(getLabel(mContext));

        mSwitchBarLayout.getBackground().setTint(mChecked ? mBackgroundActivatedColor : mBackgroundColor);
        mSwitchBarText.setTextColor(mChecked ? mTextActivatedColor : mTextColor);

        if (isEnabled()) {
            mSwitchBarText.setAlpha(1.0f);
            mSwitchBarLayout.getBackground().setAlpha(255);
        } else {
            mSwitchBarText.setAlpha(0.4f);
            mSwitchBarLayout.getBackground().setAlpha(102);
        }
    }


    public interface SwitchBarPressListener {
        void setChecked(boolean z);
    }
}
