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
    private Integer likeCount;
    /**
     * 收藏数量
     */
    private Integer favorCount;
//    /**
//     * 保存帖子与赞帖人的关系
//     */
//    private BmobRelation likeRelation;
//    /** 保存帖子与收藏人的关系 */
//    private BmobRelation favorRelation;
    /**
     * 是否已被收藏
     */
    private Boolean isFavored;
    /**
     * 是否已被赞
     */
    private Boolean isLiked;
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

//    public BmobRelation getLikeRelation() {
//        return likeRelation;
//    }
//
//    public void setLikeRelation(BmobRelation likeRelation) {
//        this.likeRelation = likeRelation;
//    }
//
//    public BmobRelation getFavorRelation() {
//        return favorRelation;
//    }
//
//    public void setFavorRelation(BmobRelation favorRelation) {
//        this.favorRelation = favorRelation;
//    }

    public Boolean isFavored() {
        return isFavored;
    }

    public void setFavored(Boolean isFavored) {
        this.isFavored = isFavored;
    }

    public Boolean isLiked() {
        return isLiked;
    }

    public void setLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
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
