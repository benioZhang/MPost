package com.benio.mpost.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 贴
 * Created by benio on 2015/10/10.
 */
public class MPost extends BmobObject implements Serializable {
    /**
     * 最大图片数量
     */
    public static final int MAX_PHOTO_COUNT = 6;
    /**
     * 最少图片数量
     */
    public static final int MIN_PHOTO_COUNT = 1;

    /**
     * 内容
     */
    private String content;
    /**
     * 图片
     */
    private List<String> photoList;
    /**
     * 作者
     */
    private MUser author;
    /**
     * 赞的数量
     */
    private int likeCount;
    /**
     * 收藏数量
     */
    private int favorCount;
    /**
     * 是否已被收藏
     */
    private boolean isFavored;
    /**
     * 是否已被赞
     */
    private boolean isLiked;
    /**
     * 可见性：公开，私有
     * {@link PostVisibility}
     */
    private Integer visibility;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

    public MUser getAuthor() {
        return author;
    }

    public void setAuthor(MUser author) {
        this.author = author;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public void increaseLike(int count) {
        this.likeCount += count;
    }

    public void increaseFavor(int count) {
        this.favorCount += count;
    }

    public Integer getFavorCount() {
        return favorCount;
    }

    public void setFavorCount(Integer favorCount) {
        this.favorCount = favorCount;
    }

    public boolean isFavored() {
        return isFavored;
    }

    public void setFavored(boolean isFavored) {
        this.isFavored = isFavored;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public boolean hasPhoto() {
        return photoList != null && !photoList.isEmpty();
    }

    @Override
    public String toString() {
        return "{" +
                "isFavored=" + isFavored +
                ", isLiked=" + isLiked +
                ", visibility=" + visibility +
                ", favorCount=" + favorCount +
                ", likeCount=" + likeCount +
                ", author=" + author +
                ", content='" + content + '\'' +
                ", photoList=" + photoList +
                '}';
    }


}
