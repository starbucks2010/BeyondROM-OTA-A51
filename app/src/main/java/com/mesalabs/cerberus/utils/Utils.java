package com.mesalabs.cerberus.utils;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    public static Object genericGetField(Object obj, String fieldName) {
        Field field;
        Object requiredObj = null;
        try {
            field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            requiredObj = field.get(obj);
        } catch (NoSuchFieldException e) {
            LogUtils.e("Utils.genericGetField", e.toString());
        } catch (IllegalArgumentException e) {
            LogUtils.e("Utils.genericGetField", e.toString());
        } catch (IllegalAccessException e) {
            LogUtils.e("Utils.genericGetField", e.toString());
        }

        return requiredObj;
    }

    public static Object genericInvokeMethod(Object obj, String methodName, Object... params) {
        int paramCount = params.length;
        Method method;
        Object requiredObj = null;
        Class<?>[] classArray = new Class<?>[paramCount];
        for (int i = 0; i < paramCount; i++) {
            // FIX
            if (params[i].getClass() == Boolean.class)
                classArray[i] = boolean.class;
            else if (params[i].getClass() == Integer.class)
                classArray[i] = int.class;
            else
                classArray[i] = params[i].getClass();
        }
        try {
            method = obj.getClass().getDeclaredMethod(methodName, classArray);
            method.setAccessible(true);
            requiredObj = method.invoke(obj, params);
        } catch (NoSuchMethodException e) {
            LogUtils.e("Utils.genericInvokeMethod", e.toString());
        } catch (IllegalArgumentException e) {
            LogUtils.e("Utils.genericInvokeMethod", e.toString());
        } catch (IllegalAccessException e) {
            LogUtils.e("Utils.genericInvokeMethod", e.toString());
        } catch (InvocationTargetException e) {
            LogUtils.e("Utils.genericInvokeMethod", e.toString());
        }

        return requiredObj;
    }

    public static Object genericInvokeMethod(Class<?> cl, Object obj, String methodName, Object... params) {
        int paramCount = params.length;
        Method method;
        Object requiredObj = null;
        Class<?>[] classArray = new Class<?>[paramCount];
        for (int i = 0; i < paramCount; i++) {
            // FIX
            if (params[i].getClass() == Boolean.class)
                classArray[i] = boolean.class;
            else if (params[i].getClass() == Integer.class)
                classArray[i] = int.class;
            else
                classArray[i] = params[i].getClass();
        }
        try {
            method = cl.getDeclaredMethod(methodName, classArray);
            method.setAccessible(true);
            requiredObj = method.invoke(obj, params);
        } catch (NoSuchMethodException e) {
            LogUtils.e("Utils.genericInvokeMethod", e.toString());
        } catch (IllegalArgumentException e) {
            LogUtils.e("Utils.genericInvokeMethod", e.toString());
        } catch (IllegalAccessException e) {
            LogUtils.e("Utils.genericInvokeMethod", e.toString());
        } catch (InvocationTargetException e) {
            LogUtils.e("Utils.genericInvokeMethod", e.toString());
        }

        return requiredObj;
    }

    public static Object genericInvokeStaticMethod(Class<?> cl, String methodName, Object... params) {
        int paramCount = params.length;
        Method method;
        Object requiredObj = null;
        Class<?>[] classArray = new Class<?>[paramCount];
        for (int i = 0; i < paramCount; i++) {
            // FIX
            if (params[i].getClass() == Boolean.class)
                classArray[i] = boolean.class;
            else if (params[i].getClass() == Integer.class)
                classArray[i] = int.class;
            else
                classArray[i] = params[i].getClass();
        }
        try {
            method = cl.getDeclaredMethod(methodName, classArray);
            method.setAccessible(true);
            requiredObj = method.invoke(null, params);
        } catch (NoSuchMethodException e) {
            LogUtils.e("Utils.genericInvokeStaticMethod", e.toString());
        } catch (IllegalArgumentException e) {
            LogUtils.e("Utils.genericInvokeStaticMethod", e.toString());
        } catch (IllegalAccessException e) {
            LogUtils.e("Utils.genericInvokeStaticMethod", e.toString());
        } catch (InvocationTargetException e) {
            LogUtils.e("Utils.genericInvokeStaticMethod", e.toString());
        }

        return requiredObj;
    }

    public static boolean isInMultiWindowMode(Activity activity) {
        if (isInSamsungMultiWindowMode())
            return true;
        else
            return activity.isInMultiWindowMode();
    }

    public static boolean isInSamsungDeXMode(Context context) {
        return context.getResources().getBoolean(R.bool.sesl_desktop_mode);
    }

    private static boolean isInSamsungMultiWindowMode() {
        String mwState = PropUtils.get("sys.multiwindow.running", "not-support");

        if (mwState.equals("not-support")) {
            LogUtils.e("Utils.isInSamsungMultiWindowMode", "S Multi Window feature is not available");
            return false;
        }

        return mwState.equals("1");
    }

    public static boolean isNightMode(Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.isLightTheme, outValue, true);

        return outValue.data == 0;
    }

    public static boolean isTabletDevice(Context context) {
        return ViewUtils.getSmallestDeviceWidthDp(context) >= 685;
    }

    public static void updateListBothSideMargin(Context context, ViewGroup layout) {
        if (layout != null) {
            if (!ViewUtils.isMultiWindowMinSize(context, 801, true))
                layout.getLayoutParams().width = (int) (((double) context.getResources().getDisplayMetrics().widthPixels) * 0.75d);
            else
                layout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        }
    }

}
