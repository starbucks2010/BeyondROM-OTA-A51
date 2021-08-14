package com.samsung.android.ui.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import com.mesalabs.ten.update.R;
import androidx.appcompat.widget.SeslAppCompatSpinner;

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

public class DropDownPreference extends SeslListPreference {
    private final ArrayAdapter mAdapter;
    private final Context mContext;
    private SeslAppCompatSpinner mSpinner;

    public DropDownPreference(Context context) {
        this(context, null);
    }

    public DropDownPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dropdownPreferenceStyle);
    }

    public DropDownPreference(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public DropDownPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        mAdapter = createAdapter();
        updateEntries();
    }

    @Override
    protected void onClick() {
        mSpinner.performClick();
    }

    protected ArrayAdapter createAdapter() {
        return new ArrayAdapter(mContext, R.layout.sesl_simple_spinner_dropdown_item);
    }

    private void updateEntries() {
        mAdapter.clear();
        if (getEntries() != null) {
            for (CharSequence c : getEntries()) {
                mAdapter.add(c.toString());
            }
        }
    }

    public int findSpinnerIndexOfValue(String value) {
        CharSequence[] entryValues = getEntryValues();
        if (value != null && entryValues != null) {
            for (int i = entryValues.length - 1; i >= 0; i--) {
                if (entryValues[i].equals(value)) {
                    return i;
                }
            }
        }
        return SeslAppCompatSpinner.INVALID_POSITION;
    }

    @Override
    protected void notifyChanged() {
        super.notifyChanged();
        mAdapter.notifyDataSetChanged();
    }

    public void onBindViewHolder(PreferenceViewHolder view) {
        mSpinner = (SeslAppCompatSpinner) view.itemView.findViewById(R.id.spinner);
        mSpinner.setSoundEffectsEnabled(false);
        if (!mAdapter.equals(mSpinner.getAdapter())) {
            mSpinner.setAdapter(mAdapter);
        }
        mSpinner.setOnItemSelectedListener(mItemSelectedListener);
        mSpinner.setSelection(findSpinnerIndexOfValue(getValue()));
        super.onBindViewHolder(view);
    }


    private final OnItemSelectedListener mItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            if (position >= 0) {
                String value = getEntryValues()[position].toString();
                if (!value.equals(getValue()) && callChangeListener(value)) {
                    setValue(value);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };
}
