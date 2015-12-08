package com.benio.mpost.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.benio.mpost.bean.ForbiddenUser;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;

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


    /**
     * 检查user是否存在list中
     *
     * @param user
     * @param list
     * @return
     */
    public static boolean isUserInList(MUser user, List<MUser> list) {
        if (list != null) {
            for (MUser u : list) {
                if (u.equals(user)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isForbiddenUser(MUser user, List<ForbiddenUser> list) {
        if (list != null) {
            for (ForbiddenUser u : list) {
                MUser tmp = u.getUser();
                if (tmp != null && tmp.equals(user)) {
                    return true;
                }
            }
        }
        return false;
    }

}
