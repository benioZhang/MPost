package com.benio.mpost.interf.impl;

import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.interf.CommentListener;
import com.benio.mpost.interf.FavorListener;
import com.benio.mpost.interf.FollowListener;
import com.benio.mpost.interf.LikeListener;

/**
 * 处理者
 * Created by benio on 2015/10/27.
 */
public class ActionHandler implements FavorListener, LikeListener, FollowListener, CommentListener {
    private static ActionHandler sActionHandler;

    private MUser mUser;

    public static ActionHandler getInstance(MUser user) {
        if (null == sActionHandler) {
            sActionHandler = new ActionHandler(user);
        }
        return sActionHandler;
    }

    public ActionHandler(MUser user) {
        this.mUser = user;
    }

    @Override
    public void comment(MPost post, String content, ResponseListener listener) {

    }

    @Override
    public void comment(MPost post, MUser toUser, String content, ResponseListener listener) {

    }

    @Override
    public void favor(MPost post, boolean isFavored, ResponseListener listener) {
        MPostApi.favorPost(mUser, post, isFavored, listener);
    }

    @Override
    public boolean isFavored(MPost post) {
//        MPostApi.isFavoredPost(mUser, post, new GetListener<MUser>() {
//            @Override
//            public void onSuccess(MUser user) {
//                if (user != null) {
//                    AKLog.i(user.toString());
//                    post.setFavored(true);
//                }
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                ErrorLog.log(i, s);
//                post.setFavored(false);
//            }
//        });
        return post.isFavored();
    }

    @Override
    public void follow(MUser user, boolean follow, ResponseListener listener) {

    }

    @Override
    public boolean isFollowing(MUser user) {
        return false;
    }

    @Override
    public void like(MPost post, boolean isLiked, ResponseListener listener) {

    }

    @Override
    public boolean isLike(MPost post) {
        return false;
    }
}
