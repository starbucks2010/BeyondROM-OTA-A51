package com.samsung.android.ui.picker.widget;

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

public class SeslRecentColorInfo {
    private Integer mCurrentColor = null;
    private Integer mNewColor = null;
    private ArrayList<Integer> mRecentColorInfo = new ArrayList();
    private Integer mSelectedColor = null;

    public SeslRecentColorInfo() {
    }

    public Integer getCurrentColor() {
        return this.mCurrentColor;
    }

    public Integer getNewColor() {
        return this.mNewColor;
    }

    public ArrayList<Integer> getRecentColorInfo() {
        return this.mRecentColorInfo;
    }

    public Integer getSelectedColor() {
        return this.mSelectedColor;
    }

    public void initRecentColorInfo(int[] var1) {
        if (var1 != null) {
            int var2 = var1.length;
            int var3 = 0;
            byte var4 = 0;
            if (var2 <= 6) {
                var2 = var1.length;

                for(var3 = var4; var3 < var2; ++var3) {
                    int var5 = var1[var3];
                    this.mRecentColorInfo.add(var5);
                }
            } else {
                while(var3 < 6) {
                    this.mRecentColorInfo.add(var1[var3]);
                    ++var3;
                }
            }
        }

    }

    public void saveSelectedColor(int var1) {
        this.mSelectedColor = var1;
    }

    public void setCurrentColor(Integer var1) {
        this.mCurrentColor = var1;
    }

    public void setNewColor(Integer var1) {
        this.mNewColor = var1;
    }
}
