package com.benio.mpost.bean;

import cn.bmob.v3.BmobObject;

/**
 * 评论
 * Created by benio on 2015/10/10.
 */
public class Comment extends BmobObject {
    /** 发起方 */
    private MUser fromUser;
    /** 要回复的人 */
    private MUser toUser;
    /** 评论内容 */
    private String content;
    /** 帖子 */
    private MPost post;

    public MUser getToUser() {
        return toUser;
    }

    public void setToUser(MUser toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MPost getPost() {
        return post;
    }

    public void setPost(MPost post) {
        this.post = post;
    }

    public MUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(MUser fromUser) {
        this.fromUser = fromUser;
    }

    @Override
    public String toString() {
        return "{" +
                "fromUser=" + fromUser +
                ", toUser=" + toUser +
                ", content='" + content + '\'' +
                ", post=" + post +
                '}';
    }
}
