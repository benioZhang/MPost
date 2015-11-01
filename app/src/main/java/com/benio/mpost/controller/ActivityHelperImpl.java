package com.benio.mpost.controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.benio.mpost.app.AppManager;
import com.benio.mpost.util.AKLog;

import butterknife.ButterKnife;

/**
 * ActivityHelper实现类
 * Created by benio on 2015/10/10.
 */
class ActivityHelperImpl extends ActivityHelper {
    private Activity mActivity;

    ActivityHelperImpl(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AKLog.d(TAG, mActivity.getClass().getSimpleName() + "=====OnCreate======");
        ButterKnife.bind(mActivity);
        AppManager.getInstance().addActivity(mActivity);
    }

    @Override
    public void onStart() {
        AKLog.d(TAG, mActivity.getClass().getSimpleName() + "=====onStart======");
    }

    @Override
    public void onResume() {
        AKLog.d(TAG, mActivity.getClass().getSimpleName() + "=====onResume======");
    }

    @Override
    public void onPause() {
        AKLog.d(TAG, mActivity.getClass().getSimpleName() + "=====onPause======");
    }

    @Override
    public void onStop() {
        AKLog.d(TAG, mActivity.getClass().getSimpleName() + "=====onStop======");
    }

    @Override
    public void onDestroy() {
        AKLog.d(TAG, mActivity.getClass().getSimpleName() + "=====onDestroy======");
        ButterKnife.unbind(mActivity);
        //取消未显示完的toast
//        AKToast.cancel();
    }

    @Override
    public void onRestart() {
        AKLog.d(TAG, mActivity.getClass().getSimpleName() + "=====onRestart======");
    }

    @Override
    public void onBackPressed() {
        AKLog.d(TAG, mActivity.getClass().getSimpleName() + "=====onBackPressed======");
        AppManager.getInstance().finishActivity(mActivity);
    }
}
