package com.samsung.android.ui.view.animation;

import android.annotation.SuppressLint;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;

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

public class SeslAnimationUtils {
    public static final Interpolator ELASTIC_40 = new SeslElasticInterpolator(1.0f, 0.8f);
    public static final Interpolator ELASTIC_50 = new SeslElasticInterpolator(1.0f, 0.7f);
    @SuppressLint({"NewApi"})
    public static final Interpolator SINE_IN_OUT_70 = new PathInterpolator(0.33f, 0.0f, 0.3f, 1.0f);
    @SuppressLint({"NewApi"})
    public static final Interpolator SINE_IN_OUT_80 = new PathInterpolator(0.33f, 0.0f, 0.2f, 1.0f);
    @SuppressLint({"NewApi"})
    public static final Interpolator SINE_IN_OUT_90 = new PathInterpolator(0.33f, 0.0f, 0.1f, 1.0f);
    @SuppressLint({"NewApi"})
    public static final Interpolator SINE_OUT_80 = new PathInterpolator(0.17f, 0.17f, 0.2f, 1.0f);
}
