package com.benio.mpost.util;

import android.app.Activity;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * View工具类
 * Created by benio on 2015/10/11.
 */
public class AKView {

    /**
     * 重设View的LayoutParams
     *
     * @param view
     * @param width
     * @param height
     */
    public static void updateLayoutParams(View view, int width, int height) {
        if (null == view) {
            return;
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = height;
        params.width = width;
        view.setLayoutParams(params);
    }

    /**
     * 获取textView的文字
     *
     * @param textView
     * @return
     */
    public static String getText(TextView textView) {
        return textView == null ? null : textView.getText().toString();
    }

    /**
     * 获取TextInputLayout中EditText的输入
     *
     * @param inputLayout
     * @return
     */
    public static String getText(TextInputLayout inputLayout) {
        if (null == inputLayout) {
            return null;
        }
        EditText editText = inputLayout.getEditText();
        return null == editText ? null : getText(editText);
    }

    /**
     * @param textView
     * @return textView里的文字是否为null，""，或者"            "
     */
    public static boolean isEmpty(TextView textView) {
        String text = getText(textView);
        return TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim());
    }

    /**
     * @param inputLayout
     * @return 获取TextInputLayout中EditText的输入是否为null，""，或者"          "
     */
    public static boolean isEmpty(TextInputLayout inputLayout) {
        String input = getText(inputLayout);
        return TextUtils.isEmpty(input) || TextUtils.isEmpty(input.trim());
    }

    /**
     * 设置toolbar
     *
     * @param activity appcompatActivity
     * @param id       toolbar id
     */
    public static void setToolbar(Activity activity, @IdRes int id) {
        if (activity != null && activity instanceof AppCompatActivity) {
            Toolbar toolbar = (Toolbar) activity.findViewById(id);
            if (toolbar != null) {
                ((AppCompatActivity) activity).setSupportActionBar(toolbar);
            }
        }
    }


    /**
     * 设置返回图标
     *
     * @param activity
     * @param resId
     */
    public static void setHomeIndicator(AppCompatActivity activity, @DrawableRes int resId) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(resId);
        }
    }

    /**
     * 设置view的可见性
     *
     * @param view
     * @param visible
     */
    public static void setViewVisible(View view, boolean visible) {
        if (null == view) {
            return;
        }
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }


    /** private constructor */
    private AKView() {
    }
}
