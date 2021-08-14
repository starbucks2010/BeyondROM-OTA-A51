package com.mesalabs.ten.update.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mesalabs.ten.update.R;

/*
 * 십 Update
 *
 * Coded by BlackMesa123 @2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */

public class CardView extends LinearLayout {
    boolean mIsDividerViewVisible;
    private Context mContext;

    private ImageView mIconImageView;
    private TextView mTitleTextView;
    private TextView mDescTextView;
    private View mDividerView;
    private RelativeLayout mParentView;
    private LinearLayout mContainerView;
    private int mIconColor;
    private Drawable mIconDrawable;
    private String mTitleText;
    private String mDescText;
    private TextView mBadgeTextView;

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnClickListener(v -> { });
        setStyleable(attrs);
        init();
    }

    private void init() {
        removeAllViews();

        if (mIconDrawable == null) {
            inflate(mContext, R.layout.mesa_view_cardview_item_layout, this);
        } else {
            inflate(mContext, R.layout.mesa_view_cardview_icon_item_layout, this);

            mIconImageView = findViewById(R.id.mesa_icon_cardview);
            mIconImageView.setImageDrawable(mIconDrawable);
            if (mIconColor != -1)
                mIconImageView.getDrawable().setTint(mIconColor);
        }

        mParentView = findViewById(R.id.mesa_container_cardview);

        mContainerView = findViewById(R.id.mesa_linearlayout_cardview);

        mTitleTextView = findViewById(R.id.mesa_titletext_cardview);
        mTitleTextView.setText(mTitleText);

        mDescTextView = findViewById(R.id.mesa_desctext_cardview);
        if (mDescText != null && !mDescText.isEmpty()) {
            mDescTextView.setText(mDescText);
            mDescTextView.setVisibility(View.VISIBLE);
        }

        mBadgeTextView = findViewById(R.id.mesa_badge_cardview);

        mDividerView = findViewById(R.id.mesa_divider_cardview);
        mDividerView.setVisibility(mIsDividerViewVisible ? View.VISIBLE : View.GONE);

    }
    
    private void setStyleable(AttributeSet attrs) {
        TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes(attrs, R.styleable.CardView);
        mIconDrawable = obtainStyledAttributes.getDrawable(R.styleable.CardView_IconDrawable);
        mIconColor = obtainStyledAttributes.getColor(R.styleable.CardView_IconColor, -1);
        mTitleText = obtainStyledAttributes.getString(R.styleable.CardView_TitleText);
        mDescText = obtainStyledAttributes.getString(R.styleable.CardView_DescText);
        mIsDividerViewVisible = obtainStyledAttributes.getBoolean(R.styleable.CardView_isDividerViewVisible, false);
        obtainStyledAttributes.recycle();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setFocusable(enabled);
        setClickable(enabled);
        mParentView.setEnabled(enabled);
        mContainerView.setAlpha(enabled ? 1.0f : 0.4f);
    }

    public String getDescText() {
        return mDescText;
    }

    public int getIconColor() {
        return mIconColor;
    }

    public Drawable getIconDrawable() {
        return mIconDrawable;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public void setDescText(String text) {
        if (text == null)
            text = "";

        mDescText = text;
        mDescTextView.setText(mDescText);
        if (mDescText.isEmpty())
            mDescTextView.setVisibility(View.GONE);
        else
            mDescTextView.setVisibility(View.VISIBLE);
    }

    public void setBadgeVisible(boolean visible) {
        mBadgeTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setDividerVisible(boolean visible) {
        mDividerView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setIconColor(int color) {
        mIconColor = color;
        mIconImageView.getDrawable().setTint(mIconColor);
    }

    public void setIconDrawable(Drawable d) {
        mIconDrawable = d;
        mIconImageView.setImageDrawable(mIconDrawable);
        init();
    }

    public void setTitleText(String title) {
        mTitleText = title;
        mTitleTextView.setText(mTitleText);
    }
}
