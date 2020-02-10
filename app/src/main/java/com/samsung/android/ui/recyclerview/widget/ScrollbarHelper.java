package com.samsung.android.ui.recyclerview.widget;

import android.view.View;

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

class ScrollbarHelper {
    static int computeScrollOffset(SeslRecyclerView.State state, OrientationHelper orientation, View startChild, View endChild, SeslRecyclerView.LayoutManager lm, boolean smoothScrollbarEnabled, boolean reverseLayout) {
        if (lm.getChildCount() == 0 || state.getItemCount() == 0 || startChild == null || endChild == null) {
            return 0;
        }
        final int minPosition = Math.min(lm.getPosition(startChild), lm.getPosition(endChild));
        final int maxPosition = Math.max(lm.getPosition(startChild), lm.getPosition(endChild));
        final int itemsBefore = reverseLayout ? Math.max(0, state.getItemCount() - maxPosition - 1) : Math.max(0, minPosition);
        if (!smoothScrollbarEnabled) {
            return itemsBefore;
        }
        final int laidOutArea = Math.abs(orientation.getDecoratedEnd(endChild) - orientation.getDecoratedStart(startChild));
        final int itemRange = Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1;
        final float avgSizePerRow = (float) laidOutArea / itemRange;

        return Math.round(itemsBefore * avgSizePerRow + (orientation.getStartAfterPadding() - orientation.getDecoratedStart(startChild)));
    }

    static int computeScrollExtent(SeslRecyclerView.State state, OrientationHelper orientation, View startChild, View endChild, SeslRecyclerView.LayoutManager lm, boolean smoothScrollbarEnabled) {
        if (lm.getChildCount() == 0 || state.getItemCount() == 0 || startChild == null || endChild == null) {
            return 0;
        }
        if (!smoothScrollbarEnabled) {
            return Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1;
        }
        final int extend = orientation.getDecoratedEnd(endChild) - orientation.getDecoratedStart(startChild);
        return Math.min(orientation.getTotalSpace(), extend);
    }

    static int computeScrollRange(SeslRecyclerView.State state, OrientationHelper orientation, View startChild, View endChild, SeslRecyclerView.LayoutManager lm, boolean smoothScrollbarEnabled) {
        if (lm.getChildCount() == 0 || state.getItemCount() == 0 || startChild == null || endChild == null) {
            return 0;
        }
        if (!smoothScrollbarEnabled) {
            return state.getItemCount();
        }
        final int laidOutArea = orientation.getDecoratedEnd(endChild) - orientation.getDecoratedStart(startChild);
        final int laidOutRange = Math.abs(lm.getPosition(startChild) - lm.getPosition(endChild)) + 1;
        return (int) ((float) laidOutArea / laidOutRange * state.getItemCount());
    }
}
