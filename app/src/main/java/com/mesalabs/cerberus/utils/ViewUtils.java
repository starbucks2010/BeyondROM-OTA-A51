package com.mesalabs.cerberus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

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

public class ViewUtils {

    public static int dp2px(Context context, float f) {
        try {
            return (int) ((context.getResources().getDisplayMetrics().density * f) + 0.5f);
        } catch (Exception e) {
            return (int) (f + 0.5f);
        }
    }

    private static double getDensity(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager == null ? null : windowManager.getDefaultDisplay();
        if (display != null) {
            display.getRealMetrics(metrics);
        }
        if (display == null) {
            return 1.0d;
        }
        return ((double) configuration.densityDpi) / ((double) metrics.densityDpi);
    }

    public static float getDIPForPX(Activity activity, int i) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) i, activity.getResources().getDisplayMetrics());
    }

    public static int getPortraitOrientation(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            if (windowManager == null) {
                return 0;
            }

            return windowManager.getDefaultDisplay().getRotation();
        } catch (Exception unused) {
            LogUtils.e("ViewUtils", "cannot get portrait orientation");
            return 0;
        }
    }

    public static int getSmallestDeviceWidthDp(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealMetrics(displayMetrics);
        LogUtils.d("ViewUtils", "metrics = " + displayMetrics);
        return Math.round(Math.min(((float) displayMetrics.heightPixels) / displayMetrics.density, ((float) displayMetrics.widthPixels) / displayMetrics.density));
    }

    public static int getStatusbarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelOffset(resourceId);
        }
        return 0;
    }

    public static int getWindowWidth(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            if (windowManager == null) {
                return 0;
            }

            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            return point.x;
        } catch (Exception unused) {
            LogUtils.e("ViewUtils", "cannot get window width");
            return 0;
        }
    }

    public static int getWindowHeight(Context context) {
        try {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            if (windowManager == null) {
                return 0;
            }

            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            return point.y;
        } catch (Exception unused) {
            LogUtils.e("ViewUtils", "cannot get window width");
            return 0;
        }
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isMultiWindowMinSize(Context context, int minSizeDp, boolean isWidth) {
        Configuration configuration = context.getResources().getConfiguration();
        return ((int) (((double) (isWidth ? configuration.screenWidthDp : configuration.screenHeightDp)) * getDensity(context))) <= minSizeDp;
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean isRTLMode(Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public static boolean isSupportSoftNavigationBar(Context context) {
        int id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && context.getResources().getBoolean(id);
    }

    public static boolean isVisibleNaviBar(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "navigationbar_hide_bar_enabled", 0) == 0;
    }

    public static void nullViewDrawablesRecursive(View view) {
        if (view == null)
            return;

        if (view instanceof ViewGroup) {
            try {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    nullViewDrawablesRecursive(viewGroup.getChildAt(index));
                }
            } catch (Exception e) {
                LogUtils.e("ViewUtils", "nullViewDrawablesRecursive InflateException");
            }
        } else {
            nullViewDrawable(view);
        }
    }

    private static void nullViewDrawable(View view) {
        try {
            view.setOnClickListener(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setOnClickListener Exception");
        }
        try {
            view.setOnCreateContextMenuListener(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setOnCreateContextMenuListener Exception");
        }
        try {
            view.setOnFocusChangeListener(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setOnFocusChangeListener Exception");
        }
        try {
            view.setOnKeyListener(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setOnKeyListener Exception");
        }
        try {
            view.setOnLongClickListener(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setOnLongClickListener Exception");
        }
        try {
            view.setTouchDelegate(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setTouchDelegate Exception");
        }
        try {
            view.setBackground(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setBackground Exception");
        }
        try {
            view.setAnimation(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setAnimation Exception");
        }
        try {
            view.setContentDescription(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setContentDescription Exception");
        }
        try {
            view.setTag(null);
        } catch (Exception e) {
            LogUtils.e("ViewUtils", "nullViewDrawable setTag Exception");
        }

        Drawable d = view.getBackground();
        if (d != null) {
            try {
                d.setCallback(null);
            } catch (Exception e11) {
                LogUtils.e("ViewUtils", "nullViewDrawable setCallback Exception");
            }
        }
        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            Drawable d2 = imageView.getDrawable();
            if (d2 != null) {
                d2.setCallback(null);
            }
            imageView.setImageDrawable(null);
            imageView.setBackground(null);
        }
    }

    public static void resolvePadding(ViewGroup viewGroup) {
        Utils.genericInvokeMethod(viewGroup, "resolvePadding");
    }

    public static Object semGetHoverPopup(View view, boolean z) {
        return Utils.genericInvokeMethod(view, Build.VERSION.SDK_INT >= 29 ? "hidden_semGetHoverPopup" : "semGetHoverPopup", z);
    }

    public static int semGetHoverPopupType(View view) {
        return (int) Utils.genericInvokeMethod(view, "semGetHoverPopupType");
    }

    public static void semSetHoverPopupType(View view, int type) {
        Utils.genericInvokeMethod(view, Build.VERSION.SDK_INT >= 29 ? "hidden_semSetHoverPopupType" : "semSetHoverPopupType", type);
    }

    public static void semSetRoundedCorners(View view, int roundMode) {
        Utils.genericInvokeMethod(view, "semSetRoundedCorners", roundMode);
    }

    public static void semSetRoundedCornerColor(View view, int roundMode, int color) {
        Utils.genericInvokeMethod(view, "semSetRoundedCornerColor", roundMode, color);
    }

    public static void updateListBothSideMargin(final Activity activity, final ViewGroup viewGroup) {
        if (viewGroup != null && activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
            activity.findViewById(android.R.id.content).post(new Runnable() {
                public void run() {
                    int width = activity.findViewById(android.R.id.content).getWidth();
                    Configuration configuration = activity.getResources().getConfiguration();
                    if (configuration.screenHeightDp <= 411 || configuration.screenWidthDp < 512) {
                        viewGroup.setPadding(0, 0, 0, 0);
                        return;
                    }
                    viewGroup.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int screenWidthDp = configuration.screenWidthDp;
                    if (screenWidthDp < 685 || screenWidthDp > 959) {
                        if (screenWidthDp >= 960 && screenWidthDp <= 1919) {
                            int i = (int) (((float) width) * 0.125f);
                            viewGroup.setPadding(i, 0, i, 0);
                        } else if (configuration.screenWidthDp >= 1920) {
                            int i = (int) (((float) width) * 0.25f);
                            viewGroup.setPadding(i, 0, i, 0);
                        } else {
                            viewGroup.setPadding(0, 0, 0, 0);
                        }
                    } else {
                        int i = (int) (((float) width) * 0.05f);
                        viewGroup.setPadding(i, 0, i, 0);
                    }
                }
            });
        }
    }


}
