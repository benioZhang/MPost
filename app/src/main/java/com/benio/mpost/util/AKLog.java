package com.benio.mpost.util;

/**
 * Log工具类
 * Created by benio on 2015/10/10.
 */
public class AKLog {
    private static final String TAG = AKLog.class.getSimpleName();

    public final static boolean DEBUG = true;

    public static void pln(Object obj) {
        if (DEBUG && null != obj)
            System.out.println(obj);
    }

    public static void p(Object obj) {
        if (DEBUG && null != obj)
            System.out.print(obj);
    }

    public static void i(String tag, String msg) {
        if (DEBUG && null != msg)
            android.util.Log.i(tag, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG && null != msg)
            android.util.Log.d(tag, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG && null != msg)
            android.util.Log.e(tag, msg);
    }

    public static void e(String msg) {
        if (DEBUG && null != msg)
            android.util.Log.e(TAG, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG && null != msg)
            android.util.Log.v(tag, msg);
    }

    public static void v(String msg) {
        v(TAG, msg);
    }

    private AKLog() {
    }
}
