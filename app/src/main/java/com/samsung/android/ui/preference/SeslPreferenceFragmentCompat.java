package com.samsung.android.ui.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.mesalabs.on.update.R;
import com.mesalabs.on.update.utils.LogUtils;
import com.samsung.android.ui.preference.internal.AbstractMultiSelectListPreference;
import com.samsung.android.ui.util.SeslRoundedCorner;
import com.samsung.android.ui.util.SeslSubheaderRoundedCorner;
import com.samsung.android.ui.recyclerview.widget.SeslLinearLayoutManager;
import com.samsung.android.ui.recyclerview.widget.SeslRecyclerView;

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

public abstract class SeslPreferenceFragmentCompat extends Fragment implements SeslDialogPreference.TargetFragment, PreferenceManager.OnDisplayPreferenceDialogListener, PreferenceManager.OnNavigateToScreenListener, PreferenceManager.OnPreferenceTreeClickListener {
    public static final int SESL_ROUNDED_CORNER_TYPE_NONE = 0;
    public static final int SESL_ROUNDED_CORNER_TYPE_SOLID = 1;
    public static final int SESL_ROUNDED_CORNER_TYPE_STROKE = 2;
    private final SeslPreferenceFragmentCompat.DividerDecoration mDividerDecoration;
    private Handler mHandler;
    private boolean mHavePrefs;
    private boolean mInitDone;
    private int mLayoutResId;
    private SeslRecyclerView mList;
    private PreferenceManager mPreferenceManager;
    private final Runnable mRequestFocus;
    private int mRoundedCornerType = 2;
    private Runnable mSelectPreferenceRunnable;
    private SeslRoundedCorner mSeslListRoundedCorner;
    private SeslRoundedCorner mSeslRoundedCorner;
    private SeslRoundedCorner mSeslStrokeListRoundedCorner;
    private SeslSubheaderRoundedCorner mSeslSubheaderRoundedCorner;
    private Context mStyledContext;
    private int mSubheaderColor;

    public SeslPreferenceFragmentCompat() {
        this.mLayoutResId = R.layout.preference_list_fragment;
        this.mDividerDecoration = new SeslPreferenceFragmentCompat.DividerDecoration();
        this.mHandler = new Handler() {
            public void handleMessage(Message var1) {
                switch(var1.what) {
                    case 1:
                        SeslPreferenceFragmentCompat.this.bindPreferences();
                    default:
                }
            }
        };
        this.mRequestFocus = new Runnable() {
            public void run() {
                SeslPreferenceFragmentCompat.this.mList.focusableViewAvailable(SeslPreferenceFragmentCompat.this.mList);
            }
        };
    }

    private void bindPreferences() {
        PreferenceScreen var1 = this.getPreferenceScreen();
        if (var1 != null) {
            if (this.mRoundedCornerType == 1) {
                var1.seslSetSubheaderColor(this.mSubheaderColor);
                var1.mIsSolidRoundedCorner = true;
            }

            this.getListView().setAdapter(this.onCreateAdapter(var1));
            var1.onAttached();
        }

        this.onBindPreferences();
    }

    private void postBindPreferences() {
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }

    }

    private void requirePreferenceManager() {
        if (this.mPreferenceManager == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }

    private void unbindPreferences() {
        PreferenceScreen var1 = this.getPreferenceScreen();
        if (var1 != null) {
            var1.onDetached();
        }

        this.onUnbindPreferences();
    }

    public SeslPreference findPreference(CharSequence var1) {
        SeslPreference var2;
        if (this.mPreferenceManager == null) {
            var2 = null;
        } else {
            var2 = this.mPreferenceManager.findPreference(var1);
        }

        return var2;
    }

    public Fragment getCallbackFragment() {
        return null;
    }

    public final SeslRecyclerView getListView() {
        return this.mList;
    }

    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceManager.getPreferenceScreen();
    }

    public void onActivityCreated(Bundle var1) {
        super.onActivityCreated(var1);
        if (var1 != null) {
            Bundle var2 = var1.getBundle("android:preferences");
            if (var2 != null) {
                PreferenceScreen var3 = this.getPreferenceScreen();
                if (var3 != null) {
                    var3.restoreHierarchyState(var2);
                }
            }
        }

    }

    protected void onBindPreferences() {
    }

    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        TypedValue var2 = new TypedValue();
        this.getActivity().getTheme().resolveAttribute(R.attr.preferenceTheme, var2, true);
        int var3 = var2.resourceId;
        if (var3 == 0) {
            throw new IllegalStateException("Must specify preferenceTheme in theme");
        } else {
            this.mStyledContext = new ContextThemeWrapper(this.getActivity(), var3);
            this.mPreferenceManager = new PreferenceManager(this.mStyledContext);
            this.mPreferenceManager.setOnNavigateToScreenListener(this);
            String var4;
            if (this.getArguments() != null) {
                var4 = this.getArguments().getString("com.samsung.android.ui.preference.SeslPreferenceFragmentCompat.PREFERENCE_ROOT");
            } else {
                var4 = null;
            }

            this.onCreatePreferences(var1, var4);
        }
    }

    protected SeslRecyclerView.Adapter onCreateAdapter(PreferenceScreen var1) {
        return new PreferenceGroupAdapter(var1);
    }

    public SeslRecyclerView.LayoutManager onCreateLayoutManager() {
        return new SeslLinearLayoutManager(this.getActivity());
    }

    public abstract void onCreatePreferences(Bundle var1, String var2);

    public SeslRecyclerView onCreateRecyclerView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
        SeslRecyclerView var4 = (SeslRecyclerView)var1.inflate(R.layout.sesl_preference_recyclerview, var2, false);
        var4.setLayoutManager(this.onCreateLayoutManager());
        var4.setAccessibilityDelegateCompat(new PreferenceRecyclerViewAccessibilityDelegate(var4));
        return var4;
    }

    public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
        TypedArray var4 = this.mStyledContext.obtainStyledAttributes((AttributeSet)null, R.styleable.SeslPreferenceFragmentCompat, R.attr.preferenceFragmentCompatStyle, 0);
        this.mLayoutResId = var4.getResourceId(R.styleable.SeslPreferenceFragmentCompat_android_layout, this.mLayoutResId);
        Drawable var5 = var4.getDrawable(R.styleable.SeslPreferenceFragmentCompat_android_divider);
        int var6 = var4.getDimensionPixelSize(R.styleable.SeslPreferenceFragmentCompat_android_dividerHeight, -1);
        boolean var7 = var4.getBoolean(R.styleable.SeslPreferenceFragmentCompat_allowDividerAfterLastItem, true);
        var4.recycle();
        Resources var13 = this.getActivity().getResources();
        @SuppressLint("ResourceType") TypedArray var8 = this.mStyledContext.obtainStyledAttributes((AttributeSet)null, R.styleable.View, 16843272, 0);
        Drawable var9 = var8.getDrawable(R.styleable.View_android_background);
        if (var9 instanceof ColorDrawable) {
            this.mSubheaderColor = ((ColorDrawable)var9).getColor();
        }

        LogUtils.d("SeslPreferenceFragmentC", " sub header color = " + this.mSubheaderColor);
        var8.recycle();
        TypedValue var16 = new TypedValue();
        this.getActivity().getTheme().resolveAttribute(R.attr.preferenceTheme, var16, true);
        int var10 = var16.resourceId;
        ContextThemeWrapper var17 = new ContextThemeWrapper(var1.getContext(), var10);
        var1 = var1.cloneInContext(var17);
        View var12 = var1.inflate(this.mLayoutResId, var2, false);
        @SuppressLint("ResourceType") View var14 = var12.findViewById(16908351);
        if (!(var14 instanceof ViewGroup)) {
            throw new RuntimeException("Content has view with id attribute 'android.R.id.list_container' that is not a ViewGroup class");
        } else {
            ViewGroup var15 = (ViewGroup)var14;
            SeslRecyclerView var11 = this.onCreateRecyclerView(var1, var15, var3);
            if (var11 == null) {
                throw new RuntimeException("Could not create RecyclerView");
            } else {
                this.mList = var11;
                var11.addItemDecoration(this.mDividerDecoration);
                this.setDivider(var5);
                if (var6 != -1) {
                    this.setDividerHeight(var6);
                }

                this.mDividerDecoration.setAllowDividerAfterLastItem(var7);
                this.mList.setItemAnimator((SeslRecyclerView.ItemAnimator)null);
                if (this.mRoundedCornerType == 1) {
                    this.mSeslSubheaderRoundedCorner = new SeslSubheaderRoundedCorner(this.getContext(), false);
                    this.mSeslRoundedCorner = new SeslRoundedCorner(var17, false);
                    this.mSubheaderColor = var13.getColor(R.color.sesl_round_and_bgcolor_dark, null);
                    this.mSeslSubheaderRoundedCorner.setRoundedCornerColor(15, this.mSubheaderColor);
                    this.mSeslRoundedCorner.setRoundedCornerColor(15, this.mSubheaderColor);
                    var11.seslSetOutlineStrokeEnabled(false);
                } else if (this.mRoundedCornerType == 2) {
                    this.mSeslRoundedCorner = new SeslRoundedCorner(var17);
                    this.mSeslSubheaderRoundedCorner = new SeslSubheaderRoundedCorner(var17);
                }

                if (this.mRoundedCornerType != 0) {
                    var11.seslSetFillBottomEnabled(true);
                    var11.seslSetFillBottomColor(this.mSubheaderColor);
                    var11.seslSetLastItemOutlineStrokeEnabled(true);
                    this.mSeslListRoundedCorner = new SeslRoundedCorner(var17, false, true);
                    this.mSeslListRoundedCorner.setRoundedCornerColor(15, this.mSubheaderColor);
                    this.mSeslListRoundedCorner.setRoundedCorners(3);
                    this.mSeslStrokeListRoundedCorner = new SeslRoundedCorner(var17);
                    this.mSeslStrokeListRoundedCorner.setRoundedCorners(3);
                }

                var15.addView(this.mList);
                this.mHandler.post(this.mRequestFocus);
                return var12;
            }
        }
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mHandler.removeMessages(1);
        if (this.mHavePrefs) {
            this.unbindPreferences();
        }

        this.mList = null;
        super.onDestroyView();
    }

    public void onDisplayPreferenceDialog(SeslPreference var1) {
        boolean var2 = false;
        if (this.getCallbackFragment() instanceof SeslPreferenceFragmentCompat.OnPreferenceDisplayDialogCallback) {
            var2 = ((SeslPreferenceFragmentCompat.OnPreferenceDisplayDialogCallback)this.getCallbackFragment()).onPreferenceDisplayDialog(this, var1);
        }

        boolean var3 = var2;
        if (!var2) {
            var3 = var2;
            if (this.getActivity() instanceof SeslPreferenceFragmentCompat.OnPreferenceDisplayDialogCallback) {
                var3 = ((SeslPreferenceFragmentCompat.OnPreferenceDisplayDialogCallback)this.getActivity()).onPreferenceDisplayDialog(this, var1);
            }
        }

        if (!var3 && this.getFragmentManager().findFragmentByTag("com.samsung.android.ui.preference.PreferenceFragment.DIALOG") == null) {
            Object var4;
            if (var1 instanceof EditTextPreference) {
                var4 = EditTextPreferenceDialogFragmentCompat.newInstance(var1.getKey());
            } else if (var1 instanceof SeslListPreference) {
                var4 = ListPreferenceDialogFragmentCompat.newInstance(var1.getKey());
            } else {
                if (!(var1 instanceof SeslMultiSelectListPreference)) {
                    throw new IllegalArgumentException("Tried to display dialog for unknown preference type. Did you forget to override onDisplayPreferenceDialog()?");
                }

                var4 = MultiSelectListPreferenceDialogFragmentCompat.newInstance(var1.getKey());
            }

            ((DialogFragment)var4).setTargetFragment(this, 0);
            ((DialogFragment)var4).show(this.getFragmentManager(), "com.samsung.android.ui.preference.PreferenceFragment.DIALOG");
        }

    }

    public void onNavigateToScreen(PreferenceScreen var1) {
        boolean var2 = false;
        if (this.getCallbackFragment() instanceof SeslPreferenceFragmentCompat.OnPreferenceStartScreenCallback) {
            var2 = ((SeslPreferenceFragmentCompat.OnPreferenceStartScreenCallback)this.getCallbackFragment()).onPreferenceStartScreen(this, var1);
        }

        if (!var2 && this.getActivity() instanceof SeslPreferenceFragmentCompat.OnPreferenceStartScreenCallback) {
            ((SeslPreferenceFragmentCompat.OnPreferenceStartScreenCallback)this.getActivity()).onPreferenceStartScreen(this, var1);
        }

    }

    public boolean onPreferenceTreeClick(SeslPreference var1) {
        boolean var3;
        if (var1.getFragment() != null) {
            boolean var2 = false;
            if (this.getCallbackFragment() instanceof SeslPreferenceFragmentCompat.OnPreferenceStartFragmentCallback) {
                var2 = ((SeslPreferenceFragmentCompat.OnPreferenceStartFragmentCallback)this.getCallbackFragment()).onPreferenceStartFragment(this, var1);
            }

            var3 = var2;
            if (!var2) {
                var3 = var2;
                if (this.getActivity() instanceof SeslPreferenceFragmentCompat.OnPreferenceStartFragmentCallback) {
                    var3 = ((SeslPreferenceFragmentCompat.OnPreferenceStartFragmentCallback)this.getActivity()).onPreferenceStartFragment(this, var1);
                }
            }
        } else {
            var3 = false;
        }

        return var3;
    }

    public void onSaveInstanceState(Bundle var1) {
        super.onSaveInstanceState(var1);
        PreferenceScreen var2 = this.getPreferenceScreen();
        if (var2 != null) {
            Bundle var3 = new Bundle();
            var2.saveHierarchyState(var3);
            var1.putBundle("android:preferences", var3);
        }

    }

    public void onStart() {
        super.onStart();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(this);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(this);
    }

    public void onStop() {
        super.onStop();
        this.mPreferenceManager.setOnPreferenceTreeClickListener((PreferenceManager.OnPreferenceTreeClickListener)null);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener((PreferenceManager.OnDisplayPreferenceDialogListener)null);
    }

    protected void onUnbindPreferences() {
    }

    public void onViewCreated(View var1, Bundle var2) {
        super.onViewCreated(var1, var2);
        if (this.mHavePrefs) {
            this.bindPreferences();
            if (this.mSelectPreferenceRunnable != null) {
                this.mSelectPreferenceRunnable.run();
                this.mSelectPreferenceRunnable = null;
            }
        }

        this.mInitDone = true;
    }

    public void setDivider(Drawable var1) {
        this.mDividerDecoration.setDivider(var1);
    }

    public void setDividerHeight(int var1) {
        this.mDividerDecoration.setDividerHeight(var1);
    }

    public void setPreferenceScreen(PreferenceScreen var1) {
        if (this.mPreferenceManager.setPreferences(var1) && var1 != null) {
            this.onUnbindPreferences();
            this.mHavePrefs = true;
            if (this.mInitDone) {
                this.postBindPreferences();
            }
        }

    }

    public void addPreferencesFromResource(int preferencesResId) {
        requirePreferenceManager();
        setPreferenceScreen(mPreferenceManager.inflateFromResource(mStyledContext, preferencesResId, getPreferenceScreen()));
    }

    public void setPreferencesFromResource(int var1, String var2) {
        this.requirePreferenceManager();
        Object var3 = this.mPreferenceManager.inflateFromResource(this.mStyledContext, var1, (PreferenceScreen)null);
        if (var2 != null) {
            SeslPreference var4 = ((PreferenceScreen)var3).findPreference(var2);
            var3 = var4;
            if (!(var4 instanceof PreferenceScreen)) {
                throw new IllegalArgumentException("Preference object with key " + var2 + " is not a PreferenceScreen");
            }
        }

        this.setPreferenceScreen((PreferenceScreen)var3);
    }

    public void seslSetRoundedCornerType(int type) {
        mRoundedCornerType = type;
    }

    private class DividerDecoration extends SeslRecyclerView.ItemDecoration {
        private boolean mAllowDividerAfterLastItem;
        private Drawable mDivider;
        private int mDividerHeight;

        private DividerDecoration() {
            this.mAllowDividerAfterLastItem = true;
        }

        private boolean canScrollUp(SeslRecyclerView var1) {
            boolean var2 = false;
            SeslRecyclerView.LayoutManager var3 = var1.getLayoutManager();
            if (var3 instanceof SeslLinearLayoutManager) {
                boolean var4;
                if (((SeslLinearLayoutManager)var3).findFirstVisibleItemPosition() > 0) {
                    var4 = true;
                } else {
                    var4 = false;
                }

                var2 = var4;
                if (!var4) {
                    View var5 = var1.getChildAt(0);
                    var2 = var4;
                    if (var5 != null) {
                        if (var5.getTop() < var1.getPaddingTop()) {
                            var2 = true;
                        } else {
                            var2 = false;
                        }
                    }
                }
            }

            return var2;
        }

        private boolean shouldDrawDividerBelow(View var1, SeslRecyclerView var2) {
            boolean var3 = false;
            SeslRecyclerView.ViewHolder var4 = var2.getChildViewHolder(var1);
            boolean var5;
            if (var4 instanceof PreferenceViewHolder && ((PreferenceViewHolder)var4).isDividerAllowedBelow()) {
                var5 = true;
            } else {
                var5 = false;
            }

            if (var5) {
                var3 = this.mAllowDividerAfterLastItem;
                int var7 = var2.indexOfChild(var1);
                if (var7 < var2.getChildCount() - 1) {
                    SeslRecyclerView.ViewHolder var6 = var2.getChildViewHolder(var2.getChildAt(var7 + 1));
                    if (var6 instanceof PreferenceViewHolder && ((PreferenceViewHolder)var6).isDividerAllowedAbove()) {
                        var3 = true;
                    } else {
                        var3 = false;
                    }
                }
            }

            return var3;
        }

        public void seslOnDispatchDraw(Canvas var1, SeslRecyclerView var2, SeslRecyclerView.State var3) {
            super.seslOnDispatchDraw(var1, var2, var3);
            int var4 = var2.getChildCount();
            int var5 = var2.getWidth();
            PreferenceViewHolder var6 = null;
            PreferenceViewHolder var7 = null;
            var2.getAdapter();

            PreferenceViewHolder var10;
            for(int var8 = 0; var8 < var4; var6 = var10) {
                View var9 = var2.getChildAt(var8);
                SeslRecyclerView.ViewHolder var12 = var2.getChildViewHolder(var9);
                PreferenceViewHolder var13;
                if (var12 instanceof PreferenceViewHolder) {
                    var13 = (PreferenceViewHolder)var12;
                } else {
                    var13 = null;
                }

                if (var8 == 0) {
                    var10 = var13;
                } else {
                    var10 = var6;
                    if (var8 == 1) {
                        var7 = var13;
                        var10 = var6;
                    }
                }

                int var11 = (int)var9.getY() + var9.getHeight();
                if (this.mDivider != null && this.shouldDrawDividerBelow(var9, var2)) {
                    this.mDivider.setBounds(0, var11, var5, this.mDividerHeight + var11);
                    this.mDivider.draw(var1);
                }

                if (SeslPreferenceFragmentCompat.this.mRoundedCornerType != 0 && var13 != null && var13.mDrawBackground) {
                    if (var13.seslIsDrawSubheaderRound()) {
                        SeslPreferenceFragmentCompat.this.mSeslSubheaderRoundedCorner.setRoundedCorners(var13.mDrawCorners);
                        SeslPreferenceFragmentCompat.this.mSeslSubheaderRoundedCorner.drawRoundedCorner(var9, var1);
                    } else {
                        SeslPreferenceFragmentCompat.this.mSeslRoundedCorner.setRoundedCorners(var13.mDrawCorners);
                        SeslPreferenceFragmentCompat.this.mSeslRoundedCorner.drawRoundedCorner(var9, var1);
                    }
                }

                ++var8;
            }

            if (SeslPreferenceFragmentCompat.this.mRoundedCornerType != 0) {
                if (SeslPreferenceFragmentCompat.this.mRoundedCornerType != 2 || var6 == null || this.canScrollUp(var2) || var6.seslIsDrawSubheaderRound() || var7 != null && (var7 == null || var7.seslGetDrawCorners() == 3)) {
                    SeslPreferenceFragmentCompat.this.mSeslListRoundedCorner.drawRoundedCorner(var1);
                } else {
                    SeslPreferenceFragmentCompat.this.mSeslStrokeListRoundedCorner.drawRoundedCorner(var1);
                }
            }

        }

        public void setAllowDividerAfterLastItem(boolean var1) {
            this.mAllowDividerAfterLastItem = var1;
        }

        public void setDivider(Drawable var1) {
            if (var1 != null) {
                this.mDividerHeight = var1.getIntrinsicHeight();
            } else {
                this.mDividerHeight = 0;
            }

            this.mDivider = var1;
            SeslPreferenceFragmentCompat.this.mList.invalidateItemDecorations();
        }

        public void setDividerHeight(int var1) {
            this.mDividerHeight = var1;
            SeslPreferenceFragmentCompat.this.mList.invalidateItemDecorations();
        }
    }

    public interface OnPreferenceDisplayDialogCallback {
        boolean onPreferenceDisplayDialog(SeslPreferenceFragmentCompat var1, SeslPreference var2);
    }

    public interface OnPreferenceStartFragmentCallback {
        boolean onPreferenceStartFragment(SeslPreferenceFragmentCompat var1, SeslPreference var2);
    }

    public interface OnPreferenceStartScreenCallback {
        boolean onPreferenceStartScreen(SeslPreferenceFragmentCompat var1, PreferenceScreen var2);
    }
}
