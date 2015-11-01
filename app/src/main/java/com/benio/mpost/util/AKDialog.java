package com.benio.mpost.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

/**
 * Dialog工具类
 * Created by benio on 2015/10/11.
 */
public class AKDialog {

    public static AlertDialog.Builder getDialog(Context context) {
        return new AlertDialog.Builder(context);
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /** 提示信息Dialog */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg) {
        return getMessageDialog(context, null, msg, null);
    }

    /** 提示信息Dialog */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg) {
        return getMessageDialog(context, title, msg, null);
    }

    /** 提示信息Dialog */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg, DialogInterface.OnClickListener okListener) {
        return getMessageDialog(context, null, msg, okListener);
    }

    /** 提示信息Dialog */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton("确定", okListener);
        return builder;
    }

    /** 确认对话框 */
    public static AlertDialog.Builder getConfirmDialog(Context context, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, null, msg, okListener, null);
    }

    /** 确认对话框 */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, title, msg, okListener, null);
    }

    /** 确认对话框 */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton("确定", okListener);
        builder.setNegativeButton("取消", cancelListener);
        return builder;
    }

    /** 列表对话框 */
    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, null, arrays, onClickListener);
    }

    /** 列表对话框 */
    public static AlertDialog.Builder getSelectDialog(Context context, String title,
                                                      String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        return builder;
    }

    /** 单选对话框 */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, null, arrays, selectIndex, onClickListener);
    }

    /** 单选对话框 */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        // builder.setNegativeButton("取消", null);
        return builder;
    }

    private AKDialog() {
    }

}
