package com.benio.mpost.app;

import android.app.Activity;

import com.benio.mpost.util.AKLog;

import java.util.Stack;

/**
 * activity堆栈式管理
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月30日 下午6:22:05
 */
public class AppManager {
    private final static String TAG = AppManager.class.getSimpleName();

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
            activityStack = new Stack<>();
        }
        return instance;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = currentActivity();
        finishActivity(activity);
    }

    /**
     * 退出指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            AKLog.i(TAG, "finish activity : " + activity.toString());
            //在从自定义集合中取出当前Activity时，也进行了Activity的finish操作
            activity.finish();
            activityStack.remove(activity);
        }
    }

    /**
     * 退出指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            finishActivity(currentActivity());
        }
        activityStack.clear();
    }
//
//    /**
//     * 退出栈中所有Activity
//     */
//    public void finishAllActivityExceptOne(Class cls) {
//        for (; ; ) {
//            Activity activity = currentActivity();
//            if (activity == null) {
//                break;
//            }
//            if (activity.getClass().equals(cls)) {
//                break;
//            }
//            finishActivity(activity);
//        }
//    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        return activityStack.empty() ? null : activityStack.lastElement();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
        AKLog.i(TAG, "add activity : " + activity.toString());
        logStack();
    }

    /**
     * 获取指定的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            // 杀死该应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    private void logStack() {
        AKLog.d(TAG, "==================stack begin=====================");
        for (int i = 0; i < activityStack.size(); i++) {
            AKLog.i(TAG, " activity" + i + " " + (activityStack.get(i) == null ? " activity is null" : activityStack.get(i).toString()));
        }
        AKLog.d(TAG, "==================stack end=======================");
    }
}