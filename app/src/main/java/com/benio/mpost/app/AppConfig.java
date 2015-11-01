package com.benio.mpost.app;

import android.os.Environment;

/**
 * 配置类
 * Created by benio on 2015/10/18.
 */
public class AppConfig {
    /**
     * 默认存放图片的路径
     */
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory() + "/MPost/";
}
