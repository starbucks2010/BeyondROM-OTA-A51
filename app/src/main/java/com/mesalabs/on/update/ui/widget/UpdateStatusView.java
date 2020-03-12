package com.mesalabs.on.update.ui.widget;

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
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mesalabs.on.update.R;
import com.mesalabs.on.update.ota.ROMUpdate;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.MultiplePulse;

/*
 * On Update
 *
 * Coded by BlackMesa @2020
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

    private LinearLayout mContainerView;
    private TextView mTextView;
    private String mText;
    private SpinKitView mLoadingView;
    private ImageView mImageView;
    private Drawable mDrawable;
    private int mDrawableColor;

    private AlphaAnimation mFadeInAnim;
    private AlphaAnimation mFadeOutAnim;
    private AlphaAnimation mFadeOutAnim_IV;
    private AlphaAnimation mFadeOutAnim_LV;
    private AlphaAnimation mFadeOutAnim_TV;
    private ObjectAnimator mMoveToZeroY_TV;

    public UpdateStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        init();
    }

    private void init() {
        removeAllViews();

        LayoutInflater.from(mContext).inflate(R.layout.mesa_ota_updatestatusview_layout, this);

        mContainerView = findViewById(R.id.mesa_container_updatestatusview);
        mTextView = findViewById(R.id.mesa_textview_updatestatusview);
        mLoadingView = findViewById(R.id.mesa_loading_updatestatusview);
        mImageView = findViewById(R.id.mesa_imageview_updatestatusview);

        initAnimationFields();
    }

    private void initAnimationFields() {
        mFadeInAnim = new AlphaAnimation(0.0f, 1.0f);
        mFadeInAnim.setDuration(1000);
        mFadeOutAnim = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim.setDuration(500);

        mFadeOutAnim_IV = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim_IV.setDuration(500);
        mFadeOutAnim_IV.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                mImageView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);

                mImageView.setImageDrawable(null);
                ((Sprite) mDrawable).setColor(mDrawableColor);
                mLoadingView.setIndeterminate(true);
                mLoadingView.setIndeterminateDrawable(mDrawable);
            }
        });

        mFadeOutAnim_LV = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim_LV.setDuration(500);
        mFadeOutAnim_LV.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLoadingView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);

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
        });

        mFadeOutAnim_TV = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim_TV.setDuration(500);
        mFadeOutAnim_TV.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                setTextInternal();
            }
        });

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
                mText = getResources().getString(R.string.mesa_checking_updates);
                mDrawable = new MultiplePulse();
                mDrawableColor = getResources().getColor(R.color.mesa_ota_primary_dark_color_light, null);
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

        if (mDrawable instanceof Sprite)
            disableImageView();
        else
            disableLoadingView();
    }

    public void setText(String text) {
        mText = text;

        if (mTextView.getText() != "")
            mTextView.startAnimation(mFadeOutAnim_TV);
        else
            setTextInternal();
    }

    public void start(int status) {
        mCheckingStatus = status;

        switch(status) {
            case ROMUpdate.STATE_DOWNLOADED:
                mText = getResources().getString(R.string.mesa_update_download_complete);
                mDrawable = getResources().getDrawable(R.drawable.mesa_ota_updatestatusview_dw_complete,null);
                mDrawableColor = getResources().getColor(R.color.mesa_ota_primary_dark_color_light, null);

                mLoadingView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setImageDrawable(mDrawable);
                mImageView.setColorFilter(mDrawableColor, PorterDuff.Mode.SRC_IN);
                break;
            case ROMUpdate.STATE_CHECKING:
            default:
                mText = getResources().getString(R.string.mesa_checking_updates);
                mDrawable = new MultiplePulse();
                mDrawableColor = getResources().getColor(R.color.mesa_ota_primary_dark_color_light, null);

                ((Sprite) mDrawable).setColor(mDrawableColor);
                mLoadingView.setIndeterminateDrawable(mDrawable);
                break;
        }

        setText(mText);
    }

    private void disableImageView() {
        mImageView.startAnimation(mFadeOutAnim_IV);
        mLoadingView.startAnimation(mFadeOutAnim);
    }

    private void disableLoadingView() {
        mImageView.startAnimation(mFadeOutAnim);
        mLoadingView.startAnimation(mFadeOutAnim_LV);
    }

    private void setTextInternal() {
        mTextView.setText(mText);
        mTextView.startAnimation(mFadeInAnim);
        if (mTextView.getLineCount() <= 1) {
            mTextView.setTranslationY(16.0f);
            mMoveToZeroY_TV.start();
        }
    }

}
