package com.benio.mpost.interf;

import com.benio.mpost.bean.MUser;
import com.benio.mpost.interf.impl.ResponseListener;

/**
 * 关注(用户)接口
 * Created by benio on 2015/10/11.
 */
public interface FollowListener {
    /**
     * 关注
     *
     * @param user     关注对象
     * @param follow   true：关注
     * @param listener 回调接口
     */
    void follow(MUser user, boolean follow, ResponseListener listener);

    /**
     * 是否正在关注该用户
     * @param user
     * @return
     */
    boolean isFollowing(MUser user);
}
