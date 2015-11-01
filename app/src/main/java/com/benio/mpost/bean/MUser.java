package com.benio.mpost.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 用户
 * Created by benio on 2015/10/10.
 */
public class MUser extends BmobUser implements Serializable {
    /** 昵称 */
    private String name;
    /** 头像url */
    private String portraitUrl;
    /** 保存关注的用户关系 */
    private BmobRelation followRelation;


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
                ", portraitUrl='" + portraitUrl + '\'' +
                ", followRelation=" + followRelation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof MUser && ((MUser) o).getObjectId().equals(getObjectId());
    }
}
