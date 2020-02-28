package com.mesalabs.on.update.ui.widget;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.mesalabs.cerberus.ui.widget.RoundFrameLayout;
import com.mesalabs.cerberus.utils.StateUtils;
import com.mesalabs.on.update.R;
import com.mesalabs.on.update.ota.utils.PreferencesUtils;
import com.samsung.android.ui.widget.SeslProgressBar;

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

public class ChangelogView extends RoundFrameLayout {
    private Context mContext;

    private SeslProgressBar mProgressBar;
    private LinearLayout mHeaderContainer;
    private LinearLayout mHeaderImgContainer;
    private TextView mErrorText;

    private AlphaAnimation mFadeInAnim;
    private AlphaAnimation mFadeInAnim_PC;
    private AlphaAnimation mFadeOutAnim_PC;

    public ChangelogView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setBackgroundResource(R.drawable.mesa_content_area_bg);
        init();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderImgContainerDimen(newConfig);
    }

    private void init() {
        removeAllViews();

        LayoutTransition itemLayoutTransition = new LayoutTransition();
        Animator scaleDown = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("alpha", 1, 0));
        Animator scaleUp = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("alpha", 0, 1));
        itemLayoutTransition.setAnimator(LayoutTransition.APPEARING, scaleUp);
        itemLayoutTransition.setDuration(LayoutTransition.APPEARING, 650);
        itemLayoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        itemLayoutTransition.setDuration(LayoutTransition.CHANGE_APPEARING, 150);
        itemLayoutTransition.setInterpolator(LayoutTransition.CHANGE_APPEARING, new FastOutSlowInInterpolator());
        itemLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, scaleDown);
        itemLayoutTransition.setDuration(LayoutTransition.DISAPPEARING, 80);

        setLayoutTransition(itemLayoutTransition);

        LayoutInflater.from(mContext).inflate(R.layout.mesa_ota_changelogview_layout, this);

        mProgressBar = findViewById(R.id.mesa_progressbar_changelogview);
        mHeaderContainer = findViewById(R.id.mesa_header_changelogview);
        mHeaderImgContainer = findViewById(R.id.mesa_img_container_header_changelogview);
        mErrorText = findViewById(R.id.mesa_errortextview_changelogview);

        setHeaderImgContainerDimen(getResources().getConfiguration());

        initAnimationFields();
    }

    private void initAnimationFields() {
        mFadeInAnim = new AlphaAnimation(0.0f, 1.0f);
        mFadeInAnim.setDuration(500);

        mFadeInAnim_PC = new AlphaAnimation(0.0f, 1.0f);
        mFadeInAnim_PC.setDuration(1000);
        mFadeInAnim_PC.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                mProgressBar.startAnimation(mFadeOutAnim_PC);
            }
        });
        mFadeOutAnim_PC = new AlphaAnimation(1.0f, 0.0f);
        mFadeOutAnim_PC.setDuration(500);
        mFadeOutAnim_PC.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation){
                mProgressBar.setVisibility(View.GONE);
                getChangelog();
            }
        });
    }

    private void getChangelog() {
        if (StateUtils.isNetworkConnected(mContext) && !PreferencesUtils.ROM.getChangelogUrl().equals("null")) {
            mHeaderContainer.setVisibility(View.VISIBLE);
            mHeaderContainer.startAnimation(mFadeInAnim);
            setFocusable(true);
            setClickable(true);
        } else {
            mErrorText.setVisibility(View.VISIBLE);
            mErrorText.startAnimation(mFadeInAnim);
        }
    }

    private void setHeaderImgContainerDimen(Configuration newConfig) {
        boolean isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mHeaderImgContainer.getLayoutParams();
        lp.weight = isLandscape ? 5.0f : 3.0f;
        mHeaderContainer.setWeightSum(isLandscape ? 7.0f : 5.0f);
        mHeaderImgContainer.setLayoutParams(lp);
    }

    public void start(boolean isNewUpdateAvailable) {
        if (isNewUpdateAvailable) {
            this.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mHeaderContainer.setVisibility(View.GONE);
            mErrorText.setVisibility(View.GONE);
            mProgressBar.startAnimation(mFadeInAnim_PC);
        }
    }

}
