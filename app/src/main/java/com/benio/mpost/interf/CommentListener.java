package com.benio.mpost.interf;

import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.interf.impl.ResponseListener;

/**
 * 评论贴/回复评论
 * Created by benio on 2015/10/13.
 */
public interface CommentListener {
    /**
     * 评论贴
     *
     * @param post     贴
     * @param content  评论内容
     * @param listener 回调接口
     */
    void comment(MPost post, String content, ResponseListener listener);

    /**
     * 回复评论
     *
     * @param post     贴
     * @param toUser   回复对象
     * @param content  评论内容
     * @param listener 回调接口
     */
    void comment(MPost post, MUser toUser, String content, ResponseListener listener);
}
