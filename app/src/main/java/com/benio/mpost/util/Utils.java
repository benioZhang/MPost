package com.benio.mpost.util;

import android.content.Context;
import android.util.DisplayMetrics;

import java.util.List;

/**
 * Created by shau-lok on 11/4/15.
 */
public class Utils {

    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean checkListEmpty(List<?> list) {
        return list == null || list.size() < 1;
    }


}
