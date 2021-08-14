package com.samsung.android.ui.preference;

import android.util.SparseArray;
import android.view.View;

import androidx.preference.AndroidResources;

import com.mesalabs.ten.update.R;
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

public class PreferenceViewHolder extends SeslRecyclerView.ViewHolder {
    private final SparseArray<View> mCachedViews = new SparseArray<>(4);
    private boolean mDividerAllowedAbove;
    private boolean mDividerAllowedBelow;
    boolean mDrawBackground = false;
    int mDrawCorners;
    boolean mSubheaderRound = false;

    PreferenceViewHolder(View itemView) {
        super(itemView);

        mCachedViews.put(android.R.id.title, itemView.findViewById(android.R.id.title));
        mCachedViews.put(android.R.id.summary, itemView.findViewById(android.R.id.summary));
        mCachedViews.put(android.R.id.icon, itemView.findViewById(android.R.id.icon));
        mCachedViews.put(R.id.icon_frame, itemView.findViewById(R.id.icon_frame));
        mCachedViews.put(AndroidResources.ANDROID_R_ICON_FRAME, itemView.findViewById(AndroidResources.ANDROID_R_ICON_FRAME));
    }

    public View findViewById(int id) {
        final View cachedView = mCachedViews.get(id);
        if (cachedView != null) {
            return cachedView;
        } else {
            final View v = itemView.findViewById(id);
            if (v != null) {
                mCachedViews.put(id, v);
            }
            return v;
        }
    }

    void seslSetPreferenceBackgroundType(boolean draw, int corners, boolean subheaderRound) {
        mDrawBackground = draw;
        mDrawCorners = corners;
        mSubheaderRound = subheaderRound;
    }

    public boolean isDividerAllowedAbove() {
        return mDividerAllowedAbove;
    }

    public void setDividerAllowedAbove(boolean allowed) {
        mDividerAllowedAbove = allowed;
    }

    public boolean isDividerAllowedBelow() {
        return mDividerAllowedBelow;
    }

    public void setDividerAllowedBelow(boolean allowed) {
        mDividerAllowedBelow = allowed;
    }

    public int seslGetDrawCorners() {
        return mDrawCorners;
    }

    public boolean seslIsDrawSubheaderRound() {
        return mSubheaderRound;
    }
}
