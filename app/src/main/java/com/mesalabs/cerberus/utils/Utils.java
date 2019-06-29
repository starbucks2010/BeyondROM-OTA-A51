package com.mesalabs.cerberus.utils;

import android.content.Context;
import android.util.TypedValue;

import com.mesalabs.cerberus.R;


/*
 * Cerberus Core App
 *
 * Coded by BlackMesa @2019
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

public class Utils {

    public static boolean isNightMode(Context mContext) {
        TypedValue outValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.isLightTheme, outValue, true);

        return outValue.data == 0;
    }

}
