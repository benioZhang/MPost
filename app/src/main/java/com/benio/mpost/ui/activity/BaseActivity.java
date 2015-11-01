package com.benio.mpost.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.benio.mpost.R;
import com.benio.mpost.controller.ActivityHelper;
import com.benio.mpost.interf.ProgressController;

/**
 * Activity基类
 * Created by benio on 2015/10/10.
 */
public abstract class BaseActivity extends AppCompatActivity implements ProgressController {
    /** activity life cycle helper */
    private ActivityHelper mActivityHelper;
    /** progress dialog */
    private ProgressDialog mProgressDialog;
    /** contentView */
    private View mContentView;

    /**
     * 要显示的布局id
     *
     * @return 返回0则表示不显示布局
     */
    @LayoutRes
    public abstract int getContentResource();

    /**
     * @return 是否实例化布局文件
     */
    public boolean isCreatedContent() {
        return getContentResource() != 0;
    }

    public ActivityHelper getActivityHelper() {
        if (null == mActivityHelper) {
            mActivityHelper = ActivityHelper.create(this);
        }
        return mActivityHelper;
    }

    public ProgressDialog getProgressDialog() {
        if (null == mProgressDialog) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(true);
        }
        return mProgressDialog;
    }

    /** 获取根View */
    public View getContentView() {
        return mContentView;
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public ProgressDialog showProgress() {
        return showProgress(R.string.loading);
    }

    @Override
    public ProgressDialog showProgress(@StringRes int resId) {
        return showProgress(getString(resId));
    }

    @Override
    public ProgressDialog showProgress(CharSequence msg) {
        ProgressDialog progressDialog = getProgressDialog();
        progressDialog.setMessage(msg);
        progressDialog.show();
        return progressDialog;
    }

    @Override
    public void onBackPressed() {
        //这里必须放在super.onBackPressed()方法之前
        getActivityHelper().onBackPressed();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onBeforeSetContent();

        mContentView = onCreateContentView();
        if (mContentView != null) {
            setContentView(mContentView);
        }

        getActivityHelper().onCreate(savedInstanceState);

        //初始化
        initData();
        initView(mContentView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getActivityHelper().onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getActivityHelper().onResume();
    }

    /**
     * 创建contentView
     */
    protected View onCreateContentView() {
        return isCreatedContent() ? View.inflate(this, getContentResource(), null) : null;
    }

    /**
     * 在{@link BaseActivity#setContentView(View)}前调用
     */
    protected void onBeforeSetContent() {
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化View,在initData()后调用
     *
     * @param view {@link BaseActivity#getContentResource()}实例化出来的view
     */
    protected abstract void initView(@Nullable View view);

    @Override
    protected void onPause() {
        super.onPause();
        getActivityHelper().onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getActivityHelper().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getActivityHelper().onDestroy();
        hideProgress();
        mProgressDialog = null;
        mActivityHelper = null;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getActivityHelper().onRestart();
    }
}
