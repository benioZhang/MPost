package com.benio.mpost.event;

/**
 * Created by shau-lok on 12/10/15.
 */
public class UserAvatarUploadEvent {
    public String path;

    public UserAvatarUploadEvent(String path) {
        this.path = path;
    }
}
