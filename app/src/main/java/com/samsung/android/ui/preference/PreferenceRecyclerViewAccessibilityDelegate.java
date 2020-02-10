package com.samsung.android.ui.preference;

import android.os.Bundle;
import android.view.View;

import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import com.samsung.android.ui.recyclerview.widget.SeslRecyclerView;
import com.samsung.android.ui.recyclerview.widget.SeslRecyclerViewAccessibilityDelegate;

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

public class PreferenceRecyclerViewAccessibilityDelegate extends SeslRecyclerViewAccessibilityDelegate {
    final AccessibilityDelegateCompat mDefaultItemDelegate = super.getItemDelegate();
    final SeslRecyclerView mRecyclerView;

    public PreferenceRecyclerViewAccessibilityDelegate(SeslRecyclerView recyclerView) {
        super(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public AccessibilityDelegateCompat getItemDelegate() {
        return mItemDelegate;
    }

    final AccessibilityDelegateCompat mItemDelegate = new AccessibilityDelegateCompat() {
        @Override
        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfoCompat info) {
            mDefaultItemDelegate.onInitializeAccessibilityNodeInfo(host, info);
        }

        @Override
        public boolean performAccessibilityAction(View host, int action, Bundle args) {
            return mDefaultItemDelegate.performAccessibilityAction(host, action, args);
        }
    };
}
