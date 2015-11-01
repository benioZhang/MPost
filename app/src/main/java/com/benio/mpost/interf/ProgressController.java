package com.benio.mpost.interf;

import android.app.ProgressDialog;
import android.support.annotation.StringRes;

/**
 * ProgressDialog的控制
 * Created by benio on 2015/10/10.
 */
public interface ProgressController {
    /**
     * 隐藏等待框
     */
    void hideProgress();

    /**
     * 显示等待框
     */
    ProgressDialog showProgress();

    /**
     * 显示等待框
     *
     * @param resId R.string.xx progressDialog显示文字
     */
    ProgressDialog showProgress(@StringRes int resId);

    /**
     * 显示等待框
     *
     * @param text progressDialog显示文字
     */
    ProgressDialog showProgress(CharSequence text);
}
