package com.benio.mpost.bean;

import com.benio.mpost.util.AKLog;

import java.util.List;

/**
 * Created by benio on 2015/10/27.
 */
public class PostDetail {
    /** 帖子 */
    private MPost post;
    /** 是否已被收藏 */
    private Boolean isFavored;
    /** 是否已被赞 */
    private Boolean isLiked;
    /** 评论列表 */
    private List<Comment> commentList;
    /**
     * 记录准备好的数量
     * 因为isFavored,isLiked和commentList
     * 要分别访问网络才能知道
     * 所以想用readyCount来记录已经成功访问的数量
     */
    private int readyCount;

    public MPost getPost() {
        return post;
    }

    public void setPost(MPost post) {
        this.post = post;
        increaseReady();
    }

    public Boolean isFavored() {
        return isFavored;
    }

    public void setFavored(Boolean isFavored) {
        this.isFavored = isFavored;
        increaseReady();
    }

    public Boolean isLiked() {
        return isLiked;
    }

    public void setLiked(Boolean isLiked) {
        this.isLiked = isLiked;
        increaseReady();
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
        increaseReady();
    }

    public boolean isReady() {
        return readyCount >= 4;
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
        AKLog.i("xxxx", "readyCount: " + readyCount);
    }
}
