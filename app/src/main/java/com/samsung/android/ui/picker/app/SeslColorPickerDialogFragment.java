package com.samsung.android.ui.picker.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.mesalabs.ten.update.R;
import com.samsung.android.ui.app.SeslAlertDialog;
import com.samsung.android.ui.picker.widget.SeslColorPicker;

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

public class SeslColorPickerDialogFragment extends AppCompatDialogFragment implements DialogInterface.OnClickListener {
    private static final String KEY_COLOR_SET_LISTENER = "color_set_listener";
    private static final String KEY_CURRENT_COLOR = "current_color";
    private static final String KEY_OPACITY_BAR_ENABLED = "opacity_bar_enabled";
    private static final String KEY_RECENTLY_USED_COLORS = "recently_used_colors";
    private static final String TAG = "SeslColorPickerDialogFragment";
    private SeslAlertDialog mAlertDialog;
    private SeslColorPicker mColorPicker;
    private Integer mCurrentColor = null;
    private boolean mIsTransparencyControlEnabled = false;
    private Integer mNewColor = null;
    private SeslColorPicker.OnColorChangedListener mOnColorChangedListener;
    private SeslColorPickerDialogFragment.OnColorSetListener mOnColorSetListener;
    private int[] mRecentlyUsedColors = null;

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public SeslColorPickerDialogFragment() {
    }

    public static SeslColorPickerDialogFragment newInstance(SeslColorPickerDialogFragment.OnColorSetListener var0) {
        SeslColorPickerDialogFragment var1 = new SeslColorPickerDialogFragment();
        Bundle var2 = new Bundle();
        var2.putSerializable("color_set_listener", var0);
        var1.setArguments(var2);
        return var1;
    }

    public static SeslColorPickerDialogFragment newInstance(SeslColorPickerDialogFragment.OnColorSetListener var0, int var1) {
        SeslColorPickerDialogFragment var2 = new SeslColorPickerDialogFragment();
        Bundle var3 = new Bundle();
        var3.putSerializable("color_set_listener", var0);
        var3.putSerializable("current_color", var1);
        var2.setArguments(var3);
        return var2;
    }

    public static SeslColorPickerDialogFragment newInstance(SeslColorPickerDialogFragment.OnColorSetListener var0, int var1, int[] var2) {
        SeslColorPickerDialogFragment var3 = new SeslColorPickerDialogFragment();
        Bundle var4 = new Bundle();
        var4.putSerializable("color_set_listener", var0);
        var4.putSerializable("current_color", var1);
        var4.putIntArray("recently_used_colors", var2);
        var3.setArguments(var4);
        return var3;
    }

    public static SeslColorPickerDialogFragment newInstance(SeslColorPickerDialogFragment.OnColorSetListener var0, int[] var1) {
        SeslColorPickerDialogFragment var2 = new SeslColorPickerDialogFragment();
        Bundle var3 = new Bundle();
        var3.putSerializable("color_set_listener", var0);
        var3.putIntArray("recently_used_colors", var1);
        var2.setArguments(var3);
        return var2;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public SeslColorPicker getColorPicker() {
        return this.mColorPicker;
    }

    public void onClick(DialogInterface var1, int var2) {
        if (var2 != -2) {
            if (var2 == -1) {
                this.mColorPicker.saveSelectedColor();
                if (this.mOnColorSetListener != null) {
                    if (this.mCurrentColor != null && !this.mColorPicker.isUserInputValid()) {
                        this.mOnColorSetListener.onColorSet(this.mCurrentColor);
                    } else {
                        this.mOnColorSetListener.onColorSet(this.mColorPicker.getRecentColorInfo().getSelectedColor());
                    }
                }
            }
        } else {
            var1.dismiss();
        }

    }

    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        if (var1 != null) {
            this.mRecentlyUsedColors = var1.getIntArray("recently_used_colors");
            this.mCurrentColor = (Integer)var1.getSerializable("current_color");
            this.mIsTransparencyControlEnabled = var1.getBoolean("opacity_bar_enabled");
        }

    }

    public Dialog onCreateDialog(Bundle var1) {
        Context var6 = this.getContext();
        TypedValue var2 = new TypedValue();
        Resources.Theme var3 = var6.getTheme();
        int var4 = R.attr.isLightTheme;
        boolean var5 = true;
        var3.resolveAttribute(var4, var2, true);
        FragmentActivity var7 = this.getActivity();
        if (var2.data == 0) {
            var5 = false;
        }

        this.mAlertDialog = new SeslColorPickerDialogFragment.ColorPickerDialog(var7, var5);
        this.mAlertDialog.setButton(-1, var6.getString(R.string.sesl_picker_done), this);
        this.mAlertDialog.setButton(-2, var6.getString(android.R.string.cancel), this);
        return this.mAlertDialog;
    }

    @Nullable
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public View onCreateView(LayoutInflater var1, @Nullable ViewGroup var2, Bundle var3) {
        this.mColorPicker = (SeslColorPicker)var1.inflate(R.layout.sesl_color_picker_dialog, (ViewGroup)null);
        Bundle var4 = this.getArguments();
        if (var4 != null) {
            this.mOnColorSetListener = (SeslColorPickerDialogFragment.OnColorSetListener)var4.getSerializable("color_set_listener");
            this.mCurrentColor = (Integer)var4.getSerializable("current_color");
            this.mRecentlyUsedColors = var4.getIntArray("recently_used_colors");
        }

        if (this.mCurrentColor != null) {
            this.mColorPicker.getRecentColorInfo().setCurrentColor(this.mCurrentColor);
        }

        if (this.mNewColor != null) {
            this.mColorPicker.getRecentColorInfo().setNewColor(this.mNewColor);
        }

        if (this.mRecentlyUsedColors != null) {
            this.mColorPicker.getRecentColorInfo().initRecentColorInfo(this.mRecentlyUsedColors);
        }

        this.mColorPicker.setOpacityBarEnabled(this.mIsTransparencyControlEnabled);
        try {
            this.mColorPicker.updateRecentColorLayout();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        this.mColorPicker.setOnColorChangedListener(this.mOnColorChangedListener);
        this.mAlertDialog.setView(this.mColorPicker);
        return super.onCreateView(var1, var2, var3);
    }

    public void onSaveInstanceState(Bundle var1) {
        super.onSaveInstanceState(var1);
        this.mColorPicker.getRecentColorInfo().setCurrentColor(this.mColorPicker.getRecentColorInfo().getSelectedColor());
        var1.putIntArray("recently_used_colors", this.mRecentlyUsedColors);
        var1.putSerializable("current_color", this.mCurrentColor);
        var1.putBoolean("opacity_bar_enabled", this.mIsTransparencyControlEnabled);
    }

    public void setNewColor(Integer var1) {
        this.mNewColor = var1;
    }

    public void setOnColorChangedListener(SeslColorPicker.OnColorChangedListener var1) {
        this.mOnColorChangedListener = var1;
    }

    public void setTransparencyControlEnabled(boolean var1) {
        this.mIsTransparencyControlEnabled = var1;
    }

    private class ColorPickerDialog extends SeslAlertDialog {
        protected ColorPickerDialog(@NonNull Context var2, boolean var3) {
            super(var2, var3 ? R.style.mesa_DialogStyle_Light : R.style.mesa_DialogStyle);
        }
    }

    public interface OnColorSetListener extends Serializable {
        void onColorSet(int var1);
    }
}
