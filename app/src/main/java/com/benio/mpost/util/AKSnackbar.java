package com.benio.mpost.util;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Snackbar工具类
 * Created by benio on 2015/10/11.
 */
public class AKSnackbar {

    public void show(View view, @StringRes int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
    }

    public void show(View view, CharSequence text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public void show(View view, @StringRes int resId, int duration) {
        Snackbar.make(view, resId, duration).show();
    }


    private AKSnackbar() {
    }
}
