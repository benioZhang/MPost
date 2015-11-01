package com.benio.mpost.interf;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 图片格式
 * Created by benio on 2015/9/5.
 */
public interface ImageFormat {
    /**
     * 扫描的图片格式
     */
    String[] IMAGE_FORMAT = {"jpg", "jpeg", "png", "webp"};
    /**
     * 图片过滤
     */
    FilenameFilter IMAGE_FILENAME_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String filename) {
            for (String str : IMAGE_FORMAT) {
                if (filename.endsWith('.' + str.toUpperCase()) || filename.endsWith('.' + str.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
    };
}
