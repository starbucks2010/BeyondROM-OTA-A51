package com.mesalabs.cerberus.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mesalabs.cerberus.R;
import com.mesalabs.cerberus.ui.widget.ToolbarImageButton;
import com.mesalabs.cerberus.utils.CerberusException;
import com.mesalabs.cerberus.utils.LogUtils;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.cerberus.utils.ViewUtils;

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

public class ActionBarUtils {
    private AppCompatActivity activity;

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout appBarHeaderLayout;
    private Toolbar toolbar;
    private LinearLayout overflowContainer;

    private ToolbarImageButton toolbarHomeButton;
    private TextView expandedAppBarTitle;
    private TextView toolbarTitle;
    private ToolbarImageButton moreOverflowButton;

    private PopupWindow moreMenuPopupWindow = null;
    private MoreMenuPopupAdapter moreMenuPopupAdapter;

    private View moreMenuPopupAnchor = null;
    private int moreMenuPopupWidth;
    private int moreMenuPopupHeight;

    private float mAppBarHeightDp;

    private boolean appBarCustomHeader = false;
    private boolean isNightMode = false;
    private boolean defaultExpandStatus;


    public ActionBarUtils(AppCompatActivity instance) {
        activity = instance;
        isNightMode = Utils.isNightMode(activity);
    }

    public void addOverflowButton(int iconResId, int contentDescResId, View.OnClickListener ocl) {
        addOverflowButton(false, iconResId, contentDescResId, ocl);
    }

    public void addOverflowButton(boolean big, int iconResId, int contentDescResId, View.OnClickListener ocl) {
        if (moreMenuPopupWindow != null)
            throw new CerberusException("Can't add a new Overflow button! Please make sure to add it BEFORE initializing moreMenuPopupWindow.");

        ToolbarImageButton overflowButton = new ToolbarImageButton(activity);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int) activity.getResources().getDimension(big ? R.dimen.mesa_toolbar_imagebutton_width : R.dimen.sesl_action_button_min_width),
                (int) activity.getResources().getDimension(R.dimen.sesl_action_bar_default_height));

        overflowButton.setBackgroundResource(isNightMode ? R.drawable.sesl_action_bar_item_background_dark : R.drawable.sesl_action_bar_item_background);
        overflowButton.setImageResource(iconResId);
        overflowButton.setImageTintList(activity.getResources().getColorStateList(isNightMode ? R.color.sesl_action_menu_text_dark : R.color.sesl_action_menu_text, activity.getTheme()));
        overflowButton.setOnClickListener(ocl);
        overflowButton.setTooltipText(activity.getString(contentDescResId));

        overflowContainer.addView(overflowButton, lp);
    }

    public void dismissMoreMenuPopupWindow() {
        if (moreMenuPopupWindow != null || moreMenuPopupWindow.isShowing()) {
            moreMenuPopupWindow.dismiss();
        } else
            LogUtils.w("AppBarActivity.createMorePopupWindow", "moreMenuPopupWindow is null or already hidden.");
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    private int getMoreMenuPopupWidth(MoreMenuPopupAdapter adapter) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        View view = null;
        int count = adapter.getCount();
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        ViewGroup viewGroup = null;
        while (i < count) {
            ViewGroup linearLayout;
            int itemViewType = adapter.getItemViewType(i);
            if (itemViewType != i2) {
                i2 = itemViewType;
                view = null;
            }
            if (viewGroup == null) {
                linearLayout = new LinearLayout(activity);
            } else {
                linearLayout = viewGroup;
            }
            view = adapter.getView(i, view, linearLayout);
            view.measure(makeMeasureSpec, makeMeasureSpec2);
            int measuredWidth = view.getMeasuredWidth();
            if (measuredWidth <= i3) {
                measuredWidth = i3;
            }
            i++;
            i3 = measuredWidth;
            viewGroup = linearLayout;
        }
        return i3 + 25;
    }

    public void initAppBar(boolean isExpanded, int customHeaderId) {
        toolbar = activity.findViewById(R.id.mesa_toolbar_appbarlayout);
        activity.setSupportActionBar(toolbar);

        if (toolbar != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            appBarLayout = activity.findViewById(R.id.mesa_appbar_appbarlayout);
            collapsingToolbarLayout = activity.findViewById(R.id.mesa_colltoolbar_appbarlayout);

            ViewStub viewstub = activity.findViewById(R.id.mesa_viewstub_appbarlayout);
            if (customHeaderId > 0) {
                viewstub.setLayoutResource(customHeaderId);
                appBarCustomHeader = true;
            } else
                viewstub.setLayoutResource(R.layout.mesa_seslheader_appbarlayout);
            viewstub.inflate();

            appBarHeaderLayout = activity.findViewById(R.id.mesa_abheaderlayout_appbarlayout);

            toolbarHomeButton = activity.findViewById(R.id.mesa_toolbarhome_appbarlayout);
            expandedAppBarTitle = activity.findViewById(R.id.mesa_abexpatitle_appbarlayout);
            toolbarTitle = activity.findViewById(R.id.mesa_toolbartitle_appbarlayout);
            overflowContainer = activity.findViewById(R.id.mesa_overflowcontainer_appbarlayout);

            moreMenuPopupAnchor = activity.findViewById(R.id.mesa_popupanchor_appbarlayout);

            int statusBarHeight = ViewUtils.getStatusbarHeight(activity);
            if (statusBarHeight > 0)
                appBarHeaderLayout.setPadding(0, 0, 0, statusBarHeight / 2);

            defaultExpandStatus = isExpanded;

            resetAppBarHeight();

            appBarLayout.addOnOffsetChangedListener(new AppBarOffsetListener());
        } else
            throw new CerberusException("Field ActionBarUtils.toolbar is null! Make sure layout in " + activity.getLocalClassName() + "has been inflated.");
    }

    public void initToolBar() {
        toolbar = activity.findViewById(R.id.mesa_toolbar_toolbarlayout);
        activity.setSupportActionBar(toolbar);

        if (toolbar != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            toolbarHomeButton = activity.findViewById(R.id.mesa_toolbarhome_toolbarlayout);
            toolbarTitle = activity.findViewById(R.id.mesa_toolbartitle_toolbarlayout);
            overflowContainer = activity.findViewById(R.id.mesa_overflowcontainer_toolbarlayout);

            moreMenuPopupAnchor = activity.findViewById(R.id.mesa_popupanchor_toolbarlayout);
        } else
            throw new CerberusException("Field ActionBarUtils.toolbar is null! Make sure layout in " + activity.getLocalClassName() + "has been inflated.");
    }

    private void initMoreMenuPopupWindow(LinkedHashMap<String, Integer> linkedHashMap, AdapterView.OnItemClickListener ocl) {
        if (moreMenuPopupWindow != null) {
            if (moreMenuPopupWindow.isShowing()) {
                moreMenuPopupWindow.dismiss();
            }
            moreMenuPopupWindow = null;
        }
        ListView listView = new ListView(activity);
        moreMenuPopupAdapter = new MoreMenuPopupAdapter(activity, linkedHashMap);
        listView.setAdapter(moreMenuPopupAdapter);
        listView.setDivider(null);
        listView.setSelector(activity.getResources().getDrawable(isNightMode ? R.drawable.mesa_menu_popup_list_selector_dark : R.drawable.mesa_menu_popup_list_selector_light, activity.getTheme()));
        listView.setOnItemClickListener(ocl);

        if (ViewUtils.isRTLMode(activity)) {
            moreMenuPopupWidth = getMoreMenuPopupWidth(moreMenuPopupAdapter);
        } else {
            moreMenuPopupWidth = -getMoreMenuPopupWidth(moreMenuPopupAdapter);
        }
        moreMenuPopupHeight = ViewUtils.dp2px(activity, -12.0f);

        moreMenuPopupWindow = new PopupWindow(listView);
        moreMenuPopupWindow.setWidth(getMoreMenuPopupWidth(moreMenuPopupAdapter));
        moreMenuPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        moreMenuPopupWindow.setAnimationStyle(R.style.mesa_MenuPopupAnimStyle);
        moreMenuPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(isNightMode ? R.drawable.sesl_menu_popup_background_dark : R.drawable.sesl_menu_popup_background, activity.getTheme()));
        moreMenuPopupWindow.setOutsideTouchable(true);
        moreMenuPopupWindow.setElevation(ViewUtils.getDIPForPX(activity, 3));
        moreMenuPopupWindow.setFocusable(true);
        if (moreMenuPopupWindow.isClippingEnabled()) {
            moreMenuPopupWindow.setClippingEnabled(false);
        }
        moreMenuPopupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() != KeyEvent.KEYCODE_MENU || keyEvent.getAction() != KeyEvent.ACTION_UP || !moreMenuPopupWindow.isShowing()) {
                    return false;
                }
                moreMenuPopupWindow.dismiss();
                return true;
            }
        });
        moreMenuPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != MotionEvent.ACTION_OUTSIDE) {
                    return false;
                }
                moreMenuPopupWindow.dismiss();
                return true;
            }
        });
    }

    public void resetAppBarHeight() {
        if (appBarLayout != null) {
            ViewGroup.LayoutParams params = appBarLayout.getLayoutParams();
            int windowHeight = ViewUtils.getWindowHeight(activity);

            if (ViewUtils.isLandscape(activity) || Utils.isInMultiWindowMode(activity)) {
                appBarLayout.setExpanded(false, false);
                appBarLayout.setActivated(false);

                mAppBarHeightDp = activity.getResources().getDimension(R.dimen.sesl_action_bar_default_height_padding);

                params.height = (int) mAppBarHeightDp;
            } else {
                appBarLayout.setExpanded(defaultExpandStatus, false);
                appBarLayout.setActivated(true);

                TypedValue outValue = new TypedValue();
                activity.getResources().getValue(R.dimen.sesl_abl_height_proportion, outValue, true);

                mAppBarHeightDp = activity.getResources().getDimension(R.dimen.sesl_action_bar_default_height);

                params.height = (int) ((float) windowHeight * outValue.getFloat());
            }

            appBarLayout.setLayoutParams(params);
        } else
            LogUtils.w("ActionBarUtils.resetAppBarHeight", "appBarLayout is null.");
    }

    public void setHomeAsUpButton(View.OnClickListener ocl) {
        setHomeAsUpButton(0, 0, ocl);
    }

    public void setHomeAsUpButton(int iconResId, int contentDescResId, View.OnClickListener ocl) {
        if (toolbarHomeButton != null) {
            toolbarHomeButton.setVisibility(View.VISIBLE);

            toolbar.setPadding(0, 0, 0, 0);

            toolbarHomeButton.setBackgroundResource(isNightMode ? R.drawable.sesl_action_bar_item_background_dark : R.drawable.sesl_action_bar_item_background);

            if (iconResId > 0)
                toolbarHomeButton.setImageResource(iconResId);
            else
                toolbarHomeButton.setImageResource(isNightMode ? R.drawable.sesl_ic_ab_back_dark : R.drawable.sesl_ic_ab_back);

            toolbarHomeButton.setImageTintList(activity.getResources().getColorStateList(isNightMode ? R.color.sesl_action_menu_text_dark : R.color.sesl_action_menu_text, activity.getTheme()));

            String contentDescription;

            if (contentDescResId > 0)
                contentDescription = activity.getString(contentDescResId);
            else
                contentDescription = activity.getString(R.string.sesl_action_bar_up_description);

            toolbarHomeButton.setOnClickListener(ocl);

            toolbarHomeButton.setTooltipText(contentDescription);
        } else
            throw new CerberusException("ActionBarUtils: Can't add Home button: AppBar/Toolbar hasn't been initialized yet!");
    }

    public void setMoreMenuButton(LinkedHashMap<String, Integer> linkedHashMap, AdapterView.OnItemClickListener ocl) {
        moreOverflowButton = new ToolbarImageButton(activity);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int) activity.getResources().getDimension(R.dimen.mesa_toolbar_imagebutton_width),
                (int) activity.getResources().getDimension(R.dimen.sesl_action_bar_default_height));

        moreOverflowButton.setBackgroundResource(isNightMode ? R.drawable.sesl_action_bar_item_background_dark : R.drawable.sesl_action_bar_item_background);
        moreOverflowButton.setImageResource(isNightMode ? R.drawable.sesl_ic_menu_overflow_dark : R.drawable.sesl_ic_menu_overflow);
        moreOverflowButton.setImageTintList(activity.getResources().getColorStateList(isNightMode ? R.color.sesl_action_menu_text_dark : R.color.sesl_action_menu_text, activity.getTheme()));
        moreOverflowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreMenuPopupWindow();
            }
        });
        moreOverflowButton.setTooltipText(activity.getString(R.string.sesl_action_menu_overflow_description));

        overflowContainer.addView(moreOverflowButton, lp);

        initMoreMenuPopupWindow(linkedHashMap, ocl);
    }

    public void setTitleText(String titleText) {
        if (expandedAppBarTitle != null && !appBarCustomHeader)
            expandedAppBarTitle.setText(titleText);
        if (toolbarTitle != null)
            toolbarTitle.setText(titleText);
        else
            throw new CerberusException("ActionBarUtils: Can't set title text: AppBar/Toolbar hasn't been initialized yet!");
    }

    public void showMoreMenuPopupWindow() {
        if (moreMenuPopupWindow != null || !moreMenuPopupWindow.isShowing())
            moreMenuPopupWindow.showAsDropDown(moreMenuPopupAnchor, moreMenuPopupWidth, moreMenuPopupHeight);
        else
            LogUtils.w(activity.getLocalClassName() + ".createMorePopupWindow", "moreMenuPopupWindow is null or already shown.");
    }



    private class AppBarOffsetListener implements AppBarLayout.OnOffsetChangedListener {
        @SuppressLint("Range")
        @Override
        public void onOffsetChanged(AppBarLayout layout, int verticalOffset) {
            int layoutPosition = Math.abs(appBarLayout.getTop());
            float alphaRange = ((float) collapsingToolbarLayout.getHeight()) * 0.17999999f;
            float toolbarTitleAlphaStart = ((float) collapsingToolbarLayout.getHeight()) * 0.35f;

            appBarHeaderLayout.setTranslationY((float) ((-verticalOffset) / 3));

            float expaTitleAlpha = 255.0f - ((100.0f / alphaRange) * (((float) layoutPosition) - 0.0f));
            if (expaTitleAlpha < 0.0f) {
                expaTitleAlpha = 0.0f;
            } else if (expaTitleAlpha > 255.0f) {
                expaTitleAlpha = 255.0f;
            }
            expaTitleAlpha /= 255.0f;

            appBarHeaderLayout.setAlpha(expaTitleAlpha);

            if (appBarLayout.getHeight() <= ((int) mAppBarHeightDp)) {
                appBarHeaderLayout.setAlpha(0.0f);
                toolbarTitle.setAlpha(1.0f);
            } else {
                double collapsedTitleAlpha = (double) ((150.0f / alphaRange) * (((float) layoutPosition) - toolbarTitleAlphaStart));

                if (collapsedTitleAlpha >= 0.0d && collapsedTitleAlpha <= 255.0d) {
                    collapsedTitleAlpha /= 255.0d;
                    toolbarTitle.setAlpha((float) collapsedTitleAlpha);
                }
                else if (collapsedTitleAlpha < 0.0d)
                    toolbarTitle.setAlpha(0.0f);
                else
                    toolbarTitle.setAlpha(1.0f);
            }
        }
    }

    private class MoreMenuPopupAdapter extends ArrayAdapter {
        ArrayList<String> keySet;
        ArrayList<Integer> values;
        AppCompatActivity activity;

        public MoreMenuPopupAdapter(AppCompatActivity instance, LinkedHashMap<String, Integer> linkedHashMap) {
            super(instance, 0);
            activity = instance;
            keySet = new ArrayList(linkedHashMap.keySet());
            values = new ArrayList(linkedHashMap.values());
        }

        public void setArrays(LinkedHashMap<String, Integer> linkedHashMap) {
            keySet = new ArrayList(linkedHashMap.keySet());
            values = new ArrayList(linkedHashMap.values());
        }

        @Override
        public int getCount() {
            return keySet.size();
        }

        @Override
        public Object getItem(int position) {
            return keySet.get(position);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            PopupMenuItem itemVar;

            if (view == null) {
                view = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mesa_menu_popup_item_layout, parent, false);
                itemVar = new PopupMenuItem(this);
                itemVar.titleText = view.findViewById(R.id.mesa_titletext_moremenupopup);
                view.setTag(itemVar);
            } else {
                itemVar = (PopupMenuItem) view.getTag();
            }

            itemVar.titleText.setText(keySet.get(position));

            if (getCount() <= 1) {
                view.setBackgroundResource(isNightMode ? R.drawable.mesa_menu_popup_item_bg_allr_dark : R.drawable.mesa_menu_popup_item_bg_allr_light);
            } else if (position == 0) {
                view.setBackgroundResource(isNightMode ? R.drawable.mesa_menu_popup_item_bg_topr_dark : R.drawable.mesa_menu_popup_item_bg_topr_light);
            } else if (position == getCount() - 1) {
                view.setBackgroundResource(isNightMode ? R.drawable.mesa_menu_popup_item_bg_bottomr_dark : R.drawable.mesa_menu_popup_item_bg_bottomr_light);
            } else {
                view.setBackgroundResource(isNightMode ? R.drawable.mesa_menu_popup_item_bg_nor_dark : R.drawable.mesa_menu_popup_item_bg_nor_light);
            }

            return view;
        }
    }

    private class PopupMenuItem {
        TextView titleText;
        MoreMenuPopupAdapter adapter;

        PopupMenuItem(MoreMenuPopupAdapter instance) {
            adapter = instance;
        }
    }

}
