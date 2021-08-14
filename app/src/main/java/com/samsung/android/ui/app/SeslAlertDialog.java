package com.samsung.android.ui.app;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;

import com.mesalabs.ten.update.R;

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

public class SeslAlertDialog extends AppCompatDialog implements DialogInterface {
    final SeslAlertController mAlert;
    static final int LAYOUT_HINT_NONE = 0;
    static final int LAYOUT_HINT_SIDE = 1;

    protected SeslAlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    protected SeslAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, resolveDialogTheme(context, themeResId));
        mAlert = new SeslAlertController(getContext(), this, getWindow());
    }

    protected SeslAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        this(context, 0);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    static int resolveDialogTheme(@NonNull Context context, @StyleRes int resid) {
        // Check to see if this resourceId has a valid package ID.
        if (((resid >>> 24) & 0x000000ff) >= 0x00000001) {   // start of real resource IDs.
            return resid;
        } else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.alertDialogTheme, outValue, true);
            return outValue.resourceId;
        }
    }

    public Button getButton(int whichButton) {
        return mAlert.getButton(whichButton);
    }

    public ListView getListView() {
        return mAlert.getListView();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mAlert.setTitle(title);
    }

    public void setCustomTitle(View customTitleView) {
        mAlert.setCustomTitle(customTitleView);
    }

    public void setMessage(CharSequence message) {
        mAlert.setMessage(message);
    }

    public void setView(View view) {
        mAlert.setView(view);
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        mAlert.setView(view, viewSpacingLeft, viewSpacingTop, viewSpacingRight, viewSpacingBottom);
    }

    void setButtonPanelLayoutHint(int layoutHint) {
        mAlert.setButtonPanelLayoutHint(layoutHint);
    }

    public void setButton(int whichButton, CharSequence text, Message msg) {
        mAlert.setButton(whichButton, text, null, msg, null);
    }

    public void setButton(int whichButton, CharSequence text, OnClickListener listener) {
        mAlert.setButton(whichButton, text, listener, null, null);
    }

    public void setButton(int whichButton, CharSequence text, Drawable icon, OnClickListener listener) {
        mAlert.setButton(whichButton, text, listener, null,  icon);
    }

    public void setIcon(int resId) {
        mAlert.setIcon(resId);
    }

    public void setIcon(Drawable icon) {
        mAlert.setIcon(icon);
    }

    public void setIconAttribute(int attrId) {
        TypedValue out = new TypedValue();
        getContext().getTheme().resolveAttribute(attrId, out, true);
        mAlert.setIcon(out.resourceId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlert.installContent();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAlert.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mAlert.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public static class Builder {
        private final SeslAlertController.AlertParams P;
        private final int mTheme;

        public Builder(@NonNull Context context) {
            this(context, resolveDialogTheme(context, 0));
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            P = new SeslAlertController.AlertParams(new ContextThemeWrapper(context, resolveDialogTheme(context, themeResId)));
            mTheme = themeResId;
        }

        @NonNull
        public Context getContext() {
            return P.mContext;
        }

        public SeslAlertDialog.Builder setTitle(@StringRes int titleId) {
            P.mTitle = P.mContext.getText(titleId);
            return this;
        }

        public SeslAlertDialog.Builder setTitle(@Nullable CharSequence title) {
            P.mTitle = title;
            return this;
        }

        public SeslAlertDialog.Builder setCustomTitle(@Nullable View customTitleView) {
            P.mCustomTitleView = customTitleView;
            return this;
        }

        public SeslAlertDialog.Builder setMessage(@StringRes int messageId) {
            P.mMessage = P.mContext.getText(messageId);
            return this;
        }

        public SeslAlertDialog.Builder setMessage(@Nullable CharSequence message) {
            P.mMessage = message;
            return this;
        }

        public SeslAlertDialog.Builder setIcon(@DrawableRes int iconId) {
            P.mIconId = iconId;
            return this;
        }

        public SeslAlertDialog.Builder setIcon(@Nullable Drawable icon) {
            P.mIcon = icon;
            return this;
        }

        public SeslAlertDialog.Builder setIconAttribute(@AttrRes int attrId) {
            TypedValue out = new TypedValue();
            P.mContext.getTheme().resolveAttribute(attrId, out, true);
            P.mIconId = out.resourceId;
            return this;
        }

        public SeslAlertDialog.Builder setPositiveButton(@StringRes int textId, final OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId);
            P.mPositiveButtonListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setPositiveButtonIcon(Drawable icon) {
            P.mPositiveButtonIcon = icon;
            return this;
        }

        public SeslAlertDialog.Builder setNegativeButton(@StringRes int textId, final OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setNegativeButtonIcon(Drawable icon) {
            P.mNegativeButtonIcon = icon;
            return this;
        }

        public SeslAlertDialog.Builder setNeutralButton(@StringRes int textId, final OnClickListener listener) {
            P.mNeutralButtonText = P.mContext.getText(textId);
            P.mNeutralButtonListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setNeutralButton(CharSequence text, final OnClickListener listener) {
            P.mNeutralButtonText = text;
            P.mNeutralButtonListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setNeutralButtonIcon(Drawable icon) {
            P.mNeutralButtonIcon = icon;
            return this;
        }

        public SeslAlertDialog.Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public SeslAlertDialog.Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        public SeslAlertDialog.Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        public SeslAlertDialog.Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        public SeslAlertDialog.Builder setItems(@ArrayRes int itemsId, final OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setItems(CharSequence[] items, final OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setAdapter(final ListAdapter adapter, final OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setCursor(final Cursor cursor, final OnClickListener listener, String labelColumn) {
            P.mCursor = cursor;
            P.mLabelColumn = labelColumn;
            P.mOnClickListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setMultiChoiceItems(@ArrayRes int itemsId, boolean[] checkedItems, final OnMultiChoiceClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
            return this;
        }

        public SeslAlertDialog.Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems, final OnMultiChoiceClickListener listener) {
            P.mItems = items;
            P.mOnCheckboxClickListener = listener;
            P.mCheckedItems = checkedItems;
            P.mIsMultiChoice = true;
            return this;
        }

        public SeslAlertDialog.Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn, final OnMultiChoiceClickListener listener) {
            P.mCursor = cursor;
            P.mOnCheckboxClickListener = listener;
            P.mIsCheckedColumn = isCheckedColumn;
            P.mLabelColumn = labelColumn;
            P.mIsMultiChoice = true;
            return this;
        }

        public SeslAlertDialog.Builder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem, final OnClickListener listener) {
            P.mItems = P.mContext.getResources().getTextArray(itemsId);
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        public SeslAlertDialog.Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn, final OnClickListener listener) {
            P.mCursor = cursor;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mLabelColumn = labelColumn;
            P.mIsSingleChoice = true;
            return this;
        }

        public SeslAlertDialog.Builder setSingleChoiceItems(CharSequence[] items, int checkedItem, final OnClickListener listener) {
            P.mItems = items;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        public SeslAlertDialog.Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, final OnClickListener listener) {
            P.mAdapter = adapter;
            P.mOnClickListener = listener;
            P.mCheckedItem = checkedItem;
            P.mIsSingleChoice = true;
            return this;
        }

        public SeslAlertDialog.Builder setOnItemSelectedListener(final AdapterView.OnItemSelectedListener listener) {
            P.mOnItemSelectedListener = listener;
            return this;
        }

        public SeslAlertDialog.Builder setView(int layoutResId) {
            P.mView = null;
            P.mViewLayoutResId = layoutResId;
            P.mViewSpacingSpecified = false;
            return this;
        }

        public SeslAlertDialog.Builder setView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            P.mViewSpacingSpecified = false;
            return this;
        }

        @Deprecated
        public SeslAlertDialog.Builder setView(View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            P.mViewSpacingSpecified = true;
            P.mViewSpacingLeft = viewSpacingLeft;
            P.mViewSpacingTop = viewSpacingTop;
            P.mViewSpacingRight = viewSpacingRight;
            P.mViewSpacingBottom = viewSpacingBottom;
            return this;
        }

        @Deprecated
        public SeslAlertDialog.Builder setInverseBackgroundForced(boolean useInverseBackground) {
            P.mForceInverseBackground = useInverseBackground;
            return this;
        }

        public SeslAlertDialog.Builder setRecycleOnMeasureEnabled(boolean enabled) {
            P.mRecycleOnMeasure = enabled;
            return this;
        }


        @NonNull
        public SeslAlertDialog create() {
            // We can't use Dialog's 3-arg constructor with the createThemeContextWrapper param,
            // so we always have to re-set the theme
            final SeslAlertDialog dialog = new SeslAlertDialog(P.mContext, mTheme);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        public SeslAlertDialog show() {
            final SeslAlertDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

}
