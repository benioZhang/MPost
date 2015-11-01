package com.benio.mpost.util;

import java.util.regex.Pattern;

/**
 * 校验工具类
 * Created by benio on 2015/10/11.
 */
public class AkValidate {

    /**
     * 是否为手机
     *
     * @param s
     * @return
     */
    public static boolean isPhone(String s) {
        return s != null && Pattern.matches("^1[3|4|5|7|8][0-9]\\d{8}$", s);
    }


    private AkValidate() {
    }
}
