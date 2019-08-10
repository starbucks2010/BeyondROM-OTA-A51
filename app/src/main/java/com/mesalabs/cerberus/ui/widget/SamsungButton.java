package com.mesalabs.cerberus.ui.widget;

import android.content.Context;
//import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;

import com.mesalabs.cerberus.CerberusApp;
import com.mesalabs.cerberus.R;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2019
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

public class SamsungButton extends LinearLayout {
    private View sbDivider;
    private TextView sbTitleText;
    private TextView sbSummaryText;

    public SamsungButton(Context context) {
        super(context);
    }

    public SamsungButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SamsungButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray vars = context.obtainStyledAttributes(attrs, R.styleable.SamsungButton);

        if (vars.getDrawable(R.styleable.SamsungButton_icon) != null) {
            inflate(context, R.layout.mesa_button_icon_layout, this);
            ImageView bIcon = findViewById(R.id.mesa_icon_button);
            bIcon.setImageDrawable(vars.getDrawable(R.styleable.SamsungButton_icon));
        } else {
            inflate(context, R.layout.mesa_button_layout, this);
        }

        sbDivider = findViewById(R.id.mesa_divider_button);
        sbTitleText = findViewById(R.id.mesa_titletext_button);
        sbSummaryText = findViewById(R.id.mesa_summarytext_button);

        setDivider(vars.getBoolean(R.styleable.SamsungButton_addDivider, false));
        setTitleText(vars.getString(R.styleable.SamsungButton_titleText));
        setSummaryText(vars.getString(R.styleable.SamsungButton_summaryText));
        setSummaryTextHighlight(vars.getBoolean(R.styleable.SamsungButton_summaryHighlight, false));

        vars.recycle();
    }

    public void setDivider(boolean z) {
        if (z && sbDivider.getVisibility() == View.GONE) {
            sbDivider.setVisibility(View.VISIBLE);
        } else if (sbDivider.getVisibility() == View.VISIBLE) {
            sbDivider.setVisibility(View.GONE);
        }
    }

    public void setTitleText(String s) {
        sbTitleText.setText(s);
    }

    public void setSummaryText(String s) {
        if (s != null && !s.isEmpty() && sbSummaryText.getVisibility() == View.GONE) {
            sbSummaryText.setVisibility(View.VISIBLE);
        } else if (sbSummaryText.getVisibility() == View.VISIBLE) {
            sbSummaryText.setVisibility(View.GONE);
        }
        sbSummaryText.setText(s);
    }

    public void setSummaryTextHighlight(boolean z) {
        TypedValue value = new TypedValue();

        if (z) {
            getContext().getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
            sbSummaryText.setTextColor(value.data);
        } else {
            getContext().getTheme().resolveAttribute(R.attr.secondaryTextColor, value, true);
            sbSummaryText.setTextColor(getContext().getResources().getColorStateList(value.resourceId, getContext().getTheme());
        }
    }

}