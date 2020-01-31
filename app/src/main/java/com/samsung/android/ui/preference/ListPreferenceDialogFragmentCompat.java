package com.samsung.android.ui.preference;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.samsung.android.ui.app.SeslAlertDialog;

import java.util.ArrayList;

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

public class ListPreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat {
    private static final String SAVE_STATE_INDEX = "ListPreferenceDialogFragment.index";
    private static final String SAVE_STATE_ENTRIES = "ListPreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "ListPreferenceDialogFragment.entryValues";
    private int mClickedDialogEntryIndex;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    public static ListPreferenceDialogFragmentCompat newInstance(String key) {
        final ListPreferenceDialogFragmentCompat fragment = new ListPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            final SeslListPreference preference = getListPreference();

            if (preference.getEntries() == null || preference.getEntryValues() == null) {
                throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
            }

            mClickedDialogEntryIndex = preference.findIndexOfValue(preference.getValue());
            mEntries = preference.getEntries();
            mEntryValues = preference.getEntryValues();
        } else {
            mClickedDialogEntryIndex = savedInstanceState.getInt(SAVE_STATE_INDEX, 0);
            mEntries = getCharSequenceArray(savedInstanceState, SAVE_STATE_ENTRIES);
            mEntryValues = getCharSequenceArray(savedInstanceState, SAVE_STATE_ENTRY_VALUES);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_INDEX, mClickedDialogEntryIndex);
        putCharSequenceArray(outState, SAVE_STATE_ENTRIES, mEntries);
        putCharSequenceArray(outState, SAVE_STATE_ENTRY_VALUES, mEntryValues);
    }

    private static void putCharSequenceArray(Bundle out, String key, CharSequence[] entries) {
        final ArrayList<String> stored = new ArrayList<>(entries.length);

        for (final CharSequence cs : entries) {
            stored.add(cs.toString());
        }

        out.putStringArrayList(key, stored);
    }

    private static CharSequence[] getCharSequenceArray(Bundle in, String key) {
        final ArrayList<String> stored = in.getStringArrayList(key);

        return stored == null ? null : stored.toArray(new CharSequence[stored.size()]);
    }

    private SeslListPreference getListPreference() {
        return (SeslListPreference) getPreference();
    }

    @Override
    protected void onPrepareDialogBuilder(SeslAlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        builder.setSingleChoiceItems(mEntries, mClickedDialogEntryIndex,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mClickedDialogEntryIndex = which;

                        ListPreferenceDialogFragmentCompat.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });

        builder.setPositiveButton(null, null);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        final SeslListPreference preference = getListPreference();
        if (positiveResult && mClickedDialogEntryIndex >= 0) {
            String value = mEntryValues[mClickedDialogEntryIndex].toString();
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
            }
        }
    }
}
