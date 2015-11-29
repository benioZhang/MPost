package com.benio.mpost.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.benio.mpost.bean.MPost;

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

    /**
     * 检查post是否存在list中
     *
     * @param post
     * @param list
     * @return
     */
    public static boolean isPostInList(MPost post, List<MPost> list) {
        if (list == null || list.isEmpty()) return false;
        for (MPost mPost : list) {
            if (TextUtils.equals(mPost.getObjectId(), post.getObjectId())) return true;
        }
        return false;
    }
}
