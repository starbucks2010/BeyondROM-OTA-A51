package com.mesalabs.cerberus.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mesalabs.ten.update.R;
import com.mesalabs.cerberus.ui.widget.ToolbarImageButton;
import com.mesalabs.cerberus.utils.CerberusException;
import com.mesalabs.ten.update.utils.LogUtils;
import com.mesalabs.cerberus.utils.Utils;
import com.mesalabs.cerberus.utils.ViewUtils;
import com.samsung.android.ui.appbar.SeslAppBarLayout;
import com.samsung.android.ui.appbar.SeslCollapsingToolbarLayout;

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

public class ActionBarUtils {
    public static int NEW_UPDATE_AVAILABLE = -1;

    private AppCompatActivity activity;
    private NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());

    private SeslAppBarLayout appBarLayout;
    private SeslCollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private LinearLayout overflowContainer;

    private ToolbarImageButton toolbarHomeButton;
    private TextView toolbarTitle;
    private FrameLayout moreOverflowButtonContainer;
    private ToolbarImageButton moreOverflowButton;
    public ViewGroup moreOverflowBadgeBackground;
    public TextView moreOverflowBadgeText;

    private PopupWindow moreMenuPopupWindow = null;
    private MoreMenuPopupAdapter moreMenuPopupAdapter;

    private View moreMenuPopupAnchor = null;
    private int moreMenuPopupOffX;

    private float mAppBarHeightDp;

    private boolean isNightMode;
    private boolean defaultExpandStatus = true;


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

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams((int) activity.getResources().getDimension(big ? R.dimen.mesa_toolbar_imagebutton_width : R.dimen.sesl_action_button_min_width), (int) activity.getResources().getDimension(R.dimen.sesl_action_bar_default_height));

        overflowButton.setBackgroundResource(R.drawable.sesl_action_bar_item_background);
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
            LogUtils.w(activity.getLocalClassName() + ".dismissMoreMenuPopupWindow", "moreMenuPopupWindow is null or already hidden.");
    }

    private int getMoreMenuPopupWidth(MoreMenuPopupAdapter adapter) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        View view = null;
        ViewGroup viewGroup = null;
        int measuredWidth = 0;

        int i = 0;
        int count = adapter.getCount();

        while (i < count) {
            ViewGroup linearLayout;
            int itemViewType = adapter.getItemViewType(i);

            if (itemViewType != 0)
                view = null;

            if (viewGroup == null)
                linearLayout = new LinearLayout(activity);
            else
                linearLayout = viewGroup;

            view = adapter.getView(i, view, linearLayout);
            view.measure(makeMeasureSpec, makeMeasureSpec);
            measuredWidth = view.getMeasuredWidth();
            if (measuredWidth <= 0) {
                measuredWidth = 0;
            }
            i++;
            viewGroup = linearLayout;
        }
        return measuredWidth;
    }

    public SeslAppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    public ToolbarImageButton getOverflowIcon(int index) {
        if (overflowContainer != null && overflowContainer.getChildCount() != 0) {
            return (ToolbarImageButton) overflowContainer.getChildAt(index);
        } else {
            LogUtils.w(activity.getLocalClassName() + ".getOverflowIcon", "overflowContainer is null or contains no icons.");
            return null;
        }
    }

    public void initAppBar(boolean isExpanded) {
        toolbar = activity.findViewById(R.id.mesa_toolbar_appbarlayout);
        activity.setSupportActionBar(toolbar);

        if (toolbar != null) {
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            appBarLayout = activity.findViewById(R.id.mesa_appbar_appbarlayout);
            collapsingToolbarLayout = activity.findViewById(R.id.mesa_colltoolbar_appbarlayout);

            toolbarHomeButton = activity.findViewById(R.id.mesa_toolbarhome_appbarlayout);
            toolbarTitle = activity.findViewById(R.id.mesa_toolbartitle_appbarlayout);
            overflowContainer = activity.findViewById(R.id.mesa_overflowcontainer_appbarlayout);

            moreMenuPopupAnchor = activity.findViewById(R.id.mesa_popupanchor_appbarlayout);

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
            moreMenuPopupOffX = getMoreMenuPopupWidth(moreMenuPopupAdapter);
        } else {
            moreMenuPopupOffX = -getMoreMenuPopupWidth(moreMenuPopupAdapter);
        }

        moreMenuPopupWindow = new PopupWindow(listView);
        moreMenuPopupWindow.setWidth(getMoreMenuPopupWidth(moreMenuPopupAdapter));
        moreMenuPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        moreMenuPopupWindow.setAnimationStyle(R.style.mesa_MenuPopupAnimStyle);
        moreMenuPopupWindow.setBackgroundDrawable(activity.getResources().getDrawable(isNightMode ? R.drawable.sesl_menu_popup_background_dark : R.drawable.sesl_menu_popup_background, activity.getTheme()));
        moreMenuPopupWindow.setOutsideTouchable(true);
        moreMenuPopupWindow.setElevation(ViewUtils.getDIPForPX(activity, 12));
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

    private void initMoreMenuButtonBadge(int count) {
        if (moreOverflowBadgeBackground == null) {
            moreOverflowBadgeBackground = (ViewGroup) ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mesa_view_menu_button_badge_layout, overflowContainer, false);
            moreOverflowBadgeText = (TextView) moreOverflowBadgeBackground.getChildAt(0);
            moreOverflowBadgeText.setTextSize(0, (float) ((int) activity.getResources().getDimension(R.dimen.sesl_menu_item_badge_text_size)));
            moreOverflowButtonContainer.addView(moreOverflowBadgeBackground);
        }
        if (moreOverflowBadgeText != null) {
            if (count > 0) {
                if (count > 99) {
                    count = 99;
                }
                String countString = numberFormat.format((long) count);
                moreOverflowBadgeText.setText(countString);
                int width = (int) (activity.getResources().getDimension(R.dimen.sesl_badge_default_width) + (float) countString.length() * activity.getResources().getDimension(R.dimen.sesl_badge_additional_width));
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) moreOverflowBadgeBackground.getLayoutParams();
                lp.width = width;
                lp.height = (int) activity.getResources().getDimension(R.dimen.sesl_menu_item_badge_size);
                lp.setMarginEnd((int) activity.getResources().getDimension(R.dimen.sesl_menu_item_badge_end_margin));
                moreOverflowBadgeBackground.setLayoutParams(lp);
                moreOverflowBadgeBackground.setVisibility(View.VISIBLE);
            } else if (count == NEW_UPDATE_AVAILABLE) {
                moreOverflowBadgeText.setText("N");
                moreOverflowBadgeBackground.setVisibility(View.VISIBLE);
            } else {
                moreOverflowBadgeBackground.setVisibility(View.GONE);
            }
        }
    }

    public void resetAppBarHeight() {
        if (appBarLayout != null) {
            ViewGroup.LayoutParams params = appBarLayout.getLayoutParams();
            int windowHeight = ViewUtils.getWindowHeight(activity);
            int abBottomPadding;

            if (Utils.isInMultiWindowMode(activity)) {
                appBarLayout.setExpanded(false, false);
                appBarLayout.setActivated(false);

                abBottomPadding = activity.getResources().getDimensionPixelSize(R.dimen.sesl_extended_appbar_bottom_padding);
                mAppBarHeightDp = activity.getResources().getDimension(abBottomPadding == 0 ? R.dimen.sesl_action_bar_default_height : R.dimen.sesl_action_bar_default_height_padding);

                params.height = (int) mAppBarHeightDp;
            } else if (ViewUtils.isLandscape(activity)) {
                appBarLayout.setExpanded(false, false);
                appBarLayout.setActivated(false);

                abBottomPadding = 0;
                mAppBarHeightDp = activity.getResources().getDimension(R.dimen.sesl_action_bar_default_height);

                params.height = (int) mAppBarHeightDp;
            } else {
                appBarLayout.setExpanded(defaultExpandStatus, false);
                appBarLayout.setActivated(true);

                abBottomPadding = activity.getResources().getDimensionPixelSize(R.dimen.sesl_extended_appbar_bottom_padding);
                mAppBarHeightDp = activity.getResources().getDimension(R.dimen.sesl_action_bar_default_height_padding);

                TypedValue outValue = new TypedValue();
                activity.getResources().getValue(R.dimen.sesl_appbar_height_proportion, outValue, true);

                params.height = (int) ((float) windowHeight * outValue.getFloat());
            }

            appBarLayout.setLayoutParams(params);
            appBarLayout.setPadding(0, 0, 0, abBottomPadding);
        } else
            LogUtils.w(activity.getLocalClassName() + ".resetAppBarHeight", "appBarLayout is null.");
    }


    public void setHomeAsUpButton(View.OnClickListener ocl) {
        setHomeAsUpButton(0, 0, ocl);
    }

    public void setHomeAsUpButtonVisible(boolean visible) {
        toolbarHomeButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        toolbar.setPaddingRelative(visible ? 0 : activity.getResources().getDimensionPixelSize(R.dimen.sesl_action_bar_content_inset), 0, 0, 0);
    }

    public void setHomeAsUpButton(int iconResId, int contentDescResId, View.OnClickListener ocl) {
        if (toolbarHomeButton != null) {
            toolbarHomeButton.setVisibility(View.VISIBLE);

            toolbar.setPadding(0, 0, 0, 0);

            toolbarHomeButton.setBackgroundResource(R.drawable.sesl_action_bar_item_background);

            if (iconResId > 0)
                toolbarHomeButton.setImageResource(iconResId);
            else
                toolbarHomeButton.setImageResource(isNightMode ? R.drawable.sesl_ic_ab_back_dark : R.drawable.sesl_ic_ab_back_light);

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
        if (moreOverflowButton == null) {
            moreOverflowButtonContainer = new FrameLayout(activity);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
            overflowContainer.addView(moreOverflowButtonContainer, lp);

            moreOverflowButton = new ToolbarImageButton(activity);

            ViewGroup.LayoutParams lp2 = new ViewGroup.LayoutParams((int) activity.getResources().getDimension(R.dimen.mesa_toolbar_imagebutton_width),
                    (int) activity.getResources().getDimension(R.dimen.sesl_action_bar_default_height));

            moreOverflowButton.setBackgroundResource(R.drawable.sesl_action_bar_item_background);
            moreOverflowButton.setImageResource(isNightMode ? R.drawable.sesl_ic_menu_overflow_dark : R.drawable.sesl_ic_menu_overflow_light);
            moreOverflowButton.setImageTintList(activity.getResources().getColorStateList(isNightMode ? R.color.sesl_action_menu_text_dark : R.color.sesl_action_menu_text, activity.getTheme()));
            moreOverflowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMoreMenuPopupWindow();
                }
            });
            moreOverflowButton.setTooltipText(activity.getString(R.string.sesl_action_menu_overflow_description));

            moreOverflowButtonContainer.addView(moreOverflowButton, lp2);
        }

        for (int i : linkedHashMap.values()) {
            if (i != 0) {
                initMoreMenuButtonBadge(i);
                break;
            }
        }

        initMoreMenuPopupWindow(linkedHashMap, ocl);
    }

    public void setSubtitleText(String subtitleText) {
        if (collapsingToolbarLayout != null)
            collapsingToolbarLayout.setSubtitle(subtitleText);
        else
            LogUtils.w(activity.getLocalClassName() + ".ActionBarUtils.setSubtitleText", "collapsingToolbarLayout is null.");
    }

    public void setTitleText(String titleText) {
        if (collapsingToolbarLayout != null)
            collapsingToolbarLayout.setTitle(titleText);
        if (toolbarTitle != null)
            toolbarTitle.setText(titleText);
        else
            throw new CerberusException("ActionBarUtils: Can't set title text: AppBar/Toolbar hasn't been initialized yet!");
    }

    public void setTitleText(String bigTitleText, String smallTitleText) {
        if (collapsingToolbarLayout != null && toolbarTitle != null) {
            collapsingToolbarLayout.setTitle(bigTitleText);
            toolbarTitle.setText(smallTitleText);
        } else
            throw new CerberusException("ActionBarUtils: Can't set title text: AppBar/Toolbar hasn't been initialized yet!");
    }

    public void showMoreMenuPopupWindow() {
        if (moreMenuPopupWindow != null || !moreMenuPopupWindow.isShowing())
            moreMenuPopupWindow.showAsDropDown(moreMenuPopupAnchor, moreMenuPopupOffX, 0);
        else
            LogUtils.w(activity.getLocalClassName() + ".showMoreMenuPopupWindow", "moreMenuPopupWindow is null or already shown.");
    }



    private class AppBarOffsetListener implements SeslAppBarLayout.OnOffsetChangedListener {
        @SuppressLint("Range")
        @Override
        public void onOffsetChanged(SeslAppBarLayout layout, int verticalOffset) {
            int layoutPosition = Math.abs(appBarLayout.getTop());
            float alphaRange = ((float) collapsingToolbarLayout.getHeight()) * 0.17999999f;
            float toolbarTitleAlphaStart = ((float) collapsingToolbarLayout.getHeight()) * 0.35f;

            if (appBarLayout.getHeight() <= ((int) mAppBarHeightDp)) {
                toolbarTitle.setAlpha(1.0f);
            } else {
                float collapsedTitleAlpha = ((150.0f / alphaRange) * (((float) layoutPosition) - toolbarTitleAlphaStart));

                if (collapsedTitleAlpha >= 0.0f && collapsedTitleAlpha <= 255.0f) {
                    collapsedTitleAlpha /= 255.0f;
                    toolbarTitle.setAlpha(collapsedTitleAlpha);
                }
                else if (collapsedTitleAlpha < 0.0f)
                    toolbarTitle.setAlpha(0.0f);
                else
                    toolbarTitle.setAlpha(1.0f);
            }
        }
    }

    private class MoreMenuPopupAdapter extends ArrayAdapter {
        ArrayList<String> itemTitle;
        ArrayList<Integer> badgeCount;
        AppCompatActivity activity;

        public MoreMenuPopupAdapter(AppCompatActivity instance, LinkedHashMap<String, Integer> linkedHashMap) {
            super(instance, 0);
            activity = instance;
            itemTitle = new ArrayList(linkedHashMap.keySet());
            badgeCount = new ArrayList(linkedHashMap.values());
        }

        public void setArrays(LinkedHashMap<String, Integer> linkedHashMap) {
            itemTitle = new ArrayList(linkedHashMap.keySet());
            badgeCount = new ArrayList(linkedHashMap.values());
        }

        @Override
        public int getCount() {
            return itemTitle.size();
        }

        @Override
        public Object getItem(int position) {
            return itemTitle.get(position);
        }

        @Override
        public View getView(int index, View view, ViewGroup parent) {
            PopupMenuItem itemVar;

            if (view == null) {
                view = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mesa_view_menu_popup_item_layout, parent, false);
                itemVar = new PopupMenuItem(this);
                itemVar.titleText = view.findViewById(R.id.mesa_titletext_moremenupopup);
                itemVar.badgeIcon = view.findViewById(R.id.mesa_badgetext_moremenupopup);
                view.setTag(itemVar);
            } else {
                itemVar = (PopupMenuItem) view.getTag();
            }

            itemVar.titleText.setText(itemTitle.get(index));
            if (badgeCount.get(index) > 0) {
                int count = badgeCount.get(index);
                if (count > 99) {
                    count = 99;
                }
                String countString = numberFormat.format((long) count);
                itemVar.badgeIcon.setText(countString);
                int width = (int) (activity.getResources().getDimension(R.dimen.sesl_badge_default_width) + (float) countString.length() * activity.getResources().getDimension(R.dimen.sesl_badge_additional_width));
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) itemVar.badgeIcon.getLayoutParams();
                lp.width = width;
                itemVar.badgeIcon.setLayoutParams(lp);
                itemVar.badgeIcon.setVisibility(View.VISIBLE);
            } else if (badgeCount.get(index) == NEW_UPDATE_AVAILABLE) {
                itemVar.badgeIcon.setText("N");
                itemVar.badgeIcon.setVisibility(View.VISIBLE);
            } else {
                itemVar.badgeIcon.setVisibility(View.GONE);
            }

            if (getCount() <= 1) {
                view.setBackgroundResource(isNightMode ? R.drawable.mesa_menu_popup_item_bg_allr_dark : R.drawable.mesa_menu_popup_item_bg_allr_light);
            } else if (index == 0) {
                view.setBackgroundResource(isNightMode ? R.drawable.mesa_menu_popup_item_bg_topr_dark : R.drawable.mesa_menu_popup_item_bg_topr_light);
            } else if (index == getCount() - 1) {
                view.setBackgroundResource(isNightMode ? R.drawable.mesa_menu_popup_item_bg_bottomr_dark : R.drawable.mesa_menu_popup_item_bg_bottomr_light);
            } else {
                view.setBackgroundResource(isNightMode ? R.drawable.mesa_menu_popup_item_bg_nor_dark : R.drawable.mesa_menu_popup_item_bg_nor_light);
            }

            return view;
        }
    }

    private class PopupMenuItem {
        MoreMenuPopupAdapter adapter;
        TextView titleText;
        TextView badgeIcon;

        PopupMenuItem(MoreMenuPopupAdapter instance) {
            adapter = instance;
        }
    }

}
