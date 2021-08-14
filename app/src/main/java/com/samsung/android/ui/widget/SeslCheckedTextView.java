package com.samsung.android.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.appcompat.widget.DrawableUtils;
import androidx.appcompat.widget.ViewUtils;

import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.ten.update.R;

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

@SuppressLint("AppCompatCustomView")
public class SeslCheckedTextView extends TextView implements Checkable {
    public static final int[] CHECKED_STATE_SET = new int[]{16842912};
    public int mBasePadding;
    public Drawable mCheckMarkDrawable;
    public int mCheckMarkGravity;
    public int mCheckMarkResource;
    public ColorStateList mCheckMarkTintList;
    public PorterDuff.Mode mCheckMarkTintMode;
    public int mCheckMarkWidth;
    public boolean mChecked;
    public int mDrawablePadding;
    public boolean mHasCheckMarkTint;
    public boolean mHasCheckMarkTintMode;
    public boolean mNeedRequestlayout;

    public SeslCheckedTextView(Context var1) {
        this(var1, (AttributeSet)null);
    }

    public SeslCheckedTextView(Context var1, AttributeSet var2) {
        this(var1, var2, 16843720);
    }

    public SeslCheckedTextView(Context var1, AttributeSet var2, int var3) {
        this(var1, var2, var3, 0);
    }

    @SuppressLint("RestrictedApi")
    public SeslCheckedTextView(Context var1, AttributeSet var2, int var3, int var4) {
        super(var1, var2, var3, var4);
        this.mCheckMarkTintList = null;
        this.mCheckMarkTintMode = null;
        this.mHasCheckMarkTint = false;
        this.mHasCheckMarkTintMode = false;
        this.mCheckMarkGravity = 8388611;
        TypedArray var6 = var1.obtainStyledAttributes(var2, R.styleable.SeslCheckedTextView, var3, var4);
        Drawable var5 = var6.getDrawable(R.styleable.SeslCheckedTextView_android_checkMark);
        if (var5 != null) {
            this.setCheckMarkDrawable(var5);
        }

        if (var6.hasValue(R.styleable.SeslCheckedTextView_android_checkMarkTintMode)) {
            this.mCheckMarkTintMode = DrawableUtils.parseTintMode(var6.getInt(R.styleable.SeslCheckedTextView_android_checkMarkTintMode, -1), this.mCheckMarkTintMode);
            this.mHasCheckMarkTintMode = true;
        }

        if (var6.hasValue(R.styleable.SeslCheckedTextView_android_checkMarkTint)) {
            this.mCheckMarkTintList = var6.getColorStateList(R.styleable.SeslCheckedTextView_android_checkMarkTint);
            this.mHasCheckMarkTint = true;
        }

        this.mCheckMarkGravity = var6.getInt(R.styleable.SeslCheckedTextView_checkMarkGravity, 8388611);
        this.setChecked(var6.getBoolean(R.styleable.SeslCheckedTextView_android_checked, false));
        this.mDrawablePadding = var1.getResources().getDimensionPixelSize(R.dimen.sesl_checked_text_padding);
        var6.recycle();
        this.applyCheckMarkTint();
    }

    private void applyCheckMarkTint() {
        if (this.mCheckMarkDrawable != null && (this.mHasCheckMarkTint || this.mHasCheckMarkTintMode)) {
            this.mCheckMarkDrawable = this.mCheckMarkDrawable.mutate();
            if (this.mHasCheckMarkTint) {
                this.mCheckMarkDrawable.setTintList(this.mCheckMarkTintList);
            }

            if (this.mHasCheckMarkTintMode) {
                this.mCheckMarkDrawable.setTintMode(this.mCheckMarkTintMode);
            }

            if (this.mCheckMarkDrawable.isStateful()) {
                this.mCheckMarkDrawable.setState(this.getDrawableState());
            }
        }

    }

    private boolean isCheckMarkAtStart() {
        boolean var1;
        if ((Gravity.getAbsoluteGravity(this.mCheckMarkGravity, this.getLayoutDirection()) & 7) == 3) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
    }

    private void setBasePadding(boolean var1) {
        if (var1) {
            this.mBasePadding = this.getPaddingLeft();
        } else {
            this.mBasePadding = this.getPaddingRight();
        }

    }

    private void setCheckMarkDrawableInternal(Drawable var1, int var2) {
        Drawable var3 = this.mCheckMarkDrawable;
        if (var3 != null) {
            var3.setCallback((Drawable.Callback)null);
            this.unscheduleDrawable(this.mCheckMarkDrawable);
        }

        var3 = this.mCheckMarkDrawable;
        boolean var4 = true;
        boolean var5;
        if (var1 != var3) {
            var5 = true;
        } else {
            var5 = false;
        }

        this.mNeedRequestlayout = var5;
        if (var1 != null) {
            var1.setCallback(this);
            if (this.getVisibility() == VISIBLE) {
                var5 = var4;
            } else {
                var5 = false;
            }

            var1.setVisible(var5, false);
            var1.setState(CHECKED_STATE_SET);
            this.setMinHeight(var1.getIntrinsicHeight());
            this.mCheckMarkWidth = var1.getIntrinsicWidth();
            var1.setState(this.getDrawableState());
        } else {
            this.mCheckMarkWidth = 0;
        }

        this.mCheckMarkDrawable = var1;
        this.mCheckMarkResource = var2;
        this.applyCheckMarkTint();
        Utils.genericInvokeMethod(View.class, this, "resolvePadding");
        this.setBasePadding(this.isCheckMarkAtStart());
    }

    private void updatePadding() {
        Utils.genericInvokeMethod(View.class, this, "resetPaddingToInitialValues");
        int var1;
        if (this.mCheckMarkDrawable != null) {
            var1 = this.mCheckMarkWidth + this.mBasePadding + this.mDrawablePadding;
        } else {
            var1 = this.mBasePadding;
        }

        boolean var2 = this.isCheckMarkAtStart();
        boolean var3 = true;
        boolean var4 = true;
        if (var2) {
            var2 = this.mNeedRequestlayout;
            if ((int) Utils.genericGetField(View.class, this, "mPaddingLeft") == var1) {
                var4 = false;
            }

            this.mNeedRequestlayout = var2 | var4;
            Utils.genericSetField(View.class, this, "mPaddingLeft", var1);
        } else {
            var2 = this.mNeedRequestlayout;
            if ((int) Utils.genericGetField(View.class, this, "mPaddingRight") != var1) {
                var4 = var3;
            } else {
                var4 = false;
            }

            this.mNeedRequestlayout = var2 | var4;
            Utils.genericSetField(View.class, this, "mPaddingRight", var1);
        }

        if (this.mNeedRequestlayout) {
            this.requestLayout();
            this.mNeedRequestlayout = false;
        }

    }

    public void drawableHotspotChanged(float var1, float var2) {
        super.drawableHotspotChanged(var1, var2);
        Drawable var3 = this.mCheckMarkDrawable;
        if (var3 != null) {
            var3.setHotspot(var1, var2);
        }

    }

    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable var1 = this.mCheckMarkDrawable;
        if (var1 != null && var1.isStateful() && var1.setState(this.getDrawableState())) {
            this.invalidateDrawable(var1);
        }

    }

    public CharSequence getAccessibilityClassName() {
        return CheckedTextView.class.getName();
    }

    public Drawable getCheckMarkDrawable() {
        return this.mCheckMarkDrawable;
    }

    public ColorStateList getCheckMarkTintList() {
        return this.mCheckMarkTintList;
    }

    public PorterDuff.Mode getCheckMarkTintMode() {
        return this.mCheckMarkTintMode;
    }

    @SuppressLint("RestrictedApi")
    public void invalidateDrawable(Drawable var1) {
        super.invalidateDrawable(var1);
        if (this.verifyDrawable(var1)) {
            Rect var2 = var1.getBounds();
            if (ViewUtils.isLayoutRtl(this) && (boolean) Utils.genericGetField(TextView.class, this, "mSingleLine")) {
                this.invalidate(var2.left, var2.top, var2.right, var2.bottom);
            }
        }

    }

    @ViewDebug.ExportedProperty
    public boolean isChecked() {
        return this.mChecked;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable var1 = this.mCheckMarkDrawable;
        if (var1 != null) {
            var1.jumpToCurrentState();
        }

    }

    public int[] onCreateDrawableState(int var1) {
        int[] var2 = super.onCreateDrawableState(var1 + 1);
        if (this.isChecked()) {
            TextView.mergeDrawableStates(var2, CHECKED_STATE_SET);
        }

        return var2;
    }

    @SuppressLint("RestrictedApi")
    public void onDraw(Canvas var1) {
        super.onDraw(var1);
        Drawable var2 = this.mCheckMarkDrawable;
        if (var2 != null) {
            int var3 = this.getGravity() & 112;
            int var4 = var2.getIntrinsicHeight();
            int var5 = 0;
            if (var3 != 16) {
                if (var3 == 80) {
                    var5 = this.getHeight() - var4;
                }
            } else {
                var5 = (this.getHeight() - var4) / 2;
            }

            boolean var6 = this.isCheckMarkAtStart();
            var3 = this.getWidth();
            int var7 = var4 + var5;
            if (var6) {
                var4 = this.mBasePadding;
                var3 = this.mCheckMarkWidth + var4;
            } else {
                var3 -= this.mBasePadding;
                var4 = var3 - this.mCheckMarkWidth;
            }

            if (ViewUtils.isLayoutRtl(this)) {
                var2.setBounds(this.getScrollX() + var4, var5, this.getScrollX() + var3, var7);
            } else {
                var2.setBounds(var4, var5, var3, var7);
            }

            var2.draw(var1);
            Drawable var8 = this.getBackground();
            if (var8 != null) {
                var8.setHotspotBounds(this.getScrollX() + var4, var5, this.getScrollX() + var3, var7);
            }
        }

    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
        super.onInitializeAccessibilityEvent(var1);
        var1.setChecked(this.mChecked);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo var1) {
        super.onInitializeAccessibilityNodeInfo(var1);
        var1.setCheckable(true);
        var1.setChecked(this.mChecked);
    }

    public void onRestoreInstanceState(Parcelable var1) {
        SeslCheckedTextView.SavedState var2 = (SeslCheckedTextView.SavedState)var1;
        super.onRestoreInstanceState(var2.getSuperState());
        this.setChecked(var2.checked);
        this.requestLayout();
    }

    public void onRtlPropertiesChanged(int var1) {
        super.onRtlPropertiesChanged(var1);
        this.updatePadding();
    }

    public Parcelable onSaveInstanceState() {
        SeslCheckedTextView.SavedState var1 = new SeslCheckedTextView.SavedState(super.onSaveInstanceState());
        var1.checked = this.isChecked();
        return var1;
    }

    public void setCheckMarkDrawable(int var1) {
        if (var1 == 0 || var1 != this.mCheckMarkResource) {
            Drawable var2;
            if (var1 != 0) {
                var2 = this.getContext().getDrawable(var1);
            } else {
                var2 = null;
            }

            this.setCheckMarkDrawableInternal(var2, var1);
        }
    }

    public void setCheckMarkDrawable(Drawable var1) {
        this.setCheckMarkDrawableInternal(var1, 0);
    }

    public void setCheckMarkTintList(ColorStateList var1) {
        this.mCheckMarkTintList = var1;
        this.mHasCheckMarkTint = true;
        this.applyCheckMarkTint();
    }

    public void setCheckMarkTintMode(PorterDuff.Mode var1) {
        this.mCheckMarkTintMode = var1;
        this.mHasCheckMarkTintMode = true;
        this.applyCheckMarkTint();
    }

    public void setChecked(boolean var1) {
        if (this.mChecked != var1) {
            this.mChecked = var1;
            this.refreshDrawableState();
            Utils.genericInvokeMethod(View.class, this, "notifyViewAccessibilityStateChangedIfNeeded", 0);
        }

    }

    public void setVisibility(int var1) {
        super.setVisibility(var1);
        Drawable var2 = this.mCheckMarkDrawable;
        if (var2 != null) {
            boolean var3;
            if (var1 == 0) {
                var3 = true;
            } else {
                var3 = false;
            }

            var2.setVisible(var3, false);
        }

    }

    public void toggle() {
        this.setChecked(this.mChecked ^ true);
    }

    public boolean verifyDrawable(Drawable var1) {
        boolean var2;
        if (var1 != this.mCheckMarkDrawable && !super.verifyDrawable(var1)) {
            var2 = false;
        } else {
            var2 = true;
        }

        return var2;
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator<SeslCheckedTextView.SavedState> CREATOR = new Creator<SeslCheckedTextView.SavedState>() {
            public SeslCheckedTextView.SavedState createFromParcel(Parcel var1) {
                return new SeslCheckedTextView.SavedState(var1);
            }

            public SeslCheckedTextView.SavedState[] newArray(int var1) {
                return new SeslCheckedTextView.SavedState[var1];
            }
        };
        public boolean checked;

        public SavedState(Parcel var1) {
            super(var1);
            this.checked = (Boolean)var1.readValue((ClassLoader)null);
        }

        public SavedState(Parcelable var1) {
            super(var1);
        }

        public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append("SeslCheckedTextView.SavedState{");
            var1.append(Integer.toHexString(System.identityHashCode(this)));
            var1.append(" checked=");
            var1.append(this.checked);
            var1.append("}");
            return var1.toString();
        }

        public void writeToParcel(Parcel var1, int var2) {
            super.writeToParcel(var1, var2);
            var1.writeValue(this.checked);
        }
    }
}
