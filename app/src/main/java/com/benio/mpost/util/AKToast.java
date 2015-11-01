package com.benio.mpost.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * Created by benio on 2015/10/10.
 */
public class AKToast {
    private static Toast sToast;

    public static void show(Context ctx, int resId) {
        show(ctx, Toast.LENGTH_SHORT, resId);
    }

    public static void show(Context ctx, CharSequence text) {
        show(ctx, Toast.LENGTH_SHORT, text);
    }

    public static void show(Context ctx, int duration, int resID) {
        show(ctx, duration, ctx.getString(resID));
    }

    public static void show(Context ctx, int duration, CharSequence text) {
        if (sToast == null) {
            sToast = Toast.makeText(ctx, text, duration);
        } else {
            sToast.setText(text);
            sToast.setDuration(duration);
        }
        sToast.show();
    }

    public static void cancel() {
        if (sToast != null) {
            sToast.cancel();
        }
    }

    private AKToast() {
    }
}
