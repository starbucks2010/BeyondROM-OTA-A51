package com.samsung.android.ui.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.LinearInterpolator;
import android.widget.AbsSeekBar;
import android.widget.SeekBar;
import java.util.ArrayList;

import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.utils.Utils;

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

public abstract class SeslAbsSeekBar extends SeslProgressBar {
    public static final String CURRENT_SEC_ACTIVE_THEMEPACKAGE = "current_sec_active_themepackage";
    public static final int HOVER_DETECT_TIME = 200;
    public static final int HOVER_POPUP_WINDOW_GRAVITY_CENTER_HORIZONTAL_ON_POINT = 513;
    public static final int HOVER_POPUP_WINDOW_GRAVITY_TOP_ABOVE = 12336;
    public static final boolean IS_BASE_SDK_VERSION;
    public static final int MUTE_VIB_DISTANCE_LVL = 400;
    public static final int MUTE_VIB_DURATION = 500;
    public static final int MUTE_VIB_TOTAL = 4;
    public static final int NO_ALPHA = 255;
    public static final String TAG = "SeslAbsSeekBar";
    public boolean mAllowedSeekBarAnimation;
    public int mCurrentProgressLevel;
    public ColorStateList mDefaultActivatedProgressColor;
    public ColorStateList mDefaultActivatedThumbColor;
    public ColorStateList mDefaultNormalProgressColor;
    public float mDisabledAlpha;
    public Drawable mDivider;
    public boolean mHasThumbTint;
    public boolean mHasThumbTintMode;
    public boolean mHasTickMarkTint;
    public boolean mHasTickMarkTintMode;
    public int mHoveringLevel;
    public boolean mIsDragging;
    public boolean mIsDraggingForSliding;
    public boolean mIsFirstSetProgress;
    public boolean mIsLightTheme;
    public boolean mIsTouchDisabled;
    public boolean mIsUserSeekable;
    public int mKeyProgressIncrement;
    public boolean mLargeFont;
    public AnimatorSet mMuteAnimationSet;
    public ColorStateList mOverlapActivatedProgressColor;
    public ColorStateList mOverlapActivatedThumbColor;
    public Drawable mOverlapBackground;
    public ColorStateList mOverlapNormalProgressColor;
    public int mOverlapPoint;
    public Drawable mOverlapPrimary;
    public Paint mPaint;
    public int mPreviousHoverPopupType;
    public int mScaledTouchSlop;
    public Drawable mSplitProgress;
    public boolean mSplitTrack;
    public final Rect mTempRect;
    public Drawable mThumb;
    public int mThumbOffset;
    public int mThumbPosX;
    public float mThumbPosXFloat;
    public int mThumbPosY;
    public float mThumbPosYFloat;
    public ColorStateList mThumbTintList;
    public PorterDuff.Mode mThumbTintMode;
    public Drawable mTickMark;
    public ColorStateList mTickMarkTintList;
    public PorterDuff.Mode mTickMarkTintMode;
    public float mTouchDownX;
    public float mTouchDownY;
    public float mTouchProgressOffset;
    public boolean mUseMuteAnimation;

    static {
        boolean var0;
        if (Build.VERSION.SDK_INT <= 23) {
            var0 = true;
        } else {
            var0 = false;
        }

        IS_BASE_SDK_VERSION = var0;
    }

    public SeslAbsSeekBar(Context var1) throws Throwable {
        super(var1);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTickMarkTintList = null;
        this.mTickMarkTintMode = null;
        this.mHasTickMarkTint = false;
        this.mHasTickMarkTintMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mHoveringLevel = 0;
        this.mOverlapPoint = -1;
        this.mAllowedSeekBarAnimation = false;
        this.mUseMuteAnimation = false;
        this.mIsFirstSetProgress = false;
        this.mIsDraggingForSliding = false;
        this.mLargeFont = false;
        this.mIsTouchDisabled = false;
        this.mPreviousHoverPopupType = 0;
    }

    public SeslAbsSeekBar(Context var1, AttributeSet var2) throws Throwable {
        super(var1, var2);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTickMarkTintList = null;
        this.mTickMarkTintMode = null;
        this.mHasTickMarkTint = false;
        this.mHasTickMarkTintMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mHoveringLevel = 0;
        this.mOverlapPoint = -1;
        this.mAllowedSeekBarAnimation = false;
        this.mUseMuteAnimation = false;
        this.mIsFirstSetProgress = false;
        this.mIsDraggingForSliding = false;
        this.mLargeFont = false;
        this.mIsTouchDisabled = false;
        this.mPreviousHoverPopupType = 0;
    }

    public SeslAbsSeekBar(Context var1, AttributeSet var2, int var3) throws Throwable {
        this(var1, var2, var3, 0);
    }

    public SeslAbsSeekBar(Context var1, AttributeSet var2, int var3, int var4) throws Throwable {
        super(var1, var2, var3, var4);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbTintMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbTintMode = false;
        this.mTickMarkTintList = null;
        this.mTickMarkTintMode = null;
        this.mHasTickMarkTint = false;
        this.mHasTickMarkTintMode = false;
        boolean var5 = true;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mHoveringLevel = 0;
        this.mOverlapPoint = -1;
        this.mAllowedSeekBarAnimation = false;
        this.mUseMuteAnimation = false;
        this.mIsFirstSetProgress = false;
        this.mIsDraggingForSliding = false;
        this.mLargeFont = false;
        this.mIsTouchDisabled = false;
        this.mPreviousHoverPopupType = 0;
        TypedArray var6 = var1.obtainStyledAttributes(var2, R.styleable.SeslAbsSeekBar, var3, var4);
        this.setThumb(var6.getDrawable(R.styleable.SeslAbsSeekBar_android_thumb));
        if (var6.hasValue(R.styleable.SeslAbsSeekBar_android_thumbTintMode)) {
            this.mThumbTintMode = DrawableUtils.parseTintMode(var6.getInt(R.styleable.SeslAbsSeekBar_android_thumbTintMode, -1), this.mThumbTintMode);
            this.mHasThumbTintMode = true;
        }

        if (var6.hasValue(R.styleable.SeslAbsSeekBar_android_thumbTint)) {
            this.mThumbTintList = var6.getColorStateList(R.styleable.SeslAbsSeekBar_android_thumbTint);
            this.mHasThumbTint = true;
        }

        this.setTickMark(var6.getDrawable(R.styleable.SeslAbsSeekBar_tickMark));
        if (var6.hasValue(R.styleable.SeslAbsSeekBar_tickMarkTintMode)) {
            this.mTickMarkTintMode = DrawableUtils.parseTintMode(var6.getInt(R.styleable.SeslAbsSeekBar_tickMarkTintMode, -1), this.mTickMarkTintMode);
            this.mHasTickMarkTintMode = true;
        }

        if (var6.hasValue(R.styleable.SeslAbsSeekBar_tickMarkTint)) {
            this.mTickMarkTintList = var6.getColorStateList(R.styleable.SeslAbsSeekBar_tickMarkTint);
            this.mHasTickMarkTint = true;
        }

        this.mSplitTrack = var6.getBoolean(R.styleable.SeslAbsSeekBar_android_splitTrack, false);
        this.setThumbOffset(var6.getDimensionPixelOffset(R.styleable.SeslAbsSeekBar_android_thumbOffset, this.getThumbOffset()));
        boolean var7 = var6.getBoolean(R.styleable.SeslAbsSeekBar_useDisabledAlpha, true);
        var6.recycle();
        if (var7) {
            TypedArray var9 = var1.obtainStyledAttributes(var2, R.styleable.AppCompatTheme, 0, 0);
            this.mDisabledAlpha = var9.getFloat(R.styleable.AppCompatTheme_android_disabledAlpha, 0.5F);
            var9.recycle();
        } else {
            this.mDisabledAlpha = 1.0F;
        }

        this.applyThumbTint();
        this.applyTickMarkTint();
        this.mScaledTouchSlop = ViewConfiguration.get(var1).getScaledTouchSlop();
        TypedValue var10 = new TypedValue();
        var1.getTheme().resolveAttribute(R.attr.isLightTheme, var10, true);
        if (var10.data == 0) {
            var5 = false;
        }

        this.mIsLightTheme = var5;
        Resources var8 = var1.getResources();
        this.mDefaultNormalProgressColor = this.colorToColorStateList(var8.getColor(R.color.sesl_seekbar_control_color_normal, null));
        this.mDefaultActivatedProgressColor = this.colorToColorStateList(var8.getColor(R.color.sesl_seekbar_control_color_activated, null));
        if (this.mIsLightTheme) {
            var3 = R.color.sesl_seekbar_overlap_color_normal;
        } else {
            var3 = R.color.sesl_seekbar_overlap_color_normal_dark;
        }

        this.mOverlapNormalProgressColor = this.colorToColorStateList(var8.getColor(var3, null));
        this.mOverlapActivatedProgressColor = this.colorToColorStateList(var8.getColor(R.color.sesl_seekbar_overlap_color_activated, null));
        this.mOverlapActivatedThumbColor = this.mOverlapActivatedProgressColor;
        this.mDefaultActivatedThumbColor = this.getThumbTintList();
        if (this.mDefaultActivatedThumbColor == null) {
            this.mDefaultActivatedThumbColor = this.colorToColorStateList(var8.getColor(R.color.sesl_thumb_control_color_activated, null));
        }

        this.mAllowedSeekBarAnimation = var8.getBoolean(R.bool.sesl_seekbar_sliding_animation);
        if (this.mAllowedSeekBarAnimation) {
            this.initMuteAnimation();
        }

    }

    private void applyThumbTint() {
        if (this.mThumb != null && (this.mHasThumbTint || this.mHasThumbTintMode)) {
            this.mThumb = this.mThumb.mutate();
            if (this.mHasThumbTint) {
                DrawableCompat.setTintList(this.mThumb, this.mThumbTintList);
            }

            if (this.mHasThumbTintMode) {
                DrawableCompat.setTintMode(this.mThumb, this.mThumbTintMode);
            }

            if (this.mThumb.isStateful()) {
                this.mThumb.setState(this.getDrawableState());
            }
        }

    }

    private void applyTickMarkTint() {
        if (this.mTickMark != null && (this.mHasTickMarkTint || this.mHasTickMarkTintMode)) {
            this.mTickMark = this.mTickMark.mutate();
            if (this.mHasTickMarkTint) {
                DrawableCompat.setTintList(this.mTickMark, this.mTickMarkTintList);
            }

            if (this.mHasTickMarkTintMode) {
                DrawableCompat.setTintMode(this.mTickMark, this.mTickMarkTintMode);
            }

            if (this.mTickMark.isStateful()) {
                this.mTickMark.setState(this.getDrawableState());
            }
        }

    }

    private void attemptClaimDrag() {
        if (this.getParent() != null) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
        }

    }

    private void cancelMuteAnimation() {
        AnimatorSet var1 = this.mMuteAnimationSet;
        if (var1 != null && var1.isRunning()) {
            this.mMuteAnimationSet.cancel();
        }

    }

    private boolean checkInvalidatedDualColorMode() {
        boolean var1;
        if (this.mOverlapPoint != -1 && this.mOverlapBackground != null) {
            var1 = false;
        } else {
            var1 = true;
        }

        return var1;
    }

    private ColorStateList colorToColorStateList(int var1) {
        return new ColorStateList(new int[][]{new int[0]}, new int[]{var1});
    }

    private void getDualOverlapDrawable() {
        int var1 = super.mCurrentMode;
        if (var1 == 0) {
            this.mOverlapPrimary = this.getContext().getResources().getDrawable(R.drawable.sesl_scrubber_progress_horizontal_extra, null);
            this.mOverlapBackground = this.getContext().getResources().getDrawable(R.drawable.sesl_scrubber_progress_horizontal_extra, null);
        } else if (var1 == 3) {
            this.mOverlapPrimary = this.getContext().getResources().getDrawable(R.drawable.sesl_scrubber_progress_vertical_extra, null);
            this.mOverlapBackground = this.getContext().getResources().getDrawable(R.drawable.sesl_scrubber_progress_vertical_extra, null);
        }

    }

    private float getScale() {
        int var1 = this.getMin();
        int var2 = this.getMax() - var1;
        float var3;
        if (var2 > 0) {
            var3 = (float)(this.getProgress() - var1) / (float)var2;
        } else {
            var3 = 0.0F;
        }

        return var3;
    }

    private void initMuteAnimation() {
        this.mMuteAnimationSet = new AnimatorSet();
        ArrayList var1 = new ArrayList();
        int var2 = 400;

        int var6;
        for(int var3 = 0; var3 < 8; var2 = var6) {
            boolean var4;
            if (var3 % 2 == 0) {
                var4 = true;
            } else {
                var4 = false;
            }

            ValueAnimator var5;
            if (var4) {
                var5 = ValueAnimator.ofInt(new int[]{0, var2});
            } else {
                var5 = ValueAnimator.ofInt(new int[]{var2, 0});
            }

            var5.setDuration((long)62);
            var5.setInterpolator(new LinearInterpolator());
            var5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator var1) {
                    SeslAbsSeekBar.this.mCurrentProgressLevel = (Integer)var1.getAnimatedValue();
                    SeslAbsSeekBar var2 = SeslAbsSeekBar.this;
                    var2.onSlidingRefresh(var2.mCurrentProgressLevel);
                }
            });
            var1.add(var5);
            var6 = var2;
            if (var4) {
                var6 = (int)((double)var2 * 0.6D);
            }

            ++var3;
        }

        this.mMuteAnimationSet.playSequentially(var1);
    }

    private void setHotspot(float var1, float var2) {
        Drawable var3 = this.getBackground();
        if (var3 != null) {
            DrawableCompat.setHotspot(var3, var1, var2);
        }

    }

    private void setProgressOverlapTintList(ColorStateList var1) {
        super.setProgressTintList(var1);
    }

    private void setThumbOverlapTintList(ColorStateList var1) {
        this.mThumbTintList = var1;
        this.mHasThumbTint = true;
        this.applyThumbTint();
    }

    private void setThumbPos(int var1, Drawable var2, float var3, int var4) {
        if (super.mCurrentMode == 3) {
            this.setThumbPosInVertical(this.getHeight(), var2, var3, var4);
        } else {
            int var5 = this.getPaddingLeft();
            int var6 = this.getPaddingRight();
            int var7 = var2.getIntrinsicWidth();
            int var8 = var2.getIntrinsicHeight();
            int var9 = var1 - var5 - var6 - var7 + this.mThumbOffset * 2;
            var5 = (int)(var3 * (float)var9 + 0.5F);
            if (var4 == -2147483648) {
                Rect var10 = var2.getBounds();
                var4 = var10.top;
                var1 = var10.bottom;
            } else {
                var1 = var4 + var8;
            }

            var6 = var5;
            if (super.mMirrorForRtl) {
                var6 = var5;
                if (ViewUtils.isLayoutRtl(this)) {
                    var6 = var9 - var5;
                }
            }

            var9 = var6 + var7;
            Drawable var12 = this.getBackground();
            if (var12 != null) {
                int var11 = this.getPaddingLeft() - this.mThumbOffset;
                var5 = this.getPaddingTop();
                DrawableCompat.setHotspotBounds(var12, var6 + var11, var4 + var5, var11 + var9, var5 + var1);
            }

            var2.setBounds(var6, var4, var9, var1);
            this.mThumbPosX = var6 + this.getPaddingLeft();
            this.mThumbPosY = var8 / 2 + var4 + this.getPaddingTop();
            this.mThumbPosXFloat = (float)this.mThumbPosX + (float)var7 / 2.0F - (float)this.mThumbOffset;
            this.mThumbPosYFloat = (float)var4 + (float)var8 / 2.0F + (float)this.getPaddingTop();
            this.updateSplitProgress();
        }
    }

    private void setThumbPosInVertical(int var1, Drawable var2, float var3, int var4) {
        int var5 = this.getPaddingTop();
        int var6 = this.getPaddingBottom();
        int var7 = var2.getIntrinsicHeight();
        int var8 = var2.getIntrinsicHeight();
        var6 = var1 - var5 - var6 - var8 + this.mThumbOffset * 2;
        var5 = (int)(var3 * (float)var6 + 0.5F);
        if (var4 == -2147483648) {
            Rect var9 = var2.getBounds();
            var4 = var9.left;
            var1 = var9.right;
        } else {
            var1 = var4 + var7;
        }

        var5 = var6 - var5;
        var8 += var5;
        Drawable var11 = this.getBackground();
        if (var11 != null) {
            int var10 = this.getPaddingLeft();
            var6 = this.getPaddingTop() - this.mThumbOffset;
            DrawableCompat.setHotspotBounds(var11, var4 + var10, var5 + var6, var10 + var1, var6 + var8);
        }

        var2.setBounds(var4, var5, var1, var8);
        this.mThumbPosX = var7 / 2 + var4 + this.getPaddingLeft();
        this.mThumbPosY = var5 + this.getPaddingTop();
        this.mThumbPosXFloat = (float)var4 + (float)var7 / 2.0F + (float)this.getPaddingLeft();
        this.mThumbPosYFloat = (float)this.mThumbPosY;
    }

    private void startDrag(MotionEvent var1) throws Throwable {
        this.setPressed(true);
        Drawable var2 = this.mThumb;
        if (var2 != null) {
            this.invalidate(var2.getBounds());
        }

        this.onStartTrackingTouch();
        this.trackTouchEvent(var1);
        this.attemptClaimDrag();
    }

    private void startMuteAnimation() {
        this.cancelMuteAnimation();
        AnimatorSet var1 = this.mMuteAnimationSet;
        if (var1 != null) {
            var1.start();
        }

    }

    private void trackHoverEvent(int var1) {
        int var2 = this.getWidth();
        int var3 = this.getPaddingLeft();
        int var4 = this.getPaddingRight();
        int var5 = this.getPaddingLeft();
        float var6 = 0.0F;
        float var7;
        if (var1 < var5) {
            var7 = 0.0F;
        } else if (var1 > var2 - this.getPaddingRight()) {
            var7 = 1.0F;
        } else {
            var7 = (float)(var1 - this.getPaddingLeft()) / (float)(var2 - var3 - var4);
            var6 = this.mTouchProgressOffset;
        }

        this.mHoveringLevel = (int)(var6 + var7 * (float)this.getMax());
    }

    private void trackTouchEvent(MotionEvent var1) throws Throwable {
        if (super.mCurrentMode == 3) {
            this.trackTouchEventInVertical(var1);
        } else {
            int var2;
            int var3;
            int var4;
            int var5;
            float var6;
            float var7;
            label41: {
                label48: {
                    var2 = Math.round(var1.getX());
                    var3 = Math.round(var1.getY());
                    var4 = this.getWidth();
                    var5 = var4 - this.getPaddingLeft() - this.getPaddingRight();
                    if (ViewUtils.isLayoutRtl(this) && super.mMirrorForRtl) {
                        if (var2 > var4 - this.getPaddingRight()) {
                            break label48;
                        }

                        if (var2 >= this.getPaddingLeft()) {
                            var6 = (float)(var5 - var2 + this.getPaddingLeft()) / (float)var5;
                            var7 = this.mTouchProgressOffset;
                            break label41;
                        }
                    } else {
                        if (var2 < this.getPaddingLeft()) {
                            break label48;
                        }

                        if (var2 <= var4 - this.getPaddingRight()) {
                            var6 = (float)(var2 - this.getPaddingLeft()) / (float)var5;
                            var7 = this.mTouchProgressOffset;
                            break label41;
                        }
                    }

                    var6 = 1.0F;
                    var7 = 0.0F;
                    break label41;
                }

                var6 = 0.0F;
                var7 = var6;
            }

            var4 = this.getMax();
            var5 = this.getMin();
            float var8 = 1.0F / (float)this.getMax();
            float var9 = var6;
            if (var6 > 0.0F) {
                var9 = var6;
                if (var6 < 1.0F) {
                    float var10 = var6 % var8;
                    var9 = var6;
                    if (var10 > var8 / 2.0F) {
                        var9 = var6 + (var8 - var10);
                    }
                }
            }

            var6 = (float)(var4 - var5);
            this.setHotspot((float)var2, (float)var3);
            this.setProgressInternal(Math.round(var7 + var9 * var6), true, false);
        }
    }

    private void trackTouchEventInVertical(MotionEvent var1) throws Throwable {
        int var2 = this.getHeight();
        int var3 = this.getPaddingTop();
        int var4 = this.getPaddingBottom();
        int var5 = Math.round(var1.getX());
        int var6 = var2 - Math.round(var1.getY());
        int var7 = this.getPaddingBottom();
        float var8 = 0.0F;
        float var9;
        if (var6 < var7) {
            var9 = 0.0F;
        } else if (var6 > var2 - this.getPaddingTop()) {
            var9 = 1.0F;
        } else {
            var9 = (float)(var6 - this.getPaddingBottom()) / (float)(var2 - var3 - var4);
            var8 = this.mTouchProgressOffset;
        }

        float var10 = (float)this.getMax();
        this.setHotspot((float)var5, (float)var6);
        this.setProgressInternal((int)(var8 + var9 * var10), true, false);
    }

    private void updateBoundsForDualColor() {
        if (this.getCurrentDrawable() != null && !this.checkInvalidatedDualColorMode()) {
            Rect var1 = this.getCurrentDrawable().getBounds();
            int var2 = this.getMax();
            int var3 = this.getProgress();
            int var4 = super.mCurrentMode;
            int var5;
            if (var4 == 0) {
                var5 = var1.right - var1.left;
                var4 = this.mOverlapPoint;
                if (var4 != 0 && var4 < this.getProgress() && !this.mLargeFont) {
                    var4 = var1.left;
                } else {
                    var4 = (int)((float)var1.left + (float)var5 * ((float)this.mOverlapPoint / (float)var2));
                }

                var2 = Math.min((int)((float)var1.left + (float)var5 * ((float)var3 / (float)var2)), var1.right);
                this.mOverlapBackground.setBounds(var4, var1.top, var1.right, var1.bottom);
                this.mOverlapPrimary.setBounds(var4, var1.top, var2, var1.bottom);
            } else if (var4 == 3) {
                var5 = var1.bottom;
                var4 = var1.top;
                float var6 = (float)var4;
                float var7 = (float)(var5 - var4);
                float var8 = (float)(var2 - this.mOverlapPoint);
                float var9 = (float)var2;
                var5 = (int)(var6 + var8 / var9 * var7);
                var2 = (int)((float)var4 + var7 * ((float)(var2 - var3) / var9));
                this.mOverlapBackground.setBounds(var1.left, var4, var1.right, var5);
                this.mOverlapPrimary.setBounds(var1.left, var2, var1.right, var5);
            }
        }

    }

    private void updateDualColorMode() {
        if (!this.checkInvalidatedDualColorMode()) {
            DrawableCompat.setTintList(this.mOverlapPrimary, this.mOverlapActivatedProgressColor);
            DrawableCompat.setTintList(this.mOverlapBackground, this.mOverlapNormalProgressColor);
            if (!this.mLargeFont) {
                if (this.getProgress() > this.mOverlapPoint) {
                    this.setProgressOverlapTintList(this.mOverlapActivatedProgressColor);
                    this.setThumbOverlapTintList(this.mOverlapActivatedThumbColor);
                } else {
                    this.setProgressTintList(this.mDefaultActivatedProgressColor);
                    this.setThumbTintList(this.mDefaultActivatedThumbColor);
                }
            }

            this.updateBoundsForDualColor();
        }
    }

    private void updateSplitProgress() {
        if (super.mCurrentMode == 4) {
            Drawable var1 = this.mSplitProgress;
            Rect var2 = this.getCurrentDrawable().getBounds();
            if (var1 != null) {
                if (super.mMirrorForRtl && ViewUtils.isLayoutRtl(this)) {
                    var1.setBounds(this.mThumbPosX, var2.top, this.getWidth() - this.getPaddingRight(), var2.bottom);
                } else {
                    var1.setBounds(this.getPaddingLeft(), var2.top, this.mThumbPosX, var2.bottom);
                }
            }

            int var3 = this.getWidth();
            int var4 = this.getHeight();
            var1 = this.mDivider;
            if (var1 != null) {
                float var5 = (float)var3 / 2.0F;
                float var6 = super.mDensity;
                var3 = (int)(var5 - var6 * 4.0F / 2.0F);
                float var7 = (float)var4 / 2.0F;
                var1.setBounds(var3, (int)(var7 - var6 * 22.0F / 2.0F), (int)(var5 + 4.0F * var6 / 2.0F), (int)(var7 + var6 * 22.0F / 2.0F));
            }

        }
    }

    private void updateThumbAndTrackPos(int var1, int var2) {
        if (super.mCurrentMode == 3) {
            this.updateThumbAndTrackPosInVertical(var1, var2);
        } else {
            int var3 = var2 - this.getPaddingTop() - this.getPaddingBottom();
            Drawable var4 = this.getCurrentDrawable();
            Drawable var5 = this.mThumb;
            int var6 = Math.min(super.mMaxHeight, var3);
            if (var5 == null) {
                var2 = 0;
            } else {
                var2 = var5.getIntrinsicHeight();
            }

            int var7;
            if (var2 > var6) {
                var3 = (var3 - var2) / 2;
                var7 = (var2 - var6) / 2;
                var7 += var3;
                var3 = var3;
                var2 = var7;
            } else {
                var7 = (var3 - var6) / 2;
                var3 = (var6 - var2) / 2 + var7;
                var2 = var7;
            }

            if (var4 != null) {
                var4.setBounds(0, var2, var1 - this.getPaddingRight() - this.getPaddingLeft(), var6 + var2);
            }

            if (var5 != null) {
                this.setThumbPos(var1, var5, this.getScale(), var3);
            }

            this.updateSplitProgress();
        }
    }

    private void updateThumbAndTrackPosInVertical(int var1, int var2) {
        int var3 = var1 - this.getPaddingLeft() - this.getPaddingRight();
        Drawable var4 = this.getCurrentDrawable();
        Drawable var5 = this.mThumb;
        int var6 = Math.min(super.mMaxWidth, var3);
        if (var5 == null) {
            var1 = 0;
        } else {
            var1 = var5.getIntrinsicWidth();
        }

        int var7;
        if (var1 > var6) {
            var7 = (var3 - var1) / 2;
            var1 = (var1 - var6) / 2 + var7;
        } else {
            var7 = (var3 - var6) / 2;
            var6 = (var6 - var1) / 2 + var7;
            var1 = var7;
            var7 = var6;
        }

        if (var4 != null) {
            var4.setBounds(var1, 0, var3 - var1, var2 - this.getPaddingBottom() - this.getPaddingTop());
        }

        if (var5 != null) {
            this.setThumbPosInVertical(var2, var5, this.getScale(), var7);
        }

    }

    private void updateWarningMode(int var1) {
        int var2 = super.mCurrentMode;
        boolean var3 = true;
        if (var2 == 1) {
            boolean var4;
            if (var1 == this.getMax()) {
                var4 = var3;
            } else {
                var4 = false;
            }

            if (var4) {
                this.setProgressOverlapTintList(this.mOverlapActivatedProgressColor);
                this.setThumbOverlapTintList(this.mOverlapActivatedThumbColor);
            } else {
                this.setProgressTintList(this.mDefaultActivatedProgressColor);
                this.setThumbTintList(this.mDefaultActivatedThumbColor);
            }
        }

    }

    public boolean canUserSetProgress() {
        boolean var1;
        if (!this.isIndeterminate() && this.isEnabled()) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
    }

    public void drawThumb(Canvas var1) {
        if (this.mThumb != null) {
            int var2 = var1.save();
            if (super.mCurrentMode == 3) {
                var1.translate((float)this.getPaddingLeft(), (float)(this.getPaddingTop() - this.mThumbOffset));
            } else {
                var1.translate((float)(this.getPaddingLeft() - this.mThumbOffset), (float)this.getPaddingTop());
            }

            this.mThumb.draw(var1);
            var1.restoreToCount(var2);
        }

    }

    public void drawTickMarks(Canvas var1) {
        if (this.mTickMark != null) {
            int var2 = this.getMax() - this.getMin();
            int var3 = 1;
            if (var2 > 1) {
                int var4 = this.mTickMark.getIntrinsicWidth();
                int var5 = this.mTickMark.getIntrinsicHeight();
                if (var4 >= 0) {
                    var4 /= 2;
                } else {
                    var4 = 1;
                }

                if (var5 >= 0) {
                    var3 = var5 / 2;
                }

                this.mTickMark.setBounds(-var4, -var3, var4, var3);
                float var6 = (float)(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight()) / (float)var2;
                var3 = var1.save();
                var1.translate((float)this.getPaddingLeft(), (float)this.getHeight() / 2.0F);

                for(var4 = 0; var4 <= var2; ++var4) {
                    this.mTickMark.draw(var1);
                    var1.translate(var6, 0.0F);
                }

                var1.restoreToCount(var3);
            }
        }

    }

    public void drawTrack(Canvas var1) {
        Drawable var2 = this.mThumb;
        if (var2 != null && this.mSplitTrack) {
            Rect var3 = DrawableUtils.getOpticalBounds(var2);
            Rect var4 = this.mTempRect;
            var2.copyBounds(var4);
            var4.offset(this.getPaddingLeft() - this.mThumbOffset, this.getPaddingTop());
            var4.left += var3.left;
            var4.right -= var3.right;
            int var5 = var1.save();
            var1.clipRect(var4, Region.Op.DIFFERENCE);
            super.drawTrack(var1);
            this.drawTickMarks(var1);
            var1.restoreToCount(var5);
        } else {
            super.drawTrack(var1);
            this.drawTickMarks(var1);
        }

        if (!this.checkInvalidatedDualColorMode()) {
            var1.save();
            if (super.mCurrentMode == 3) {
                var1.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
            } else if (super.mMirrorForRtl && ViewUtils.isLayoutRtl(this)) {
                var1.translate((float)(this.getWidth() - this.getPaddingRight()), (float)this.getPaddingTop());
                var1.scale(-1.0F, 1.0F);
            } else {
                var1.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
            }

            this.mOverlapBackground.draw(var1);
            if (this.getProgress() > this.mOverlapPoint) {
                this.mOverlapPrimary.draw(var1);
            }

            var1.restore();
        }

    }

    public void drawableHotspotChanged(float var1, float var2) {
        super.drawableHotspotChanged(var1, var2);
        Drawable var3 = this.mThumb;
        if (var3 != null) {
            DrawableCompat.setHotspot(var3, var1, var2);
        }

    }

    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable var1 = this.getProgressDrawable();
        if (var1 != null && this.mDisabledAlpha < 1.0F) {
            int var2;
            if (this.isEnabled()) {
                var2 = 255;
            } else {
                var2 = (int)(this.mDisabledAlpha * 255.0F);
            }

            var1.setAlpha(var2);
            var1 = this.mOverlapPrimary;
            if (var1 != null && this.mOverlapBackground != null) {
                var1.setAlpha(var2);
                this.mOverlapBackground.setAlpha(var2);
            }
        }

        if (this.mThumb != null && this.mHasThumbTint) {
            if (!this.isEnabled()) {
                DrawableCompat.setTintList(this.mThumb, (ColorStateList)null);
            } else {
                DrawableCompat.setTintList(this.mThumb, this.mDefaultActivatedThumbColor);
                this.updateDualColorMode();
            }
        }

        var1 = this.mThumb;
        if (var1 != null && var1.isStateful() && var1.setState(this.getDrawableState())) {
            this.invalidateDrawable(var1);
        }

        var1 = this.mTickMark;
        if (var1 != null && var1.isStateful() && var1.setState(this.getDrawableState())) {
            this.invalidateDrawable(var1);
        }

    }

    public CharSequence getAccessibilityClassName() {
        Log.d("SeslAbsSeekBar", "Stack:", new Throwable("stack dump"));
        return AbsSeekBar.class.getName();
    }

    public int getHoverPopupType() {
            return IS_BASE_SDK_VERSION ? com.mesalabs.cerberus.utils.ViewUtils.semGetHoverPopupType(this) : 0;
    }

    public int getKeyProgressIncrement() {
        return this.mKeyProgressIncrement;
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public Drawable getThumb() {
        return this.mThumb;
    }

    public Rect getThumbBounds() {
        Drawable var1 = this.mThumb;
        Rect var2;
        if (var1 != null) {
            var2 = var1.getBounds();
        } else {
            var2 = null;
        }

        return var2;
    }

    public int getThumbHeight() {
        return this.mThumb.getIntrinsicHeight();
    }

    public int getThumbOffset() {
        return this.mThumbOffset;
    }

    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public PorterDuff.Mode getThumbTintMode() {
        return this.mThumbTintMode;
    }

    public Drawable getTickMark() {
        return this.mTickMark;
    }

    public ColorStateList getTickMarkTintList() {
        return this.mTickMarkTintList;
    }

    public PorterDuff.Mode getTickMarkTintMode() {
        return this.mTickMarkTintMode;
    }

    public boolean isHoverPopupTypeNone(int var1) {
        boolean var2;
        if (IS_BASE_SDK_VERSION && var1 == 0 /*SeslHoverPopupWindowReflector.getField_TYPE_NONE()*/) {
            var2 = true;
        } else {
            var2 = false;
        }

        return var2;
    }

    public boolean isHoverPopupTypeUserCustom(int var1) {
        boolean var2;
        if (IS_BASE_SDK_VERSION && var1 == 3 /*SeslHoverPopupWindowReflector.getField_TYPE_USER_CUSTOM()*/) {
            var2 = true;
        } else {
            var2 = false;
        }

        return var2;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable var1 = this.mThumb;
        if (var1 != null) {
            var1.jumpToCurrentState();
        }

        var1 = this.mTickMark;
        if (var1 != null) {
            var1.jumpToCurrentState();
        }

    }

    public void onDraw(Canvas var1) {
        synchronized(this){}

        try {
            super.onDraw(var1);
            if (this.supportIsHoveringUIEnabled()) {
                int var2 = this.getHoverPopupType();
                if (this.isHoverPopupTypeUserCustom(var2) && this.mPreviousHoverPopupType != var2) {
                    this.mPreviousHoverPopupType = var2;
                    this.setHoverPopupGravity(12849);
                    this.setHoverPopupOffset(0, this.getMeasuredHeight() / 2);
                    this.setHoverPopupDetectTime();
                }
            }

            if (super.mCurrentMode == 4) {
                this.mSplitProgress.draw(var1);
                this.mDivider.draw(var1);
            }

            if (!this.mIsTouchDisabled) {
                this.drawThumb(var1);
            }
        } finally {
            ;
        }

    }

    public void onHoverChanged(int var1, int var2, int var3) {
    }

    public boolean onHoverEvent(MotionEvent var1) {
        if (this.supportIsHoveringUIEnabled()) {
            int var2 = var1.getAction();
            int var3 = (int)var1.getX();
            int var4 = (int)var1.getY();
            if (var2 != 7) {
                if (var2 != 9) {
                    if (var2 == 10) {
                        this.onStopTrackingHover();
                    }
                } else {
                    this.trackHoverEvent(var3);
                    this.onStartTrackingHover(this.mHoveringLevel, var3, var4);
                }
            } else {
                this.trackHoverEvent(var3);
                this.onHoverChanged(this.mHoveringLevel, var3, var4);
                if (this.isHoverPopupTypeUserCustom(this.getHoverPopupType())) {
                    this.setHoveringPoint((int)var1.getRawX(), (int)var1.getRawY());
                    this.updateHoverPopup();
                }
            }
        }

        return super.onHoverEvent(var1);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
        super.onInitializeAccessibilityNodeInfo(var1);
        var1.setClassName(SeekBar.class.getName());
        if (this.isEnabled()) {
            int var2 = this.getProgress();
            if (var2 > this.getMin()) {
                AccessibilityNodeInfoCompat.wrap(var1).addAction(8192);
            }

            if (var2 < this.getMax()) {
                AccessibilityNodeInfoCompat.wrap(var1).addAction(4096);
            }
        }

    }

    public void onKeyChange() {
    }

    public boolean onKeyDown(int var1, KeyEvent var2) {
        if (this.isEnabled()) {
            int var3 = this.mKeyProgressIncrement;
            int var4;
            if (super.mCurrentMode == 3) {
                var4 = var3;
                if (var1 != 19) {
                    if (var1 != 20 && var1 != 69) {
                        var4 = var3;
                        if (var1 != 70) {
                            var4 = var3;
                            if (var1 != 81) {
                                return super.onKeyDown(var1, var2);
                            }
                        }
                    } else {
                        var4 = -var3;
                    }
                }

                var3 = var4;
                if (ViewUtils.isLayoutRtl(this)) {
                    var3 = -var4;
                }

                try {
                    if (this.setProgressInternal(this.getProgress() + var3, true, true)) {
                        this.onKeyChange();
                        return true;
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            } else {
                label43: {
                    if (var1 != 21) {
                        var4 = var3;
                        if (var1 == 22) {
                            break label43;
                        }

                        if (var1 != 69) {
                            var4 = var3;
                            if (var1 != 70) {
                                var4 = var3;
                                if (var1 != 81) {
                                    return super.onKeyDown(var1, var2);
                                }
                            }
                            break label43;
                        }
                    }

                    var4 = -var3;
                }

                var3 = var4;
                if (ViewUtils.isLayoutRtl(this)) {
                    var3 = -var4;
                }

                try {
                    if (this.setProgressInternal(this.getProgress() + var3, true, true)) {
                        this.onKeyChange();
                        return true;
                    }
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        return super.onKeyDown(var1, var2);
    }

    public void onMeasure(int var1, int var2) {
        synchronized(this){}

        Throwable var10000;
        label637: {
            Drawable var3;
            boolean var10001;
            try {
                var3 = this.getCurrentDrawable();
            } catch (Throwable var81) {
                var10000 = var81;
                var10001 = false;
                break label637;
            }

            int var4;
            int var5;
            int var6;
            if (var3 != null) {
                label638: {
                    label639: {
                        label625: {
                            label624: {
                                try {
                                    if (super.mCurrentMode != 3) {
                                        break label639;
                                    }

                                    if (this.mThumb != null) {
                                        break label624;
                                    }
                                } catch (Throwable var80) {
                                    var10000 = var80;
                                    var10001 = false;
                                    break label637;
                                }

                                var4 = 0;
                                break label625;
                            }

                            try {
                                var4 = this.mThumb.getIntrinsicHeight();
                            } catch (Throwable var78) {
                                var10000 = var78;
                                var10001 = false;
                                break label637;
                            }
                        }

                        try {
                            var5 = Math.max(super.mMinWidth, Math.min(super.mMaxWidth, var3.getIntrinsicHeight()));
                            var6 = Math.max(super.mMinHeight, Math.min(super.mMaxHeight, var3.getIntrinsicWidth()));
                            var5 = Math.max(var4, var5);
                        } catch (Throwable var77) {
                            var10000 = var77;
                            var10001 = false;
                            break label637;
                        }

                        var4 = var6;
                        var6 = var5;
                        break label638;
                    }

                    label614: {
                        label613: {
                            try {
                                if (this.mThumb == null) {
                                    break label613;
                                }
                            } catch (Throwable var79) {
                                var10000 = var79;
                                var10001 = false;
                                break label637;
                            }

                            try {
                                var4 = this.mThumb.getIntrinsicHeight();
                                break label614;
                            } catch (Throwable var76) {
                                var10000 = var76;
                                var10001 = false;
                                break label637;
                            }
                        }

                        var4 = 0;
                    }

                    try {
                        var6 = Math.max(super.mMinWidth, Math.min(super.mMaxWidth, var3.getIntrinsicWidth()));
                        var4 = Math.max(var4, Math.max(super.mMinHeight, Math.min(super.mMaxHeight, var3.getIntrinsicHeight())));
                    } catch (Throwable var75) {
                        var10000 = var75;
                        var10001 = false;
                        break label637;
                    }
                }
            } else {
                var4 = 0;
                var6 = var4;
            }

            label601:
            try {
                var5 = this.getPaddingLeft();
                int var7 = this.getPaddingRight();
                int var8 = this.getPaddingTop();
                int var9 = this.getPaddingBottom();
                this.setMeasuredDimension(View.resolveSizeAndState(var6 + var5 + var7, var1, 0), View.resolveSizeAndState(var4 + var8 + var9, var2, 0));
                return;
            } catch (Throwable var74) {
                var10000 = var74;
                var10001 = false;
                break label601;
            }
        }

        Throwable var82 = var10000;
        try {
            throw var82;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void onProgressRefresh(float var1, boolean var2, int var3) throws Throwable {
        int var4 = (int)(10000.0F * var1);
        boolean var5;
        if (this.mUseMuteAnimation && !this.mIsFirstSetProgress && !this.mIsDraggingForSliding) {
            var5 = true;
        } else {
            var5 = false;
        }

        if (var5 && this.mCurrentProgressLevel != 0 && var4 == 0) {
            this.startMuteAnimation();
        } else {
            this.cancelMuteAnimation();
            this.mIsFirstSetProgress = false;
            this.mCurrentProgressLevel = var4;
            super.onProgressRefresh(var1, var2, var3);
            Drawable var6 = this.mThumb;
            if (var6 != null) {
                this.setThumbPos(this.getWidth(), var6, var1, -2147483648);
                this.invalidate();
            }
        }

    }

    public void onResolveDrawables(int var1) {
        super.onResolveDrawables(var1);
        Drawable var2 = this.mThumb;
        if (var2 != null) {
            DrawableCompat.setLayoutDirection(var2, var1);
        }

    }

    public void onRtlPropertiesChanged(int var1) {
        super.onRtlPropertiesChanged(var1);
        Drawable var2 = this.mThumb;
        if (var2 != null) {
            this.setThumbPos(this.getWidth(), var2, this.getScale(), -2147483648);
            this.invalidate();
        }

    }

    public void onSizeChanged(int var1, int var2, int var3, int var4) {
        super.onSizeChanged(var1, var2, var3, var4);
        this.updateThumbAndTrackPos(var1, var2);
    }

    public void onSlidingRefresh(int var1) {
        super.onSlidingRefresh(var1);
        float var2 = (float)var1 / 10000.0F;
        Drawable var3 = this.mThumb;
        if (var3 != null) {
            this.setThumbPos(this.getWidth(), var3, var2, -2147483648);
            this.invalidate();
        }

    }

    public void onStartTrackingHover(int var1, int var2, int var3) {
    }

    public void onStartTrackingTouch() {
        this.mIsDragging = true;
    }

    public void onStopTrackingHover() {
    }

    public void onStopTrackingTouch() {
        this.mIsDragging = false;
    }

    public boolean onTouchEvent(MotionEvent var1) {
        if (this.mIsUserSeekable && !this.mIsTouchDisabled && this.isEnabled()) {
            int var2 = var1.getAction();
            if (var2 != 0) {
                if (var2 != 1) {
                    if (var2 != 2) {
                        if (var2 == 3) {
                            this.mIsDraggingForSliding = false;
                            if (this.mIsDragging) {
                                this.onStopTrackingTouch();
                                this.setPressed(false);
                            }

                            this.invalidate();
                        }
                    } else {
                        this.mIsDraggingForSliding = true;
                        if (this.mIsDragging) {
                            try {
                                this.trackTouchEvent(var1);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        } else {
                            float var3 = var1.getX();
                            float var4 = var1.getY();
                            if (super.mCurrentMode != 3 && Math.abs(var3 - this.mTouchDownX) > (float)this.mScaledTouchSlop || super.mCurrentMode == 3 && Math.abs(var4 - this.mTouchDownY) > (float)this.mScaledTouchSlop) {
                                try {
                                    this.startDrag(var1);
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    if (this.mIsDraggingForSliding) {
                        this.mIsDraggingForSliding = false;
                    }

                    if (this.mIsDragging) {
                        try {
                            this.trackTouchEvent(var1);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        this.onStopTrackingTouch();
                        this.setPressed(false);
                    } else {
                        this.onStartTrackingTouch();
                        try {
                            this.trackTouchEvent(var1);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        this.onStopTrackingTouch();
                    }

                    this.invalidate();
                }
            } else {
                this.mIsDraggingForSliding = false;
                if (this.supportIsInScrollingContainer()) {
                    this.mTouchDownX = var1.getX();
                    this.mTouchDownY = var1.getY();
                } else {
                    try {
                        this.startDrag(var1);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public void onVisualProgressChanged(int var1, float var2) {
        super.onVisualProgressChanged(var1, var2);
        if (var1 == R.id.progress) {
            Drawable var3 = this.mThumb;
            if (var3 != null) {
                this.setThumbPos(this.getWidth(), var3, var2, -2147483648);
                this.invalidate();
            }
        }

    }

    public boolean performAccessibilityAction(int var1, Bundle var2) {
        if (super.performAccessibilityAction(var1, var2)) {
            return true;
        } else if (!this.isEnabled()) {
            return false;
        } else if (var1 != 4096 && var1 != 8192) {
            if (var1 != 16908349) {
                return false;
            } else if (!this.canUserSetProgress()) {
                return false;
            } else {
                try {
                    return var2 != null && var2.containsKey("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE") ? this.setProgressInternal((int)var2.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"), true, true) : false;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                    return false;
                }
            }
        } else if (!this.canUserSetProgress()) {
            return false;
        } else {
            int var3 = Math.max(1, Math.round((float)(this.getMax() - this.getMin()) / 20.0F));
            int var4 = var3;
            if (var1 == 8192) {
                var4 = -var3;
            }

            try {
                if (this.setProgressInternal(this.getProgress() + var4, true, true)) {
                    this.onKeyChange();
                    return true;
                } else {
                    return false;
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return false;
            }
        }
    }

    public void setHoverPopupDetectTime() {
        if (IS_BASE_SDK_VERSION) {
            Utils.genericInvokeMethod("com.samsung.android.widget.SemHoverPopupWindow", com.mesalabs.cerberus.utils.ViewUtils.semGetHoverPopup(this, true), Build.VERSION.SDK_INT >= 29 ? "hidden_setHoverDetectTime" : "setHoverDetectTime", 200);
        }

    }

    public void setHoverPopupGravity(int var1) {
        if (IS_BASE_SDK_VERSION) {
            Utils.genericInvokeMethod("com.samsung.android.widget.SemHoverPopupWindow", com.mesalabs.cerberus.utils.ViewUtils.semGetHoverPopup(this, true), Build.VERSION.SDK_INT >= 29 ? "hidden_setGravity" : "setGravity", var1);
        }

    }

    public void setHoverPopupOffset(int var1, int var2) {
        if (IS_BASE_SDK_VERSION) {
            Utils.genericInvokeMethod("com.samsung.android.widget.SemHoverPopupWindow", com.mesalabs.cerberus.utils.ViewUtils.semGetHoverPopup(this, true), Build.VERSION.SDK_INT >= 29 ? "hidden_setOffset" : "setOffset", var1, var2);
        }

    }

    public void setHoveringPoint(int var1, int var2) {
        if (IS_BASE_SDK_VERSION) {
            Utils.genericInvokeMethod("com.samsung.android.widget.SemHoverPopupWindow", this, "setHoveringPoint", var1, var2);
        }

    }

    public void setKeyProgressIncrement(int var1) {
        int var2 = var1;
        if (var1 < 0) {
            var2 = -var1;
        }

        this.mKeyProgressIncrement = var2;
    }

    public void setMax(int var1) throws Throwable {
        synchronized(this){}

        try {
            super.setMax(var1);
            this.mIsFirstSetProgress = true;
            var1 = this.getMax() - this.getMin();
            if (this.mKeyProgressIncrement == 0 || var1 / this.mKeyProgressIncrement > 20) {
                this.setKeyProgressIncrement(Math.max(1, Math.round((float)var1 / 20.0F)));
            }
        } finally {
            ;
        }

    }

    public void setMin(int var1) throws Throwable {
        synchronized(this){}

        try {
            super.setMin(var1);
            var1 = this.getMax() - this.getMin();
            if (this.mKeyProgressIncrement == 0 || var1 / this.mKeyProgressIncrement > 20) {
                this.setKeyProgressIncrement(Math.max(1, Math.round((float)var1 / 20.0F)));
            }
        } finally {
            ;
        }

    }

    public void setMode(int var1) throws Throwable {
        super.setMode(var1);
        if (var1 != 0) {
            if (var1 != 1) {
                if (var1 != 3) {
                    if (var1 == 4) {
                        this.mSplitProgress = this.getContext().getResources().getDrawable(R.drawable.sesl_split_seekbar_primary_progress, null);
                        this.mDivider = this.getContext().getResources().getDrawable(R.drawable.sesl_split_seekbar_vertical_bar, null);
                        this.updateSplitProgress();
                    }
                } else {
                    this.setThumb(this.getContext().getResources().getDrawable(R.drawable.sesl_scrubber_control_anim, null));
                }
            } else {
                this.updateWarningMode(this.getProgress());
            }
        } else {
            this.setProgressTintList(this.mDefaultActivatedProgressColor);
            this.setThumbTintList(this.mDefaultActivatedThumbColor);
        }

        this.invalidate();
    }

    public void setOverlapPointForDualColor(int var1) {
        if (var1 < this.getMax()) {
            if (var1 == -1) {
                this.mOverlapPoint = var1;
                this.setProgressTintList(this.mDefaultActivatedProgressColor);
                this.setThumbTintList(this.mDefaultActivatedThumbColor);
            } else {
                this.mOverlapPoint = var1;
                this.getDualOverlapDrawable();
                this.updateDualColorMode();
            }

            this.invalidate();
        }
    }

    public void setProgressDrawable(Drawable var1) throws Throwable {
        super.setProgressDrawable(var1);
    }

    public boolean setProgressInternal(int var1, boolean var2, boolean var3) throws Throwable {
        var2 = super.setProgressInternal(var1, var2, var3);
        this.updateWarningMode(var1);
        this.updateDualColorMode();
        return var2;
    }

    public void setProgressTintList(ColorStateList var1) {
        super.setProgressTintList(var1);
        this.mDefaultActivatedProgressColor = var1;
    }

    public void setSplitTrack(boolean var1) {
        this.mSplitTrack = var1;
        this.invalidate();
    }

    public void setThumb(Drawable var1) {
        Drawable var2 = this.mThumb;
        boolean var3;
        if (var2 != null && var1 != var2) {
            var2.setCallback((Drawable.Callback)null);
            var3 = true;
        } else {
            var3 = false;
        }

        if (var1 != null) {
            var1.setCallback(this);
            if (this.canResolveLayoutDirection()) {
                DrawableCompat.setLayoutDirection(var1, ViewCompat.getLayoutDirection(this));
            }

            if (super.mCurrentMode == 3) {
                this.mThumbOffset = var1.getIntrinsicHeight() / 2;
            } else {
                this.mThumbOffset = var1.getIntrinsicWidth() / 2;
            }

            if (var3 && (var1.getIntrinsicWidth() != this.mThumb.getIntrinsicWidth() || var1.getIntrinsicHeight() != this.mThumb.getIntrinsicHeight())) {
                this.requestLayout();
            }
        }

        this.mThumb = var1;
        this.applyThumbTint();
        this.invalidate();
        if (var3) {
            this.updateThumbAndTrackPos(this.getWidth(), this.getHeight());
            if (var1 != null && var1.isStateful()) {
                var1.setState(this.getDrawableState());
            }
        }

    }

    public void setThumbOffset(int var1) {
        this.mThumbOffset = var1;
        this.invalidate();
    }

    public void setThumbTintColor(int var1) {
        ColorStateList var2 = this.colorToColorStateList(var1);
        if (!var2.equals(this.mDefaultActivatedThumbColor)) {
            this.mDefaultActivatedThumbColor = var2;
        }

    }

    public void setThumbTintList(ColorStateList var1) {
        this.mThumbTintList = var1;
        this.mHasThumbTint = true;
        this.applyThumbTint();
        this.mDefaultActivatedThumbColor = var1;
    }

    public void setThumbTintMode(PorterDuff.Mode var1) {
        this.mThumbTintMode = var1;
        this.mHasThumbTintMode = true;
        this.applyThumbTint();
    }

    public void setTickMark(Drawable var1) {
        Drawable var2 = this.mTickMark;
        if (var2 != null) {
            var2.setCallback((Drawable.Callback)null);
        }

        this.mTickMark = var1;
        if (var1 != null) {
            var1.setCallback(this);
            DrawableCompat.setLayoutDirection(var1, ViewCompat.getLayoutDirection(this));
            if (var1.isStateful()) {
                var1.setState(this.getDrawableState());
            }

            this.applyTickMarkTint();
        }

        this.invalidate();
    }

    public void setTickMarkTintList(ColorStateList var1) {
        this.mTickMarkTintList = var1;
        this.mHasTickMarkTint = true;
        this.applyTickMarkTint();
    }

    public void setTickMarkTintMode(PorterDuff.Mode var1) {
        this.mTickMarkTintMode = var1;
        this.mHasTickMarkTintMode = true;
        this.applyTickMarkTint();
    }

    public boolean supportIsHoveringUIEnabled() {
        boolean var1;
        if (IS_BASE_SDK_VERSION && (boolean) Utils.genericInvokeMethod(View.class, this, "isHoveringUIEnabled")) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
    }

    public boolean supportIsInScrollingContainer() {
        return (boolean) Utils.genericInvokeMethod(View.class, this, "isInScrollingContainer");
    }

    public void updateDrawableBounds(int var1, int var2) {
        super.updateDrawableBounds(var1, var2);
        this.updateThumbAndTrackPos(var1, var2);
        this.updateBoundsForDualColor();
    }

    public void updateHoverPopup() {
        if (IS_BASE_SDK_VERSION) {
            Utils.genericInvokeMethod("com.samsung.android.widget.SemHoverPopupWindow", com.mesalabs.cerberus.utils.ViewUtils.semGetHoverPopup(this, true), Build.VERSION.SDK_INT >= 29 ? "hidden_update" : "update");
        }

    }

    public boolean verifyDrawable(Drawable var1) {
        boolean var2;
        if (var1 != this.mThumb && var1 != this.mTickMark && !super.verifyDrawable(var1)) {
            var2 = false;
        } else {
            var2 = true;
        }

        return var2;
    }
}
