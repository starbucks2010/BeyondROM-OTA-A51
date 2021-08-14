package com.samsung.android.ui.picker.app;

import android.content.Context;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class SeslColorPickerDialog extends SeslAlertDialog implements DialogInterface.OnClickListener {
    private static final String TAG = "SeslColorPickerDialog";
    private final SeslColorPicker mColorPicker;
    private Integer mCurrentColor;
    private final SeslColorPickerDialog.OnColorSetListener mOnColorSetListener;

    public SeslColorPickerDialog(Context context, SeslColorPickerDialog.OnColorSetListener listener) {
        super(context, resolveDialogTheme(context));
        this.mCurrentColor = null;
        Context var3 = this.getContext();
        View var4 = LayoutInflater.from(var3).inflate(R.layout.sesl_color_picker_dialog, (ViewGroup)null);
        this.setView(var4);
        this.setButton(-1, var3.getString(R.string.sesl_picker_done), this);
        this.setButton(-2, var3.getString(android.R.string.cancel), this);
        this.requestWindowFeature(1);
        this.getWindow().setSoftInputMode(16);
        this.mOnColorSetListener = listener;
        this.mColorPicker = (SeslColorPicker)var4.findViewById(R.id.sesl_color_picker_content_view);
    }

    public SeslColorPickerDialog(Context context, SeslColorPickerDialog.OnColorSetListener listener, int currentColor) throws Throwable {
        this(context, listener);
        this.mColorPicker.getRecentColorInfo().setCurrentColor(currentColor);
        this.mCurrentColor = currentColor;
        this.mColorPicker.updateRecentColorLayout();
    }

    public SeslColorPickerDialog(Context context, SeslColorPickerDialog.OnColorSetListener listener, int currentColor, int[] recentColors) throws Throwable {
        this(context, listener);
        this.mColorPicker.getRecentColorInfo().initRecentColorInfo(recentColors);
        this.mColorPicker.getRecentColorInfo().setCurrentColor(currentColor);
        this.mCurrentColor = currentColor;
        this.mColorPicker.updateRecentColorLayout();
    }

    public SeslColorPickerDialog(Context context, SeslColorPickerDialog.OnColorSetListener listener, int[] recentColors) throws Throwable {
        this(context, listener);
        this.mColorPicker.getRecentColorInfo().initRecentColorInfo(recentColors);
        this.mColorPicker.updateRecentColorLayout();
    }

    private static int resolveDialogTheme(Context var0) {
        TypedValue var1 = new TypedValue();
        var0.getTheme().resolveAttribute(R.attr.isLightTheme, var1, true);
        int var2;
        if (var1.data != 0) {
            var2 = R.style.mesa_DialogStyle_Light;
        } else {
            var2 = R.style.mesa_DialogStyle;
        }

        return var2;
    }

    public SeslColorPicker getColorPicker() {
        return this.mColorPicker;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onClick(DialogInterface var1, int var2) {
        if (var2 != -2 && var2 == -1) {
            this.mColorPicker.saveSelectedColor();
            if (this.mOnColorSetListener != null) {
                if (!this.mColorPicker.isUserInputValid()) {
                    Integer var3 = this.mCurrentColor;
                    if (var3 != null) {
                        this.mOnColorSetListener.onColorSet(var3);
                        return;
                    }
                }

                this.mOnColorSetListener.onColorSet(this.mColorPicker.getRecentColorInfo().getSelectedColor());
            }
        }

    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setNewColor(Integer var1) throws Throwable {
        this.mColorPicker.getRecentColorInfo().setNewColor(var1);
        this.mColorPicker.updateRecentColorLayout();
    }

    public void setTransparencyControlEnabled(boolean var1) {
        this.mColorPicker.setOpacityBarEnabled(var1);
    }

    public interface OnColorSetListener {
        void onColorSet(int var1);
    }
}
