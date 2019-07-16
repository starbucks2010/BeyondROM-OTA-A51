package com.mesalabs.cerberus.ui.widget.appbarbehavior;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;

import com.mesalabs.cerberus.utils.Utils;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2019
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

public class ScrollingViewBehavior extends HeaderScrollingViewBehavior {

    public ScrollingViewBehavior() {}

    public ScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOverlayTop(0);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    @Override
    public boolean onRequestChildRectangleOnScreen(CoordinatorLayout parent, View child, Rect rectangle, boolean immediate) {
        final AppBarLayout header = findFirstDependency(parent.getDependencies(child));
        if (header != null) {
            rectangle.offset(child.getLeft(), child.getTop());

            final Rect parentRect = mTempRect1;
            parentRect.set(0, 0, parent.getWidth(), parent.getHeight());

            if (!parentRect.contains(rectangle)) {
                header.setExpanded(false, !immediate);
                return true;
            }
        }
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarBehavior) {
            final AppBarBehavior ablBehavior = (AppBarBehavior) behavior;
            ViewCompat.offsetTopAndBottom(child, (dependency.getBottom() - child.getTop()) + ablBehavior.mOffsetDelta + getVerticalLayoutGap() - getOverlapPixelsForOffset(dependency));
        }
    }

    @Override
    float getOverlapRatioForOffset(final View header) {
        if (header instanceof AppBarLayout) {
            final AppBarLayout abl = (AppBarLayout) header;
            final int totalScrollRange = abl.getTotalScrollRange();
            final int preScrollDown = (int) Utils.genericInvokeMethod(abl, "getDownNestedPreScrollRange");
            final int offset = getAppBarLayoutOffset(abl);

            if (preScrollDown != 0 && (totalScrollRange + offset) <= preScrollDown) {
                return 0;
            } else {
                final int availScrollRange = totalScrollRange - preScrollDown;
                if (availScrollRange != 0) {
                    return 1f + (offset / (float) availScrollRange);
                }
            }
        }
        return 0f;
    }

    private static int getAppBarLayoutOffset(AppBarLayout abl) {
        final CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) abl.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarBehavior) {
            return ((AppBarBehavior) behavior).getTopBottomOffsetForScrollingSibling();
        }
        return 0;
    }

    @Override
    AppBarLayout findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (view instanceof AppBarLayout) {
                return (AppBarLayout) view;
            }
        }
        return null;
    }

    @Override
    int getScrollRange(View v) {
        if (v instanceof AppBarLayout) {
            return ((AppBarLayout) v).getTotalScrollRange();
        } else {
            return super.getScrollRange(v);
        }
    }
}