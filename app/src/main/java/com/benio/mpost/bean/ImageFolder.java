package com.benio.mpost.bean;


import com.benio.mpost.interf.ImageFormat;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 图片文件夹
 */
public class ImageFolder implements ImageFormat {
    /**
     * 对应的文件
     */
    private File file;
    /**
     * 第一张图片的路径
     */
    private String firstImagePath;
    /**
     * 文件夹的名称
     */
    private String name;
    /**
     * 图片的数量
     */
    private int count;
    /**
     * 是否被选中
     */
    private boolean isSelected;
    /**
     * 默认过滤器IMAGE_FILENAME_FILTER下的的图片文件
     */
    private List<ImageFile> imageFileList;

    public ImageFolder(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getFirstImagePath() {
        return null == firstImagePath ? (null == imageFileList ? null : imageFileList.get(0).getPath()) : firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name == null ? (file == null ? null : file.getName()) : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean add(ImageFile imageFile) {
        if (null == imageFile) {
            return false;
        }
        if (null == imageFileList) {
            imageFileList = new ArrayList<>();
        }
        return imageFileList.add(imageFile);
    }

    public boolean remove(ImageFile imageFile) {
        return null != imageFile && imageFileList.remove(imageFile);
    }

    public boolean contains(ImageFile imageFile) {
        return imageFileList != null && imageFileList.contains(imageFile);
    }

    public List<ImageFile> listFiles() {
        if (null == imageFileList) {
            imageFileList = listFiles(IMAGE_FILENAME_FILTER);
        }
        return imageFileList;
    }

    public List<ImageFile> listFiles(FilenameFilter filter) {
        return filePathsToFiles(list(filter));
    }

    /**
     * 过滤出图片文件的绝对路径列表
     *
     * @return
     */
    public List<String> list() {
        return list(IMAGE_FILENAME_FILTER);
    }

    /**
     * 过滤出符合filter的文件的绝对路径列表
     *
     * @param filter
     * @return
     */
    public List<String> list(FilenameFilter filter) {
        List<String> result = listFilesAbsolutePath(filter);
        //放在这里是因为不想再算一次，直接从这里获取数量就好了
        if (result != null) {
            setCount(result.size());
        }
        return result;
    }

    /**
     * 过滤出file文件下的符合filter的文件的绝对路径列表
     *
     * @param filter the filter to match names against, may be {@code null}.
     * @return an array of files or {@code null}.
     */
    protected List<String> listFilesAbsolutePath(FilenameFilter filter) {
        if (file == null || !file.isDirectory()) {
            return null;
        }

        String[] filenames = file.list();
        if (filter == null || filenames == null) {
            return null == filenames ? null : Arrays.asList(filenames);
        }

        List<String> result = new ArrayList<String>(filenames.length);
        //以倒序的形式存放到list中
        //不然的话第一张图片和显示的第一张不一致
        for (int i = filenames.length - 1; i >= 0; i--) {
            if (filter.accept(file, filenames[i])) {
                result.add(file.getAbsolutePath() + "/" + filenames[i]);
            }
        }
        return result;
    }

    /**
     * 路径对应的图片转成ImageFile
     *
     * @param filePaths
     * @return
     */
    protected List<ImageFile> filePathsToFiles(List<String> filePaths) {
        if (null == filePaths) {
            return null;
        }
        List<ImageFile> result = new ArrayList<>(filePaths.size());
        for (String p : filePaths) {
            ImageFile image = new ImageFile(p);
            result.add(image);
        }
        return result;
    }

    protected List<ImageFile> getImageFileList() {
        return imageFileList;
    }

    public void setImageFileList(List<ImageFile> imageFileList) {
        this.imageFileList = imageFileList;
    }

    @Override
    public String toString() {
        return "{" +
                "file=" + file +
                ", firstImagePath='" + firstImagePath + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", isSelected=" + isSelected +
                '}';
    }
}
