package com.samsung.android.ui.view.animation;

import android.view.animation.Interpolator;

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

public class SeslElasticInterpolator implements Interpolator {
    public static final Interpolator ELASTIC_40 = new SeslElasticInterpolator(1.0f, 0.8f);
    public static final Interpolator ELASTIC_50 = new SeslElasticInterpolator(1.0f, 0.7f);
    private float mAmplitude = 1.0f;
    private float mPeriod = 0.2f;

    public SeslElasticInterpolator(float amplitude, float period) {
        mAmplitude = amplitude;
        mPeriod = period;
    }

    public float getInterpolation(float t) {
        return out(t, mAmplitude, mPeriod);
    }

    private float out(float t, float a, float p) {
        float s;
        if (t == 0.0f) {
            return 0.0f;
        }
        if (t >= 1.0f) {
            return 1.0f;
        }
        if (p == 0.0f) {
            p = 0.3f;
        }
        if (a == 0.0f || a < 1.0f) {
            a = 1.0f;
            s = p / 4.0f;
        } else {
            s = (float) (Math.asin((double) (1.0f / a)) * (((double) p) / 6.283185307179586d));
        }
        return (float) ((((double) a) * Math.pow(2.0d, (double) (-10.0f * t)) * Math.sin((((double) (t - s)) * 6.283185307179586d) / ((double) p))) + 1.0d);
    }
}
