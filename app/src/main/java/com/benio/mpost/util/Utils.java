package com.benio.mpost.util;

import android.content.Context;
import android.util.DisplayMetrics;

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


}
