package com.samsung.android.ui.preference;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mesalabs.ten.update.R;
import com.samsung.android.ui.recyclerview.widget.DiffUtil;
import com.samsung.android.ui.recyclerview.widget.SeslRecyclerView;

import java.util.ArrayList;
import java.util.List;

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

public class PreferenceGroupAdapter extends SeslRecyclerView.Adapter<PreferenceViewHolder> implements SeslPreference.OnPreferenceChangeInternalListener, SeslPreferenceGroup.PreferencePositionCallback {
    private int mCategoryLayoutId = R.layout.sesl_preference_category;
    private Handler mHandler = new Handler();
    boolean mIsCategoryAfter = false;
    SeslPreference mNextGroupPreference = null;
    SeslPreference mNextPreference = null;
    private SeslPreferenceGroup mPreferenceGroup;
    private List<PreferenceLayout> mPreferenceLayouts;
    private List<SeslPreference> mPreferenceList;
    private List<SeslPreference> mPreferenceListInternal;
    SeslPreference mPrevPreference = null;
    private PreferenceLayout mTempPreferenceLayout = new PreferenceLayout();
    private Runnable mSyncRunnable = new Runnable() {
        @Override
        public void run() {
            syncMyPreferences();
        }
    };

    public PreferenceGroupAdapter(SeslPreferenceGroup preferenceGroup) {
        mPreferenceGroup = preferenceGroup;
        mPreferenceGroup.setOnPreferenceChangeInternalListener(this);

        mPreferenceList = new ArrayList<>();
        mPreferenceListInternal = new ArrayList<>();
        mPreferenceLayouts = new ArrayList<>();

        if (mPreferenceGroup instanceof PreferenceScreen) {
            setHasStableIds(((PreferenceScreen) mPreferenceGroup).shouldUseGeneratedIds());
        } else {
            setHasStableIds(true);
        }

        syncMyPreferences();
    }

    private void syncMyPreferences() {
        for (final SeslPreference preference : mPreferenceListInternal) {
            preference.setOnPreferenceChangeInternalListener(null);
        }
        final List<SeslPreference> fullPreferenceList = new ArrayList<>(mPreferenceListInternal.size());
        flattenPreferenceGroup(fullPreferenceList, mPreferenceGroup);

        final List<SeslPreference> visiblePreferenceList = new ArrayList<>(fullPreferenceList.size());
        for (final SeslPreference preference : fullPreferenceList) {
            if (preference.isVisible()) {
                visiblePreferenceList.add(preference);
            }
        }

        final List<SeslPreference> oldVisibleList = mPreferenceList;
        mPreferenceList = visiblePreferenceList;
        mPreferenceListInternal = fullPreferenceList;

        final PreferenceManager preferenceManager = mPreferenceGroup.getPreferenceManager();
        if (preferenceManager != null && preferenceManager.getPreferenceComparisonCallback() != null) {
            final PreferenceManager.PreferenceComparisonCallback comparisonCallback = preferenceManager.getPreferenceComparisonCallback();
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return oldVisibleList.size();
                }

                @Override
                public int getNewListSize() {
                    return visiblePreferenceList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return comparisonCallback.arePreferenceItemsTheSame(oldVisibleList.get(oldItemPosition), visiblePreferenceList.get(newItemPosition));
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return comparisonCallback.arePreferenceContentsTheSame(oldVisibleList.get(oldItemPosition), visiblePreferenceList.get(newItemPosition));
                }
            });

            result.dispatchUpdatesTo(this);
        } else {
            notifyDataSetChanged();
        }

        for (final SeslPreference preference : fullPreferenceList) {
            preference.clearWasDetached();
        }
    }

    private void flattenPreferenceGroup(List<SeslPreference> preferences, SeslPreferenceGroup group) {
        group.sortPreferences();

        final int groupSize = group.getPreferenceCount();
        for (int i = 0; i < groupSize; i++) {

            final SeslPreference preference = group.getPreference(i);
            if (i == groupSize - 1) {
                mNextPreference = null;
                if (mIsCategoryAfter && preference == mNextGroupPreference) {
                    mNextGroupPreference = null;
                }
            } else {
                mNextPreference = group.getPreference(i + 1);
                if (preference == mNextGroupPreference) {
                    mNextGroupPreference = null;
                }
            }

            if (preference instanceof PreferenceCategory) {
                if (!preference.mIsRoundChanged) {
                    preference.seslSetSubheaderRoundedBg(15);
                }
                preference.mIsSolidRoundedCorner = group.mIsSolidRoundedCorner;
                preference.seslSetSubheaderColor(group.mSubheaderColor);
            }
            preferences.add(preference);

            if ((preference instanceof PreferenceCategory) && TextUtils.isEmpty(preference.getTitle()) && mCategoryLayoutId == preference.getLayoutResource()) {
                preference.setLayoutResource(R.layout.sesl_preference_category_empty);
            }
            addPreferenceClassName(preference);

            if (preference instanceof SeslPreferenceGroup) {
                final SeslPreferenceGroup preferenceAsGroup = (SeslPreferenceGroup) preference;
                if (preferenceAsGroup.isOnSameScreenAsChildren()) {
                    mNextGroupPreference = mNextPreference;
                    flattenPreferenceGroup(preferences, preferenceAsGroup);
                }
            }

            preference.setOnPreferenceChangeInternalListener(this);
        }
    }

    private PreferenceLayout createPreferenceLayout(SeslPreference preference, PreferenceLayout in) {
        PreferenceLayout pl = in != null? in : new PreferenceLayout();
        pl.name = preference.getClass().getName();
        pl.resId = preference.getLayoutResource();
        pl.widgetResId = preference.getWidgetLayoutResource();
        return pl;
    }

    private void addPreferenceClassName(SeslPreference preference) {
        final PreferenceLayout pl = createPreferenceLayout(preference, null);
        if (!mPreferenceLayouts.contains(pl)) {
            mPreferenceLayouts.add(pl);
        }
    }

    @Override
    public int getItemCount() {
        return mPreferenceList.size();
    }

    public SeslPreference getItem(int position) {
        if (position < 0 || position >= getItemCount()) return null;
        return mPreferenceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (!hasStableIds()) {
            return SeslRecyclerView.NO_ID;
        }
        return this.getItem(position).getId();
    }

    @Override
    public void onPreferenceChange(SeslPreference preference) {
        final int index = mPreferenceList.indexOf(preference);
        if (index != -1) {
            notifyItemChanged(index, preference);
        }
    }

    @Override
    public void onPreferenceHierarchyChange(SeslPreference preference) {
        mHandler.removeCallbacks(mSyncRunnable);
        mHandler.post(mSyncRunnable);
    }

    @Override
    public int getItemViewType(int position) {
        final SeslPreference preference = this.getItem(position);

        mTempPreferenceLayout = createPreferenceLayout(preference, mTempPreferenceLayout);

        int viewType = mPreferenceLayouts.indexOf(mTempPreferenceLayout);
        if (viewType != -1) {
            return viewType;
        } else {
            viewType = mPreferenceLayouts.size();
            mPreferenceLayouts.add(new PreferenceLayout(mTempPreferenceLayout));
            return viewType;
        }
    }

    public PreferenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final PreferenceLayout pl = mPreferenceLayouts.get(viewType);
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final View view = inflater.inflate(pl.resId, parent, false);

        final ViewGroup widgetFrame = (ViewGroup) view.findViewById(android.R.id.widget_frame);
        if (widgetFrame != null) {
            if (pl.widgetResId != 0) {
                inflater.inflate(pl.widgetResId, widgetFrame);
            } else {
                widgetFrame.setVisibility(View.GONE);
            }
        }

        return new PreferenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder, int position) {
        final SeslPreference preference = getItem(position);
        preference.onBindViewHolder(holder);
    }

    @Override
    public int getPreferenceAdapterPosition(String key) {
        final int size = mPreferenceList.size();
        for (int i = 0; i < size; i++) {
            final SeslPreference candidate = mPreferenceList.get(i);
            if (TextUtils.equals(key, candidate.getKey())) {
                return i;
            }
        }
        return SeslRecyclerView.NO_POSITION;
    }

    @Override
    public int getPreferenceAdapterPosition(SeslPreference preference) {
        final int size = mPreferenceList.size();
        for (int i = 0; i < size; i++) {
            final SeslPreference candidate = mPreferenceList.get(i);
            if (candidate != null && candidate.equals(preference)) {
                return i;
            }
        }
        return SeslRecyclerView.NO_POSITION;
    }


    private static class PreferenceLayout {
        private String name;
        private int resId;
        private int widgetResId;

        public PreferenceLayout() { }

        public PreferenceLayout(PreferenceLayout other) {
            resId = other.resId;
            widgetResId = other.widgetResId;
            name = other.name;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof PreferenceLayout)) {
                return false;
            }
            final PreferenceLayout other = (PreferenceLayout) o;
            return resId == other.resId
                    && widgetResId == other.widgetResId
                    && TextUtils.equals(name, other.name);
        }

        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + resId;
            result = 31 * result + widgetResId;
            result = 31 * result + name.hashCode();
            return result;
        }
    }
}
