package com.benio.mpost.bean;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片根文件夹
 * Created by benio on 2015/9/4.
 */
public class ImageRootFolder extends ImageFolder {

    private List<ImageFolder> folderList;

    public ImageRootFolder(File file) {
        super(file);
    }

    public List<ImageFolder> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<ImageFolder> folderList) {
        this.folderList = folderList;
    }

    public boolean add(ImageFolder imageFolder) {
        if (null == imageFolder) {
            return false;
        }
        if (null == folderList) {
            folderList = new ArrayList<>();
        }
        return folderList.add(imageFolder);
    }

    public boolean remove(ImageFolder imageFolder) {
        return null != imageFolder && folderList.remove(imageFolder);
    }

    public boolean contains(ImageFolder imageFolder) {
        return folderList != null && folderList.contains(imageFolder);
    }

    @Override
    public List<ImageFile> listFiles() {
        List<ImageFile> imageFileList = getImageFileList();
        if (null == imageFileList) {
            imageFileList = new ArrayList<>();
            //遍历子文件夹
            if (folderList != null) {
                for (ImageFolder folder : folderList) {
                    if (folder != null) {
                        List<ImageFile> child = folder.listFiles();
                        if (child != null && !child.isEmpty()) {
                            imageFileList.addAll(child);
                        }
                    }
                }
                setCount(imageFileList.size());
            }
        }
        return imageFileList;
    }

    @Override
    public List<String> list(FilenameFilter filter) {
        File file = getFile();
        if (file == null || !file.isDirectory()) {
            return null;
        }

        List<String> result = null;

        //遍历子文件夹
        if (folderList != null) {
            result = new ArrayList<>();
            for (ImageFolder folder : folderList) {
                if (folder != null) {
                    List<String> child = folder.list(filter);
                    if (child != null && !child.isEmpty()) {
                        result.addAll(child);
                    }
                }
            }
            setCount(result.size());
        }
        return result;
    }
}
