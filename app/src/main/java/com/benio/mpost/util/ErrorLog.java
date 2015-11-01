package com.benio.mpost.util;

/**
 * 错误Log
 * Created by benio on 2015/10/22.
 */
public class ErrorLog {

    private static final String TAG = ErrorLog.class.getSimpleName();

    public static void log(int code, String msg) {
        AKLog.e(TAG, "Error: code: " + code + " msg:" + msg);
    }

    private ErrorLog() {
    }
}
