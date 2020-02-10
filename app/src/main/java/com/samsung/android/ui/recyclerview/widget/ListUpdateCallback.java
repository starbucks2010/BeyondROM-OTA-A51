package com.samsung.android.ui.recyclerview.widget;

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

public interface ListUpdateCallback {
    void onInserted(int position, int count);

    void onRemoved(int position, int count);

    void onMoved(int fromPosition, int toPosition);

    void onChanged(int position, int count, Object payload);
}
