package com.mesalabs.cerberus.ui.preference;

import android.content.Context;
import android.util.AttributeSet;

import com.mesalabs.on.update.R;
import com.samsung.android.ui.preference.SeslPreference;

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

public class CreditsPreference extends SeslPreference {
    public CreditsPreference(Context context) {
        this(context, null);
    }

    public CreditsPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CreditsPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.mesa_view_creditspage_item_layout);
    }
}
