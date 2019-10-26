package com.samsung.android.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.util.Pools;
import androidx.core.view.GravityCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.tabs.TabItem;
import com.mesalabs.cerberus.R;
import com.samsung.android.ui.internal.SeslAbsIndicatorView;

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

@ViewPager.DecorView
public class SeslTabLayout extends HorizontalScrollView {
    private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
    static final int DEFAULT_GAP_TEXT_ICON = 8;
    private static final int INVALID_WIDTH = -1;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int TAB_MIN_WIDTH_MARGIN = 56;
    static final int FIXED_WRAP_GUTTER_MIN = 16;
    static final int MOTION_NON_ADJACENT_OFFSET = 24;
    public static final int MODE_SCROLLABLE = 0;
    public static final int MODE_FIXED = 1;
    private static final int ANIMATION_DURATION = 300;
    public static final int GRAVITY_FILL = 0;
    public static final int GRAVITY_CENTER = 1;

    public static final int SESL_TAB_ANIM_INTERPOLATOR = R.anim.sine_in_out_80;
    private static final Pools.Pool<SeslTab> sTabPool = new Pools.SynchronizedPool(16);
    private AdapterChangeListener mAdapterChangeListener;
    private int mBadgeColor = -1;
    private int mBadgeTextColor = -1;
    private Typeface mBoldTypeface;
    private int mContentInsetStart;
    private ContentResolver mContentResolver;
    private OnTabSelectedListener mCurrentVpSelectedListener;
    private int mDepthStyle = 1;
    private boolean mIsScaledTextSizeType = false;
    int mMode;
    private Typeface mNormalTypeface;
    private TabLayoutOnPageChangeListener mPageChangeListener;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mPagerAdapterObserver;
    private final int mRequestedTabMaxWidth;
    private final int mRequestedTabMinWidth;
    private int mRequestedTabWidth = -1;
    private ValueAnimator mScrollAnimator;
    private final int mScrollableTabMinWidth;
    private OnTabSelectedListener mSelectedListener;
    private final ArrayList<OnTabSelectedListener> mSelectedListeners = new ArrayList<>();
    private SeslTab mSelectedTab;
    private boolean mSetupViewPagerImplicitly;
    private int mSubTabIndicatorHeight = 1;
    private int mSubTabSelectedIndicatorColor = -1;
    final int mTabBackgroundResId;
    int mTabGravity;
    int mTabMaxWidth = 0x7fffffff;
    int mTabPaddingBottom;
    int mTabPaddingEnd;
    int mTabPaddingStart;
    int mTabPaddingTop;
    private int mTabSelectedIndicatorColor;
    private final SlidingTabStrip mTabStrip;
    int mTabTextAppearance;
    ColorStateList mTabTextColors;
    float mTabTextMultiLineSize;
    float mTabTextSize;
    private final Pools.Pool<SeslTabView> mTabViewPool = new Pools.SimplePool(12);
    private final ArrayList<SeslTab> mTabs = new ArrayList<>();
    ViewPager mViewPager;

    public SeslTabLayout(Context context) {
        this(context, null);
    }

    public SeslTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeslTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setHorizontalScrollBarEnabled(false);

        mTabStrip = new SlidingTabStrip(context);
        super.addView(mTabStrip, 0, new HorizontalScrollView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

        TypedArray a;
        if (isLightTheme()) {
            a = context.obtainStyledAttributes(attrs, R.styleable.SeslTabLayout, defStyleAttr, R.style.mesa_SeslTabLayoutStyle_Light);
        } else {
            a = context.obtainStyledAttributes(attrs, R.styleable.SeslTabLayout, defStyleAttr, R.style.mesa_SeslTabLayoutStyle);
        }

        mTabStrip.setSelectedIndicatorHeight(a.getDimensionPixelSize(R.styleable.SeslTabLayout_tabIndicatorHeight, 0));
        mTabSelectedIndicatorColor = a.getColor(R.styleable.SeslTabLayout_tabIndicatorColor, 0);
        mTabStrip.setSelectedIndicatorColor(mTabSelectedIndicatorColor);

        int dimensionPixelSize = a.getDimensionPixelSize(R.styleable.SeslTabLayout_tabPadding, 0);
        mTabPaddingStart = a.getDimensionPixelSize(R.styleable.SeslTabLayout_tabPaddingStart, dimensionPixelSize);
        mTabPaddingTop = a.getDimensionPixelSize(R.styleable.SeslTabLayout_tabPaddingTop, dimensionPixelSize);
        mTabPaddingEnd = a.getDimensionPixelSize(R.styleable.SeslTabLayout_tabPaddingEnd, dimensionPixelSize);
        mTabPaddingBottom = a.getDimensionPixelSize(R.styleable.SeslTabLayout_tabPaddingBottom, dimensionPixelSize);

        mTabTextAppearance = a.getResourceId(R.styleable.SeslTabLayout_tabTextAppearance, R.style.mesa_SeslTabTextStyle_Light);

        final TypedArray ta = context.obtainStyledAttributes(mTabTextAppearance, androidx.appcompat.R.styleable.TextAppearance);
        try {
            mTabTextSize = ta.getDimensionPixelSize(androidx.appcompat.R.styleable.TextAppearance_android_textSize, 0);
            mIsScaledTextSizeType = ta.getText(androidx.appcompat.R.styleable.TextAppearance_android_textSize).toString().contains("sp");
            mTabTextColors = ta.getColorStateList(androidx.appcompat.R.styleable.TextAppearance_android_textColor);
            ta.recycle();

            mBoldTypeface = Typeface.create("sec-roboto-light", Typeface.BOLD);
            mNormalTypeface = Typeface.create("sec-roboto-light", Typeface.NORMAL);

            mContentResolver = context.getContentResolver();

            mSubTabIndicatorHeight = getContext().getResources().getDimensionPixelSize(R.dimen.sesl_tablayout_subtab_indicator_height);

            if (a.hasValue(R.styleable.SeslTabLayout_tabTextColor)) {
                mTabTextColors = a.getColorStateList(R.styleable.SeslTabLayout_tabTextColor);
            }
            if (a.hasValue(R.styleable.SeslTabLayout_tabSelectedTextColor)) {
                mTabTextColors = createColorStateList(mTabTextColors.getDefaultColor(), a.getColor(R.styleable.SeslTabLayout_tabSelectedTextColor, 0));
            }

            mRequestedTabMinWidth = a.getDimensionPixelSize(R.styleable.SeslTabLayout_tabMinWidth, -1);
            mRequestedTabMaxWidth = a.getDimensionPixelSize(R.styleable.SeslTabLayout_tabMaxWidth, -1);
            mTabBackgroundResId = a.getResourceId(R.styleable.SeslTabLayout_tabBackground, 0);
            mContentInsetStart = a.getDimensionPixelSize(R.styleable.SeslTabLayout_tabContentStart, 0);

            mMode = a.getInt(R.styleable.SeslTabLayout_tabMode, 1);
            if (mMode == 16) {
                mMode = MODE_FIXED;
            }

            mTabGravity = a.getInt(R.styleable.SeslTabLayout_tabGravity, 0);
            a.recycle();

            Resources res = getResources();
            mTabTextMultiLineSize = res.getDimensionPixelSize(R.dimen.sesl_tab_text_size_2line);
            mScrollableTabMinWidth = res.getDimensionPixelSize(R.dimen.sesl_tab_scrollable_min_width);

            applyModeAndGravity();
        } catch (Throwable th) {
            ta.recycle();
            throw th;
        }
    }

    private boolean isLightTheme() {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.isLightTheme, outValue, true);
        return outValue.data != 0;
    }

    public void setSelectedTabIndicatorColor(int color) {
        mTabSelectedIndicatorColor = color;

        Iterator it = mTabs.iterator();
        while (it.hasNext()) {
            SeslAbsIndicatorView iv = ((SeslTab) it.next()).mView.mIndicatorView;
            if (iv != null) {
                if (mDepthStyle != 2 || mSubTabSelectedIndicatorColor == -1) {
                    iv.setSelectedIndicatorColor(color);
                } else {
                    iv.setSelectedIndicatorColor(mSubTabSelectedIndicatorColor);
                }
                iv.invalidate();
            }
        }
    }

    public void setSelectedTabIndicatorHeight(int height) {
        mTabStrip.setSelectedIndicatorHeight(height);
    }

    public void setScrollPosition(int position, float positionOffset, boolean updateSelectedText) {
        setScrollPosition(position, positionOffset, updateSelectedText, true);
    }

    void setScrollPosition(int position, float positionOffset, boolean updateSelectedText, boolean updateIndicatorPosition) {
        final int roundedPosition = Math.round(position + positionOffset);
        if (roundedPosition < 0 || roundedPosition >= mTabStrip.getChildCount()) {
            return;
        }

        if (updateIndicatorPosition) {
            mTabStrip.setIndicatorPositionFromTabPosition(position, positionOffset);
        }

        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            mScrollAnimator.cancel();
        }
        scrollTo(calculateScrollXForTab(position, positionOffset), 0);

        if (updateSelectedText) {
            setSelectedTabView(roundedPosition);
        }
    }

    private float getScrollPosition() {
        return mTabStrip.getIndicatorPosition();
    }

    public void addTab(SeslTab tab) {
        addTab(tab, mTabs.isEmpty());
    }

    public void addTab(SeslTab tab, boolean setSelected) {
        addTab(tab, mTabs.size(), setSelected);
    }

    public void addTab(SeslTab tab, int position, boolean setSelected) {
        if (tab.mParent != this) {
            throw new IllegalArgumentException("SeslTab belongs to a different SeslTabLayout.");
        }
        configureTab(tab, position);
        addTabView(tab);

        if (setSelected) {
            tab.select();
        }
    }

    private void addTabFromItemView(TabItem item) {
        final SeslTab tab = newTab();
        if (item.text != null) {
            tab.setText(item.text);
        }
        if (item.icon != null) {
            tab.setIcon(item.icon);
        }
        if (item.customLayout != 0) {
            tab.setCustomView(item.customLayout);
        }
        if (!TextUtils.isEmpty(item.getContentDescription())) {
            tab.setContentDescription(item.getContentDescription());
        }
        addTab(tab);
    }

    @Deprecated
    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        if (mSelectedListener != null) {
            removeOnTabSelectedListener(mSelectedListener);
        }

        mSelectedListener = listener;
        if (listener != null) {
            addOnTabSelectedListener(listener);
        }
    }

    public void addOnTabSelectedListener(OnTabSelectedListener listener) {
        if (!mSelectedListeners.contains(listener)) {
            mSelectedListeners.add(listener);
        }
    }

    public void removeOnTabSelectedListener(OnTabSelectedListener listener) {
        mSelectedListeners.remove(listener);
    }

    public SeslTab newTab() {
        SeslTab tab = sTabPool.acquire();
        if (tab == null) {
            tab = new SeslTab();
        }
        tab.mParent = this;
        tab.mView = createTabView(tab);
        return tab;
    }

    public int getTabCount() {
        return mTabs.size();
    }

    public SeslTab getTabAt(int index) {
        return (index < 0 || index >= getTabCount()) ? null : mTabs.get(index);
    }

    public int getSelectedTabPosition() {
        return mSelectedTab != null ? mSelectedTab.getPosition() : -1;
    }

    public void removeAllTabs() {
        for (int i = mTabStrip.getChildCount() - 1; i >= 0; i--) {
            removeTabViewAt(i);
        }

        for (final Iterator<SeslTab> i2 = mTabs.iterator(); i2.hasNext();) {
            final SeslTab tab = i2.next();
            i2.remove();
            tab.reset();
            sTabPool.release(tab);
        }

        mSelectedTab = null;
    }

    public void setTabMode(int mode) {
        if (mode != mMode) {
            if (mode == 16) {
                mMode = MODE_FIXED;
            } else {
                mMode = mode;
            }
            applyModeAndGravity();
        }
    }

    public int getTabMode() {
        return mMode;
    }

    public void setTabGravity(int gravity) {
        if (mTabGravity != gravity) {
            mTabGravity = gravity;
            applyModeAndGravity();
        }
    }

    public int getTabGravity() {
        return mTabGravity;
    }

    public void setTabTextColors(ColorStateList textColor) {
        if (mTabTextColors != textColor) {
            mTabTextColors = textColor;
            updateAllTabs();
        }
    }
    public ColorStateList getTabTextColors() {
        return mTabTextColors;
    }

    public void setupWithViewPager(ViewPager viewPager) {
        setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(final ViewPager viewPager, boolean autoRefresh) {
        setupWithViewPager(viewPager, autoRefresh, false);
    }

    private void setupWithViewPager(final ViewPager viewPager, boolean autoRefresh, boolean implicitSetup) {
        if (mViewPager != null) {
            if (mPageChangeListener != null) {
                mViewPager.removeOnPageChangeListener(mPageChangeListener);
            }
            if (mAdapterChangeListener != null) {
                mViewPager.removeOnAdapterChangeListener(mAdapterChangeListener);
            }
        }

        if (mCurrentVpSelectedListener != null) {
            removeOnTabSelectedListener(mCurrentVpSelectedListener);
            mCurrentVpSelectedListener = null;
        }

        if (viewPager != null) {
            mViewPager = viewPager;

            if (mPageChangeListener == null) {
                mPageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            mPageChangeListener.reset();
            viewPager.addOnPageChangeListener(mPageChangeListener);

            mCurrentVpSelectedListener = new ViewPagerOnTabSelectedListener(viewPager);
            addOnTabSelectedListener(mCurrentVpSelectedListener);

            final PagerAdapter adapter = viewPager.getAdapter();
            if (adapter != null) {
                setPagerAdapter(adapter, autoRefresh);
            }

            if (mAdapterChangeListener == null) {
                mAdapterChangeListener = new AdapterChangeListener();
            }
            mAdapterChangeListener.setAutoRefresh(autoRefresh);
            viewPager.addOnAdapterChangeListener(mAdapterChangeListener);

            setScrollPosition(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            mViewPager = null;
            setPagerAdapter(null, false);
        }

        mSetupViewPagerImplicitly = implicitSetup;
    }

    @Deprecated
    public void setTabsFromPagerAdapter(final PagerAdapter adapter) {
        setPagerAdapter(adapter, false);
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return getTabScrollRange() > 0;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        for (int i = 0; i < getTabCount(); i++) {
            if (getTabAt(i) != null || getTabAt(i).mView != null) {
                if (getTabAt(i).mView.mMainTabTouchBackground != null) {
                    getTabAt(i).mView.mMainTabTouchBackground.setAlpha(0.0f);
                }
                if (getTabAt(i).mView.mIndicatorView != null) {
                    if (getSelectedTabPosition() == i) {
                        getTabAt(i).mView.mIndicatorView.setShow();
                    } else {
                        getTabAt(i).mView.mIndicatorView.setHideImmediatly();
                    }
                }
            }
        }

        if (mViewPager == null) {
            final ViewParent vp = getParent();
            if (vp instanceof ViewPager) {
                setupWithViewPager((ViewPager) vp, true, true);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mSetupViewPagerImplicitly) {
            setupWithViewPager(null);
            mSetupViewPagerImplicitly = false;
        }
    }

    private int getTabScrollRange() {
        return Math.max(0, mTabStrip.getWidth() - getWidth() - getPaddingLeft() - getPaddingRight());
    }

    void setPagerAdapter(final PagerAdapter adapter, final boolean addObserver) {
        if (mPagerAdapter != null && mPagerAdapterObserver != null) {
            mPagerAdapter.unregisterDataSetObserver(mPagerAdapterObserver);
        }

        mPagerAdapter = adapter;

        if (addObserver && adapter != null) {
            if (mPagerAdapterObserver == null) {
                mPagerAdapterObserver = new PagerAdapterObserver();
            }
            adapter.registerDataSetObserver(mPagerAdapterObserver);
        }

        populateFromPagerAdapter();
    }

    void populateFromPagerAdapter() {
        removeAllTabs();

        if (mPagerAdapter != null) {
            final int adapterCount = mPagerAdapter.getCount();
            for (int i = 0; i < adapterCount; i++) {
                addTab(newTab().setText(mPagerAdapter.getPageTitle(i)), false);
            }

            if (mViewPager != null && adapterCount > 0) {
                final int curItem = mViewPager.getCurrentItem();
                if (curItem != getSelectedTabPosition() && curItem < getTabCount()) {
                    selectTab(getTabAt(curItem));
                }
            }
        }
    }

    private void updateAllTabs() {
        for (int i = 0, z = mTabs.size(); i < z; i++) {
            mTabs.get(i).updateView();
        }
    }

    private SeslTabView createTabView(final SeslTab tab) {
        SeslTabView tabView = mTabViewPool != null ? mTabViewPool.acquire() : null;
        if (tabView == null) {
            tabView = new SeslTabView(getContext());
        }
        tabView.setTab(tab);
        tabView.setFocusable(true);
        tabView.setMinimumWidth(getTabMinWidth());
        return tabView;
    }

    private void configureTab(SeslTab tab, int position) {
        tab.setPosition(position);
        mTabs.add(position, tab);

        final int count = mTabs.size();
        for (int i = position + 1; i < count; i++) {
            mTabs.get(i).setPosition(i);
        }
    }

    private void addTabView(SeslTab tab) {
        final SeslTabView tabView = tab.mView;
        mTabStrip.addView(tabView, tab.getPosition(), createLayoutParamsForTabs());
    }

    @Override
    public void addView(View child) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        addViewInternal(child);
    }

    private void addViewInternal(View child) {
        if (child instanceof TabItem) {
            addTabFromItemView((TabItem) child);
            return;
        }
        throw new IllegalArgumentException("Only TabItem instances can be added to SeslTabLayout");
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        updateTabViewLayoutParams(lp);
        return lp;
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams lp) {
        if (mMode == MODE_FIXED && mTabGravity == GRAVITY_FILL) {
            lp.width = 0;
            lp.weight = 1.0f;
        } else {
            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            lp.weight = 0.0f;
        }
    }

    int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int idealHeight = dpToPx(getDefaultHeight()) + getPaddingTop() + getPaddingBottom();
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(idealHeight, MeasureSpec.getSize(heightMeasureSpec)), MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.UNSPECIFIED:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(idealHeight, MeasureSpec.EXACTLY);
                break;
        }

        final int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            mTabMaxWidth = mRequestedTabMaxWidth > 0 ? mRequestedTabMaxWidth : specWidth - dpToPx(TAB_MIN_WIDTH_MARGIN);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getChildCount() == 1) {
            final View child = getChildAt(0);
            boolean remeasure = false;

            switch (mMode) {
                case MODE_SCROLLABLE:
                    remeasure = child.getMeasuredWidth() < getMeasuredWidth();
                    break;
                case MODE_FIXED:
                    remeasure = child.getMeasuredWidth() != getMeasuredWidth();
                    break;
            }
            if (remeasure) {
                int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), child.getLayoutParams().height);
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
    }

    private void removeTabViewAt(int position) {
        SeslTabView view = (SeslTabView) mTabStrip.getChildAt(position);
        mTabStrip.removeViewAt(position);
        if (view != null) {
            view.reset();
            mTabViewPool.release(view);
        }
        requestLayout();
    }

    private void animateToTab(int newPosition) {
        if (newPosition == SeslTab.INVALID_POSITION) {
            return;
        }

        if (getWindowToken() == null || !ViewCompat.isLaidOut(this) || mTabStrip.childrenNeedLayout()) {
            setScrollPosition(newPosition, 0.0f, true);
            return;
        }

        final int startScrollX = getScrollX();
        final int targetScrollX = calculateScrollXForTab(newPosition, 0);

        if (startScrollX != targetScrollX) {
            ensureScrollAnimator();

            mScrollAnimator.setIntValues(startScrollX, targetScrollX);
            mScrollAnimator.start();
        }

        mTabStrip.animateIndicatorToPosition(newPosition, ANIMATION_DURATION);
    }

    private void ensureScrollAnimator() {
        if (mScrollAnimator == null) {
            mScrollAnimator = new ValueAnimator();
            mScrollAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            mScrollAnimator.setDuration(ANIMATION_DURATION);
            mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    scrollTo((int) animator.getAnimatedValue(), 0);
                }
            });
        }
    }

    void setScrollAnimatorListener(Animator.AnimatorListener listener) {
        ensureScrollAnimator();
        mScrollAnimator.addListener(listener);
    }

    private void setSelectedTabView(int position) {
        int selectedTabPosition = getSelectedTabPosition();
        int tabCount = mTabStrip.getChildCount();
        if (position < tabCount) {
            for (int i = 0; i < tabCount; i++) {
                View child = mTabStrip.getChildAt(i);
                child.setSelected(i == position);
            }

            mTabs.get(position).mView.setSelected(true);
            for (int i2 = 0; i2 < getTabCount(); i2++) {
                SeslTabView tabView = mTabs.get(i2).mView;
                if (i2 == position) {
                    if (tabView.mTextView != null) {
                        if (tabView.mTextView.getCurrentTextColor() != seslGetSelctedTabTextColor()) {
                            seslStartTextColorChangeAnimation(tabView.mTextView, mTabTextColors.getDefaultColor(), seslGetSelctedTabTextColor());
                        } else {
                            seslStartTextColorChangeAnimation(tabView.mTextView, seslGetSelctedTabTextColor(), seslGetSelctedTabTextColor());
                        }
                        tabView.mTextView.setTypeface(mBoldTypeface);
                        tabView.mTextView.setSelected(true);
                    }
                    if (mTabs.get(i2).mView.mIndicatorView != null) {
                        mTabs.get(i2).mView.mIndicatorView.setReleased();
                    }
                } else {
                    if (tabView.mIndicatorView != null) {
                        tabView.mIndicatorView.setHideImmediatly();
                    }
                    if (tabView.mTextView != null) {
                        tabView.mTextView.setTypeface(mNormalTypeface);
                        if (tabView.mTextView.getCurrentTextColor() != mTabTextColors.getDefaultColor()) {
                            seslStartTextColorChangeAnimation(tabView.mTextView, seslGetSelctedTabTextColor(), mTabTextColors.getDefaultColor());
                        } else {
                            seslStartTextColorChangeAnimation(tabView.mTextView, mTabTextColors.getDefaultColor(), mTabTextColors.getDefaultColor());
                        }
                        tabView.mTextView.setSelected(false);
                    }
                }
            }
        }
    }

    void selectTab(SeslTab tab) {
        selectTab(tab, true);
    }

    void selectTab(SeslTab tab, boolean updateIndicator) {
        if (tab.mView.isEnabled() || mViewPager == null) {
            final SeslTab currentTab = mSelectedTab;

            if (currentTab == tab) {
                if (currentTab != null) {
                    dispatchTabReselected(tab);
                    animateToTab(tab.getPosition());
                }
            } else {
                final int newPosition = tab != null ? tab.getPosition() : SeslTab.INVALID_POSITION;
                if (updateIndicator) {
                    if ((currentTab == null || currentTab.getPosition() == SeslTab.INVALID_POSITION) && newPosition != SeslTab.INVALID_POSITION) {
                        setScrollPosition(newPosition, 0f, true);
                    } else {
                        animateToTab(newPosition);
                    }
                    if (newPosition != SeslTab.INVALID_POSITION) {
                        setSelectedTabView(newPosition);
                    }
                }
                if (currentTab != null) {
                    dispatchTabUnselected(currentTab);
                }
                mSelectedTab = tab;
                if (tab != null) {
                    dispatchTabSelected(tab);
                }
            }
        } else {
            mViewPager.setCurrentItem(getSelectedTabPosition());
        }
    }

    private void dispatchTabSelected(SeslTab tab) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabSelected(tab);
        }
    }

    private void dispatchTabUnselected(SeslTab tab) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabUnselected(tab);
        }
    }

    private void dispatchTabReselected(SeslTab tab) {
        for (int i = mSelectedListeners.size() - 1; i >= 0; i--) {
            mSelectedListeners.get(i).onTabReselected(tab);
        }
    }

    private int calculateScrollXForTab(int position, float positionOffset) {
        if (mMode == MODE_SCROLLABLE) {
            final View selectedChild = mTabStrip.getChildAt(position);
            final View nextChild = position + 1 < mTabStrip.getChildCount() ? mTabStrip.getChildAt(position + 1) : null;
            final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
            final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;

            int scrollBase = selectedChild.getLeft() + (selectedWidth / 2) - (getWidth() / 2);
            int scrollOffset = (int) ((selectedWidth + nextWidth) * 0.5f * positionOffset);

            return (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_LTR) ? scrollBase + scrollOffset : scrollBase - scrollOffset;
        }
        return 0;
    }

    private void applyModeAndGravity() {
        mTabStrip.setPaddingRelative(0, 0, 0, 0);
        switch (mMode) {
            case MODE_SCROLLABLE:
                mTabStrip.setGravity(GravityCompat.START);
                break;
            case MODE_FIXED:
                mTabStrip.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
        }
        updateTabViews(true);
    }

    void updateTabViews(boolean requestLayout) {
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            View child = mTabStrip.getChildAt(i);
            child.setMinimumWidth(getTabMinWidth());
            updateTabViewLayoutParams((LinearLayout.LayoutParams) child.getLayoutParams());
            if (requestLayout) {
                child.requestLayout();
            }
        }
    }

    private static ColorStateList createColorStateList(int defaultColor, int selectedColor) {
        final int[][] states = new int[2][];
        final int[] colors = new int[2];
        int i = 0;

        states[i] = SELECTED_STATE_SET;
        colors[i] = selectedColor;
        i++;

        states[i] = EMPTY_STATE_SET;
        colors[i] = defaultColor;
        i++;

        return new ColorStateList(states, colors);
    }

    private int getDefaultHeight() {
        boolean hasIconAndText = false;
        for (int i = 0, count = mTabs.size(); i < count; i++) {
            SeslTab tab = mTabs.get(i);
            if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty(tab.getText())) {
                hasIconAndText = true;
                break;
            }
        }
        return hasIconAndText ? DEFAULT_HEIGHT_WITH_TEXT_ICON : DEFAULT_HEIGHT;
    }

    private int getTabMinWidth() {
        if (mRequestedTabMinWidth != INVALID_WIDTH) {
            return mRequestedTabMinWidth;
        }
        return mMode == MODE_SCROLLABLE ? mScrollableTabMinWidth : 0;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return generateDefaultLayoutParams();
    }

    int getTabMaxWidth() {
        return mTabMaxWidth;
    }

    private void checkMaxFontScale(TextView textview, int baseSize) {
        float currentFontScale = getResources().getConfiguration().fontScale;
        if (textview != null && mIsScaledTextSizeType && currentFontScale > 1.3f) {
            textview.setTextSize(0, 1.3f * (((float) baseSize) / currentFontScale));
        }
    }

    private void seslUpdateBadgePosition() {
        if (mTabs != null && mTabs.size() > 0) {
            for (int i = 0; i < mTabs.size(); i++) {
                if (!(mTabs.get(i) == null || ((SeslTab) mTabs.get(i)).mView == null)) {
                    SeslTabView tabView = ((SeslTab) mTabs.get(i)).mView;
                    TextView title = tabView.mTextView;
                    if (tabView.getWidth() > 0 && title != null && title.getWidth() > 0) {
                        TextView badge = null;
                        if (tabView.mNBadgeView != null && tabView.mNBadgeView.getVisibility() == View.VISIBLE) {
                            badge = tabView.mNBadgeView;
                        } else if (tabView.mDotBadgeView != null && tabView.mDotBadgeView.getVisibility() == View.VISIBLE) {
                            badge = tabView.mDotBadgeView;
                        }
                        if (badge != null) {
                            badge.measure(0, 0);
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) badge.getLayoutParams();
                            if ((tabView.getWidth() - title.getWidth()) / 2 < badge.getMeasuredWidth()) {
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) badge.getLayoutParams();
                                if (getRelativeLayoutRule(params, 17) != 0) {
                                    params.addRule(17, 0);
                                    params.addRule(21);
                                    badge.setLayoutParams(params);
                                }
                            } else {
                                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) badge.getLayoutParams();
                                if (getRelativeLayoutRule(params2, 17) != R.id.title) {
                                    params2.addRule(17, R.id.title);
                                    params2.removeRule(21);
                                    badge.setLayoutParams(params2);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int getRelativeLayoutRule(RelativeLayout.LayoutParams params, int verb) {
        int[] rules = params.getRules();
        if (verb == 17) {
            verb = isLayoutRTL() ? 16 : 1;
        }
        return rules[verb];
    }

    private boolean isLayoutRTL() {
        return getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    private int seslGetSelctedTabTextColor() {
        if (mTabTextColors != null) {
            return mTabTextColors.getColorForState(new int[]{android.R.attr.state_selected, android.R.attr.state_enabled}, mTabTextColors.getDefaultColor());
        }
        return -1;
    }

    private void seslStartTextColorChangeAnimation(TextView textview, int fromColor, int toColor) {
        if (textview != null) {
            textview.setTextColor(toColor);
        }
    }

    @Override
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        for (int i = 0; i < getTabCount(); i++) {
            if (getTabAt(i) != null || getTabAt(i).mView != null || getTabAt(i).mView.mMainTabTouchBackground != null) {
                getTabAt(i).mView.mMainTabTouchBackground.setAlpha(0.0f);
            }
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        for (int i = 0; i < getTabCount(); i++) {
            if (getTabAt(i) != null || getTabAt(i).mView != null || getTabAt(i).mView.mMainTabTouchBackground != null) {
                getTabAt(i).mView.mMainTabTouchBackground.setAlpha(0.0f);
            }
        }
    }


    private class AdapterChangeListener implements ViewPager.OnAdapterChangeListener {
        private boolean mAutoRefresh;

        AdapterChangeListener() { }

        @Override
        public void onAdapterChanged(ViewPager viewPager, PagerAdapter oldAdapter, PagerAdapter newAdapter) {
            if (mViewPager == viewPager) {
                setPagerAdapter(newAdapter, mAutoRefresh);
            }
        }

        void setAutoRefresh(boolean autoRefresh) {
            mAutoRefresh = autoRefresh;
        }
    }

    public interface OnTabSelectedListener {
        public void onTabSelected(SeslTab tab);

        public void onTabUnselected(SeslTab tab);

        public void onTabReselected(SeslTab tab);
    }

    private class PagerAdapterObserver extends DataSetObserver {
        PagerAdapterObserver() { }

        @Override
        public void onChanged() {
            populateFromPagerAdapter();
        }

        @Override
        public void onInvalidated() {
            populateFromPagerAdapter();
        }
    }

    private class SlidingTabStrip extends LinearLayout {
        private ValueAnimator mIndicatorAnimator;
        private int mIndicatorLeft = -1;
        private int mIndicatorRight = -1;
        private int mIndicatorTop = -1;
        private int mInterval;
        private int mLayoutDirection = -1;
        private int mRadius;
        private int mSelectedIndicatorHeight;
        private final Paint mSelectedIndicatorPaint;
        int mSelectedPosition = -1;
        float mSelectionOffset;

        SlidingTabStrip(Context context) {
            super(context);
            setWillNotDraw(false);
            mSelectedIndicatorPaint = new Paint();
            mRadius = (int) TypedValue.applyDimension(1, 1.0f, context.getResources().getDisplayMetrics());
            mInterval = mRadius * 2;
        }

        void setSelectedIndicatorColor(int color) {
            if (mSelectedIndicatorPaint.getColor() != color) {
                mSelectedIndicatorPaint.setColor(color);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        void setSelectedIndicatorHeight(int height) {
            if (mSelectedIndicatorHeight != height) {
                mSelectedIndicatorHeight = height;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        boolean childrenNeedLayout() {
            for (int i = 0, z = getChildCount(); i < z; i++) {
                final View child = getChildAt(i);
                if (child.getWidth() <= 0) {
                    return true;
                }
            }
            return false;
        }

        void setIndicatorPositionFromTabPosition(int position, float positionOffset) {
            if (mIndicatorAnimator != null && mIndicatorAnimator.isRunning()) {
                mIndicatorAnimator.cancel();
            }

            mSelectedPosition = position;
            mSelectionOffset = positionOffset;
            updateIndicatorPosition();
        }

        float getIndicatorPosition() {
            return mSelectedPosition + mSelectionOffset;
        }

        @Override
        public void onRtlPropertiesChanged(int layoutDirection) {
            super.onRtlPropertiesChanged(layoutDirection);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if (mLayoutDirection != layoutDirection) {
                    requestLayout();
                    mLayoutDirection = layoutDirection;
                }
            }
        }

        @Override
        protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
                return;
            }

            if (mMode == MODE_FIXED && mTabGravity == GRAVITY_CENTER) {
                final int count = getChildCount();

                int largestTabWidth = 0;
                for (int i = 0, z = count; i < z; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() == VISIBLE) {
                        largestTabWidth = Math.max(largestTabWidth, child.getMeasuredWidth());
                    }
                }

                if (largestTabWidth <= 0) {
                    return;
                }

                final int gutter = dpToPx(FIXED_WRAP_GUTTER_MIN);
                boolean remeasure = false;

                if (largestTabWidth * count <= getMeasuredWidth() - gutter * 2) {
                    for (int i2 = 0; i2 < count; i2++) {
                        final LinearLayout.LayoutParams lp = (LayoutParams) getChildAt(i2).getLayoutParams();
                        if (lp.width != largestTabWidth || lp.weight != 0) {
                            lp.width = largestTabWidth;
                            lp.weight = 0;
                            remeasure = true;
                        }
                    }
                } else {
                    mTabGravity = GRAVITY_FILL;
                    updateTabViews(false);
                    remeasure = true;
                }

                if (remeasure) {
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);

            if (mIndicatorAnimator != null && mIndicatorAnimator.isRunning()) {
                mIndicatorAnimator.cancel();
                final long duration = mIndicatorAnimator.getDuration();
                animateIndicatorToPosition(mSelectedPosition, Math.round((1f - mIndicatorAnimator.getAnimatedFraction()) * duration));
            } else {
                updateIndicatorPosition();
            }
        }

        private void updateIndicatorPosition() {
            final View selectedTitle = getChildAt(mSelectedPosition);
            int left, right;

            if (selectedTitle != null && selectedTitle.getWidth() > 0) {
                left = selectedTitle.getLeft();
                right = selectedTitle.getRight();

                if (mSelectionOffset > 0f && mSelectedPosition < getChildCount() - 1) {
                    View nextTitle = getChildAt(mSelectedPosition + 1);
                    left = (int) (mSelectionOffset * nextTitle.getLeft() + (1.0f - mSelectionOffset) * left);
                    right = (int) (mSelectionOffset * nextTitle.getRight() + (1.0f - mSelectionOffset) * right);
                }

                SeslTabView selectedGroup = (SeslTabView) selectedTitle;
                if (selectedGroup.mCustomView == null) {
                    int bottom = -1;
                    for (int i = 0; i < selectedGroup.getChildCount(); i++) {
                        View child = selectedGroup.getChildAt(i);
                        if (bottom < child.getBottom()) {
                            bottom = child.getBottom();
                        }
                        if (child instanceof TextView) {
                            left = selectedTitle.getLeft() + child.getLeft();
                            right = selectedTitle.getLeft() + child.getRight();
                        }
                    }
                    mIndicatorTop = bottom;
                }
            } else {
                left = right = -1;
            }

            setIndicatorPosition(left, right);
        }

        void setIndicatorPosition(int left, int right) {
            if (left != mIndicatorLeft || right != mIndicatorRight) {
                mIndicatorLeft = left;
                mIndicatorRight = right;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        void animateIndicatorToPosition(final int position, int duration) {
            final int startLeft;
            final int startRight;

            if (mIndicatorAnimator != null && mIndicatorAnimator.isRunning()) {
                mIndicatorAnimator.cancel();
            }

            final boolean isRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;

            final View targetView = getChildAt(position);
            if (targetView == null) {
                updateIndicatorPosition();
                return;
            }

            int tLeft = targetView.getLeft();
            int tRight = targetView.getRight();

            if (((SeslTabView) targetView).mCustomView == null) {
                SeslTabView tv = (SeslTabView) targetView;
                for (int i = 0; i < tv.getChildCount(); i++) {
                    View child = tv.getChildAt(i);
                    if (child instanceof TextView) {
                        tLeft = child.getLeft() + targetView.getLeft();
                        tRight = child.getRight() + targetView.getLeft();
                    }
                }
                mSelectedPosition = position;
                mIndicatorLeft = tLeft;
                mIndicatorRight = tRight;
                invalidate();
                return;
            }

            final int targetLeft = tLeft;
            final int targetRight = tRight;

            if (Math.abs(position - mSelectedPosition) <= 1) {
                startLeft = mIndicatorLeft;
                startRight = mIndicatorRight;
            } else {
                final int offset = dpToPx(MOTION_NON_ADJACENT_OFFSET);
                if (position < mSelectedPosition) {
                    if (isRtl) {
                        startLeft = startRight = targetLeft - offset;
                    } else {
                        startLeft = startRight = targetRight + offset;
                    }
                } else {
                    if (isRtl) {
                        startLeft = startRight = targetRight + offset;
                    } else {
                        startLeft = startRight = targetLeft - offset;
                    }
                }
            }

            if (startLeft != targetLeft || startRight != targetRight) {
                ValueAnimator animator = mIndicatorAnimator = new ValueAnimator();
                animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                animator.setDuration(duration);
                animator.setFloatValues(0, 1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        final float fraction = animator.getAnimatedFraction();
                        setIndicatorPosition(AnimationUtils.lerp(startLeft, targetLeft, fraction), AnimationUtils.lerp(startRight, targetRight, fraction));
                    }
                });
                final int i2 = position;
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mSelectedPosition = i2;
                        mSelectionOffset = 0f;
                    }
                });
                animator.start();
            }
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
        }
    }

    public static final class SeslTab {
        public static final int INVALID_POSITION = -1;

        private CharSequence mContentDesc;
        private View mCustomView;
        private Drawable mIcon;
        SeslTabLayout mParent;
        private int mPosition = -1;
        private Object mTag;
        private CharSequence mText;
        SeslTabView mView;

        SeslTab() { }

        public View getCustomView() {
            return mCustomView;
        }

        public TextView seslGetTextView() {
            if (mCustomView != null || mView == null) {
                return null;
            }
            return mView.mTextView;
        }

        public SeslTab setCustomView(View view) {
            if (mView.mTextView != null) {
                mView.removeAllViews();
            }
            mCustomView = view;
            updateView();
            return this;
        }

        public SeslTab setCustomView(int resId) {
            final LayoutInflater inflater = LayoutInflater.from(mView.getContext());
            return setCustomView(inflater.inflate(resId, mView, false));
        }

        public Drawable getIcon() {
            return mIcon;
        }

        public int getPosition() {
            return mPosition;
        }

        void setPosition(int position) {
            mPosition = position;
        }

        public CharSequence getText() {
            return mText;
        }

        public SeslTab setIcon(Drawable icon) {
            mIcon = icon;
            updateView();
            return this;
        }

        public SeslTab setText(CharSequence text) {
            mText = text;
            updateView();
            return this;
        }

        public void select() {
            if (mParent == null) {
                throw new IllegalArgumentException("SeslTab not attached to a SeslTabLayout");
            }
            mParent.selectTab(this);
        }

        public boolean isSelected() {
            if (mParent == null) {
                throw new IllegalArgumentException("SeslTab not attached to a SeslTabLayout");
            }
            return mParent.getSelectedTabPosition() == mPosition;
        }

        public SeslTab setContentDescription(CharSequence contentDesc) {
            mContentDesc = contentDesc;
            updateView();
            return this;
        }

        public CharSequence getContentDescription() {
            return mContentDesc;
        }

        void updateView() {
            if (mView != null) {
                mView.update();
            }
        }

        void reset() {
            mParent = null;
            mView = null;
            mTag = null;
            mIcon = null;
            mText = null;
            mContentDesc = null;
            mPosition = INVALID_POSITION;
            mCustomView = null;
        }
    }

    public static class TabLayoutOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<SeslTabLayout> mTabLayoutRef;
        private int mPreviousScrollState;
        private int mScrollState;

        public TabLayoutOnPageChangeListener(SeslTabLayout tabLayout) {
            mTabLayoutRef = new WeakReference<>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            mPreviousScrollState = mScrollState;
            mScrollState = state;
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            final SeslTabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null) {
                final boolean updateText = mScrollState != SCROLL_STATE_SETTLING || mPreviousScrollState == SCROLL_STATE_DRAGGING;
                final boolean updateIndicator = !(mScrollState == SCROLL_STATE_SETTLING && mPreviousScrollState == SCROLL_STATE_IDLE);
                tabLayout.setScrollPosition(position, positionOffset, updateText, updateIndicator);
            }
        }

        @Override
        public void onPageSelected(final int position) {
            final SeslTabLayout tabLayout = mTabLayoutRef.get();
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != position && position < tabLayout.getTabCount()) {
                final boolean updateIndicator = mScrollState == SCROLL_STATE_IDLE || (mScrollState == SCROLL_STATE_SETTLING && mPreviousScrollState == SCROLL_STATE_IDLE);
                tabLayout.selectTab(tabLayout.getTabAt(position), updateIndicator);
            }
        }

        void reset() {
            mPreviousScrollState = mScrollState = SCROLL_STATE_IDLE;
        }
    }

    @SuppressWarnings("deprecation")
    class SeslTabView extends LinearLayout {
        private ImageView mCustomIconView;
        private TextView mCustomTextView;
        private View mCustomView;
        private int mDefaultMaxLines = 1;
        TextView mDotBadgeView;
        private ImageView mIconView;
        private SeslAbsIndicatorView mIndicatorView;
        private boolean mIsCallPerformClick;
        private View mMainTabTouchBackground;
        TextView mNBadgeView;
        private SeslTab mTab;
        private RelativeLayout mTextParentView;
        private TextView mTextView;

        public SeslTabView(Context context) {
            super(context);
            if (mTabBackgroundResId != 0 || mDepthStyle != 2) {
                ViewCompat.setBackground(this, AppCompatResources.getDrawable(context, mTabBackgroundResId));
            }
            if (mDepthStyle == 1) {
                setPaddingRelative(mTabPaddingStart, mTabPaddingTop, mTabPaddingEnd, mTabPaddingBottom);
            } else {
                setPaddingRelative(0, mTabPaddingTop, 0, mTabPaddingBottom);
            }
            setGravity(Gravity.CENTER);
            setOrientation(VERTICAL);
            setClickable(true);
            ViewCompat.setPointerIcon(this, PointerIconCompat.getSystemIcon(getContext(), PointerIconCompat.TYPE_HAND));
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (isEnabled()) {
                return startTabTouchAnimation(event);
            }
            return super.onTouchEvent(event);
        }

        private boolean startTabTouchAnimation(MotionEvent event) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            if (mTab.getCustomView() == null && mTextView != null) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mIsCallPerformClick = false;
                        if (mTab.mPosition != getSelectedTabPosition() && mTextView != null) {
                            mTextView.setTypeface(mBoldTypeface);
                            seslStartTextColorChangeAnimation(mTextView, mTabTextColors.getDefaultColor(), seslGetSelctedTabTextColor());
                            if (mIndicatorView != null) {
                                mIndicatorView.setPressed();
                            }
                            SeslTab oldSelectedTab = getTabAt(getSelectedTabPosition());
                            if (oldSelectedTab != null) {
                                if (oldSelectedTab.mView.mTextView != null) {
                                    oldSelectedTab.mView.mTextView.setTypeface(mNormalTypeface);
                                    seslStartTextColorChangeAnimation(oldSelectedTab.mView.mTextView, seslGetSelctedTabTextColor(), mTabTextColors.getDefaultColor());
                                }
                                if (oldSelectedTab.mView.mIndicatorView != null) {
                                    oldSelectedTab.mView.mIndicatorView.setHide();
                                }
                            }
                        } else if (mTab.mPosition == getSelectedTabPosition() && mIndicatorView != null) {
                            mIndicatorView.setPressed();
                        }
                        showMainTabTouchBackground(MotionEvent.ACTION_DOWN);
                        break;
                    case MotionEvent.ACTION_UP:
                        showMainTabTouchBackground(MotionEvent.ACTION_UP);
                        if (mTab.mPosition == getSelectedTabPosition() && mIndicatorView != null) {
                            mIndicatorView.setReleased();
                            mIndicatorView.onTouchEvent(event);
                        }
                        performClick();
                        mIsCallPerformClick = true;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mTextView.setTypeface(mNormalTypeface);
                        seslStartTextColorChangeAnimation(mTextView, seslGetSelctedTabTextColor(), mTabTextColors.getDefaultColor());
                        if (mIndicatorView != null && !mIndicatorView.isSelected()) {
                            mIndicatorView.setHide();
                        }
                        SeslTab oldSelectedTab2 = getTabAt(getSelectedTabPosition());
                        if (oldSelectedTab2 != null) {
                            if (oldSelectedTab2.mView.mTextView != null) {
                                oldSelectedTab2.mView.mTextView.setTypeface(mBoldTypeface);
                                seslStartTextColorChangeAnimation(oldSelectedTab2.mView.mTextView, mTabTextColors.getDefaultColor(), seslGetSelctedTabTextColor());
                            }
                            if (oldSelectedTab2.mView.mIndicatorView != null) {
                                oldSelectedTab2.mView.mIndicatorView.setShow();
                            }
                        }
                        showMainTabTouchBackground(MotionEvent.ACTION_CANCEL);
                        break;
                }
            }
            return super.onTouchEvent(event);
        }

        private void showMainTabTouchBackground(int action) {
            if (mMainTabTouchBackground != null && mDepthStyle == 1) {
                mMainTabTouchBackground.setAlpha(1.0f);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.setFillAfter(true);
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
                        aa.setDuration(100);
                        aa.setFillAfter(true);
                        animationSet.addAnimation(aa);
                        ScaleAnimation ca = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, 1, 0.5f, 1, 0.5f);
                        ca.setDuration(350);
                        ca.setInterpolator(getContext(), SESL_TAB_ANIM_INTERPOLATOR);
                        ca.setFillAfter(true);
                        animationSet.addAnimation(ca);
                        mMainTabTouchBackground.startAnimation(animationSet);
                        return;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (mMainTabTouchBackground.getAnimation() == null) {
                            return;
                        }
                        if (mMainTabTouchBackground.getAnimation().hasEnded()) {
                            AlphaAnimation aa_u = new AlphaAnimation(1.0f, 0.0f);
                            aa_u.setDuration(400);
                            aa_u.setFillAfter(true);
                            animationSet.addAnimation(aa_u);
                            mMainTabTouchBackground.startAnimation(animationSet);
                            return;
                        }
                        mMainTabTouchBackground.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation animation) {
                            }

                            public void onAnimationRepeat(Animation animation) {
                            }

                            public void onAnimationEnd(Animation animation) {
                                AnimationSet as_u = new AnimationSet(true);
                                as_u.setFillAfter(true);
                                AlphaAnimation aa_u = new AlphaAnimation(1.0f, 0.0f);
                                aa_u.setDuration(400);
                                aa_u.setFillAfter(true);
                                as_u.addAnimation(aa_u);
                                SeslTabView.this.mMainTabTouchBackground.startAnimation(aa_u);
                            }
                        });
                        return;
                    default:
                        return;
                }
            }
        }

        @Override
        public boolean performClick() {
            if (mIsCallPerformClick) {
                mIsCallPerformClick = false;
                return true;
            }

            final boolean performClick = super.performClick();

            if (mTab != null) {
                playSoundEffect(SoundEffectConstants.CLICK);
                mTab.select();
                return true;
            } else {
                return performClick;
            }

        }

        @Override
        public void setSelected(final boolean selected) {
            if (isEnabled()) {
                final boolean changed = isSelected() != selected;

                super.setSelected(selected);

                if (mTextView != null) {
                    mTextView.setSelected(selected);
                }
                if (mIconView != null) {
                    mIconView.setSelected(selected);
                }
                if (mCustomView != null) {
                    mCustomView.setSelected(selected);
                }
                if (mCustomView == null && mIndicatorView != null) {
                    if (selected) {
                        mIndicatorView.setShow();
                    } else {
                        mIndicatorView.setHideImmediatly();
                    }
                }
            }
        }

        @Override
        public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(event);
            event.setClassName(ActionBar.Tab.class.getName());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setClassName(ActionBar.Tab.class.getName());
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            if (mMainTabTouchBackground != null) {
                mMainTabTouchBackground.setLeft(0);
                mMainTabTouchBackground.setRight(right - left);
                if (mMainTabTouchBackground.getAnimation() != null && mMainTabTouchBackground.getAnimation().hasEnded()) {
                    mMainTabTouchBackground.setAlpha(0.0f);
                }
            }
            seslUpdateBadgePosition();
        }

        @Override
        public void onMeasure(final int origWidthMeasureSpec, final int origHeightMeasureSpec) {
            final int specWidthSize = MeasureSpec.getSize(origWidthMeasureSpec);
            final int specWidthMode = MeasureSpec.getMode(origWidthMeasureSpec);
            final int maxWidth = getTabMaxWidth();

            final int widthMeasureSpec;
            final int heightMeasureSpec = origHeightMeasureSpec;

            if (mRequestedTabWidth != -1) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(mRequestedTabWidth, MeasureSpec.EXACTLY);
            } else if (maxWidth > 0 && (specWidthMode == MeasureSpec.UNSPECIFIED || specWidthSize > maxWidth)) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(mTabMaxWidth, MeasureSpec.AT_MOST);
            } else {
                widthMeasureSpec = origWidthMeasureSpec;
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            if (mTextView != null && mCustomView == null) {
                final Resources resources = getResources();
                float textSize = mTabTextSize;
                checkMaxFontScale(mTextView, (int) textSize);
                int maxLines = mDefaultMaxLines;

                if (mIconView != null && mIconView.getVisibility() == VISIBLE) {
                    maxLines = 1;
                } else if (mTextView != null && mTextView.getLineCount() > 1) {
                    textSize = mTabTextMultiLineSize;
                }

                final float curTextSize = mTextView.getTextSize();
                final int curLineCount = mTextView.getLineCount();
                final int curMaxLines = TextViewCompat.getMaxLines(mTextView);

                if (textSize != curTextSize || (curMaxLines >= 0 && maxLines != curMaxLines)) {
                    boolean updateTextView = true;

                    if (mMode == MODE_FIXED && textSize > curTextSize && curLineCount == 1) {
                        final Layout layout = mTextView.getLayout();
                        if (layout == null || approximateLineWidth(layout, 0, textSize) > getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) {
                            updateTextView = false;
                        }
                    }

                    if (updateTextView) {
                        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                        checkMaxFontScale(mTextView, (int) textSize);
                        mTextView.setMaxLines(maxLines);
                        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                    }
                }
            }
        }

        void setTab(final SeslTab tab) {
            if (tab != mTab) {
                mTab = tab;
                update();
            }
        }

        void reset() {
            setTab(null);
            setSelected(false);
        }

        final void update() {
            final SeslTab tab = mTab;
            final View custom = tab != null ? tab.getCustomView() : null;
            RelativeLayout parentView;
            int height_mode;
            int width_mode = LayoutParams.MATCH_PARENT;
            Drawable drawable;

            if (custom != null) {
                final ViewParent customParent = custom.getParent();
                if (customParent != this) {
                    if (customParent != null) {
                        ((ViewGroup) customParent).removeView(custom);
                    }
                    addView(custom);
                }
                mCustomView = custom;
                if (mTextView != null) {
                    mTextView.setVisibility(GONE);
                }
                if (mIconView != null) {
                    mIconView.setVisibility(GONE);
                    mIconView.setImageDrawable(null);
                }

                mCustomTextView = custom.findViewById(android.R.id.text1);
                if (mCustomTextView != null) {
                    mDefaultMaxLines = TextViewCompat.getMaxLines(mCustomTextView);
                }
                mCustomIconView = custom.findViewById(android.R.id.icon);
            } else {
                if (mCustomView != null) {
                    removeView(mCustomView);
                    mCustomView = null;
                }
                mCustomTextView = null;
                mCustomIconView = null;
            }

            if (mCustomView == null) {
                if (mIconView == null) {
                    ImageView iconView = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.sesl_layout_tab_icon, this, false);
                    addView(iconView, 0);
                    mIconView = iconView;
                }
                if (mTab != null || mTab.mIcon != null || mTextParentView != null) {
                    removeView(mTextParentView);
                    mTextView = null;
                }
                if (mTextView == null) {
                    if (mDepthStyle == 2) {
                        parentView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.sesl_layout_sub_tab_text, this, false);
                        if (mMode == 0) {
                            width_mode = LayoutParams.WRAP_CONTENT;
                        }
                        height_mode = mSubTabIndicatorHeight;
                        mIndicatorView = parentView.findViewById(R.id.indicator);
                        if (mIndicatorView != null || mSubTabSelectedIndicatorColor != -1) {
                            mIndicatorView.setSelectedIndicatorColor(mSubTabSelectedIndicatorColor);
                        }
                    } else {
                        parentView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.sesl_layout_tab_text, this, false);
                        if (mTab.mIcon == null) {
                            height_mode = LayoutParams.MATCH_PARENT;
                        } else {
                            height_mode = LayoutParams.MATCH_PARENT;
                            width_mode = LayoutParams.WRAP_CONTENT;
                        }
                        mIndicatorView = parentView.findViewById(R.id.indicator);
                        if (mIndicatorView != null) {
                            mIndicatorView.setSelectedIndicatorColor(mTabSelectedIndicatorColor);
                        }
                        mMainTabTouchBackground = parentView.findViewById(R.id.main_tab_touch_background);
                        if (mMainTabTouchBackground != null && mTab.mIcon == null) {
                            View view = mMainTabTouchBackground;
                            if (isLightTheme()) {
                                drawable = getContext().getDrawable(R.drawable.sesl_tablayout_maintab_touch_background_light);
                            } else {
                                drawable = getContext().getDrawable(R.drawable.sesl_tablayout_maintab_touch_background_dark);
                            }
                            view.setBackground(drawable);
                            mMainTabTouchBackground.setAlpha(0.0f);
                        }
                    }
                    TextView textView = parentView.findViewById(R.id.title);
                    addView(parentView, width_mode, height_mode);
                    mTextView = textView;
                    mTextParentView = parentView;
                    mDefaultMaxLines = TextViewCompat.getMaxLines(mTextView);
                }
                TextViewCompat.setTextAppearance(mTextView, mTabTextAppearance);
                checkMaxFontScale(mTextView, (int) mTabTextSize);
                if (mTextView != null || mTabTextColors != null) {
                    mTextView.setTextColor(mTabTextColors);
                }
                updateTextAndIcon(mTextView, mIconView);
            } else {
                if (mCustomTextView != null && mCustomIconView != null) {
                    updateTextAndIcon(mCustomTextView, mCustomIconView);
                }
            }

            setSelected(tab != null && tab.isSelected());
        }

        private void updateTextAndIcon(final TextView textView, final ImageView iconView) {
            final Drawable icon = mTab != null ? mTab.getIcon() : null;
            final CharSequence text = mTab != null ? mTab.getText() : null;
            final CharSequence contentDesc = mTab != null ? mTab.getContentDescription() : null;

            if (iconView != null) {
                if (icon != null) {
                    iconView.setImageDrawable(icon);
                    iconView.setVisibility(VISIBLE);
                    setVisibility(VISIBLE);
                } else {
                    iconView.setVisibility(GONE);
                    iconView.setImageDrawable(null);
                }
                iconView.setContentDescription(contentDesc);
            }

            final boolean hasText = !TextUtils.isEmpty(text);
            if (textView != null) {
                if (hasText) {
                    textView.setText(text);
                    textView.setVisibility(VISIBLE);
                    setVisibility(VISIBLE);
                } else {
                    textView.setVisibility(GONE);
                    textView.setText(null);
                }
                textView.setContentDescription(contentDesc);
            }

            if (iconView != null) {
                MarginLayoutParams lp = ((MarginLayoutParams) iconView.getLayoutParams());
                int bottomMargin = 0;
                if (hasText && iconView.getVisibility() == VISIBLE) {
                    bottomMargin = dpToPx(DEFAULT_GAP_TEXT_ICON);
                }
                if (bottomMargin != lp.bottomMargin) {
                    lp.bottomMargin = bottomMargin;
                    iconView.requestLayout();
                }
            }
            SeslTooltip.setTooltipText(this, hasText ? null : contentDesc);
        }

        private float approximateLineWidth(Layout layout, int line, float textSize) {
            return layout.getLineWidth(line) * (textSize / layout.getPaint().getTextSize());
        }
    }

    public static class ViewPagerOnTabSelectedListener implements SeslTabLayout.OnTabSelectedListener {
        private final ViewPager mViewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        @Override
        public void onTabSelected(SeslTabLayout.SeslTab tab) {
            mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(SeslTabLayout.SeslTab tab) { }

        @Override
        public void onTabReselected(SeslTabLayout.SeslTab tab) { }
    }
}
