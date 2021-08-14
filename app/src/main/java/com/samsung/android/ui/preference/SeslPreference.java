package com.samsung.android.ui.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.AbsSavedState;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.TypedArrayUtils;

import com.mesalabs.ten.update.R;
import com.mesalabs.ten.update.utils.LogUtils;
import com.samsung.android.ui.preference.internal.SeslPreferenceImageView;

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

public class SeslPreference implements Comparable<SeslPreference> {
    public static final int DEFAULT_ORDER = Integer.MAX_VALUE;
    private boolean mAllowDividerAbove;
    private boolean mAllowDividerBelow;
    private boolean mBaseMethodCalled;
    private boolean mChangedSummaryColor;
    private boolean mChangedSummaryColorStateList;
    private final OnClickListener mClickListener;
    private Context mContext;
    private Object mDefaultValue;
    private String mDependencyKey;
    private boolean mDependencyMet;
    private List<SeslPreference> mDependents;
    private boolean mEnabled;
    private Bundle mExtras;
    private String mFragment;
    private boolean mHasId;
    private boolean mHasSingleLineTitleAttr;
    private Drawable mIcon;
    private int mIconResId;
    private boolean mIconSpaceReserved;
    private long mId;
    private Intent mIntent;
    boolean mIsPreferenceRoundedBg;
    boolean mIsRoundChanged;
    public boolean mIsSolidRoundedCorner;
    private String mKey;
    private int mLayoutResId;
    private SeslPreference.OnPreferenceChangeInternalListener mListener;
    private SeslPreference.OnPreferenceChangeListener mOnChangeListener;
    private SeslPreference.OnPreferenceClickListener mOnClickListener;
    private int mOrder;
    private boolean mParentDependencyMet;
    private SeslPreferenceGroup mParentGroup;
    private boolean mPersistent;
    private PreferenceDataStore mPreferenceDataStore;
    private PreferenceManager mPreferenceManager;
    private boolean mSelectable;
    private boolean mShouldDisableView;
    private boolean mSingleLineTitle;
    int mSubheaderColor;
    boolean mSubheaderRound;
    private CharSequence mSummary;
    private int mSummaryColor;
    private ColorStateList mSummaryColorStateList;
    private ColorStateList mTextColorSecondary;
    private CharSequence mTitle;
    private int mViewId;
    private boolean mVisible;
    private boolean mWasDetached;
    int mWhere;
    private int mWidgetLayoutResId;

    public SeslPreference(Context var1, AttributeSet var2) {
        this(var1, var2, TypedArrayUtils.getAttr(var1, R.attr.preferenceStyle, 16842894));
    }

    public SeslPreference(Context var1, AttributeSet var2, int var3) {
        this(var1, var2, var3, 0);
    }

    public SeslPreference(Context var1, AttributeSet var2, int var3, int var4) {
        this.mOrder = 2147483647;
        this.mViewId = 0;
        this.mEnabled = true;
        this.mSelectable = true;
        this.mPersistent = true;
        this.mDependencyMet = true;
        this.mParentDependencyMet = true;
        this.mVisible = true;
        this.mAllowDividerAbove = true;
        this.mAllowDividerBelow = true;
        this.mSingleLineTitle = true;
        this.mIsSolidRoundedCorner = false;
        this.mIsPreferenceRoundedBg = false;
        this.mSubheaderRound = false;
        this.mWhere = 0;
        this.mIsRoundChanged = false;
        this.mChangedSummaryColor = false;
        this.mChangedSummaryColorStateList = false;
        this.mShouldDisableView = true;
        this.mLayoutResId = R.layout.sesl_preference;
        this.mClickListener = new OnClickListener() {
            public void onClick(View var1) {
                SeslPreference.this.performClick(var1);
            }
        };
        this.mContext = var1;
        TypedArray var5 = var1.obtainStyledAttributes(var2, R.styleable.SeslPreference, var3, var4);
        this.mIconResId = TypedArrayUtils.getResourceId(var5, R.styleable.SeslPreference_icon, R.styleable.SeslPreference_android_icon, 0);
        this.mKey = TypedArrayUtils.getString(var5, R.styleable.SeslPreference_key, R.styleable.SeslPreference_android_key);
        this.mTitle = TypedArrayUtils.getText(var5, R.styleable.SeslPreference_title, R.styleable.SeslPreference_android_title);
        this.mSummary = TypedArrayUtils.getText(var5, R.styleable.SeslPreference_summary, R.styleable.SeslPreference_android_summary);
        this.mOrder = TypedArrayUtils.getInt(var5, R.styleable.SeslPreference_order, R.styleable.SeslPreference_android_order, 2147483647);
        this.mFragment = TypedArrayUtils.getString(var5, R.styleable.SeslPreference_fragment, R.styleable.SeslPreference_android_fragment);
        this.mLayoutResId = TypedArrayUtils.getResourceId(var5, R.styleable.SeslPreference_layout, R.styleable.SeslPreference_android_layout, R.layout.preference);
        this.mWidgetLayoutResId = TypedArrayUtils.getResourceId(var5, R.styleable.SeslPreference_widgetLayout, R.styleable.SeslPreference_android_widgetLayout, 0);
        this.mEnabled = TypedArrayUtils.getBoolean(var5, R.styleable.SeslPreference_enabled, R.styleable.SeslPreference_android_enabled, true);
        this.mSelectable = TypedArrayUtils.getBoolean(var5, R.styleable.SeslPreference_selectable, R.styleable.SeslPreference_android_selectable, true);
        this.mPersistent = TypedArrayUtils.getBoolean(var5, R.styleable.SeslPreference_persistent, R.styleable.SeslPreference_android_persistent, true);
        this.mDependencyKey = TypedArrayUtils.getString(var5, R.styleable.SeslPreference_dependency, R.styleable.SeslPreference_android_dependency);
        this.mAllowDividerAbove = TypedArrayUtils.getBoolean(var5, R.styleable.SeslPreference_allowDividerAbove, R.styleable.SeslPreference_allowDividerAbove, this.mSelectable);
        this.mAllowDividerBelow = TypedArrayUtils.getBoolean(var5, R.styleable.SeslPreference_allowDividerBelow, R.styleable.SeslPreference_allowDividerBelow, this.mSelectable);
        if (var5.hasValue(R.styleable.SeslPreference_defaultValue)) {
            this.mDefaultValue = this.onGetDefaultValue(var5, R.styleable.SeslPreference_defaultValue);
        } else if (var5.hasValue(R.styleable.SeslPreference_android_defaultValue)) {
            this.mDefaultValue = this.onGetDefaultValue(var5, R.styleable.SeslPreference_android_defaultValue);
        }

        this.mShouldDisableView = TypedArrayUtils.getBoolean(var5, R.styleable.SeslPreference_shouldDisableView, R.styleable.SeslPreference_android_shouldDisableView, true);
        this.mHasSingleLineTitleAttr = var5.hasValue(R.styleable.SeslPreference_singleLineTitle);
        if (this.mHasSingleLineTitleAttr) {
            this.mSingleLineTitle = TypedArrayUtils.getBoolean(var5, R.styleable.SeslPreference_singleLineTitle, R.styleable.SeslPreference_android_singleLineTitle, true);
        }

        this.mIconSpaceReserved = TypedArrayUtils.getBoolean(var5, R.styleable.SeslPreference_iconSpaceReserved, R.styleable.SeslPreference_android_iconSpaceReserved, false);
        var5.recycle();
        TypedValue var6 = new TypedValue();
        var1.getTheme().resolveAttribute(16842808, var6, true);
        if (var6.resourceId > 0) {
            this.mTextColorSecondary = var1.getResources().getColorStateList(var6.resourceId, null);
        }

    }

    private void dispatchSetInitialValue() {
        if (this.getPreferenceDataStore() != null) {
            this.onSetInitialValue(true, this.mDefaultValue);
        } else if (this.shouldPersist() && this.getSharedPreferences().contains(this.mKey)) {
            this.onSetInitialValue(true, (Object)null);
        } else if (this.mDefaultValue != null) {
            this.onSetInitialValue(false, this.mDefaultValue);
        }

    }

    public boolean persistStringSet(Set<String> var1) {
        if (!this.shouldPersist()) {
            return false;
        } else if (var1.equals(this.getPersistedStringSet((Set<String>)null))) {
            return true;
        } else {
            PreferenceDataStore var2 = this.getPreferenceDataStore();
            if (var2 != null) {
                var2.putStringSet(this.mKey, var1);
            } else {
                Editor var3 = this.mPreferenceManager.getEditor();
                var3.putStringSet(this.mKey, var1);
                this.tryCommit(var3);
            }

            return true;
        }
    }

    private void registerDependency() {
        if (!TextUtils.isEmpty(this.mDependencyKey)) {
            SeslPreference var1 = this.findPreferenceInHierarchy(this.mDependencyKey);
            if (var1 == null) {
                throw new IllegalStateException("Dependency \"" + this.mDependencyKey + "\" not found for preference \"" + this.mKey + "\" (title: \"" + this.mTitle + "\"");
            }

            var1.registerDependent(this);
        }

    }

    private void registerDependent(SeslPreference var1) {
        if (this.mDependents == null) {
            this.mDependents = new ArrayList();
        }

        this.mDependents.add(var1);
        var1.onDependencyChanged(this, this.shouldDisableDependents());
    }

    private void setEnabledStateOnViews(View var1, boolean var2) {
        var1.setEnabled(var2);
        if (var1 instanceof ViewGroup) {
            ViewGroup var4 = (ViewGroup)var1;

            for(int var3 = var4.getChildCount() - 1; var3 >= 0; --var3) {
                this.setEnabledStateOnViews(var4.getChildAt(var3), var2);
            }
        }

    }

    private void tryCommit(Editor var1) {
        if (this.mPreferenceManager.shouldCommit()) {
            var1.apply();
        }

    }

    private void unregisterDependency() {
        if (this.mDependencyKey != null) {
            SeslPreference var1 = this.findPreferenceInHierarchy(this.mDependencyKey);
            if (var1 != null) {
                var1.unregisterDependent(this);
            }
        }

    }

    private void unregisterDependent(SeslPreference var1) {
        if (this.mDependents != null) {
            this.mDependents.remove(var1);
        }

    }

    void assignParent(SeslPreferenceGroup var1) {
        this.mParentGroup = var1;
    }

    public boolean callChangeListener(Object var1) {
        boolean var2;
        if (this.mOnChangeListener != null && !this.mOnChangeListener.onPreferenceChange(this, var1)) {
            var2 = false;
        } else {
            var2 = true;
        }

        return var2;
    }

    protected void callClickListener() {
        if (this.mOnClickListener != null) {
            this.mOnClickListener.onPreferenceClick(this);
        }
    }

    public final void clearWasDetached() {
        this.mWasDetached = false;
    }

    public int compareTo(SeslPreference var1) {
        int var2;
        if (this.mOrder != var1.mOrder) {
            var2 = this.mOrder - var1.mOrder;
        } else if (this.mTitle == var1.mTitle) {
            var2 = 0;
        } else if (this.mTitle == null) {
            var2 = 1;
        } else if (var1.mTitle == null) {
            var2 = -1;
        } else {
            var2 = this.mTitle.toString().compareToIgnoreCase(var1.mTitle.toString());
        }

        return var2;
    }

    void dispatchRestoreInstanceState(Bundle var1) {
        if (this.hasKey()) {
            Parcelable var2 = var1.getParcelable(this.mKey);
            if (var2 != null) {
                this.mBaseMethodCalled = false;
                this.onRestoreInstanceState(var2);
                if (!this.mBaseMethodCalled) {
                    throw new IllegalStateException("Derived class did not call super.onRestoreInstanceState()");
                }
            }
        }

    }

    void dispatchSaveInstanceState(Bundle var1) {
        if (this.hasKey()) {
            this.mBaseMethodCalled = false;
            Parcelable var2 = this.onSaveInstanceState();
            if (!this.mBaseMethodCalled) {
                throw new IllegalStateException("Derived class did not call super.onSaveInstanceState()");
            }

            if (var2 != null) {
                var1.putParcelable(this.mKey, var2);
            }
        }

    }

    protected SeslPreference findPreferenceInHierarchy(String var1) {
        SeslPreference var2;
        if (!TextUtils.isEmpty(var1) && this.mPreferenceManager != null) {
            var2 = this.mPreferenceManager.findPreference(var1);
        } else {
            var2 = null;
        }

        return var2;
    }

    public Context getContext() {
        return this.mContext;
    }

    public Bundle getExtras() {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }

        return this.mExtras;
    }

    StringBuilder getFilterableStringBuilder() {
        StringBuilder var1 = new StringBuilder();
        CharSequence var2 = this.getTitle();
        if (!TextUtils.isEmpty(var2)) {
            var1.append(var2).append(' ');
        }

        var2 = this.getSummary();
        if (!TextUtils.isEmpty(var2)) {
            var1.append(var2).append(' ');
        }

        if (var1.length() > 0) {
            var1.setLength(var1.length() - 1);
        }

        return var1;
    }

    public String getFragment() {
        return this.mFragment;
    }

    long getId() {
        return this.mId;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public String getKey() {
        return this.mKey;
    }

    public final int getLayoutResource() {
        return this.mLayoutResId;
    }

    public int getOrder() {
        return this.mOrder;
    }

    public SeslPreferenceGroup getParent() {
        return this.mParentGroup;
    }

    protected boolean getPersistedBoolean(boolean var1) {
        if (this.shouldPersist()) {
            PreferenceDataStore var2 = this.getPreferenceDataStore();
            if (var2 != null) {
                var1 = var2.getBoolean(this.mKey, var1);
            } else {
                var1 = this.mPreferenceManager.getSharedPreferences().getBoolean(this.mKey, var1);
            }
        }

        return var1;
    }

    protected int getPersistedInt(int var1) {
        if (this.shouldPersist()) {
            PreferenceDataStore var2 = this.getPreferenceDataStore();
            if (var2 != null) {
                var1 = var2.getInt(this.mKey, var1);
            } else {
                var1 = this.mPreferenceManager.getSharedPreferences().getInt(this.mKey, var1);
            }
        }

        return var1;
    }

    protected String getPersistedString(String var1) {
        if (this.shouldPersist()) {
            PreferenceDataStore var2 = this.getPreferenceDataStore();
            if (var2 != null) {
                var1 = var2.getString(this.mKey, var1);
            } else {
                var1 = this.mPreferenceManager.getSharedPreferences().getString(this.mKey, var1);
            }
        }

        return var1;
    }

    public Set<String> getPersistedStringSet(Set<String> var1) {
        if (!this.shouldPersist()) {
            return var1;
        } else {
            PreferenceDataStore var2 = this.getPreferenceDataStore();
            return var2 != null ? var2.getStringSet(this.mKey, var1) : this.mPreferenceManager.getSharedPreferences().getStringSet(this.mKey, var1);
        }
    }

    public PreferenceDataStore getPreferenceDataStore() {
        PreferenceDataStore var1;
        if (this.mPreferenceDataStore != null) {
            var1 = this.mPreferenceDataStore;
        } else if (this.mPreferenceManager != null) {
            var1 = this.mPreferenceManager.getPreferenceDataStore();
        } else {
            var1 = null;
        }

        return var1;
    }

    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    public SharedPreferences getSharedPreferences() {
        SharedPreferences var1;
        if (this.mPreferenceManager != null && this.getPreferenceDataStore() == null) {
            var1 = this.mPreferenceManager.getSharedPreferences();
        } else {
            var1 = null;
        }

        return var1;
    }

    public CharSequence getSummary() {
        return this.mSummary;
    }

    public void setSummary(CharSequence summary) {
        if (!TextUtils.equals(mSummary, summary)) {
            mSummary = summary;
            notifyChanged();
        }
    }


    public void setSummary(int summaryResId) {
        setSummary(mContext.getString(summaryResId));
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public final int getWidgetLayoutResource() {
        return this.mWidgetLayoutResId;
    }

    public boolean hasKey() {
        boolean var1;
        if (!TextUtils.isEmpty(this.mKey)) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
    }

    public void seslSetSummaryColor(int color) {
        mSummaryColor = color;
        mChangedSummaryColor = true;
        mChangedSummaryColorStateList = false;
    }

    public void seslSetSummaryColor(ColorStateList colorStateList) {
        mSummaryColorStateList = colorStateList;
        mChangedSummaryColorStateList = true;
        mChangedSummaryColor = false;
    }

    public void seslSetRoundedBg(int where) {
        mIsPreferenceRoundedBg = true;
        mWhere = where;
        mSubheaderRound = false;
        mIsRoundChanged = true;
    }

    public void setEnabled(boolean enabled) {
        if (mEnabled != enabled) {
            mEnabled = enabled;

            notifyDependencyChange(shouldDisableDependents());

            notifyChanged();
        }
    }

    public boolean isEnabled() {
        boolean var1;
        if (this.mEnabled && this.mDependencyMet && this.mParentDependencyMet) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
    }

    public boolean isPersistent() {
        return this.mPersistent;
    }

    public void setSelectable(boolean selectable) {
        if (mSelectable != selectable) {
            mSelectable = selectable;
            notifyChanged();
        }
    }

    public boolean isSelectable() {
        return this.mSelectable;
    }

    public void setShouldDisableView(boolean shouldDisableView) {
        this.mShouldDisableView = shouldDisableView;
        notifyChanged();
    }

    protected boolean isTalkBackIsRunning() {
        AccessibilityManager var1 = (AccessibilityManager)this.getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        boolean var2;
        if (var1 != null && var1.isEnabled()) {
            String var3 = Secure.getString(this.getContext().getContentResolver(), "enabled_accessibility_services");
            if (var3 != null && (var3.matches("(?i).*com.samsung.accessibility/com.samsung.android.app.talkback.TalkBackService.*") || var3.matches("(?i).*com.google.android.marvin.talkback.TalkBackService.*") || var3.matches("(?i).*com.samsung.accessibility/com.samsung.accessibility.universalswitch.UniversalSwitchService.*"))) {
                var2 = true;
                return var2;
            }
        }

        var2 = false;
        return var2;
    }

    public final boolean isVisible() {
        return this.mVisible;
    }

    protected void notifyChanged() {
        if (this.mListener != null) {
            this.mListener.onPreferenceChange(this);
        }

    }

    public void notifyDependencyChange(boolean var1) {
        List var2 = this.mDependents;
        if (var2 != null) {
            int var3 = var2.size();

            for(int var4 = 0; var4 < var3; ++var4) {
                ((SeslPreference)var2.get(var4)).onDependencyChanged(this, var1);
            }
        }

    }

    protected void notifyHierarchyChanged() {
        if (this.mListener != null) {
            this.mListener.onPreferenceHierarchyChange(this);
        }

    }

    public void onAttached() {
        this.registerDependency();
    }

    protected void onAttachedToHierarchy(PreferenceManager var1) {
        this.mPreferenceManager = var1;
        if (!this.mHasId) {
            this.mId = var1.getNextId();
        }

        this.dispatchSetInitialValue();
    }

    protected void onAttachedToHierarchy(PreferenceManager var1, long var2) {
        this.mId = var2;
        this.mHasId = true;

        try {
            this.onAttachedToHierarchy(var1);
        } finally {
            this.mHasId = false;
        }

    }

    @SuppressLint("WrongConstant")
    public void onBindViewHolder(PreferenceViewHolder var1) {
        byte var2 = 4;
        var1.itemView.setOnClickListener(this.mClickListener);
        var1.itemView.setId(this.mViewId);
        var1.seslSetPreferenceBackgroundType(this.mIsPreferenceRoundedBg, this.mWhere, this.mSubheaderRound);
        TextView var3 = (TextView)var1.findViewById(16908310);
        if (var3 != null) {
            CharSequence var4 = this.getTitle();
            if (!TextUtils.isEmpty(var4)) {
                var3.setText(var4);
                var3.setVisibility(0);
                if (this.mHasSingleLineTitleAttr) {
                    var3.setSingleLine(this.mSingleLineTitle);
                }
            } else if (TextUtils.isEmpty(var4) && this instanceof PreferenceCategory) {
                var3.setVisibility(0);
                if (this.mHasSingleLineTitleAttr) {
                    var3.setSingleLine(this.mSingleLineTitle);
                }
            } else {
                var3.setVisibility(8);
            }
        }

        TextView var9 = (TextView)var1.findViewById(16908304);
        if (var9 != null) {
            CharSequence var7 = this.getSummary();
            if (!TextUtils.isEmpty(var7)) {
                var9.setText(var7);
                if (this.mChangedSummaryColor) {
                    var9.setTextColor(this.mSummaryColor);
                    LogUtils.d("SeslPreference", "set Summary Color : " + this.mSummaryColor);
                } else if (this.mChangedSummaryColorStateList) {
                    var9.setTextColor(this.mSummaryColorStateList);
                    LogUtils.d("SeslPreference", "set Summary ColorStateList : " + this.mSummaryColorStateList);
                } else if (this.mTextColorSecondary != null) {
                    var9.setTextColor(this.mTextColorSecondary);
                }

                var9.setVisibility(0);
            } else {
                var9.setVisibility(8);
            }
        }

        SeslPreferenceImageView imageView = (SeslPreferenceImageView) var1.findViewById(16908294);
        byte var6;
        if (imageView != null) {
            if (!(this.mIconResId == 0 && this.mIcon == null)) {
                if (this.mIcon == null) {
                    this.mIcon = AppCompatResources.getDrawable(this.mContext, this.mIconResId);
                }
                Drawable drawable = this.mIcon;
                if (drawable != null) {
                    imageView.setImageDrawable(drawable);
                }
            }
            if (this.mIcon != null) {
                imageView.setVisibility(0);
            } else {
                if (this.mIconSpaceReserved) {
                    var6 = 4;
                } else {
                    var6 = 8;
                }

                imageView.setVisibility(var6);
            }
        }

        View var8 = var1.findViewById(R.id.icon_frame);
        View var11 = var8;
        if (var8 == null) {
            var11 = var1.findViewById(16908350);
        }

        if (var11 != null) {
            if (this.mIcon != null) {
                var11.setVisibility(0);
            } else {
                if (this.mIconSpaceReserved) {
                    var6 = var2;
                } else {
                    var6 = 8;
                }

                var11.setVisibility(var6);
            }
        }

        if (this.mShouldDisableView) {
            this.setEnabledStateOnViews(var1.itemView, this.isEnabled());
        } else {
            this.setEnabledStateOnViews(var1.itemView, true);
        }

        boolean var5 = this.isSelectable();
        var1.itemView.setFocusable(var5);
        var1.itemView.setClickable(var5);
        var1.setDividerAllowedAbove(this.mAllowDividerAbove);
        var1.setDividerAllowedBelow(this.mAllowDividerBelow);
    }

    protected void onClick() {
    }

    public void onDependencyChanged(SeslPreference var1, boolean var2) {
        if (this.mDependencyMet == var2) {
            if (!var2) {
                var2 = true;
            } else {
                var2 = false;
            }

            this.mDependencyMet = var2;
            this.notifyDependencyChange(this.shouldDisableDependents());
            this.notifyChanged();
        }

    }

    public void onDetached() {
        this.unregisterDependency();
        this.mWasDetached = true;
    }

    protected Object onGetDefaultValue(TypedArray var1, int var2) {
        return null;
    }

    public void onParentChanged(SeslPreference var1, boolean var2) {
        if (this.mParentDependencyMet == var2) {
            if (!var2) {
                var2 = true;
            } else {
                var2 = false;
            }

            this.mParentDependencyMet = var2;
            this.notifyDependencyChange(this.shouldDisableDependents());
            this.notifyChanged();
        }

    }

    protected void onPrepareForRemoval() {
        this.unregisterDependency();
    }

    protected void onRestoreInstanceState(Parcelable var1) {
        this.mBaseMethodCalled = true;
        if (var1 != SeslPreference.BaseSavedState.EMPTY_STATE && var1 != null) {
            throw new IllegalArgumentException("Wrong state class -- expecting SeslPreference State");
        }
    }

    protected Parcelable onSaveInstanceState() {
        this.mBaseMethodCalled = true;
        return SeslPreference.BaseSavedState.EMPTY_STATE;
    }

    protected void onSetInitialValue(boolean var1, Object var2) {
    }

    public void performClick() {
        if (this.isEnabled()) {
            this.onClick();
            if (this.mOnClickListener == null || !this.mOnClickListener.onPreferenceClick(this)) {
                PreferenceManager var1 = this.getPreferenceManager();
                if (var1 != null) {
                    PreferenceManager.OnPreferenceTreeClickListener var2 = var1.getOnPreferenceTreeClickListener();
                    if (var2 != null && var2.onPreferenceTreeClick(this)) {
                        return;
                    }
                }

                if (this.mIntent != null) {
                    this.getContext().startActivity(this.mIntent);
                }
            }
        }

    }

    protected void performClick(View var1) {
        this.performClick();
    }

    protected boolean persistBoolean(boolean var1) {
        boolean var2 = false;
        boolean var3 = true;
        boolean var4;
        if (!this.shouldPersist()) {
            var4 = false;
        } else {
            if (!var1) {
                var2 = true;
            }

            var4 = var3;
            if (var1 != this.getPersistedBoolean(var2)) {
                PreferenceDataStore var5 = this.getPreferenceDataStore();
                if (var5 != null) {
                    var5.putBoolean(this.mKey, var1);
                    var4 = var3;
                } else {
                    Editor var6 = this.mPreferenceManager.getEditor();
                    var6.putBoolean(this.mKey, var1);
                    this.tryCommit(var6);
                    var4 = var3;
                }
            }
        }

        return var4;
    }

    protected boolean persistInt(int var1) {
        boolean var2 = true;
        boolean var3;
        if (!this.shouldPersist()) {
            var3 = false;
        } else {
            var3 = var2;
            if (var1 != this.getPersistedInt(~var1)) {
                PreferenceDataStore var4 = this.getPreferenceDataStore();
                if (var4 != null) {
                    var4.putInt(this.mKey, var1);
                    var3 = var2;
                } else {
                    Editor var5 = this.mPreferenceManager.getEditor();
                    var5.putInt(this.mKey, var1);
                    this.tryCommit(var5);
                    var3 = var2;
                }
            }
        }

        return var3;
    }

    protected boolean persistString(String var1) {
        boolean var2 = true;
        boolean var3;
        if (!this.shouldPersist()) {
            var3 = false;
        } else {
            var3 = var2;
            if (!TextUtils.equals(var1, this.getPersistedString((String)null))) {
                PreferenceDataStore var4 = this.getPreferenceDataStore();
                if (var4 != null) {
                    var4.putString(this.mKey, var1);
                    var3 = var2;
                } else {
                    Editor var5 = this.mPreferenceManager.getEditor();
                    var5.putString(this.mKey, var1);
                    this.tryCommit(var5);
                    var3 = var2;
                }
            }
        }

        return var3;
    }

    public void restoreHierarchyState(Bundle var1) {
        this.dispatchRestoreInstanceState(var1);
    }

    public void saveHierarchyState(Bundle var1) {
        this.dispatchSaveInstanceState(var1);
    }

    public void seslSetSubheaderColor(int var1) {
        this.mSubheaderColor = var1;
    }

    public void seslSetSubheaderRoundedBg(int var1) {
        this.mIsPreferenceRoundedBg = true;
        this.mWhere = var1;
        this.mSubheaderRound = true;
        this.mIsRoundChanged = true;
    }

    public void setIntent(Intent var1) {
        this.mIntent = var1;
    }

    public void setLayoutResource(int var1) {
        this.mLayoutResId = var1;
    }

    final void setOnPreferenceChangeInternalListener(SeslPreference.OnPreferenceChangeInternalListener var1) {
        this.mListener = var1;
    }

    public void setOnPreferenceChangeListener(OnPreferenceChangeListener var1) {
        this.mOnChangeListener = var1;
    }

    public OnPreferenceChangeListener getOnPreferenceChangeListener() {
        return this.mOnChangeListener;
    }

    public void setOnPreferenceClickListener(SeslPreference.OnPreferenceClickListener var1) {
        this.mOnClickListener = var1;
    }

    public OnPreferenceClickListener getOnPreferenceClickListener() {
        return this.mOnClickListener;
    }

    public void setOrder(int var1) {
        if (var1 != this.mOrder) {
            this.mOrder = var1;
            this.notifyHierarchyChanged();
        }

    }

    public void setWidgetLayoutResource(int var1) {
        this.mWidgetLayoutResId = var1;
    }

    public boolean shouldDisableDependents() {
        boolean var1;
        if (!this.isEnabled()) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
    }

    protected boolean shouldPersist() {
        boolean var1;
        if (this.mPreferenceManager != null && this.isPersistent() && this.hasKey()) {
            var1 = true;
        } else {
            var1 = false;
        }

        return var1;
    }

    public String toString() {
        return this.getFilterableStringBuilder().toString();
    }

    public static class BaseSavedState extends AbsSavedState {
        public static final Creator<SeslPreference.BaseSavedState> CREATOR = new Creator<SeslPreference.BaseSavedState>() {
            public SeslPreference.BaseSavedState createFromParcel(Parcel var1) {
                return new SeslPreference.BaseSavedState(var1);
            }

            public SeslPreference.BaseSavedState[] newArray(int var1) {
                return new SeslPreference.BaseSavedState[var1];
            }
        };

        public BaseSavedState(Parcel var1) {
            super(var1);
        }

        public BaseSavedState(Parcelable var1) {
            super(var1);
        }
    }

    interface OnPreferenceChangeInternalListener {
        void onPreferenceChange(SeslPreference var1);

        void onPreferenceHierarchyChange(SeslPreference var1);
    }

    public interface OnPreferenceChangeListener {
        boolean onPreferenceChange(SeslPreference var1, Object var2);
    }

    public interface OnPreferenceClickListener {
        boolean onPreferenceClick(SeslPreference var1);
    }
}

