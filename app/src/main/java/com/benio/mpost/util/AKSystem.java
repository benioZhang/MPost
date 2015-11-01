package com.benio.mpost.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * File工具类
 * Created by benio on 2015/10/18.
 */
public class AKSystem {
    /**
     * @return 否有外部存储设备
     */
    public static boolean isExternalStorageMounted() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 转换dip为px
     */
    public static int convertDIP2PX(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 转换px为dip
     */
    public static int convertPX2DIP(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    public static int screenHeight = 0;
    public static int screenWidth = 0;
    public static float screenDensity = 0;

    public static int getScreenHeight(Activity context) {
        if (screenWidth == 0 || screenHeight == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenDensity = dm.density;
            screenHeight = dm.heightPixels;
            screenWidth = dm.widthPixels;
        }
        return screenHeight;
    }

    public static int getScreenWidth(Activity context) {
        if (screenWidth == 0 || screenHeight == 0) {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenDensity = dm.density;
            screenHeight = dm.heightPixels;
            screenWidth = dm.widthPixels;
        }
        return screenWidth;
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    private AKSystem() {
    }
}
