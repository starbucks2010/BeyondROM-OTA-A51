package com.samsung.android.ui.preference;

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

import java.util.Set;

public abstract class PreferenceDataStore {
    public void putString(String key, String value) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putStringSet(String key, Set<String> values) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putInt(String key, int value) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putLong(String key, long value) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putFloat(String key, float value) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putBoolean(String key, boolean value) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public String getString(String key, String defValue) {
        return defValue;
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return defValues;
    }

    public int getInt(String key, int defValue) {
        return defValue;
    }

    public long getLong(String key, long defValue) {
        return defValue;
    }

    public float getFloat(String key, float defValue) {
        return defValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        return defValue;
    }
}
