package com.samsung.android.ui.tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.mesalabs.cerberus.R;

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

public class SeslTabRoundRectIndicator extends SeslAbsIndicatorView {
    private Drawable mBackground;
    private AnimationSet mPressAnimationSet;

    public SeslTabRoundRectIndicator(Context context) {
        this(context, null);
    }

    public SeslTabRoundRectIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeslTabRoundRectIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslTabRoundRectIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        Drawable drawable;
        if (isLightTheme()) {
            drawable = getContext().getDrawable(R.drawable.sesl_tablayout_subtab_indicator_background);
        } else {
            drawable = getContext().getDrawable(R.drawable.sesl_tablayout_subtab_indicator_background_dark);
        }
        mBackground = drawable;
        setBackground(mBackground);
    }

    @Override
    public void setHideImmediatly() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 0.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        startAnimation(alphaAnimation);
    }

    @Override
    void onHide() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        startAnimation(alphaAnimation);
    }

    @Override
    void onShow() {
        if (mPressAnimationSet == null) {
            onReleased();
        } else if (mPressAnimationSet != null && mPressAnimationSet.hasEnded()) {
            onReleased();
        }
    }

    @Override
    void onPressed() {
        mPressAnimationSet = new AnimationSet(false);
        mPressAnimationSet.setStartOffset(50);
        mPressAnimationSet.setFillAfter(true);
        mPressAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                mPressAnimationSet = null;
            }
        });

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.95f, 1.0f, 0.95f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(50);
        scaleAnimation.setInterpolator(getContext(), SeslTabLayout.SESL_TAB_ANIM_INTERPOLATOR);
        scaleAnimation.setFillAfter(true);
        mPressAnimationSet.addAnimation(scaleAnimation);

        if (!isSelected()) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(50);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setInterpolator(getContext(), SeslTabLayout.SESL_TAB_ANIM_INTERPOLATOR);
            mPressAnimationSet.addAnimation(alphaAnimation);
        }

        startAnimation(mPressAnimationSet);
    }

    @Override
    void onReleased() {
        mPressAnimationSet = null;

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration(350);
        scaleAnimation.setInterpolator(getContext(), SeslTabLayout.SESL_TAB_ANIM_INTERPOLATOR);
        scaleAnimation.setFillAfter(true);
        animationSet.addAnimation(scaleAnimation);

        startAnimation(animationSet);
    }

    private boolean isLightTheme() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.isLightTheme, outValue, true);
        return outValue.data != 0;
    }

    @Override
    void onSetSelectedIndicatorColor(int color) {
        if (!(getBackground() instanceof NinePatchDrawable)) {
            getBackground().setTint(color);
            if (!isSelected()) {
                setHideImmediatly();
            }
        }
    }
}
