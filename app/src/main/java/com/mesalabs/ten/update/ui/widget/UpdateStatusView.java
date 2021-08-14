package com.mesalabs.ten.update.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.ota.ROMUpdate;
import com.samsung.android.ui.widget.SeslProgressBar;

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

public class UpdateStatusView extends LinearLayout {
    private Context mContext;
    private int mCheckingStatus = 0;

    private SeslProgressBar mProgress;
    private LinearLayout mContainerView;
    private TextView mTextView;
    private ImageView mImageView;
    private Drawable mDrawable;
    private int mDrawableColor;

    private String mText;

    private AlphaAnimation mFadeInAnim;
    private AlphaAnimation mFadeOutAnim;
    private ObjectAnimator mMoveToZeroY_TV;

    public UpdateStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        init();
    }

    private void init() {
        removeAllViews();

        LayoutInflater.from(mContext).inflate(R.layout.mesa_ota_view_updatestatusview_layout, this);

        mProgress = findViewById(R.id.mesa_progress_updatestatusview);
        mContainerView = findViewById(R.id.mesa_container_updatestatusview);
        mTextView = findViewById(R.id.mesa_textview_updatestatusview);
        mImageView = findViewById(R.id.mesa_imageview_updatestatusview);

        initAnimationFields();
    }

    private void initAnimationFields() {
        mFadeInAnim = new AlphaAnimation(0.0f, 1.0f);
        mFadeInAnim.setDuration(1000);
        mFadeOutAnim = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim.setDuration(500);

        mMoveToZeroY_TV = ObjectAnimator.ofFloat(mTextView, "translationY", 0.0f);
        mMoveToZeroY_TV.setDuration(1000);
    }

    public int getCheckingStatus() {
        return mCheckingStatus;
    }

    public void setUpdateStatus(int status) {
        mCheckingStatus = status;

        switch(status) {
            case ROMUpdate.STATE_DOWNLOADED:
                mText = getResources().getString(R.string.mesa_update_download_complete);
                mDrawable = getResources().getDrawable(R.drawable.mesa_ota_updatestatusview_dw_complete,null);
                mDrawableColor = getResources().getColor(R.color.mesa_ota_primary_dark_color_light, null);
                break;
            case ROMUpdate.STATE_CHECKING:
                mText = "";
                mDrawable = null;
                mDrawableColor = 0;
                break;
            case ROMUpdate.STATE_NO_UPDATES:
                mText = getResources().getString(R.string.mesa_no_updates_available);
                mDrawable = getResources().getDrawable(R.drawable.mesa_ota_updatestatusview_no_updates,null);
                mDrawableColor = getResources().getColor(R.color.mesa_ota_primary_dark_color_light, null);
                break;
            case ROMUpdate.STATE_NEW_VERSION_AVAILABLE:
                mText = getResources().getString(R.string.mesa_new_update_available);
                mDrawable = getResources().getDrawable(R.drawable.mesa_ota_updatestatusview_new_update,null);
                mDrawableColor = getResources().getColor(R.color.mesa_ota_primary_dark_color_light, null);
                break;
            case ROMUpdate.STATE_ERROR:
            default:
                mText = getResources().getString(R.string.mesa_updates_check_error);
                mDrawable = getResources().getDrawable(R.drawable.mesa_ota_updatestatusview_error,null);
                mDrawableColor = getResources().getColor(R.color.sesl_error_color, null);
                break;
        }
        setText(mText);

        if (mDrawable == null)
            disableImageView();
        else
            disableProgress();
    }

    public void setText(String text) {
        mText = text;
        mTextView.setText(mText);
        mTextView.startAnimation(mFadeInAnim);
        if (mTextView.getLineCount() <= 1) {
            mTextView.setTranslationY(16.0f);
            mMoveToZeroY_TV.start();
        }
    }

    public void start(int status) {
        mCheckingStatus = status;

        switch(status) {
            case ROMUpdate.STATE_DOWNLOADED:
                mText = getResources().getString(R.string.mesa_update_download_complete);
                mDrawable = getResources().getDrawable(R.drawable.mesa_ota_updatestatusview_dw_complete,null);
                mDrawableColor = getResources().getColor(R.color.mesa_ota_primary_dark_color_light, null);

                mProgress.setVisibility(View.GONE);
                mContainerView.setVisibility(View.VISIBLE);
                mImageView.setImageDrawable(mDrawable);
                mImageView.setColorFilter(mDrawableColor, PorterDuff.Mode.SRC_IN);
                break;
            case ROMUpdate.STATE_CHECKING:
            default:
                mText = "";
                mDrawable = null;
                mDrawableColor = 0;

                mProgress.setVisibility(View.VISIBLE);
                mContainerView.setVisibility(View.GONE);
                break;
        }

        setText(mText);
    }

    private void disableImageView() {
        mContainerView.setVisibility(View.GONE);
        mContainerView.setAlpha(0.0f);
        mProgress.setVisibility(View.VISIBLE);
    }

    private void disableProgress() {
        mProgress.setVisibility(View.GONE);
        mContainerView.setAlpha(1.0f);
        mContainerView.setVisibility(View.VISIBLE);

        mImageView.setImageDrawable(mDrawable);
        mImageView.setColorFilter(mDrawableColor, PorterDuff.Mode.SRC_IN);

        switch(mCheckingStatus) {
            case ROMUpdate.STATE_NO_UPDATES:
                Animatable animatable = (Animatable) mImageView.getDrawable();
                animatable.start();
                break;
            case ROMUpdate.STATE_NEW_VERSION_AVAILABLE:
                mImageView.setScaleX(0.7f);
                mImageView.setScaleY(0.7f);
                ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(mImageView,
                        PropertyValuesHolder.ofFloat("scaleX", 1.0f),
                        PropertyValuesHolder.ofFloat("scaleY", 1.0f));
                scaleDown.setDuration(1000);
                scaleDown.setInterpolator(new BounceInterpolator());
                scaleDown.start();
                mImageView.startAnimation(mFadeInAnim);
                break;
            case ROMUpdate.STATE_DOWNLOADED:
            case ROMUpdate.STATE_ERROR:
            default:
                mImageView.startAnimation(mFadeInAnim);
                break;
        }
    }

}
