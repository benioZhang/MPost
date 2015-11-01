package com.benio.mpost.bean;

/**
 * 可见性
 * Created by benio on 2015/10/21.
 */
public class PostVisibility {

    /**
     * 公开
     */
    public static final PostVisibility PUBLIC = new PostVisibility(0, "公开");
    /**
     * 私有
     */
    public static final PostVisibility PRIVATE = new PostVisibility(1, "私有");

    public static final PostVisibility[] VALUES = {PUBLIC, PRIVATE};

    private int visibility;

    private String description;

    public static boolean isVisible(int visibility) {
        return visibility == PUBLIC.visibility;
    }

    public static boolean isVisible(PostVisibility postVisibility) {
        return postVisibility != null && postVisibility.visibility == PUBLIC.visibility;
    }

    public int getVisibility() {
        return visibility;
    }

    public String getDescription() {
        return description;
    }

    private PostVisibility(int visibility, String description) {
        this.visibility = visibility;
        this.description = description;
    }
}
