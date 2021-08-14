package com.samsung.android.ui.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.Drawable.Callback;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.RangeInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.RemoteViews.RemoteView;
import java.util.ArrayList;

import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pools;
import androidx.core.view.ViewCompat;

import com.mesalabs.ten.update.R;
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

@RemoteView
public class SeslProgressBar extends View {
    public static final DecelerateInterpolator PROGRESS_ANIM_INTERPOLATOR = new DecelerateInterpolator();
    public final FloatProperty<SeslProgressBar> VISUAL_PROGRESS;
    public SeslProgressBar.AccessibilityEventSender mAccessibilityEventSender;
    public boolean mAggregatedIsVisible;
    public AlphaAnimation mAnimation;
    public boolean mAttached;
    public int mBehavior;
    public Drawable mCurrentDrawable;
    public int mCurrentMode;
    public float mDensity;
    public int mDuration;
    public boolean mHasAnimation;
    public boolean mInDrawing;
    public boolean mIndeterminate;
    public Drawable mIndeterminateDrawable;
    public Interpolator mInterpolator;
    public int mMax;
    public int mMaxHeight;
    public boolean mMaxInitialized;
    public int mMaxWidth;
    public int mMin;
    public int mMinHeight;
    public boolean mMinInitialized;
    public int mMinWidth;
    public boolean mMirrorForRtl;
    public boolean mNoInvalidate;
    public boolean mOnlyIndeterminate;
    public int mProgress;
    public Drawable mProgressDrawable;
    public SeslProgressBar.ProgressTintInfo mProgressTintInfo;
    public final ArrayList<SeslProgressBar.RefreshData> mRefreshData;
    public boolean mRefreshIsPosted;
    public SeslProgressBar.RefreshProgressRunnable mRefreshProgressRunnable;
    public int mSampleWidth;
    public int mSecondaryProgress;
    public boolean mShouldStartAnimationDrawable;
    public Transformation mTransformation;
    public long mUiThreadId;
    public float mVisualProgress;

    public SeslProgressBar(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public SeslProgressBar(Context var1, AttributeSet var2) {
        this(var1, var2, 16842871);
    }

    public SeslProgressBar(Context var1, AttributeSet var2, int var3) {
        this(var1, var2, var3, 0);
    }

    @SuppressLint("RestrictedApi")
    public SeslProgressBar(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
        boolean var5 = false;
        this.mCurrentMode = 0;
        this.mSampleWidth = 0;
        this.mMirrorForRtl = false;
        this.mRefreshData = new ArrayList();
        this.VISUAL_PROGRESS = new FloatProperty<SeslProgressBar>("visual_progress") {
            public Float get(SeslProgressBar var1) {
                return var1.mVisualProgress;
            }

            public void setValue(SeslProgressBar var1, float var2) {
                var1.setVisualProgress(R.id.progress, var2);
                var1.mVisualProgress = var2;
            }
        };
        this.mUiThreadId = Thread.currentThread().getId();
        this.initProgressBar();
        TypedArray var6 = var1.obtainStyledAttributes(var2, R.styleable.SeslProgressBar, var3, var4);
        if (VERSION.SDK_INT >= 29) {
            this.saveAttributeDataForStyleable(var1, R.styleable.SeslProgressBar, var2, var6, var3, var4);
        }

        this.mNoInvalidate = true;
        Drawable var7 = var6.getDrawable(R.styleable.SeslProgressBar_android_progressDrawable);
        if (var7 != null) {
            if (needsTileify(var7)) {
                this.setProgressDrawableTiled(var7);
            } else {
                this.setProgressDrawable(var7);
            }
        }

        this.mDuration = var6.getInt(R.styleable.SeslProgressBar_android_indeterminateDuration, this.mDuration);
        this.mMinWidth = var6.getDimensionPixelSize(R.styleable.SeslProgressBar_android_minWidth, this.mMinWidth);
        this.mMaxWidth = var6.getDimensionPixelSize(R.styleable.SeslProgressBar_android_maxWidth, this.mMaxWidth);
        this.mMinHeight = var6.getDimensionPixelSize(R.styleable.SeslProgressBar_android_minHeight, this.mMinHeight);
        this.mMaxHeight = var6.getDimensionPixelSize(R.styleable.SeslProgressBar_android_maxHeight, this.mMaxHeight);
        this.mBehavior = var6.getInt(R.styleable.SeslProgressBar_android_indeterminateBehavior, this.mBehavior);
        var3 = var6.getResourceId(R.styleable.SeslProgressBar_android_interpolator, 17432587);
        if (var3 > 0) {
            this.setInterpolator(var1, var3);
        }

        this.setMin(var6.getInt(R.styleable.SeslProgressBar_android_min, this.mMin));
        this.setMax(var6.getInt(R.styleable.SeslProgressBar_android_max, this.mMax));
        this.setProgress(var6.getInt(R.styleable.SeslProgressBar_android_progress, this.mProgress));
        this.setSecondaryProgress(var6.getInt(R.styleable.SeslProgressBar_android_secondaryProgress, this.mSecondaryProgress));
        var7 = var6.getDrawable(R.styleable.SeslProgressBar_android_indeterminateDrawable);
        if (var7 != null) {
            if (needsTileify(var7)) {
                this.setIndeterminateDrawableTiled(var7);
            } else {
                this.setIndeterminateDrawable(var7);
            }
        }

        this.mOnlyIndeterminate = var6.getBoolean(R.styleable.SeslProgressBar_android_indeterminateOnly, this.mOnlyIndeterminate);
        this.mNoInvalidate = false;
        if (this.mOnlyIndeterminate || var6.getBoolean(R.styleable.SeslProgressBar_android_indeterminate, this.mIndeterminate)) {
            var5 = true;
        }

        this.setIndeterminate(var5);
        this.mMirrorForRtl = var6.getBoolean(R.styleable.SeslProgressBar_android_mirrorForRtl, this.mMirrorForRtl);
        if (var6.hasValue(R.styleable.SeslProgressBar_android_progressTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mProgressTintMode = DrawableUtils.parseTintMode(var6.getInt(R.styleable.SeslProgressBar_android_progressTintMode, -1), (Mode)null);
            this.mProgressTintInfo.mHasProgressTintMode = true;
        }

        if (var6.hasValue(R.styleable.SeslProgressBar_android_progressTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mProgressTintList = var6.getColorStateList(R.styleable.SeslProgressBar_android_progressTint);
            this.mProgressTintInfo.mHasProgressTint = true;
        }

        if (var6.hasValue(R.styleable.SeslProgressBar_android_progressBackgroundTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mProgressBackgroundTintMode = DrawableUtils.parseTintMode(var6.getInt(R.styleable.SeslProgressBar_android_progressBackgroundTintMode, -1), (Mode)null);
            this.mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        }

        if (var6.hasValue(R.styleable.SeslProgressBar_android_progressBackgroundTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mProgressBackgroundTintList = var6.getColorStateList(R.styleable.SeslProgressBar_android_progressBackgroundTint);
            this.mProgressTintInfo.mHasProgressBackgroundTint = true;
        }

        if (var6.hasValue(R.styleable.SeslProgressBar_android_secondaryProgressTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mSecondaryProgressTintMode = DrawableUtils.parseTintMode(var6.getInt(R.styleable.SeslProgressBar_android_secondaryProgressTintMode, -1), (Mode)null);
            this.mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        }

        if (var6.hasValue(R.styleable.SeslProgressBar_android_secondaryProgressTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mSecondaryProgressTintList = var6.getColorStateList(R.styleable.SeslProgressBar_android_secondaryProgressTint);
            this.mProgressTintInfo.mHasSecondaryProgressTint = true;
        }

        if (var6.hasValue(R.styleable.SeslProgressBar_android_indeterminateTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mIndeterminateTintMode = DrawableUtils.parseTintMode(var6.getInt(R.styleable.SeslProgressBar_android_indeterminateTintMode, -1), (Mode)null);
            this.mProgressTintInfo.mHasIndeterminateTintMode = true;
        }

        if (var6.hasValue(R.styleable.SeslProgressBar_android_indeterminateTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mIndeterminateTintList = var6.getColorStateList(R.styleable.SeslProgressBar_android_indeterminateTint);
            this.mProgressTintInfo.mHasIndeterminateTint = true;
        }

        var6.recycle();
        this.applyProgressTints();
        this.applyIndeterminateTint();
        if (ViewCompat.getImportantForAccessibility(this) == ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            ViewCompat.setImportantForAccessibility(this, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }

        this.mDensity = var1.getResources().getDisplayMetrics().density;
    }

    public static boolean needsTileify(Drawable var0) {
        int var1;
        int var2;
        if (var0 instanceof LayerDrawable) {
            LayerDrawable var4 = (LayerDrawable)var0;
            var1 = var4.getNumberOfLayers();

            for(var2 = 0; var2 < var1; ++var2) {
                if (needsTileify(var4.getDrawable(var2))) {
                    return true;
                }
            }

            return false;
        } else if (var0 instanceof StateListDrawable) {
            StateListDrawable var3 = (StateListDrawable)var0;
            var1 = SeslProgressBar.StateListDrawableCompat.getStateCount(var3);

            for(var2 = 0; var2 < var1; ++var2) {
                var0 = SeslProgressBar.StateListDrawableCompat.getStateDrawable(var3, var2);
                if (var0 != null && needsTileify(var0)) {
                    return true;
                }
            }

            return false;
        } else {
            return var0 instanceof BitmapDrawable;
        }
    }

    public final void applyIndeterminateTint() {
        if (this.mIndeterminateDrawable != null) {
            SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
            if (var1 != null && (var1.mHasIndeterminateTint || var1.mHasIndeterminateTintMode)) {
                this.mIndeterminateDrawable = this.mIndeterminateDrawable.mutate();
                if (var1.mHasIndeterminateTint) {
                    DrawableCompat.setTintList(this.mIndeterminateDrawable, var1.mIndeterminateTintList);
                }

                if (var1.mHasIndeterminateTintMode) {
                    DrawableCompat.setTintMode(this.mIndeterminateDrawable, var1.mIndeterminateTintMode);
                }

                if (this.mIndeterminateDrawable.isStateful()) {
                    this.mIndeterminateDrawable.setState(this.getDrawableState());
                }
            }
        }

    }

    public final void applyPrimaryProgressTint() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        if (var1.mHasProgressTint || var1.mHasProgressTintMode) {
            Drawable var3 = this.getTintTarget(R.id.progress, true);
            if (var3 != null) {
                SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
                if (var2.mHasProgressTint) {
                    DrawableCompat.setTintList(var3, var2.mProgressTintList);
                }

                var2 = this.mProgressTintInfo;
                if (var2.mHasProgressTintMode) {
                    DrawableCompat.setTintMode(var3, var2.mProgressTintMode);
                }

                if (var3.isStateful()) {
                    var3.setState(this.getDrawableState());
                }
            }
        }

    }

    public final void applyProgressBackgroundTint() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        if (var1.mHasProgressBackgroundTint || var1.mHasProgressBackgroundTintMode) {
            Drawable var3 = this.getTintTarget(R.id.background, false);
            if (var3 != null) {
                SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
                if (var2.mHasProgressBackgroundTint) {
                    DrawableCompat.setTintList(var3, var2.mProgressBackgroundTintList);
                }

                var2 = this.mProgressTintInfo;
                if (var2.mHasProgressBackgroundTintMode) {
                    DrawableCompat.setTintMode(var3, var2.mProgressBackgroundTintMode);
                }

                if (var3.isStateful()) {
                    var3.setState(this.getDrawableState());
                }
            }
        }

    }

    public final void applyProgressTints() {
        if (this.mProgressDrawable != null && this.mProgressTintInfo != null) {
            this.applyPrimaryProgressTint();
            this.applyProgressBackgroundTint();
            this.applySecondaryProgressTint();
        }

    }

    public final void applySecondaryProgressTint() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        if (var1.mHasSecondaryProgressTint || var1.mHasSecondaryProgressTintMode) {
            Drawable var3 = this.getTintTarget(R.id.secondaryProgress, false);
            if (var3 != null) {
                SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
                if (var2.mHasSecondaryProgressTint) {
                    DrawableCompat.setTintList(var3, var2.mSecondaryProgressTintList);
                }

                var2 = this.mProgressTintInfo;
                if (var2.mHasSecondaryProgressTintMode) {
                    DrawableCompat.setTintMode(var3, var2.mSecondaryProgressTintMode);
                }

                if (var3.isStateful()) {
                    var3.setState(this.getDrawableState());
                }
            }
        }

    }

    public final void doRefreshProgress(int var1, int var2, boolean var3, boolean var4, boolean var5) {
        synchronized(this){}

        Throwable var10000;
        label3794: {
            int var6;
            boolean var10001;
            try {
                var6 = this.mMax - this.mMin;
            } catch (Throwable var393) {
                var10000 = var393;
                var10001 = false;
                break label3794;
            }

            float var7;
            if (var6 > 0) {
                try {
                    var7 = (float)(var2 - this.mMin) / (float)var6;
                } catch (Throwable var392) {
                    var10000 = var392;
                    var10001 = false;
                    break label3794;
                }
            } else {
                var7 = 0.0F;
            }

            boolean var394;
            label3775: {
                label3774: {
                    try {
                        if (var1 == R.id.progress) {
                            break label3774;
                        }
                    } catch (Throwable var391) {
                        var10000 = var391;
                        var10001 = false;
                        break label3794;
                    }

                    var394 = false;
                    break label3775;
                }

                var394 = true;
            }

            Drawable var8;
            try {
                var8 = this.mCurrentDrawable;
            } catch (Throwable var390) {
                var10000 = var390;
                var10001 = false;
                break label3794;
            }

            if (var8 != null) {
                label3790: {
                    int var9 = (int)(10000.0F * var7);

                    Drawable var10;
                    label3791: {
                        try {
                            if (var8 instanceof LayerDrawable) {
                                var10 = ((LayerDrawable)var8).findDrawableByLayerId(var1);
                                break label3791;
                            }
                        } catch (Throwable var389) {
                            var10000 = var389;
                            var10001 = false;
                            break label3794;
                        }

                        int var11;
                        label3753: {
                            try {
                                if (var8 instanceof StateListDrawable) {
                                    var11 = SeslProgressBar.StateListDrawableCompat.getStateCount((StateListDrawable)var8);
                                    break label3753;
                                }
                            } catch (Throwable var388) {
                                var10000 = var388;
                                var10001 = false;
                                break label3794;
                            }

                            try {
                                var8.setLevel(var9);
                                break label3790;
                            } catch (Throwable var380) {
                                var10000 = var380;
                                var10001 = false;
                                break label3794;
                            }
                        }

                        int var12 = 0;

                        while(true) {
                            if (var12 >= var11) {
                                break label3790;
                            }

                            try {
                                var10 = SeslProgressBar.StateListDrawableCompat.getStateDrawable((StateListDrawable)var8, var12);
                            } catch (Throwable var382) {
                                var10000 = var382;
                                var10001 = false;
                                break label3794;
                            }

                            if (var10 == null) {
                                return;
                            }

                            label3742: {
                                label3793: {
                                    Drawable var13;
                                    try {
                                        if (!(var10 instanceof LayerDrawable)) {
                                            break label3793;
                                        }

                                        var13 = ((LayerDrawable)var10).findDrawableByLayerId(var1);
                                    } catch (Throwable var387) {
                                        var10000 = var387;
                                        var10001 = false;
                                        break label3794;
                                    }

                                    var10 = var13;
                                    if (var13 == null) {
                                        break label3742;
                                    }

                                    var10 = var13;

                                    try {
                                        if (VERSION.SDK_INT <= 19) {
                                            break label3742;
                                        }
                                    } catch (Throwable var386) {
                                        var10000 = var386;
                                        var10001 = false;
                                        break label3794;
                                    }

                                    var10 = var13;

                                    try {
                                        if (!this.canResolveLayoutDirection()) {
                                            break label3742;
                                        }

                                        DrawableCompat.setLayoutDirection(var13, ViewCompat.getLayoutDirection(this));
                                    } catch (Throwable var385) {
                                        var10000 = var385;
                                        var10001 = false;
                                        break label3794;
                                    }

                                    var10 = var13;
                                    break label3742;
                                }

                                var10 = null;
                            }

                            if (var10 == null) {
                                var10 = var8;
                            }

                            try {
                                var10.setLevel(var9);
                            } catch (Throwable var381) {
                                var10000 = var381;
                                var10001 = false;
                                break label3794;
                            }

                            ++var12;
                        }
                    }

                    if (var10 != null) {
                        try {
                            if (VERSION.SDK_INT > 19 && this.canResolveLayoutDirection()) {
                                DrawableCompat.setLayoutDirection(var10, ViewCompat.getLayoutDirection(this));
                            }
                        } catch (Throwable var384) {
                            var10000 = var384;
                            var10001 = false;
                            break label3794;
                        }
                    }

                    if (var10 != null) {
                        var8 = var10;
                    }

                    try {
                        var8.setLevel(var9);
                    } catch (Throwable var383) {
                        var10000 = var383;
                        var10001 = false;
                        break label3794;
                    }
                }
            } else {
                try {
                    this.invalidate();
                } catch (Throwable var379) {
                    var10000 = var379;
                    var10001 = false;
                    break label3794;
                }
            }

            if (var394 && var5) {
                ObjectAnimator var395;
                try {
                    var395 = ObjectAnimator.ofFloat(this, this.VISUAL_PROGRESS, new float[]{var7});
                    if (VERSION.SDK_INT > 18) {
                        var395.setAutoCancel(true);
                    }
                } catch (Throwable var378) {
                    var10000 = var378;
                    var10001 = false;
                    break label3794;
                }

                try {
                    var395.setDuration(80L);
                    var395.setInterpolator(PROGRESS_ANIM_INTERPOLATOR);
                    var395.start();
                } catch (Throwable var377) {
                    var10000 = var377;
                    var10001 = false;
                    break label3794;
                }
            } else {
                try {
                    this.setVisualProgress(var1, var7);
                } catch (Throwable var376) {
                    var10000 = var376;
                    var10001 = false;
                    break label3794;
                }
            }

            if (!var394 || !var4) {
                return;
            }

            label3691:
            try {
                this.onProgressRefresh(var7, var3, var2);
                return;
            } catch (Throwable var375) {
                var10000 = var375;
                var10001 = false;
                break label3691;
            }
        }

        Throwable var396 = var10000;
        try {
            throw var396;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    public void drawTrack(Canvas var1) {
        Drawable var2 = this.mCurrentDrawable;
        if (var2 != null) {
            int var3 = var1.save();
            if (this.mCurrentMode != 3 && this.mMirrorForRtl && ViewUtils.isLayoutRtl(this)) {
                var1.translate((float)(this.getWidth() - this.getPaddingRight()), (float)this.getPaddingTop());
                var1.scale(-1.0F, 1.0F);
            } else {
                var1.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
            }

            long var4 = this.getDrawingTime();
            if (this.mHasAnimation) {
                this.mAnimation.getTransformation(var4, this.mTransformation);
                float var6 = this.mTransformation.getAlpha();

                try {
                    this.mInDrawing = true;
                    var2.setLevel((int)(var6 * 10000.0F));
                } finally {
                    this.mInDrawing = false;
                }

                ViewCompat.postInvalidateOnAnimation(this);
            }

            var2.draw(var1);
            var1.restoreToCount(var3);
            if (this.mShouldStartAnimationDrawable && var2 instanceof Animatable) {
                ((Animatable)var2).start();
                this.mShouldStartAnimationDrawable = false;
            }
        }

    }

    public void drawableHotspotChanged(float var1, float var2) {
        super.drawableHotspotChanged(var1, var2);
        Drawable var3 = this.mProgressDrawable;
        if (var3 != null) {
            DrawableCompat.setHotspot(var3, var1, var2);
        }

        var3 = this.mIndeterminateDrawable;
        if (var3 != null) {
            DrawableCompat.setHotspot(var3, var1, var2);
        }

    }

    public void drawableStateChanged() {
        super.drawableStateChanged();
        this.updateDrawableState();
    }

    public CharSequence getAccessibilityClassName() {
        return ProgressBar.class.getName();
    }

    public Drawable getCurrentDrawable() {
        return this.mCurrentDrawable;
    }

    public Drawable getIndeterminateDrawable() {
        return this.mIndeterminateDrawable;
    }

    public ColorStateList getIndeterminateTintList() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        ColorStateList var2;
        if (var1 != null) {
            var2 = var1.mIndeterminateTintList;
        } else {
            var2 = null;
        }

        return var2;
    }

    public Mode getIndeterminateTintMode() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        Mode var2;
        if (var1 != null) {
            var2 = var1.mIndeterminateTintMode;
        } else {
            var2 = null;
        }

        return var2;
    }

    public Interpolator getInterpolator() {
        return this.mInterpolator;
    }

    @ExportedProperty(
            category = "progress"
    )
    public int getMax() {
        synchronized(this){}

        int var1;
        try {
            var1 = this.mMax;
        } finally {
            ;
        }

        return var1;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    @ExportedProperty(
            category = "progress"
    )
    public int getMin() {
        synchronized(this){}

        int var1;
        try {
            var1 = this.mMin;
        } finally {
            ;
        }

        return var1;
    }

    public int getMinHeight() {
        return this.mMinHeight;
    }

    public int getMinWidth() {
        return this.mMinWidth;
    }

    public boolean getMirrorForRtl() {
        return this.mMirrorForRtl;
    }

    public int getPaddingLeft() {
        return (int) Utils.genericGetField(View.class, this, "mPaddingLeft");
    }

    public int getPaddingRight() {
        return (int) Utils.genericGetField(View.class, this, "mPaddingRight");
    }

    @ExportedProperty(
            category = "progress"
    )
    public int getProgress() {
        synchronized(this){}
        boolean var4 = false;

        int var1;
        try {
            var4 = true;
            if (!this.mIndeterminate) {
                var1 = this.mProgress;
                var4 = false;
                return var1;
            }

            var4 = false;
        } finally {
            if (var4) {
                ;
            }
        }

        var1 = 0;
        return var1;
    }

    public ColorStateList getProgressBackgroundTintList() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        ColorStateList var2;
        if (var1 != null) {
            var2 = var1.mProgressBackgroundTintList;
        } else {
            var2 = null;
        }

        return var2;
    }

    public Mode getProgressBackgroundTintMode() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        Mode var2;
        if (var1 != null) {
            var2 = var1.mProgressBackgroundTintMode;
        } else {
            var2 = null;
        }

        return var2;
    }

    public Drawable getProgressDrawable() {
        return this.mProgressDrawable;
    }

    public ColorStateList getProgressTintList() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        ColorStateList var2;
        if (var1 != null) {
            var2 = var1.mProgressTintList;
        } else {
            var2 = null;
        }

        return var2;
    }

    public Mode getProgressTintMode() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        Mode var2;
        if (var1 != null) {
            var2 = var1.mProgressTintMode;
        } else {
            var2 = null;
        }

        return var2;
    }

    @ExportedProperty(
            category = "progress"
    )
    public int getSecondaryProgress() {
        synchronized(this){}
        boolean var4 = false;

        int var1;
        try {
            var4 = true;
            if (!this.mIndeterminate) {
                var1 = this.mSecondaryProgress;
                var4 = false;
                return var1;
            }

            var4 = false;
        } finally {
            if (var4) {
                ;
            }
        }

        var1 = 0;
        return var1;
    }

    public ColorStateList getSecondaryProgressTintList() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        ColorStateList var2;
        if (var1 != null) {
            var2 = var1.mSecondaryProgressTintList;
        } else {
            var2 = null;
        }

        return var2;
    }

    public Mode getSecondaryProgressTintMode() {
        SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
        Mode var2;
        if (var1 != null) {
            var2 = var1.mSecondaryProgressTintMode;
        } else {
            var2 = null;
        }

        return var2;
    }

    public final Drawable getTintTarget(int var1, boolean var2) {
        Drawable var3 = this.mProgressDrawable;
        Drawable var5;
        if (var3 != null) {
            this.mProgressDrawable = var3.mutate();
            Drawable var4;
            if (var3 instanceof LayerDrawable) {
                var4 = ((LayerDrawable)var3).findDrawableByLayerId(var1);
            } else {
                var4 = null;
            }

            var5 = var4;
            if (var2) {
                var5 = var4;
                if (var4 == null) {
                    var5 = var3;
                }
            }
        } else {
            var5 = null;
        }

        return var5;
    }

    public final void initProgressBar() {
        this.mMin = 0;
        this.mMax = 100;
        this.mProgress = 0;
        this.mSecondaryProgress = 0;
        this.mIndeterminate = false;
        this.mOnlyIndeterminate = false;
        this.mDuration = 4000;
        this.mBehavior = 1;
        this.mMinWidth = 24;
        this.mMaxWidth = 48;
        this.mMinHeight = 24;
        this.mMaxHeight = 48;
    }

    public final void incrementProgressBy(int var1) throws Throwable {
        synchronized(this){}

        try {
            this.setProgress(this.mProgress + var1);
        } finally {
            ;
        }

    }

    public final void incrementSecondaryProgressBy(int var1) throws Throwable {
        synchronized(this){}

        try {
            this.setSecondaryProgress(this.mSecondaryProgress + var1);
        } finally {
            ;
        }

    }

    public void invalidateDrawable(Drawable var1) {
        if (!this.mInDrawing) {
            if (this.verifyDrawable(var1)) {
                Rect var4 = var1.getBounds();
                int var2 = this.getScrollX() + this.getPaddingLeft();
                int var3 = this.getScrollY() + this.getPaddingTop();
                this.invalidate(var4.left + var2, var4.top + var3, var4.right + var2, var4.bottom + var3);
            } else {
                super.invalidateDrawable(var1);
            }
        }

    }

    @ExportedProperty(
            category = "progress"
    )
    public boolean isIndeterminate() {
        synchronized(this){}

        boolean var1;
        try {
            var1 = this.mIndeterminate;
        } finally {
            ;
        }

        return var1;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable var1 = this.mProgressDrawable;
        if (var1 != null) {
            var1.jumpToCurrentState();
        }

        var1 = this.mIndeterminateDrawable;
        if (var1 != null) {
            var1.jumpToCurrentState();
        }

    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mIndeterminate) {
            this.startAnimation();
        }

        if (this.mRefreshData != null) {
            label272: {
                synchronized(this){}

                Throwable var10000;
                boolean var10001;
                label270: {
                    int var1;
                    try {
                        var1 = this.mRefreshData.size();
                    } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label270;
                    }

                    for(int var2 = 0; var2 < var1; ++var2) {
                        try {
                            SeslProgressBar.RefreshData var3 = (SeslProgressBar.RefreshData)this.mRefreshData.get(var2);
                            this.doRefreshProgress(var3.id, var3.progress, var3.fromUser, true, var3.animate);
                            var3.recycle();
                        } catch (Throwable var22) {
                            var10000 = var22;
                            var10001 = false;
                            break label270;
                        }
                    }

                    label250:
                    try {
                        this.mRefreshData.clear();
                        break label272;
                    } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label250;
                    }
                }

                while(true) {
                    Throwable var24 = var10000;

                    try {
                        throw var24;
                    } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        continue;
                    }
                }
            }
        }

        this.mAttached = true;
    }

    public void onDetachedFromWindow() {
        if (this.mIndeterminate) {
            this.stopAnimation();
        }

        SeslProgressBar.RefreshProgressRunnable var1 = this.mRefreshProgressRunnable;
        if (var1 != null) {
            this.removeCallbacks(var1);
            this.mRefreshIsPosted = false;
        }

        SeslProgressBar.AccessibilityEventSender var2 = this.mAccessibilityEventSender;
        if (var2 != null) {
            this.removeCallbacks(var2);
        }

        super.onDetachedFromWindow();
        this.mAttached = false;
    }

    public void onDraw(Canvas var1) {
        synchronized(this){}

        try {
            super.onDraw(var1);
            this.drawTrack(var1);
        } finally {
            ;
        }

    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
        super.onInitializeAccessibilityEvent(var1);
        var1.setItemCount(this.mMax - this.mMin);
        var1.setCurrentItemIndex(this.mProgress);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
        super.onInitializeAccessibilityNodeInfo(var1);
        if (VERSION.SDK_INT >= 19 && !this.isIndeterminate()) {
            var1.setRangeInfo(RangeInfo.obtain(0, (float)this.getMin(), (float)this.getMax(), (float)this.getProgress()));
        }

    }

    public void onMeasure(int var1, int var2) {
        synchronized(this){}

        Throwable var10000;
        label128: {
            Drawable var3;
            boolean var10001;
            try {
                var3 = this.mCurrentDrawable;
            } catch (Throwable var21) {
                var10000 = var21;
                var10001 = false;
                break label128;
            }

            int var4;
            int var5;
            if (var3 != null) {
                try {
                    var4 = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, var3.getIntrinsicWidth()));
                    var5 = Math.max(this.mMinHeight, Math.min(this.mMaxHeight, var3.getIntrinsicHeight()));
                } catch (Throwable var20) {
                    var10000 = var20;
                    var10001 = false;
                    break label128;
                }
            } else {
                var5 = 0;
                var4 = var5;
            }

            label115:
            try {
                this.updateDrawableState();
                int var6 = this.getPaddingLeft();
                int var7 = this.getPaddingRight();
                int var8 = this.getPaddingTop();
                int var9 = this.getPaddingBottom();
                this.setMeasuredDimension(View.resolveSizeAndState(var4 + var6 + var7, var1, 0), View.resolveSizeAndState(var5 + var8 + var9, var2, 0));
                return;
            } catch (Throwable var19) {
                var10000 = var19;
                var10001 = false;
                break label115;
            }
        }

        Throwable var22 = var10000;
        try {
            throw var22;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void onProgressRefresh(float var1, boolean var2, int var3) {
        if (((AccessibilityManager)this.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE)).isEnabled()) {
            this.scheduleAccessibilityEventSender();
        }

        var3 = this.mSecondaryProgress;
        if (var3 > this.mProgress && !var2) {
            this.refreshProgress(R.id.secondaryProgress, var3, false, false);
        }

    }

    public void onRestoreInstanceState(Parcelable var1) {
        SeslProgressBar.SavedState var2 = (SeslProgressBar.SavedState)var1;
        super.onRestoreInstanceState(var2.getSuperState());
        this.setProgress(var2.progress);
        this.setSecondaryProgress(var2.secondaryProgress);
    }

    public Parcelable onSaveInstanceState() {
        SeslProgressBar.SavedState var1 = new SeslProgressBar.SavedState(super.onSaveInstanceState());
        var1.progress = this.mProgress;
        var1.secondaryProgress = this.mSecondaryProgress;
        return var1;
    }

    public void onSizeChanged(int var1, int var2, int var3, int var4) {
        this.updateDrawableBounds(var1, var2);
    }

    public void onSlidingRefresh(int var1) {
        Drawable var2 = this.mCurrentDrawable;
        if (var2 != null) {
            Drawable var3 = null;
            if (var2 instanceof LayerDrawable) {
                var3 = ((LayerDrawable)var2).findDrawableByLayerId(R.id.progress);
            }

            if (var3 != null) {
                var3.setLevel(var1);
            }
        }

    }

    public void onVisibilityAggregated(boolean var1) {
        super.onVisibilityAggregated(var1);
        if (var1 != this.mAggregatedIsVisible) {
            this.mAggregatedIsVisible = var1;
            if (this.mIndeterminate) {
                if (var1) {
                    this.startAnimation();
                } else {
                    this.stopAnimation();
                }
            }

            Drawable var2 = this.mCurrentDrawable;
            if (var2 != null) {
                var2.setVisible(var1, false);
            }
        }

    }

    public void onVisualProgressChanged(int var1, float var2) {
    }

    public void postInvalidate() {
        if (!this.mNoInvalidate) {
            super.postInvalidate();
        }

    }

    public final void refreshProgress(int var1, int var2, boolean var3, boolean var4) {
        synchronized(this){}

        try {
            if (this.mUiThreadId == Thread.currentThread().getId()) {
                this.doRefreshProgress(var1, var2, var3, true, var4);
            } else {
                if (this.mRefreshProgressRunnable == null) {
                    SeslProgressBar.RefreshProgressRunnable var5 = new SeslProgressBar.RefreshProgressRunnable();
                    this.mRefreshProgressRunnable = var5;
                }

                SeslProgressBar.RefreshData var8 = SeslProgressBar.RefreshData.obtain(var1, var2, var3, var4);
                this.mRefreshData.add(var8);
                if (this.mAttached && !this.mRefreshIsPosted) {
                    this.post(this.mRefreshProgressRunnable);
                    this.mRefreshIsPosted = true;
                }
            }
        } finally {
            ;
        }

    }

    public final void scheduleAccessibilityEventSender() {
        SeslProgressBar.AccessibilityEventSender var1 = this.mAccessibilityEventSender;
        if (var1 == null) {
            this.mAccessibilityEventSender = new SeslProgressBar.AccessibilityEventSender();
        } else {
            this.removeCallbacks(var1);
        }

        this.postDelayed(this.mAccessibilityEventSender, 200L);
    }

    public void setIndeterminate(boolean var1) {
        synchronized(this){}

        Throwable var10000;
        label214: {
            boolean var10001;
            try {
                if (this.mOnlyIndeterminate && this.mIndeterminate) {
                    return;
                }
            } catch (Throwable var22) {
                var10000 = var22;
                var10001 = false;
                break label214;
            }

            try {
                if (var1 == this.mIndeterminate) {
                    return;
                }

                this.mIndeterminate = var1;
            } catch (Throwable var21) {
                var10000 = var21;
                var10001 = false;
                break label214;
            }

            if (var1) {
                try {
                    this.swapCurrentDrawable(this.mIndeterminateDrawable);
                    this.startAnimation();
                } catch (Throwable var19) {
                    var10000 = var19;
                    var10001 = false;
                    break label214;
                }
            } else {
                try {
                    this.swapCurrentDrawable(this.mProgressDrawable);
                    this.stopAnimation();
                } catch (Throwable var20) {
                    var10000 = var20;
                    var10001 = false;
                    break label214;
                }
            }

            return;
        }

        Throwable var2 = var10000;
        try {
            throw var2;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setIndeterminateDrawable(Drawable var1) {
        Drawable var2 = this.mIndeterminateDrawable;
        if (var2 != var1) {
            if (var2 != null) {
                var2.setCallback((Callback)null);
                this.unscheduleDrawable(this.mIndeterminateDrawable);
            }

            this.mIndeterminateDrawable = var1;
            if (var1 != null) {
                var1.setCallback(this);
                DrawableCompat.setLayoutDirection(var1, ViewCompat.getLayoutDirection(this));
                if (var1.isStateful()) {
                    var1.setState(this.getDrawableState());
                }

                this.applyIndeterminateTint();
            }

            if (this.mIndeterminate) {
                this.swapCurrentDrawable(var1);
                this.postInvalidate();
            }
        }

    }

    public void setIndeterminateDrawableTiled(Drawable var1) {
        Drawable var2 = var1;
        if (var1 != null) {
            var2 = this.tileifyIndeterminate(var1);
        }

        this.setIndeterminateDrawable(var2);
    }

    public void setIndeterminateTintList(ColorStateList var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
        var2.mIndeterminateTintList = var1;
        var2.mHasIndeterminateTint = true;
        this.applyIndeterminateTint();
    }

    public void setIndeterminateTintMode(Mode var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
        var2.mIndeterminateTintMode = var1;
        var2.mHasIndeterminateTintMode = true;
        this.applyIndeterminateTint();
    }

    public void setInterpolator(Context var1, int var2) {
        this.setInterpolator(AnimationUtils.loadInterpolator(var1, var2));
    }

    public void setInterpolator(Interpolator var1) {
        this.mInterpolator = var1;
    }

    public void setMax(int var1) {
        synchronized(this){}
        int var2 = var1;

        Throwable var10000;
        label324: {
            boolean var10001;
            label325: {
                try {
                    if (!this.mMinInitialized) {
                        break label325;
                    }
                } catch (Throwable var33) {
                    var10000 = var33;
                    var10001 = false;
                    break label324;
                }

                var2 = var1;

                try {
                    if (var1 < this.mMin) {
                        var2 = this.mMin;
                    }
                } catch (Throwable var32) {
                    var10000 = var32;
                    var10001 = false;
                    break label324;
                }
            }

            label310: {
                try {
                    this.mMaxInitialized = true;
                    if (this.mMinInitialized && var2 != this.mMax) {
                        this.mMax = var2;
                        this.postInvalidate();
                        if (this.mProgress > var2) {
                            this.mProgress = var2;
                        }
                        break label310;
                    }
                } catch (Throwable var31) {
                    var10000 = var31;
                    var10001 = false;
                    break label324;
                }

                try {
                    this.mMax = var2;
                    return;
                } catch (Throwable var30) {
                    var10000 = var30;
                    var10001 = false;
                    break label324;
                }
            }

            label300:
            try {
                this.refreshProgress(R.id.progress, this.mProgress, false, false);
                return;
            } catch (Throwable var29) {
                var10000 = var29;
                var10001 = false;
                break label300;
            }
        }

        Throwable var3 = var10000;
        try {
            throw var3;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setMaxHeight(int var1) {
        this.mMaxHeight = var1;
        this.requestLayout();
    }

    public void setMaxWidth(int var1) {
        this.mMaxWidth = var1;
        this.requestLayout();
    }

    public void setMin(int var1) {
        synchronized(this){}
        int var2 = var1;

        Throwable var10000;
        label324: {
            boolean var10001;
            label325: {
                try {
                    if (!this.mMaxInitialized) {
                        break label325;
                    }
                } catch (Throwable var33) {
                    var10000 = var33;
                    var10001 = false;
                    break label324;
                }

                var2 = var1;

                try {
                    if (var1 > this.mMax) {
                        var2 = this.mMax;
                    }
                } catch (Throwable var32) {
                    var10000 = var32;
                    var10001 = false;
                    break label324;
                }
            }

            label310: {
                try {
                    this.mMinInitialized = true;
                    if (this.mMaxInitialized && var2 != this.mMin) {
                        this.mMin = var2;
                        this.postInvalidate();
                        if (this.mProgress < var2) {
                            this.mProgress = var2;
                        }
                        break label310;
                    }
                } catch (Throwable var31) {
                    var10000 = var31;
                    var10001 = false;
                    break label324;
                }

                try {
                    this.mMin = var2;
                    return;
                } catch (Throwable var30) {
                    var10000 = var30;
                    var10001 = false;
                    break label324;
                }
            }

            label300:
            try {
                this.refreshProgress(R.id.progress, this.mProgress, false, false);
                return;
            } catch (Throwable var29) {
                var10000 = var29;
                var10001 = false;
                break label300;
            }
        }

        Throwable var3 = var10000;
        try {
            throw var3;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setMinHeight(int var1) {
        this.mMinHeight = var1;
        this.requestLayout();
    }

    public void setMinWidth(int var1) {
        this.mMinWidth = var1;
        this.requestLayout();
    }

    public void setMode(int var1) {
        this.mCurrentMode = var1;
        Drawable var2;
        if (var1 != 3) {
            if (var1 != 4) {
                var2 = null;
            } else {
                var2 = ContextCompat.getDrawable(this.getContext(), R.drawable.sesl_split_seekbar_background_progress);
            }
        } else {
            var2 = ContextCompat.getDrawable(this.getContext(), R.drawable.sesl_scrubber_progress_vertical);
        }

        if (var2 != null) {
            this.setProgressDrawableTiled(var2);
        }

    }

    public void setProgress(int var1) {
        synchronized(this){}

        try {
            this.setProgressInternal(var1, false, false);
        } finally {
            ;
        }

    }

    public void setProgressBackgroundTintList(ColorStateList var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
        var2.mProgressBackgroundTintList = var1;
        var2.mHasProgressBackgroundTint = true;
        if (this.mProgressDrawable != null) {
            this.applyProgressBackgroundTint();
        }

    }

    public void setProgressBackgroundTintMode(Mode var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
        var2.mProgressBackgroundTintMode = var1;
        var2.mHasProgressBackgroundTintMode = true;
        if (this.mProgressDrawable != null) {
            this.applyProgressBackgroundTint();
        }

    }

    public void setProgressDrawable(Drawable var1) {
        Drawable var2 = this.mProgressDrawable;
        if (var2 != var1) {
            if (var2 != null) {
                var2.setCallback((Callback)null);
                this.unscheduleDrawable(this.mProgressDrawable);
            }

            this.mProgressDrawable = var1;
            if (var1 != null) {
                var1.setCallback(this);
                DrawableCompat.setLayoutDirection(var1, ViewCompat.getLayoutDirection(this));
                if (var1.isStateful()) {
                    var1.setState(this.getDrawableState());
                }

                int var3;
                if (this.mCurrentMode == 3) {
                    var3 = var1.getMinimumWidth();
                    if (this.mMaxWidth < var3) {
                        this.mMaxWidth = var3;
                        this.requestLayout();
                    }
                } else {
                    var3 = var1.getMinimumHeight();
                    if (this.mMaxHeight < var3) {
                        this.mMaxHeight = var3;
                        this.requestLayout();
                    }
                }

                this.applyProgressTints();
            }

            if (!this.mIndeterminate) {
                this.swapCurrentDrawable(var1);
                this.postInvalidate();
            }

            this.updateDrawableBounds(this.getWidth(), this.getHeight());
            this.updateDrawableState();
            this.doRefreshProgress(R.id.progress, this.mProgress, false, false, false);
            this.doRefreshProgress(R.id.secondaryProgress, this.mSecondaryProgress, false, false, false);
        }

    }

    public void setProgressDrawableTiled(Drawable var1) {
        Drawable var2 = var1;
        if (var1 != null) {
            var2 = this.tileify(var1, false);
        }

        this.setProgressDrawable(var2);
    }

    public boolean setProgressInternal(int var1, boolean var2, boolean var3) {
        synchronized(this){}

        Throwable var10000;
        label140: {
            boolean var10001;
            boolean var4;
            try {
                var4 = this.mIndeterminate;
            } catch (Throwable var18) {
                var10000 = var18;
                var10001 = false;
                break label140;
            }

            if (var4) {
                return false;
            }

            int var5;
            try {
                var5 = constrain(var1, this.mMin, this.mMax);
                var1 = this.mProgress;
            } catch (Throwable var17) {
                var10000 = var17;
                var10001 = false;
                break label140;
            }

            if (var5 == var1) {
                return false;
            }

            try {
                this.mProgress = var5;
                this.refreshProgress(R.id.progress, this.mProgress, var2, var3);
            } catch (Throwable var16) {
                var10000 = var16;
                var10001 = false;
                break label140;
            }

            return true;
        }

        Throwable var6 = var10000;
        try {
            throw var6;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    public void setProgressTintList(ColorStateList var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
        var2.mProgressTintList = var1;
        var2.mHasProgressTint = true;
        if (this.mProgressDrawable != null) {
            this.applyPrimaryProgressTint();
        }

    }

    public void setProgressTintMode(Mode var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
        var2.mProgressTintMode = var1;
        var2.mHasProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            this.applyPrimaryProgressTint();
        }

    }

    public void setSecondaryProgress(int var1) {
        synchronized(this){}

        Throwable var10000;
        label250: {
            boolean var10001;
            boolean var2;
            try {
                var2 = this.mIndeterminate;
            } catch (Throwable var24) {
                var10000 = var24;
                var10001 = false;
                break label250;
            }

            if (var2) {
                return;
            }

            int var3 = var1;

            try {
                if (var1 < this.mMin) {
                    var3 = this.mMin;
                }
            } catch (Throwable var23) {
                var10000 = var23;
                var10001 = false;
                break label250;
            }

            var1 = var3;

            try {
                if (var3 > this.mMax) {
                    var1 = this.mMax;
                }
            } catch (Throwable var22) {
                var10000 = var22;
                var10001 = false;
                break label250;
            }

            try {
                if (var1 != this.mSecondaryProgress) {
                    this.mSecondaryProgress = var1;
                    this.refreshProgress(R.id.secondaryProgress, this.mSecondaryProgress, false, false);
                }
            } catch (Throwable var21) {
                var10000 = var21;
                var10001 = false;
                break label250;
            }

            return;
        }

        Throwable var4 = var10000;
        try {
            throw var4;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void setSecondaryProgressTintList(ColorStateList var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
        var2.mSecondaryProgressTintList = var1;
        var2.mHasSecondaryProgressTint = true;
        if (this.mProgressDrawable != null) {
            this.applySecondaryProgressTint();
        }

    }

    public void setSecondaryProgressTintMode(Mode var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        SeslProgressBar.ProgressTintInfo var2 = this.mProgressTintInfo;
        var2.mSecondaryProgressTintMode = var1;
        var2.mHasSecondaryProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            this.applySecondaryProgressTint();
        }

    }

    public final void setVisualProgress(int var1, float var2) {
        this.mVisualProgress = var2;
        Drawable var3 = this.mCurrentDrawable;
        Drawable var4 = var3;
        if (var3 instanceof LayerDrawable) {
            var3 = ((LayerDrawable)var3).findDrawableByLayerId(var1);
            var4 = var3;
            if (var3 == null) {
                var4 = this.mCurrentDrawable;
            }
        }

        if (var4 != null) {
            var4.setLevel((int)(10000.0F * var2));
        } else {
            this.invalidate();
        }

        this.onVisualProgressChanged(var1, var2);
    }

    @SuppressLint("WrongConstant")
    public final void startAnimation() {
        if (this.getVisibility() == 0 && (VERSION.SDK_INT > 23 || this.getWindowVisibility() == 0)) {
            if (this.mIndeterminateDrawable instanceof Animatable) {
                this.mShouldStartAnimationDrawable = true;
                this.mHasAnimation = false;
            } else {
                this.mHasAnimation = true;
                if (this.mInterpolator == null) {
                    this.mInterpolator = new LinearInterpolator();
                }

                Transformation var1 = this.mTransformation;
                if (var1 == null) {
                    this.mTransformation = new Transformation();
                } else {
                    var1.clear();
                }

                AlphaAnimation var2 = this.mAnimation;
                if (var2 == null) {
                    this.mAnimation = new AlphaAnimation(0.0F, 1.0F);
                } else {
                    var2.reset();
                }

                this.mAnimation.setRepeatMode(this.mBehavior);
                this.mAnimation.setRepeatCount(-1);
                this.mAnimation.setDuration((long)this.mDuration);
                this.mAnimation.setInterpolator(this.mInterpolator);
                this.mAnimation.setStartTime(-1L);
            }

            this.postInvalidate();
        }

    }

    public final void stopAnimation() {
        this.mHasAnimation = false;
        Drawable var1 = this.mIndeterminateDrawable;
        if (var1 instanceof Animatable) {
            ((Animatable)var1).stop();
            this.mShouldStartAnimationDrawable = false;
        }

        this.postInvalidate();
    }

    @SuppressLint("WrongConstant")
    public final void swapCurrentDrawable(Drawable var1) {
        Drawable var2 = this.mCurrentDrawable;
        this.mCurrentDrawable = var1;
        if (var2 != this.mCurrentDrawable) {
            if (var2 != null) {
                var2.setVisible(false, false);
            }

            var1 = this.mCurrentDrawable;
            if (var1 != null) {
                boolean var3;
                if (this.getWindowVisibility() == 0 && this.isShown()) {
                    var3 = true;
                } else {
                    var3 = false;
                }

                var1.setVisible(var3, false);
            }
        }

    }

    public final Drawable tileify(Drawable var1, boolean var2) {
        boolean var3 = var1 instanceof LayerDrawable;
        int var4 = 0;
        byte var5 = 0;
        if (!var3) {
            if (var1 instanceof StateListDrawable) {
                StateListDrawable var13 = (StateListDrawable)var1;
                StateListDrawable var18 = new StateListDrawable();

                for(int var14 = SeslProgressBar.StateListDrawableCompat.getStateCount(var13); var4 < var14; ++var4) {
                    int[] var17 = SeslProgressBar.StateListDrawableCompat.getStateSet(var13, var4);
                    Drawable var10 = SeslProgressBar.StateListDrawableCompat.getStateDrawable(var13, var4);
                    if (var10 != null) {
                        var18.addState(var17, this.tileify(var10, var2));
                    }
                }

                return var18;
            } else {
                Object var16 = var1;
                if (var1 instanceof BitmapDrawable) {
                    BitmapDrawable var12 = (BitmapDrawable)var1.getConstantState().newDrawable(this.getResources());
                    var12.setTileModeXY(TileMode.REPEAT, TileMode.CLAMP);
                    if (this.mSampleWidth <= 0) {
                        this.mSampleWidth = var12.getIntrinsicWidth();
                    }

                    var16 = var12;
                    if (var2) {
                        return new ClipDrawable(var12, 3, 1);
                    }
                }

                return (Drawable)var16;
            }
        } else {
            LayerDrawable var11 = (LayerDrawable)var1;
            int var6 = var11.getNumberOfLayers();
            Drawable[] var7 = new Drawable[var6];

            for(var4 = 0; var4 < var6; ++var4) {
                int var8 = var11.getId(var4);
                Drawable var9 = var11.getDrawable(var4);
                if (var8 != R.id.progress && var8 != R.id.secondaryProgress) {
                    var2 = false;
                } else {
                    var2 = true;
                }

                var7[var4] = this.tileify(var9, var2);
            }

            LayerDrawable var15 = new LayerDrawable(var7);
            if (VERSION.SDK_INT >= 23) {
                for(var4 = var5; var4 < var6; ++var4) {
                    var15.setId(var4, var11.getId(var4));
                    var15.setLayerGravity(var4, var11.getLayerGravity(var4));
                    var15.setLayerWidth(var4, var11.getLayerWidth(var4));
                    var15.setLayerHeight(var4, var11.getLayerHeight(var4));
                    var15.setLayerInsetLeft(var4, var11.getLayerInsetLeft(var4));
                    var15.setLayerInsetRight(var4, var11.getLayerInsetRight(var4));
                    var15.setLayerInsetTop(var4, var11.getLayerInsetTop(var4));
                    var15.setLayerInsetBottom(var4, var11.getLayerInsetBottom(var4));
                    var15.setLayerInsetStart(var4, var11.getLayerInsetStart(var4));
                    var15.setLayerInsetEnd(var4, var11.getLayerInsetEnd(var4));
                }
            }

            return var15;
        }
    }

    public final Drawable tileifyIndeterminate(Drawable var1) {
        Object var2 = var1;
        if (var1 instanceof AnimationDrawable) {
            AnimationDrawable var3 = (AnimationDrawable)var1;
            int var4 = var3.getNumberOfFrames();
            var2 = new AnimationDrawable();
            ((AnimationDrawable)var2).setOneShot(var3.isOneShot());

            for(int var5 = 0; var5 < var4; ++var5) {
                var1 = this.tileify(var3.getFrame(var5), true);
                var1.setLevel(10000);
                ((AnimationDrawable)var2).addFrame(var1, var3.getDuration(var5));
            }

            ((AnimationDrawable)var2).setLevel(10000);
        }

        return (Drawable)var2;
    }

    @SuppressLint("RestrictedApi")
    public void updateDrawableBounds(int var1, int var2) {
        int var3 = var1 - (this.getPaddingRight() + this.getPaddingLeft());
        int var4 = var2 - (this.getPaddingTop() + this.getPaddingBottom());
        Drawable var5 = this.mIndeterminateDrawable;
        var1 = var3;
        var2 = var4;
        if (var5 != null) {
            int var10;
            label32: {
                if (this.mOnlyIndeterminate && !(var5 instanceof AnimationDrawable)) {
                    var1 = var5.getIntrinsicWidth();
                    var2 = this.mIndeterminateDrawable.getIntrinsicHeight();
                    float var6 = (float)var1 / (float)var2;
                    float var7 = (float)var3;
                    float var8 = (float)var4;
                    float var9 = var7 / var8;
                    if ((double)Math.abs(var6 - var9) < 1.0E-7D) {
                        if (var9 > var6) {
                            var2 = (int)(var8 * var6);
                            var1 = (var3 - var2) / 2;
                            var2 += var1;
                            var10 = 0;
                        } else {
                            var2 = (int)(var7 * (1.0F / var6));
                            var1 = (var4 - var2) / 2;
                            var10 = var1;
                            var4 = var2 + var1;
                            var1 = 0;
                            var2 = var3;
                        }
                        break label32;
                    }
                }

                var2 = var3;
                var1 = 0;
                var10 = var1;
            }

            if (this.mMirrorForRtl && ViewUtils.isLayoutRtl(this)) {
                var2 = var3 - var2;
                var1 = var3 - var1;
            } else {
                var3 = var1;
                var1 = var2;
                var2 = var3;
            }

            this.mIndeterminateDrawable.setBounds(var2, var10, var1, var4);
            var2 = var4;
        }

        var5 = this.mProgressDrawable;
        if (var5 != null) {
            var5.setBounds(0, 0, var1, var2);
        }

    }

    public final void updateDrawableState() {
        int[] var1 = this.getDrawableState();
        Drawable var2 = this.mProgressDrawable;
        boolean var3 = false;
        boolean var4 = var3;
        if (var2 != null) {
            var4 = var3;
            if (var2.isStateful()) {
                var4 = false | var2.setState(var1);
            }
        }

        var2 = this.mIndeterminateDrawable;
        var3 = var4;
        if (var2 != null) {
            var3 = var4;
            if (var2.isStateful()) {
                var3 = var4 | var2.setState(var1);
            }
        }

        if (var3) {
            this.invalidate();
        }

    }

    public boolean verifyDrawable(Drawable var1) {
        boolean var2;
        if (var1 != this.mProgressDrawable && var1 != this.mIndeterminateDrawable && !super.verifyDrawable(var1)) {
            var2 = false;
        } else {
            var2 = true;
        }

        return var2;
    }

    private class AccessibilityEventSender implements Runnable {
        public AccessibilityEventSender() {
        }

        public void run() {
            SeslProgressBar.this.sendAccessibilityEvent(4);
        }
    }

    private static class ProgressTintInfo {
        public boolean mHasIndeterminateTint;
        public boolean mHasIndeterminateTintMode;
        public boolean mHasProgressBackgroundTint;
        public boolean mHasProgressBackgroundTintMode;
        public boolean mHasProgressTint;
        public boolean mHasProgressTintMode;
        public boolean mHasSecondaryProgressTint;
        public boolean mHasSecondaryProgressTintMode;
        public ColorStateList mIndeterminateTintList;
        public Mode mIndeterminateTintMode;
        public ColorStateList mProgressBackgroundTintList;
        public Mode mProgressBackgroundTintMode;
        public ColorStateList mProgressTintList;
        public Mode mProgressTintMode;
        public ColorStateList mSecondaryProgressTintList;
        public Mode mSecondaryProgressTintMode;

        public ProgressTintInfo() {
        }
    }

    private static class RefreshData {
        public static final Pools.SynchronizedPool<RefreshData> sPool = new Pools.SynchronizedPool(24);
        public boolean animate;
        public boolean fromUser;
        public int id;
        public int progress;

        public RefreshData() {
        }

        public static SeslProgressBar.RefreshData obtain(int var0, int var1, boolean var2, boolean var3) {
            SeslProgressBar.RefreshData var4 = (SeslProgressBar.RefreshData)sPool.acquire();
            SeslProgressBar.RefreshData var5 = var4;
            if (var4 == null) {
                var5 = new SeslProgressBar.RefreshData();
            }

            var5.id = var0;
            var5.progress = var1;
            var5.fromUser = var2;
            var5.animate = var3;
            return var5;
        }

        public void recycle() {
            sPool.release(this);
        }
    }

    private class RefreshProgressRunnable implements Runnable {
        public RefreshProgressRunnable() {
        }

        public void run() {
            SeslProgressBar var1 = SeslProgressBar.this;
            synchronized(var1){}

            Throwable var10000;
            boolean var10001;
            label215: {
                int var2;
                try {
                    var2 = SeslProgressBar.this.mRefreshData.size();
                } catch (Throwable var24) {
                    var10000 = var24;
                    var10001 = false;
                    break label215;
                }

                for(int var3 = 0; var3 < var2; ++var3) {
                    try {
                        SeslProgressBar.RefreshData var4 = (SeslProgressBar.RefreshData)SeslProgressBar.this.mRefreshData.get(var3);
                        SeslProgressBar.this.doRefreshProgress(var4.id, var4.progress, var4.fromUser, true, var4.animate);
                        var4.recycle();
                    } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label215;
                    }
                }

                label199:
                try {
                    SeslProgressBar.this.mRefreshData.clear();
                    SeslProgressBar.this.mRefreshIsPosted = false;
                    return;
                } catch (Throwable var22) {
                    var10000 = var22;
                    var10001 = false;
                    break label199;
                }
            }

            while(true) {
                Throwable var25 = var10000;

                try {
                    throw var25;
                } catch (Throwable var21) {
                    var10000 = var21;
                    var10001 = false;
                    continue;
                }
            }
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SeslProgressBar.SavedState> CREATOR = new Creator<SeslProgressBar.SavedState>() {
            public SeslProgressBar.SavedState createFromParcel(Parcel var1) {
                return new SeslProgressBar.SavedState(var1);
            }

            public SeslProgressBar.SavedState[] newArray(int var1) {
                return new SeslProgressBar.SavedState[var1];
            }
        };
        public int progress;
        public int secondaryProgress;

        public SavedState(Parcel var1) {
            super(var1);
            this.progress = var1.readInt();
            this.secondaryProgress = var1.readInt();
        }

        public SavedState(Parcelable var1) {
            super(var1);
        }

        public void writeToParcel(Parcel var1, int var2) {
            super.writeToParcel(var1, var2);
            var1.writeInt(this.progress);
            var1.writeInt(this.secondaryProgress);
        }
    }

    private static class StateListDrawableCompat {
        public static final boolean IS_BASE_SDK_VERSION;

        static {
            boolean var0;
            if (VERSION.SDK_INT <= 23) {
                var0 = true;
            } else {
                var0 = false;
            }

            IS_BASE_SDK_VERSION = var0;
        }

        public static int getStateCount(StateListDrawable var0) {
            if (IS_BASE_SDK_VERSION) {
                Utils.genericInvokeMethod(var0, Build.VERSION.SDK_INT >= 29 ? "hidden_getStateCount" : "getStateCount");
            }

            return 0;
        }

        public static Drawable getStateDrawable(StateListDrawable var0, int var1) {
            return IS_BASE_SDK_VERSION ? (Drawable) Utils.genericInvokeMethod(var0, Build.VERSION.SDK_INT >= 29 ? "hidden_getStateDrawable" : "getStateDrawable", var1) : null;
        }

        public static int[] getStateSet(StateListDrawable var0, int var1) {
            return IS_BASE_SDK_VERSION ? (int[]) Utils.genericInvokeMethod(var0, Build.VERSION.SDK_INT >= 29 ? "hidden_getStateSet" : "getStateSet", var1) : null;
        }
    }

    // kang
    public static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
}
