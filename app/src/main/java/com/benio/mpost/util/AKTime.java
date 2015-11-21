package com.benio.mpost.util;

/**
 * 时间工具类
 * Created by benio on 2015/11/17.
 */
public class AKTime {

    public static final int SEC = 1000;
    public static final int MIN = 60 * SEC;
    public static final int HOUR = 60 * MIN;
    public static final int DAY = 24 * HOUR;

    /**
     * 以timeStamp为基准
     * 获取days天前的时间戳
     *
     * @param timeStamp
     * @param days
     * @return
     */
    public static long getMillisDaysBefore(long timeStamp, int days) {
        return getMillisDay(timeStamp, days >= 0 ? -days : days);
    }

    /**
     * 以timeStamp为基准
     * 获取days天后的时间戳
     *
     * @param timeStamp
     * @param days
     * @return
     */
    public static long getMillisDaysAfter(long timeStamp, int days) {
        return getMillisDay(timeStamp, days >= 0 ? days : -days);
    }

    private static long getMillisDay(long timeStamp, int days) {
        return timeStamp < 0 ? 0 : timeStamp + DAY * days;
    }

    private AKTime() {
    }
}
