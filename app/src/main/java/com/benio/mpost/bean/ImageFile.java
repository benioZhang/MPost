package com.benio.mpost.bean;

/**
 * 图片文件
 * Created by benio on 2015/10/18.
 */
public class ImageFile {
    /** 图片绝对路径 */
    private String path;
    /** 图片选中状态 */
    private boolean isSelected;

    public ImageFile(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "{" +
                "path='" + path + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
