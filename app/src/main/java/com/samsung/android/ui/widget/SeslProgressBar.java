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

import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pools;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

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
    private static final boolean IS_BASE_SDK_VERSION;
    private static final int MAX_LEVEL = 10000;
    public static final int MODE_DUAL_COLOR = 2;
    public static final int MODE_SPLIT = 4;
    public static final int MODE_STANDARD = 0;
    public static final int MODE_VERTICAL = 3;
    public static final int MODE_WARNING = 1;
    private static final int PROGRESS_ANIM_DURATION = 80;
    private static final DecelerateInterpolator PROGRESS_ANIM_INTERPOLATOR;
    private static final int TIMEOUT_SEND_ACCESSIBILITY_EVENT = 200;
    private final FloatProperty<SeslProgressBar> VISUAL_PROGRESS;
    private SeslProgressBar.AccessibilityEventSender mAccessibilityEventSender;
    private boolean mAggregatedIsVisible;
    private AlphaAnimation mAnimation;
    private boolean mAttached;
    private int mBehavior;
    private Drawable mCurrentDrawable;
    protected int mCurrentMode;
    protected float mDensity;
    private int mDuration;
    private boolean mHasAnimation;
    private boolean mInDrawing;
    private boolean mIndeterminate;
    private Drawable mIndeterminateDrawable;
    private Interpolator mInterpolator;
    private int mMax;
    int mMaxHeight;
    private boolean mMaxInitialized;
    int mMaxWidth;
    private int mMin;
    int mMinHeight;
    private boolean mMinInitialized;
    int mMinWidth;
    boolean mMirrorForRtl;
    private boolean mNoInvalidate;
    private boolean mOnlyIndeterminate;
    private int mProgress;
    private Drawable mProgressDrawable;
    private SeslProgressBar.ProgressTintInfo mProgressTintInfo;
    private final ArrayList<SeslProgressBar.RefreshData> mRefreshData;
    private boolean mRefreshIsPosted;
    private SeslProgressBar.RefreshProgressRunnable mRefreshProgressRunnable;
    int mSampleWidth;
    private int mSecondaryProgress;
    private boolean mShouldStartAnimationDrawable;
    private Transformation mTransformation;
    private long mUiThreadId;
    private float mVisualProgress;

    static {
        boolean var0;
        if (VERSION.SDK_INT <= 23) {
            var0 = true;
        } else {
            var0 = false;
        }

        IS_BASE_SDK_VERSION = var0;
        PROGRESS_ANIM_INTERPOLATOR = new DecelerateInterpolator();
    }

    public SeslProgressBar(Context var1) throws Throwable {
        this(var1, (AttributeSet)null);
    }

    public SeslProgressBar(Context var1, AttributeSet var2) throws Throwable {
        this(var1, var2, 16842871);
    }

    public SeslProgressBar(Context var1, AttributeSet var2, int var3) throws Throwable {
        this(var1, var2, var3, 0);
    }

    @SuppressLint("WrongConstant")
    public SeslProgressBar(Context var1, AttributeSet var2, int var3, int var4) throws Throwable {
        super(var1, var2, var3, var4);
        boolean var5 = false;
        this.mSampleWidth = 0;
        this.mMirrorForRtl = false;
        this.mRefreshData = new ArrayList();
        this.mCurrentMode = 0;
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
        TypedArray var7 = var1.obtainStyledAttributes(var2, R.styleable.SeslProgressBar, var3, var4);
        this.mNoInvalidate = true;
        Drawable var6 = var7.getDrawable(R.styleable.SeslProgressBar_android_progressDrawable);
        if (var6 != null) {
            if (needsTileify(var6)) {
                this.setProgressDrawableTiled(var6);
            } else {
                this.setProgressDrawable(var6);
            }
        }

        this.mDuration = var7.getInt(R.styleable.SeslProgressBar_android_indeterminateDuration, this.mDuration);
        this.mMinWidth = var7.getDimensionPixelSize(R.styleable.SeslProgressBar_android_minWidth, this.mMinWidth);
        this.mMaxWidth = var7.getDimensionPixelSize(R.styleable.SeslProgressBar_android_maxWidth, this.mMaxWidth);
        this.mMinHeight = var7.getDimensionPixelSize(R.styleable.SeslProgressBar_android_minHeight, this.mMinHeight);
        this.mMaxHeight = var7.getDimensionPixelSize(R.styleable.SeslProgressBar_android_maxHeight, this.mMaxHeight);
        this.mBehavior = var7.getInt(R.styleable.SeslProgressBar_android_indeterminateBehavior, this.mBehavior);
        var3 = var7.getResourceId(R.styleable.SeslProgressBar_android_interpolator, 17432587);
        if (var3 > 0) {
            this.setInterpolator(var1, var3);
        }

        this.setMin(var7.getInt(R.styleable.SeslProgressBar_min, this.mMin));
        this.setMax(var7.getInt(R.styleable.SeslProgressBar_android_max, this.mMax));
        this.setProgress(var7.getInt(R.styleable.SeslProgressBar_android_progress, this.mProgress));
        this.setSecondaryProgress(var7.getInt(R.styleable.SeslProgressBar_android_secondaryProgress, this.mSecondaryProgress));
        var6 = var7.getDrawable(R.styleable.SeslProgressBar_android_indeterminateDrawable);
        if (var6 != null) {
            if (needsTileify(var6)) {
                this.setIndeterminateDrawableTiled(var6);
            } else {
                this.setIndeterminateDrawable(var6);
            }
        }

        this.mOnlyIndeterminate = var7.getBoolean(R.styleable.SeslProgressBar_android_indeterminateOnly, this.mOnlyIndeterminate);
        this.mNoInvalidate = false;
        if (this.mOnlyIndeterminate || var7.getBoolean(R.styleable.SeslProgressBar_android_indeterminate, this.mIndeterminate)) {
            var5 = true;
        }

        this.setIndeterminate(var5);
        this.mMirrorForRtl = var7.getBoolean(R.styleable.SeslProgressBar_android_mirrorForRtl, this.mMirrorForRtl);
        if (var7.hasValue(R.styleable.SeslProgressBar_android_progressTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mProgressTintMode = DrawableUtils.parseTintMode(var7.getInt(R.styleable.SeslProgressBar_android_progressTintMode, -1), (Mode)null);
            this.mProgressTintInfo.mHasProgressTintMode = true;
        }

        if (var7.hasValue(R.styleable.SeslProgressBar_android_progressTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mProgressTintList = var7.getColorStateList(R.styleable.SeslProgressBar_android_progressTint);
            this.mProgressTintInfo.mHasProgressTint = true;
        }

        if (var7.hasValue(R.styleable.SeslProgressBar_android_progressBackgroundTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mProgressBackgroundTintMode = DrawableUtils.parseTintMode(var7.getInt(R.styleable.SeslProgressBar_android_progressBackgroundTintMode, -1), (Mode)null);
            this.mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        }

        if (var7.hasValue(R.styleable.SeslProgressBar_android_progressBackgroundTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mProgressBackgroundTintList = var7.getColorStateList(R.styleable.SeslProgressBar_android_progressBackgroundTint);
            this.mProgressTintInfo.mHasProgressBackgroundTint = true;
        }

        if (var7.hasValue(R.styleable.SeslProgressBar_android_secondaryProgressTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mSecondaryProgressTintMode = DrawableUtils.parseTintMode(var7.getInt(R.styleable.SeslProgressBar_android_secondaryProgressTintMode, -1), (Mode)null);
            this.mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        }

        if (var7.hasValue(R.styleable.SeslProgressBar_android_secondaryProgressTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mSecondaryProgressTintList = var7.getColorStateList(R.styleable.SeslProgressBar_android_secondaryProgressTint);
            this.mProgressTintInfo.mHasSecondaryProgressTint = true;
        }

        if (var7.hasValue(R.styleable.SeslProgressBar_android_indeterminateTintMode)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mIndeterminateTintMode = DrawableUtils.parseTintMode(var7.getInt(R.styleable.SeslProgressBar_android_indeterminateTintMode, -1), (Mode)null);
            this.mProgressTintInfo.mHasIndeterminateTintMode = true;
        }

        if (var7.hasValue(R.styleable.SeslProgressBar_android_indeterminateTint)) {
            if (this.mProgressTintInfo == null) {
                this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
            }

            this.mProgressTintInfo.mIndeterminateTintList = var7.getColorStateList(R.styleable.SeslProgressBar_android_indeterminateTint);
            this.mProgressTintInfo.mHasIndeterminateTint = true;
        }

        var7.recycle();
        this.applyProgressTints();
        this.applyIndeterminateTint();
        if (this.getImportantForAccessibility() == 0) {
            this.setImportantForAccessibility(1);
        }

        this.mDensity = var1.getResources().getDisplayMetrics().density;
    }

    private static int StateListDrawable_getStateCount(StateListDrawable var0) {
        if (IS_BASE_SDK_VERSION) {
            Utils.genericInvokeMethod(var0, VERSION.SDK_INT >= 29 ? "hidden_getStateCount" : "getStateCount");
        }

        return 0;
    }

    private static Drawable StateListDrawable_getStateDrawable(StateListDrawable var0, int var1) {
        Drawable var2;
        if (IS_BASE_SDK_VERSION) {
            var2 = (Drawable) Utils.genericInvokeMethod(var0, VERSION.SDK_INT >= 29 ? "hidden_getStateDrawable" : "getStateDrawable", var1);
        } else {
            var2 = null;
        }

        return var2;
    }

    private int[] StateListDrawable_getStateSet(StateListDrawable var1, int var2) {
        int[] var3;
        if (IS_BASE_SDK_VERSION) {
            var3 = (int[]) Utils.genericInvokeMethod(var1, VERSION.SDK_INT >= 29 ? "hidden_getStateSet" : "getStateSet", var2);
        } else {
            var3 = null;
        }

        return var3;
    }

    private void applyIndeterminateTint() {
        if (this.mIndeterminateDrawable != null && this.mProgressTintInfo != null) {
            SeslProgressBar.ProgressTintInfo var1 = this.mProgressTintInfo;
            if (var1.mHasIndeterminateTint || var1.mHasIndeterminateTintMode) {
                this.mIndeterminateDrawable = this.mIndeterminateDrawable.mutate();
                if (var1.mHasIndeterminateTint) {
                    this.mIndeterminateDrawable.setTintList(var1.mIndeterminateTintList);
                }

                if (var1.mHasIndeterminateTintMode) {
                    this.mIndeterminateDrawable.setTintMode(var1.mIndeterminateTintMode);
                }

                if (this.mIndeterminateDrawable.isStateful()) {
                    this.mIndeterminateDrawable.setState(this.getDrawableState());
                }
            }
        }

    }

    private void applyPrimaryProgressTint() {
        if (this.mProgressTintInfo.mHasProgressTint || this.mProgressTintInfo.mHasProgressTintMode) {
            Drawable var1 = this.getTintTarget(R.id.progress, true);
            if (var1 != null) {
                if (this.mProgressTintInfo.mHasProgressTint) {
                    var1.setTintList(this.mProgressTintInfo.mProgressTintList);
                }

                if (this.mProgressTintInfo.mHasProgressTintMode) {
                    var1.setTintMode(this.mProgressTintInfo.mProgressTintMode);
                }

                if (var1.isStateful()) {
                    var1.setState(this.getDrawableState());
                }
            }
        }

    }

    private void applyProgressBackgroundTint() {
        if (this.mProgressTintInfo.mHasProgressBackgroundTint || this.mProgressTintInfo.mHasProgressBackgroundTintMode) {
            Drawable var1 = this.getTintTarget(R.id.background, false);
            if (var1 != null) {
                if (this.mProgressTintInfo.mHasProgressBackgroundTint) {
                    var1.setTintList(this.mProgressTintInfo.mProgressBackgroundTintList);
                }

                if (this.mProgressTintInfo.mHasProgressBackgroundTintMode) {
                    var1.setTintMode(this.mProgressTintInfo.mProgressBackgroundTintMode);
                }

                if (var1.isStateful()) {
                    var1.setState(this.getDrawableState());
                }
            }
        }

    }

    private void applyProgressTints() {
        if (this.mProgressDrawable != null && this.mProgressTintInfo != null) {
            this.applyPrimaryProgressTint();
            this.applyProgressBackgroundTint();
            this.applySecondaryProgressTint();
        }

    }

    private void applySecondaryProgressTint() {
        if (this.mProgressTintInfo.mHasSecondaryProgressTint || this.mProgressTintInfo.mHasSecondaryProgressTintMode) {
            Drawable var1 = this.getTintTarget(R.id.secondaryProgress, false);
            if (var1 != null) {
                if (this.mProgressTintInfo.mHasSecondaryProgressTint) {
                    var1.setTintList(this.mProgressTintInfo.mSecondaryProgressTintList);
                }

                if (this.mProgressTintInfo.mHasSecondaryProgressTintMode) {
                    var1.setTintMode(this.mProgressTintInfo.mSecondaryProgressTintMode);
                }

                if (var1.isStateful()) {
                    var1.setState(this.getDrawableState());
                }
            }
        }

    }

    private void doRefreshProgress(int var1, int var2, boolean var3, boolean var4, boolean var5) throws Throwable {
        synchronized(this){}

        Throwable var10000;
        label3047: {
            int var6;
            boolean var10001;
            try {
                var6 = this.mMax - this.mMin;
            } catch (Throwable var319) {
                var10000 = var319;
                var10001 = false;
                break label3047;
            }

            float var7;
            if (var6 > 0) {
                try {
                    var7 = (float)(var2 - this.mMin) / (float)var6;
                } catch (Throwable var318) {
                    var10000 = var318;
                    var10001 = false;
                    break label3047;
                }
            } else {
                var7 = 0.0F;
            }

            boolean var320;
            label3027: {
                label3026: {
                    try {
                        if (var1 == R.id.progress) {
                            break label3026;
                        }
                    } catch (Throwable var317) {
                        var10000 = var317;
                        var10001 = false;
                        break label3047;
                    }

                    var320 = false;
                    break label3027;
                }

                var320 = true;
            }

            int var8 = (int)(10000.0F * var7);

            Drawable var9;
            try {
                var9 = this.mCurrentDrawable;
            } catch (Throwable var316) {
                var10000 = var316;
                var10001 = false;
                break label3047;
            }

            if (var9 != null) {
                label3041: {
                    Drawable var10;
                    label3042: {
                        try {
                            if (var9 instanceof LayerDrawable) {
                                var10 = ((LayerDrawable)var9).findDrawableByLayerId(var1);
                                break label3042;
                            }
                        } catch (Throwable var315) {
                            var10000 = var315;
                            var10001 = false;
                            break label3047;
                        }

                        int var11;
                        label3043: {
                            try {
                                if (var9 instanceof StateListDrawable) {
                                    var11 = StateListDrawable_getStateCount((StateListDrawable)var9);
                                    break label3043;
                                }
                            } catch (Throwable var314) {
                                var10000 = var314;
                                var10001 = false;
                                break label3047;
                            }

                            if (false) {
                                var9 = null;
                            }

                            try {
                                var9.setLevel(var8);
                                break label3041;
                            } catch (Throwable var307) {
                                var10000 = var307;
                                var10001 = false;
                                break label3047;
                            }
                        }

                        int var12 = 0;

                        while(true) {
                            if (var12 >= var11) {
                                break label3041;
                            }

                            Drawable var13;
                            try {
                                var13 = StateListDrawable_getStateDrawable((StateListDrawable)var9, var12);
                            } catch (Throwable var309) {
                                var10000 = var309;
                                var10001 = false;
                                break label3047;
                            }

                            var10 = null;
                            if (var13 == null) {
                                return;
                            }

                            label3045: {
                                try {
                                    if (!(var13 instanceof LayerDrawable)) {
                                        break label3045;
                                    }

                                    var13 = ((LayerDrawable)var13).findDrawableByLayerId(var1);
                                } catch (Throwable var313) {
                                    var10000 = var313;
                                    var10001 = false;
                                    break label3047;
                                }

                                var10 = var13;
                                if (var13 != null) {
                                    label3046: {
                                        var10 = var13;

                                        try {
                                            if (!this.canResolveLayoutDirection()) {
                                                break label3046;
                                            }

                                            DrawableCompat.setLayoutDirection(var13, this.getLayoutDirection());
                                        } catch (Throwable var312) {
                                            var10000 = var312;
                                            var10001 = false;
                                            break label3047;
                                        }

                                        var10 = var13;
                                    }
                                }
                            }

                            if (var10 == null) {
                                var10 = var9;
                            }

                            try {
                                var10.setLevel(var8);
                            } catch (Throwable var308) {
                                var10000 = var308;
                                var10001 = false;
                                break label3047;
                            }

                            ++var12;
                        }
                    }

                    if (var10 != null) {
                        try {
                            if (this.canResolveLayoutDirection()) {
                                DrawableCompat.setLayoutDirection(var10, this.getLayoutDirection());
                            }
                        } catch (Throwable var311) {
                            var10000 = var311;
                            var10001 = false;
                            break label3047;
                        }
                    }

                    if (var10 != null) {
                        var9 = var10;
                    }

                    try {
                        var9.setLevel(var8);
                    } catch (Throwable var310) {
                        var10000 = var310;
                        var10001 = false;
                        break label3047;
                    }
                }
            } else {
                try {
                    this.invalidate();
                } catch (Throwable var306) {
                    var10000 = var306;
                    var10001 = false;
                    break label3047;
                }
            }

            if (var320 && var5) {
                try {
                    ObjectAnimator var321 = ObjectAnimator.ofFloat(this, this.VISUAL_PROGRESS, new float[]{var7});
                    var321.setAutoCancel(true);
                    var321.setDuration(80L);
                    var321.setInterpolator(PROGRESS_ANIM_INTERPOLATOR);
                    var321.start();
                } catch (Throwable var305) {
                    var10000 = var305;
                    var10001 = false;
                    break label3047;
                }
            } else {
                try {
                    this.setVisualProgress(var1, var7);
                } catch (Throwable var304) {
                    var10000 = var304;
                    var10001 = false;
                    break label3047;
                }
            }

            if (!var320 || !var4) {
                return;
            }

            label2953:
            try {
                this.onProgressRefresh(var7, var3, var2);
                return;
            } catch (Throwable var303) {
                var10000 = var303;
                var10001 = false;
                break label2953;
            }
        }

        Throwable var322 = var10000;
        throw var322;
    }

    private Drawable getTintTarget(int var1, boolean var2) {
        Drawable var3 = null;
        Drawable var4 = null;
        Drawable var5 = this.mProgressDrawable;
        if (var5 != null) {
            this.mProgressDrawable = var5.mutate();
            if (var5 instanceof LayerDrawable) {
                var4 = ((LayerDrawable)var5).findDrawableByLayerId(var1);
            }

            var3 = var4;
            if (var2) {
                var3 = var4;
                if (var4 == null) {
                    var3 = var5;
                }
            }
        }

        return var3;
    }

    private void initProgressBar() {
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

    private static boolean needsTileify(Drawable var0) {
        boolean var1 = true;
        int var2;
        int var3;
        boolean var4;
        if (var0 instanceof LayerDrawable) {
            LayerDrawable var6 = (LayerDrawable)var0;
            var2 = var6.getNumberOfLayers();
            var3 = 0;

            while(true) {
                if (var3 >= var2) {
                    var4 = false;
                    break;
                }

                if (needsTileify(var6.getDrawable(var3))) {
                    var4 = var1;
                    break;
                }

                ++var3;
            }
        } else if (var0 instanceof StateListDrawable) {
            StateListDrawable var7 = (StateListDrawable)var0;
            var2 = StateListDrawable_getStateCount(var7);
            var3 = 0;

            while(true) {
                if (var3 >= var2) {
                    var4 = false;
                    break;
                }

                Drawable var5 = StateListDrawable_getStateDrawable(var7, var3);
                if (var5 != null) {
                    var4 = var1;
                    if (needsTileify(var5)) {
                        break;
                    }
                }

                ++var3;
            }
        } else {
            var4 = var1;
            if (!(var0 instanceof BitmapDrawable)) {
                var4 = false;
            }
        }

        return var4;
    }

    private void refreshProgress(int var1, int var2, boolean var3, boolean var4) throws Throwable {
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
        } finally { }

    }

    private void scheduleAccessibilityEventSender() {
        if (this.mAccessibilityEventSender == null) {
            this.mAccessibilityEventSender = new SeslProgressBar.AccessibilityEventSender();
        } else {
            this.removeCallbacks(this.mAccessibilityEventSender);
        }

        this.postDelayed(this.mAccessibilityEventSender, 200L);
    }

    private void setVisualProgress(int var1, float var2) {
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
    private void swapCurrentDrawable(Drawable var1) {
        Drawable var2 = this.mCurrentDrawable;
        this.mCurrentDrawable = var1;
        if (var2 != this.mCurrentDrawable) {
            if (var2 != null) {
                var2.setVisible(false, false);
            }

            if (this.mCurrentDrawable != null) {
                var1 = this.mCurrentDrawable;
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

    private Drawable tileify(Drawable var1, boolean var2) {
        int var4;
        int var6;
        if (var1 instanceof LayerDrawable) {
            LayerDrawable var3 = (LayerDrawable)var1;
            var4 = var3.getNumberOfLayers();
            Drawable[] var5 = new Drawable[var4];

            for(var6 = 0; var6 < var4; ++var6) {
                int var7 = var3.getId(var6);
                Drawable var9 = var3.getDrawable(var6);
                if (var7 != R.id.progress && var7 != R.id.secondaryProgress) {
                    var2 = false;
                } else {
                    var2 = true;
                }

                var5[var6] = this.tileify(var9, var2);
            }

            LayerDrawable var11 = new LayerDrawable(var5);
            var6 = 0;

            while(true) {
                var1 = var11;
                if (var6 >= var4) {
                    break;
                }

                var11.setId(var6, var3.getId(var6));
                if (VERSION.SDK_INT >= 23) {
                    var11.setLayerGravity(var6, var3.getLayerGravity(var6));
                    var11.setLayerWidth(var6, var3.getLayerWidth(var6));
                    var11.setLayerHeight(var6, var3.getLayerHeight(var6));
                    var11.setLayerInsetLeft(var6, var3.getLayerInsetLeft(var6));
                    var11.setLayerInsetRight(var6, var3.getLayerInsetRight(var6));
                    var11.setLayerInsetTop(var6, var3.getLayerInsetTop(var6));
                    var11.setLayerInsetBottom(var6, var3.getLayerInsetBottom(var6));
                    var11.setLayerInsetStart(var6, var3.getLayerInsetStart(var6));
                    var11.setLayerInsetEnd(var6, var3.getLayerInsetEnd(var6));
                }

                ++var6;
            }
        } else if (var1 instanceof StateListDrawable) {
            StateListDrawable var10 = (StateListDrawable)var1;
            var1 = new StateListDrawable();
            var4 = StateListDrawable_getStateCount(var10);

            for(var6 = 0; var6 < var4; ++var6) {
                int[] var12 = this.StateListDrawable_getStateSet(var10, var6);
                Drawable var8 = StateListDrawable_getStateDrawable(var10, var6);
                if (var8 != null) {
                    ((StateListDrawable)var1).addState(var12, this.tileify(var8, var2));
                }
            }
        } else if (var1 instanceof BitmapDrawable) {
            BitmapDrawable var13 = (BitmapDrawable)((Drawable)var1).getConstantState().newDrawable(this.getResources());
            var13.setTileModeXY(TileMode.REPEAT, TileMode.CLAMP);
            if (this.mSampleWidth <= 0) {
                this.mSampleWidth = var13.getIntrinsicWidth();
            }

            var1 = var13;
            if (var2) {
                var1 = new ClipDrawable(var13, 3, 1);
            }
        }

        return (Drawable)var1;
    }

    private Drawable tileifyIndeterminate(Drawable var1) {
        Object var2 = var1;
        if (var1 instanceof AnimationDrawable) {
            AnimationDrawable var6 = (AnimationDrawable)var1;
            int var3 = var6.getNumberOfFrames();
            var2 = new AnimationDrawable();
            ((AnimationDrawable)var2).setOneShot(var6.isOneShot());

            for(int var4 = 0; var4 < var3; ++var4) {
                Drawable var5 = this.tileify(var6.getFrame(var4), true);
                var5.setLevel(10000);
                ((AnimationDrawable)var2).addFrame(var5, var6.getDuration(var4));
            }

            ((AnimationDrawable)var2).setLevel(10000);
        }

        return (Drawable)var2;
    }

    private void updateDrawableState() {
        int[] var1 = this.getDrawableState();
        boolean var2 = false;
        Drawable var3 = this.mProgressDrawable;
        boolean var4 = var2;
        if (var3 != null) {
            var4 = var2;
            if (var3.isStateful()) {
                var4 = false | var3.setState(var1);
            }
        }

        var3 = this.mIndeterminateDrawable;
        var2 = var4;
        if (var3 != null) {
            var2 = var4;
            if (var3.isStateful()) {
                var2 = var4 | var3.setState(var1);
            }
        }

        if (var2) {
            this.invalidate();
        }

    }

    void drawTrack(Canvas var1) {
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
                    var2.setLevel((int)(10000.0F * var6));
                } finally {
                    this.mInDrawing = false;
                }

                this.postInvalidateOnAnimation();
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
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setHotspot(var1, var2);
        }

        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.setHotspot(var1, var2);
        }

    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.updateDrawableState();
    }

    public CharSequence getAccessibilityClassName() {
        return ProgressBar.class.getName();
    }

    Drawable getCurrentDrawable() {
        return this.mCurrentDrawable;
    }

    public Drawable getIndeterminateDrawable() {
        return this.mIndeterminateDrawable;
    }

    public ColorStateList getIndeterminateTintList() {
        ColorStateList var1;
        if (this.mProgressTintInfo != null) {
            var1 = this.mProgressTintInfo.mIndeterminateTintList;
        } else {
            var1 = null;
        }

        return var1;
    }

    public Mode getIndeterminateTintMode() {
        Mode var1;
        if (this.mProgressTintInfo != null) {
            var1 = this.mProgressTintInfo.mIndeterminateTintMode;
        } else {
            var1 = null;
        }

        return var1;
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

    public boolean getMirrorForRtl() {
        return this.mMirrorForRtl;
    }

    public int getPaddingLeft() {
        return (int) Utils.genericGetField(MeasureSpec.class, this, "mPaddingLeft");
    }

    public int getPaddingRight() {
        return (int) Utils.genericGetField(MeasureSpec.class, this, "mPaddingRight");
    }

    @ExportedProperty(
            category = "progress"
    )
    public int getProgress() throws Throwable {
        synchronized(this){}

        Throwable var10000;
        label79: {
            boolean var1;
            boolean var10001;
            try {
                var1 = this.mIndeterminate;
            } catch (Throwable var9) {
                var10000 = var9;
                var10001 = false;
                break label79;
            }

            int var2;
            if (var1) {
                var2 = 0;
                return var2;
            }

            label69:
            try {
                var2 = this.mProgress;
                return var2;
            } catch (Throwable var8) {
                var10000 = var8;
                var10001 = false;
                break label69;
            }
        }

        Throwable var3 = var10000;
        throw var3;
    }

    public ColorStateList getProgressBackgroundTintList() {
        ColorStateList var1;
        if (this.mProgressTintInfo != null) {
            var1 = this.mProgressTintInfo.mProgressBackgroundTintList;
        } else {
            var1 = null;
        }

        return var1;
    }

    public Mode getProgressBackgroundTintMode() {
        Mode var1;
        if (this.mProgressTintInfo != null) {
            var1 = this.mProgressTintInfo.mProgressBackgroundTintMode;
        } else {
            var1 = null;
        }

        return var1;
    }

    public Drawable getProgressDrawable() {
        return this.mProgressDrawable;
    }

    public ColorStateList getProgressTintList() {
        ColorStateList var1;
        if (this.mProgressTintInfo != null) {
            var1 = this.mProgressTintInfo.mProgressTintList;
        } else {
            var1 = null;
        }

        return var1;
    }

    public Mode getProgressTintMode() {
        Mode var1;
        if (this.mProgressTintInfo != null) {
            var1 = this.mProgressTintInfo.mProgressTintMode;
        } else {
            var1 = null;
        }

        return var1;
    }

    @ExportedProperty(
            category = "progress"
    )
    public int getSecondaryProgress() throws Throwable {
        synchronized(this){}

        Throwable var10000;
        label79: {
            boolean var1;
            boolean var10001;
            try {
                var1 = this.mIndeterminate;
            } catch (Throwable var9) {
                var10000 = var9;
                var10001 = false;
                break label79;
            }

            int var2;
            if (var1) {
                var2 = 0;
                return var2;
            }

            label69:
            try {
                var2 = this.mSecondaryProgress;
                return var2;
            } catch (Throwable var8) {
                var10000 = var8;
                var10001 = false;
                break label69;
            }
        }

        Throwable var3 = var10000;
        throw var3;
    }

    public ColorStateList getSecondaryProgressTintList() {
        ColorStateList var1;
        if (this.mProgressTintInfo != null) {
            var1 = this.mProgressTintInfo.mSecondaryProgressTintList;
        } else {
            var1 = null;
        }

        return var1;
    }

    public Mode getSecondaryProgressTintMode() {
        Mode var1;
        if (this.mProgressTintInfo != null) {
            var1 = this.mProgressTintInfo.mSecondaryProgressTintMode;
        } else {
            var1 = null;
        }

        return var1;
    }

    public final void incrementProgressBy(int var1) throws Throwable {
        synchronized(this){}

        try {
            this.setProgress(this.mProgress + var1);
        } finally { }

    }

    public final void incrementSecondaryProgressBy(int var1) throws Throwable {
        synchronized(this){}

        try {
            this.setSecondaryProgress(this.mSecondaryProgress + var1);
        } finally { }

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

    @SuppressLint("WrongConstant")
    public boolean isAnimating() {
        boolean var1;
        if (this.isIndeterminate() && this.getWindowVisibility() == 0 && this.isShown()) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
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
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.jumpToCurrentState();
        }

        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.jumpToCurrentState();
        }

    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mIndeterminate) {
            this.startAnimation();
        }

        if (this.mRefreshData != null) {
            label271: {
                synchronized(this){}

                Throwable var10000;
                boolean var10001;
                label269: {
                    int var1;
                    try {
                        var1 = this.mRefreshData.size();
                    } catch (Throwable var23) {
                        var10000 = var23;
                        var10001 = false;
                        break label269;
                    }

                    for(int var2 = 0; var2 < var1; ++var2) {
                        try {
                            SeslProgressBar.RefreshData var3 = (SeslProgressBar.RefreshData)this.mRefreshData.get(var2);
                            this.doRefreshProgress(var3.id, var3.progress, var3.fromUser, true, var3.animate);
                            var3.recycle();
                        } catch (Throwable var22) {
                            var10000 = var22;
                            var10001 = false;
                            break label269;
                        }
                    }

                    label249:
                    try {
                        this.mRefreshData.clear();
                        break label271;
                    } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label249;
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

    protected void onDetachedFromWindow() {
        if (this.mIndeterminate) {
            this.stopAnimation();
        }

        if (this.mRefreshProgressRunnable != null) {
            this.removeCallbacks(this.mRefreshProgressRunnable);
            this.mRefreshIsPosted = false;
        }

        if (this.mAccessibilityEventSender != null) {
            this.removeCallbacks(this.mAccessibilityEventSender);
        }

        super.onDetachedFromWindow();
        this.mAttached = false;
    }

    protected void onDraw(Canvas var1) {
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
        if (!this.isIndeterminate()) {
            try {
                var1.setRangeInfo(RangeInfo.obtain(0, 0.0F, (float)this.getMax(), (float)this.getProgress()));
            } catch (Throwable throwable) { }
        }

    }

    protected void onMeasure(int var1, int var2) {
        synchronized(this){}
        int var3 = 0;
        int var4 = 0;

        Throwable var10000;
        label116: {
            Drawable var5;
            boolean var10001;
            try {
                var5 = this.mCurrentDrawable;
            } catch (Throwable var21) {
                var10000 = var21;
                var10001 = false;
                break label116;
            }

            if (var5 != null) {
                try {
                    var3 = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, var5.getIntrinsicWidth()));
                    var4 = Math.max(this.mMinHeight, Math.min(this.mMaxHeight, var5.getIntrinsicHeight()));
                } catch (Throwable var20) {
                    var10000 = var20;
                    var10001 = false;
                    break label116;
                }
            }

            label104:
            try {
                this.updateDrawableState();
                int var6 = this.getPaddingLeft();
                int var7 = this.getPaddingRight();
                int var8 = this.getPaddingTop();
                int var9 = this.getPaddingBottom();
                this.setMeasuredDimension(resolveSizeAndState(var3 + var6 + var7, var1, 0), resolveSizeAndState(var4 + var8 + var9, var2, 0));
                return;
            } catch (Throwable var19) {
                var10000 = var19;
                var10001 = false;
                break label104;
            }
        }

        Throwable var22 = var10000;
        try {
            throw var22;
        } catch (Throwable throwable) { }
    }

    void onProgressRefresh(float var1, boolean var2, int var3) throws Throwable {
        if (((AccessibilityManager)this.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE)).isEnabled()) {
            this.scheduleAccessibilityEventSender();
        }

        if (this.mSecondaryProgress > this.mProgress && !var2) {
            this.refreshProgress(R.id.secondaryProgress, this.mSecondaryProgress, false, false);
        }

    }

    public void onResolveDrawables(int var1) {
        Drawable var2 = this.mCurrentDrawable;
        if (var2 != null) {
            DrawableCompat.setLayoutDirection(var2, this.getLayoutDirection());
        }

        if (this.mIndeterminateDrawable != null) {
            DrawableCompat.setLayoutDirection(this.mIndeterminateDrawable, this.getLayoutDirection());
        }

        if (this.mProgressDrawable != null) {
            DrawableCompat.setLayoutDirection(this.mProgressDrawable, this.getLayoutDirection());
        }

    }

    public void onRestoreInstanceState(Parcelable var1) {
        SeslProgressBar.SavedState var2 = (SeslProgressBar.SavedState)var1;
        super.onRestoreInstanceState(var2.getSuperState());
        try {
            this.setProgress(var2.progress);
            this.setSecondaryProgress(var2.secondaryProgress);
        } catch (Throwable throwable) { }

    }

    public Parcelable onSaveInstanceState() {
        SeslProgressBar.SavedState var1 = new SeslProgressBar.SavedState(super.onSaveInstanceState());
        var1.progress = this.mProgress;
        var1.secondaryProgress = this.mSecondaryProgress;
        return var1;
    }

    protected void onSizeChanged(int var1, int var2, int var3, int var4) {
        this.updateDrawableBounds(var1, var2);
    }

    protected void onSlidingRefresh(int var1) {
        Drawable var2 = this.mCurrentDrawable;
        if (this.mCurrentDrawable != null) {
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

            if (this.mCurrentDrawable != null) {
                this.mCurrentDrawable.setVisible(var1, false);
            }
        }

    }

    protected void onVisibilityChanged(View var1, int var2) {
        super.onVisibilityChanged(var1, var2);
        if (this.mIndeterminate) {
            if (var2 != 8 && var2 != 4) {
                this.startAnimation();
            } else {
                this.stopAnimation();
            }
        }

    }

    void onVisualProgressChanged(int var1, float var2) {
    }

    public void postInvalidate() {
        if (!this.mNoInvalidate) {
            super.postInvalidate();
        }

    }

    public void setIndeterminate(boolean var1) throws Throwable {
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
        throw var2;
    }

    public void setIndeterminateDrawable(Drawable var1) {
        if (this.mIndeterminateDrawable != var1) {
            if (this.mIndeterminateDrawable != null) {
                this.mIndeterminateDrawable.setCallback((Callback)null);
                this.unscheduleDrawable(this.mIndeterminateDrawable);
            }

            this.mIndeterminateDrawable = var1;
            if (var1 != null) {
                var1.setCallback(this);
                DrawableCompat.setLayoutDirection(var1, this.getLayoutDirection());
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

        this.mProgressTintInfo.mIndeterminateTintList = var1;
        this.mProgressTintInfo.mHasIndeterminateTint = true;
        this.applyIndeterminateTint();
    }

    public void setIndeterminateTintMode(Mode var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        this.mProgressTintInfo.mIndeterminateTintMode = var1;
        this.mProgressTintInfo.mHasIndeterminateTintMode = true;
        this.applyIndeterminateTint();
    }

    public void setInterpolator(Context var1, int var2) {
        this.setInterpolator(AnimationUtils.loadInterpolator(var1, var2));
    }

    public void setInterpolator(Interpolator var1) {
        this.mInterpolator = var1;
    }

    public void setMax(int var1) throws Throwable {
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
        throw var3;
    }

    public void setMin(int var1) throws Throwable {
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
        throw var3;
    }

    public void setMode(int var1) throws Throwable {
        this.mCurrentMode = var1;
        Drawable var2;
        switch(var1) {
            case 3:
                var2 = this.getContext().getDrawable(R.drawable.sesl_scrubber_progress_vertical);
                if (var2 != null) {
                    this.setProgressDrawableTiled(var2);
                }
                break;
            case 4:
                var2 = this.getContext().getDrawable(R.drawable.sesl_split_seekbar_background_progress);
                if (var2 != null) {
                    this.setProgressDrawableTiled(var2);
                }
        }

    }

    public void setProgress(int var1) throws Throwable {
        synchronized(this){}

        try {
            this.setProgressInternal(var1, false, false);
        } finally { }

    }

    public void setProgress(int var1, boolean var2) throws Throwable {
        this.setProgressInternal(var1, false, var2);
    }

    public void setProgressBackgroundTintList(ColorStateList var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        this.mProgressTintInfo.mProgressBackgroundTintList = var1;
        this.mProgressTintInfo.mHasProgressBackgroundTint = true;
        if (this.mProgressDrawable != null) {
            this.applyProgressBackgroundTint();
        }

    }

    public void setProgressBackgroundTintMode(Mode var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        this.mProgressTintInfo.mProgressBackgroundTintMode = var1;
        this.mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        if (this.mProgressDrawable != null) {
            this.applyProgressBackgroundTint();
        }

    }

    public void setProgressDrawable(Drawable var1) throws Throwable {
        if (this.mProgressDrawable != var1) {
            if (this.mProgressDrawable != null) {
                this.mProgressDrawable.setCallback((Callback)null);
                this.unscheduleDrawable(this.mProgressDrawable);
            }

            this.mProgressDrawable = var1;
            if (var1 != null) {
                var1.setCallback(this);
                DrawableCompat.setLayoutDirection(var1, this.getLayoutDirection());
                if (var1.isStateful()) {
                    var1.setState(this.getDrawableState());
                }

                int var2;
                if (this.mCurrentMode == 3) {
                    var2 = var1.getMinimumWidth();
                    if (this.mMaxWidth < var2) {
                        this.mMaxWidth = var2;
                        this.requestLayout();
                    }
                } else {
                    var2 = var1.getMinimumHeight();
                    if (this.mMaxHeight < var2) {
                        this.mMaxHeight = var2;
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

    public void setProgressDrawableTiled(Drawable var1) throws Throwable {
        Drawable var2 = var1;
        if (var1 != null) {
            var2 = this.tileify(var1, false);
        }

        this.setProgressDrawable(var2);
    }

    boolean setProgressInternal(int var1, boolean var2, boolean var3) throws Throwable {
        boolean var4 = false;
        synchronized(this){}

        label139: {
            Throwable var10000;
            label144: {
                boolean var10001;
                boolean var5;
                try {
                    var5 = this.mIndeterminate;
                } catch (Throwable var18) {
                    var10000 = var18;
                    var10001 = false;
                    break label144;
                }

                if (var5) {
                    return var4;
                }

                try {
                    var1 = constrain(var1, this.mMin, this.mMax);
                } catch (Throwable var17) {
                    var10000 = var17;
                    var10001 = false;
                    break label144;
                }

                label131:
                try {
                    if (var1 == this.mProgress) {
                        return var4;
                    }

                    this.mProgress = var1;
                    this.refreshProgress(R.id.progress, this.mProgress, var2, var3);
                    break label139;
                } catch (Throwable var16) {
                    var10000 = var16;
                    var10001 = false;
                    break label131;
                }
            }

            Throwable var6 = var10000;
            throw var6;
        }

        var4 = true;
        return var4;
    }

    public void setProgressTintList(ColorStateList var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        this.mProgressTintInfo.mProgressTintList = var1;
        this.mProgressTintInfo.mHasProgressTint = true;
        if (this.mProgressDrawable != null) {
            this.applyPrimaryProgressTint();
        }

    }

    public void setProgressTintMode(Mode var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        this.mProgressTintInfo.mProgressTintMode = var1;
        this.mProgressTintInfo.mHasProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            this.applyPrimaryProgressTint();
        }

    }

    public void setSecondaryProgress(int var1) throws Throwable {
        synchronized(this){}

        Throwable var10000;
        label246: {
            boolean var10001;
            boolean var2;
            try {
                var2 = this.mIndeterminate;
            } catch (Throwable var24) {
                var10000 = var24;
                var10001 = false;
                break label246;
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
                break label246;
            }

            var1 = var3;

            try {
                if (var3 > this.mMax) {
                    var1 = this.mMax;
                }
            } catch (Throwable var22) {
                var10000 = var22;
                var10001 = false;
                break label246;
            }

            label230:
            try {
                if (var1 != this.mSecondaryProgress) {
                    this.mSecondaryProgress = var1;
                    this.refreshProgress(R.id.secondaryProgress, this.mSecondaryProgress, false, false);
                }

                return;
            } catch (Throwable var21) {
                var10000 = var21;
                var10001 = false;
                break label230;
            }
        }

        Throwable var4 = var10000;
        throw var4;
    }

    public void setSecondaryProgressTintList(ColorStateList var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        this.mProgressTintInfo.mSecondaryProgressTintList = var1;
        this.mProgressTintInfo.mHasSecondaryProgressTint = true;
        if (this.mProgressDrawable != null) {
            this.applySecondaryProgressTint();
        }

    }

    public void setSecondaryProgressTintMode(Mode var1) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new SeslProgressBar.ProgressTintInfo();
        }

        this.mProgressTintInfo.mSecondaryProgressTintMode = var1;
        this.mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            this.applySecondaryProgressTint();
        }

    }

    public void setVisibility(int var1) {
        if (this.getVisibility() != var1) {
            super.setVisibility(var1);
            if (this.mIndeterminate) {
                if (var1 != 8 && var1 != 4) {
                    this.startAnimation();
                } else {
                    this.stopAnimation();
                }
            }
        }

    }

    @SuppressLint("WrongConstant")
    void startAnimation() {
        if (this.getVisibility() == 0 && (!IS_BASE_SDK_VERSION || this.getWindowVisibility() == 0)) {
            if (this.mIndeterminateDrawable instanceof Animatable) {
                this.mShouldStartAnimationDrawable = true;
                this.mHasAnimation = false;
            } else {
                this.mHasAnimation = true;
                if (this.mInterpolator == null) {
                    this.mInterpolator = new LinearInterpolator();
                }

                if (this.mTransformation == null) {
                    this.mTransformation = new Transformation();
                } else {
                    this.mTransformation.clear();
                }

                if (this.mAnimation == null) {
                    this.mAnimation = new AlphaAnimation(0.0F, 1.0F);
                } else {
                    this.mAnimation.reset();
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

    void stopAnimation() {
        this.mHasAnimation = false;
        if (this.mIndeterminateDrawable instanceof Animatable) {
            ((Animatable)this.mIndeterminateDrawable).stop();
            this.mShouldStartAnimationDrawable = false;
        }

        this.postInvalidate();
    }

    protected void updateDrawableBounds(int var1, int var2) {
        int var3 = var1 - (this.getPaddingRight() + this.getPaddingLeft());
        int var4 = var2 - (this.getPaddingTop() + this.getPaddingBottom());
        byte var5 = 0;
        byte var6 = 0;
        int var7 = var4;
        int var8 = var3;
        if (this.mIndeterminateDrawable != null) {
            var7 = var4;
            int var9 = var6;
            var8 = var3;
            int var10 = var5;
            if (this.mOnlyIndeterminate) {
                var7 = var4;
                var9 = var6;
                var8 = var3;
                var10 = var5;
                if (!(this.mIndeterminateDrawable instanceof AnimationDrawable)) {
                    var7 = this.mIndeterminateDrawable.getIntrinsicWidth();
                    var8 = this.mIndeterminateDrawable.getIntrinsicHeight();
                    float var11 = (float)var7 / (float)var8;
                    float var12 = (float)var3 / (float)var4;
                    var7 = var4;
                    var9 = var6;
                    var8 = var3;
                    var10 = var5;
                    if ((double)Math.abs(var11 - var12) < 1.0E-7D) {
                        if (var12 > var11) {
                            var2 = (int)((float)var4 * var11);
                            var9 = (var3 - var2) / 2;
                            var8 = var9 + var2;
                            var10 = var5;
                            var7 = var4;
                        } else {
                            var1 = (int)((float)var3 * (1.0F / var11));
                            var10 = (var4 - var1) / 2;
                            var7 = var10 + var1;
                            var9 = var6;
                            var8 = var3;
                        }
                    }
                }
            }

            var2 = var9;
            var1 = var8;
            if (this.mMirrorForRtl) {
                var2 = var9;
                var1 = var8;
                if (ViewUtils.isLayoutRtl(this)) {
                    var2 = var3 - var8;
                    var1 = var3 - var9;
                }
            }

            this.mIndeterminateDrawable.setBounds(var2, var10, var1, var7);
            var8 = var1;
        }

        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setBounds(0, 0, var8, var7);
        }

    }

    protected boolean verifyDrawable(Drawable var1) {
        boolean var2;
        if (var1 != this.mProgressDrawable && var1 != this.mIndeterminateDrawable && !super.verifyDrawable(var1)) {
            var2 = false;
        } else {
            var2 = true;
        }

        return var2;
    }

    private class AccessibilityEventSender implements Runnable {
        private AccessibilityEventSender() {
        }

        public void run() {
            SeslProgressBar.this.sendAccessibilityEvent(4);
        }
    }

    private static class ProgressTintInfo {
        boolean mHasIndeterminateTint;
        boolean mHasIndeterminateTintMode;
        boolean mHasProgressBackgroundTint;
        boolean mHasProgressBackgroundTintMode;
        boolean mHasProgressTint;
        boolean mHasProgressTintMode;
        boolean mHasSecondaryProgressTint;
        boolean mHasSecondaryProgressTintMode;
        ColorStateList mIndeterminateTintList;
        Mode mIndeterminateTintMode;
        ColorStateList mProgressBackgroundTintList;
        Mode mProgressBackgroundTintMode;
        ColorStateList mProgressTintList;
        Mode mProgressTintMode;
        ColorStateList mSecondaryProgressTintList;
        Mode mSecondaryProgressTintMode;

        private ProgressTintInfo() {
        }
    }

    private static class RefreshData {
        private static final int POOL_MAX = 24;
        private static final Pools.SynchronizedPool<RefreshData> sPool = new Pools.SynchronizedPool(24);
        public boolean animate;
        public boolean fromUser;
        public int id;
        public int progress;

        private RefreshData() {
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
        private RefreshProgressRunnable() {
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

    static class SavedState extends BaseSavedState {
        public static final Creator<SeslProgressBar.SavedState> CREATOR = new Creator<SeslProgressBar.SavedState>() {
            public SeslProgressBar.SavedState createFromParcel(Parcel var1) {
                return new SeslProgressBar.SavedState(var1);
            }

            public SeslProgressBar.SavedState[] newArray(int var1) {
                return new SeslProgressBar.SavedState[var1];
            }
        };
        int progress;
        int secondaryProgress;

        private SavedState(Parcel var1) {
            super(var1);
            this.progress = var1.readInt();
            this.secondaryProgress = var1.readInt();
        }

        SavedState(Parcelable var1) {
            super(var1);
        }

        public void writeToParcel(Parcel var1, int var2) {
            super.writeToParcel(var1, var2);
            var1.writeInt(this.progress);
            var1.writeInt(this.secondaryProgress);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SeekBarMode {
    }

    // kang
    public static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
}
