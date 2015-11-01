package com.benio.mpost.interf;

import com.benio.mpost.bean.MPost;
import com.benio.mpost.interf.impl.ResponseListener;

/**
 * 赞帖接口
 * Created by benio on 2015/10/11.
 */
public interface LikeListener {
    /**
     * 点赞
     *
     * @param post     贴
     * @param listener 回调接口
     */
    void like(MPost post, boolean isLiked, ResponseListener listener);

    /**
     * 是否已赞
     *
     * @param post 贴
     */
    boolean isLike(MPost post);
}
