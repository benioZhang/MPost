package com.benio.mpost.interf;

import com.benio.mpost.bean.MPost;
import com.benio.mpost.interf.impl.ResponseListener;

/**
 * 收藏(贴)接口
 * Created by benio on 2015/10/10.
 */
public interface FavorListener {
    /**
     * 收藏/取消收藏
     *
     * @param post      收藏的贴
     * @param isFavored 是否收藏
     * @param listener  回调接口
     */
    void favor(MPost post, boolean isFavored, ResponseListener listener);

    /**
     * @param post 收藏的贴
     * @return 是否正在收藏
     */
    boolean isFavored(MPost post);
}
