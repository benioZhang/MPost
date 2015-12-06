package com.benio.mpost.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 用户
 * Created by benio on 2015/10/10.
 */
public class MUser extends BmobUser implements Serializable {
    /**
     * 昵称
     */
    private String name;
    /**
     * 头像url
     */
    private String portraitUrl;
    /**
     * 保存关注的用户关系
     */
    private BmobRelation followRelation;
    /**
     * 保存用户收藏post关系
     */
    private BmobRelation favRelation;
    /**
     * 保存用户点赞post关系
     */
    private BmobRelation likeRelation;

    /** 管理员控制能否post **/
    private boolean canNotPost;

    public boolean getCanNotPost() {
        return canNotPost;
    }

    public void setCanNotPost(boolean canNotPost) {
        this.canNotPost = canNotPost;
    }

    public BmobRelation getLikeRelation() {
        return likeRelation;
    }

    public void setLikeRelation(BmobRelation likeRelation) {
        this.likeRelation = likeRelation;
    }

    public BmobRelation getFavRelation() {
        return favRelation;
    }

    public void setFavRelation(BmobRelation favRelation) {
        this.favRelation = favRelation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public boolean hasPortrait() {
        return portraitUrl != null;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public BmobRelation getFollowRelation() {
        return followRelation;
    }

    public void setFollowRelation(BmobRelation followRelation) {
        this.followRelation = followRelation;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                "canNotPost='" + canNotPost + '\'' +
                ", portraitUrl='" + portraitUrl + '\'' +
                ", followRelation=" + followRelation +
                ", favRelation=" + favRelation +
                ", likeRelation=" + likeRelation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof MUser && ((MUser) o).getObjectId().equals(getObjectId());
    }
}
