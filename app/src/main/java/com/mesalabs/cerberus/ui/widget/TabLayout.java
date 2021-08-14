package com.mesalabs.cerberus.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import java.util.ArrayList;

import com.mesalabs.ten.update.R;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.cerberus.utils.ViewUtils;
import com.samsung.android.ui.tabs.SeslTabLayout;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2020
 * Originally coded by Samsung. All rights reserved to their respective owners.
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

public class TabLayout extends SeslTabLayout implements View.OnSystemUiVisibilityChangeListener {
    private Activity mActivity;
    private boolean mIsResumed = false;
    private ArrayList<TextView> mTextViews;

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setup(Activity activity) {
        mActivity = activity;
        mTextViews = new ArrayList<>();

        for (int tabPosition = 0; tabPosition < getTabCount(); tabPosition++) {
            Tab tab = getTabAt(tabPosition);
            ViewGroup tabView = (ViewGroup) getTabView(tabPosition);
            if (!(tab == null || tabView == null)) {
                TextView textView = tab.seslGetTextView();
                mTextViews.add(textView);
                if (textView != null) {
                    tabView.setContentDescription(textView.getText() + " " + getResources().getString(R.string.mesa_tab_tts, new Object[]{tabPosition + 1, getTabCount()}));
                }
                tabView.setPointerIcon(PointerIcon.getSystemIcon(getContext(), 1000));
            }
        }
        invalidateTabLayout();
    }

    private void invalidateTabLayout() {
        ArrayList<Float> tabTextWidthList = new ArrayList<>();
        float tabTextWidthSum = 0.0f;
        for (int tabPosition = 0; tabPosition < getTabCount(); tabPosition++) {
            float width = getTabTextWidth((TextView) mTextViews.get(tabPosition));
            tabTextWidthList.add(width);
            tabTextWidthSum += width;
        }
        addTabPaddingValue(tabTextWidthList, tabTextWidthSum);
        // hax
        getTabView(getSelectedTabPosition()).setSelected(false);
        getTabView(getSelectedTabPosition()).setSelected(true);
        setScrollPosition(getSelectedTabPosition(), 0.0f, true);
    }

    private float getTabTextWidth(TextView textView) {
        return textView.getPaint().measureText(textView.getText().toString());
    }

    private void addTabPaddingValue(ArrayList<Float> tabTextWidthList, float tabTextWidthSum) {
        float tabTextPadding = (float) getResources().getDimensionPixelSize(R.dimen.mesa_tab_padding);
        float tabTextPaddingSum = tabTextPadding * 8.0f;
        float tabLayoutPadding = (float) getResources().getDimensionPixelSize(R.dimen.mesa_tab_layout_padding);

        Window window = mActivity.getWindow();
        Point size = new Point();
        if (ViewUtils.isVisibleNaviBar(getContext()) || Utils.isInMultiWindowMode(mActivity) || Utils.isInSamsungDeXMode(getContext())) {
            window.getWindowManager().getDefaultDisplay().getSize(size);
        } else {
            window.getWindowManager().getDefaultDisplay().getRealSize(size);
        }

        float screenWidthPixels = (float) size.x;
        if (!ViewUtils.isMultiWindowMinSize(mActivity, 480, true)) {
            float tabLayoutPaddingMax = screenWidthPixels * 0.125f;
            float tabLayoutPaddingMin = ((screenWidthPixels - tabTextWidthSum) - tabTextPaddingSum) / 2.0f;
            if (tabLayoutPaddingMin >= tabLayoutPaddingMax) {
                tabLayoutPadding = tabLayoutPaddingMax;
            } else if (tabLayoutPadding < tabLayoutPaddingMin) {
                tabLayoutPadding = tabLayoutPaddingMin;
            }
        }

        float widthPixels = screenWidthPixels - (2.0f * tabLayoutPadding);
        if (tabTextWidthSum + tabTextPaddingSum < widthPixels) {
            float paddingLeftRight = (float) Math.ceil((double) (((widthPixels - (tabTextWidthSum + tabTextPaddingSum)) / 8.0f) + tabTextPadding));
            float paddingLastTab = (widthPixels - tabTextWidthSum) - (8.0f * paddingLeftRight);
            for (int i = 0; i < tabTextWidthList.size(); i++) {
                if (paddingLastTab == 0.0f || i != 3) {
                    getTabView(i).setMinimumWidth((int) ((float) tabTextWidthList.get(i) + (2.0f * paddingLeftRight)));
                } else {
                    getTabView(i).setMinimumWidth((int) ((float) tabTextWidthList.get(i) + (2.0f * paddingLeftRight) + paddingLastTab));
                }
            }
        } else {
            for (int i2 = 0; i2 < tabTextWidthList.size(); i2++) {
                getTabView(i2).setMinimumWidth((int) ((float) tabTextWidthList.get(i2) + (2.0f * tabTextPadding)));
            }
        }

        ((MarginLayoutParams) getLayoutParams()).setMargins((int) tabLayoutPadding, 0, (int) tabLayoutPadding, 0);
        requestLayout();
    }

    private View getTabView(int position) {
        ViewGroup viewGroup = getTabViewGroup();
        if (viewGroup == null || viewGroup.getChildCount() <= position) {
            return null;
        }
        return viewGroup.getChildAt(position);
    }

    private ViewGroup getTabViewGroup() {
        if (getChildCount() <= 0) {
            return null;
        }
        View view = getChildAt(0);
        if (view == null || !(view instanceof ViewGroup)) {
            return null;
        }
        return (ViewGroup) view;
    }

    public void setTabLayoutEnabled(boolean enabled) {
        float f;
        setEnabled(enabled);
        for (int tabPosition = 0; tabPosition < getTabCount(); tabPosition++) {
            ViewGroup tabView = (ViewGroup) getTabView(tabPosition);
            if (tabView != null) {
                tabView.setEnabled(enabled);
                if (enabled) {
                    f = 1.0f;
                } else {
                    f = 0.4f;
                }
                tabView.setAlpha(f);
            }
        }
    }

    public void setResumeStatus(boolean isResumed) {
        mIsResumed = isResumed;
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if (mIsResumed) {
            invalidateTabLayout();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        invalidateTabLayout();
    }

}
