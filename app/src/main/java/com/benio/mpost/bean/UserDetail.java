package com.benio.mpost.bean;

import java.util.List;

/**
 * Created by benio on 2015/10/31.
 */
public class UserDetail {
    /** 当前登录用户是否已关注该用户 */
    private boolean isFollowing;
    /** 用户 */
    private MUser user;
    /** 用户的发帖列表 */
    private List<MPost> postList;
    /**
     * 记录准备好的数量
     * isFollowing,postList
     * 要分别访问网络才能知道
     * 所以想用readyCount来记录已经成功访问的数量
     */
    private int readyCount;

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
        increaseReady();
    }

    public MUser getUser() {
        return user;
    }

    public void setUser(MUser user) {
        this.user = user;
        increaseReady();
    }

    public List<MPost> getPostList() {
        return postList;
    }

    public void setPostList(List<MPost> postList) {
        this.postList = postList;
        increaseReady();
    }

    public boolean isReady() {
        return readyCount >= 3;
    }

    /**
     * 增加readyCount
     * 使用synchronized同步
     */
    public void increaseReady() {
        if (isReady()) {
            return;
        }
        synchronized (this) {
            this.readyCount++;
        }
//        AKLog.i("xxxx", "readyCount: " + readyCount);
    }
}
