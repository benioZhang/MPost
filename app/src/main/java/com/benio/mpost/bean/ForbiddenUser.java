package com.benio.mpost.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by benio on 2015/12/6.
 */
public class ForbiddenUser extends BmobObject {
    private MUser user;
    private int status;

    public MUser getUser() {
        return user;
    }

    public void setUser(MUser user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "user=" + user +
                ", status=" + status +
                '}';
    }
}
