package com.benio.mpost.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by shau-lok on 11/5/15.
 */
public class MLike extends BmobObject {

    // TODO: 11/5/15  
    private MUser fromUser;
    private MUser toUser;
    private MPost post;
    private Integer likeCount;

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public MUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(MUser fromUser) {
        this.fromUser = fromUser;
    }

    public MUser getToUser() {
        return toUser;
    }

    public void setToUser(MUser toUser) {
        this.toUser = toUser;
    }

    public MPost getPost() {
        return post;
    }

    public void setPost(MPost post) {
        this.post = post;
    }


}
