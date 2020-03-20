package com.mesalabs.cerberus.ui.callback;

import android.os.SystemClock;
import android.view.View;

/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2020
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

public abstract class OnSingleClickListener implements View.OnClickListener {
    public long mLastClickTime;

    public OnSingleClickListener() { }

    public void onClick(View view) {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (uptimeMillis - mLastClickTime > 600L) {
            onSingleClick(view);
        }
        mLastClickTime = uptimeMillis;
    }

    public abstract void onSingleClick(View view);
}
