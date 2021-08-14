package com.samsung.android.ui.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.TypedArrayUtils;

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

public abstract class SeslPreferenceGroup extends SeslPreference {
    private boolean mAttachedToHierarchy = false;
    private int mCurrentPreferenceOrder = 0;
    private final Handler mHandler = new Handler();
    private final SimpleArrayMap<String, Long> mIdRecycleCache = new SimpleArrayMap<>();
    private boolean mOrderingAsAdded = true;
    private List<SeslPreference> mPreferenceList = new ArrayList();
    private final Runnable mClearRecycleCacheRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (this) {
                mIdRecycleCache.clear();
            }
        }
    };

    public SeslPreferenceGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeslPreferenceGroup, defStyleAttr, defStyleRes);
        mOrderingAsAdded = TypedArrayUtils.getBoolean(a, R.styleable.SeslPreferenceGroup_orderingFromXml, R.styleable.SeslPreferenceGroup_orderingFromXml, true);
        a.recycle();
    }

    public SeslPreferenceGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeslPreferenceGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setOrderingAsAdded(boolean orderingAsAdded) {
        mOrderingAsAdded = orderingAsAdded;
    }

    public void addItemFromInflater(SeslPreference preference) {
        addPreference(preference);
    }

    public int getPreferenceCount() {
        return mPreferenceList.size();
    }

    public SeslPreference getPreference(int index) {
        return mPreferenceList.get(index);
    }

    public boolean addPreference(SeslPreference preference) {
        if (mPreferenceList.contains(preference)) {
            return true;
        }

        if (preference.getOrder() == DEFAULT_ORDER) {
            if (mOrderingAsAdded) {
                preference.setOrder(mCurrentPreferenceOrder++);
            }

            if (preference instanceof SeslPreferenceGroup) {
                ((SeslPreferenceGroup)preference).setOrderingAsAdded(mOrderingAsAdded);
            }
        }

        int insertionIndex = Collections.binarySearch(mPreferenceList, preference);
        if (insertionIndex < 0) {
            insertionIndex = insertionIndex * -1 - 1;
        }

        if (!onPrepareAddPreference(preference)) {
            return false;
        }

        synchronized(this) {
            mPreferenceList.add(insertionIndex, preference);
        }

        final PreferenceManager preferenceManager = getPreferenceManager();
        final String key = preference.getKey();
        final long id;
        if (key != null && mIdRecycleCache.containsKey(key)) {
            id = mIdRecycleCache.get(key);
            mIdRecycleCache.remove(key);
        } else {
            id = preferenceManager.getNextId();
        }
        preference.onAttachedToHierarchy(preferenceManager, id);
        preference.assignParent(this);

        if (mAttachedToHierarchy) {
            preference.onAttached();
        }

        notifyHierarchyChanged();

        return true;
    }

    public boolean removePreference(SeslPreference preference) {
        boolean returnValue = removePreferenceInt(preference);
        notifyHierarchyChanged();
        return returnValue;
    }

    private boolean removePreferenceInt(SeslPreference preference) {
        synchronized(this) {
            preference.onPrepareForRemoval();
            if (preference.getParent() == this) {
                preference.assignParent(null);
            }
            boolean success = mPreferenceList.remove(preference);
            if (success) {
                final String key = preference.getKey();
                if (key != null) {
                    mIdRecycleCache.put(key, preference.getId());
                    mHandler.removeCallbacks(mClearRecycleCacheRunnable);
                    mHandler.post(mClearRecycleCacheRunnable);
                }
                if (mAttachedToHierarchy) {
                    preference.onDetached();
                }
            }

            return success;
        }
    }

    protected boolean onPrepareAddPreference(SeslPreference preference) {
        preference.onParentChanged(this, shouldDisableDependents());
        return true;
    }

    public SeslPreference findPreference(CharSequence key) {
        if (TextUtils.equals(getKey(), key)) {
            return this;
        }
        final int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            final SeslPreference preference = getPreference(i);
            final String curKey = preference.getKey();

            if (curKey != null && curKey.equals(key)) {
                return preference;
            }

            if (preference instanceof SeslPreferenceGroup) {
                final SeslPreference returnedPreference = ((SeslPreferenceGroup)preference).findPreference(key);
                if (returnedPreference != null) {
                    return returnedPreference;
                }
            }
        }

        return null;
    }

    protected boolean isOnSameScreenAsChildren() {
        return true;
    }

    @Override
    public void onAttached() {
        super.onAttached();

        mAttachedToHierarchy = true;

        final int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            getPreference(i).onAttached();
        }
    }

    @Override
    public void onDetached() {
        super.onDetached();

        mAttachedToHierarchy = false;

        final int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            getPreference(i).onDetached();
        }
    }

    @Override
    public void notifyDependencyChange(boolean disableDependents) {
        super.notifyDependencyChange(disableDependents);

        final int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            getPreference(i).onParentChanged(this, disableDependents);
        }
    }

    void sortPreferences() {
        synchronized (this) {
            Collections.sort(mPreferenceList);
        }
    }

    @Override
    protected void dispatchSaveInstanceState(Bundle container) {
        super.dispatchSaveInstanceState(container);

        final int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            getPreference(i).dispatchSaveInstanceState(container);
        }
    }

    @Override
    protected void dispatchRestoreInstanceState(Bundle container) {
        super.dispatchRestoreInstanceState(container);

        final int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            getPreference(i).dispatchRestoreInstanceState(container);
        }
    }


    public interface PreferencePositionCallback {
        int getPreferenceAdapterPosition(SeslPreference preference);

        int getPreferenceAdapterPosition(String str);
    }
}
