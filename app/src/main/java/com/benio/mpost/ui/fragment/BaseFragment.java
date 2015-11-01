package com.benio.mpost.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benio.mpost.controller.FragmentHelper;
import com.benio.mpost.interf.ProgressController;
import com.benio.mpost.util.AKToast;

/**
 * Fragment基类
 * Created by benio on 2015/10/10.
 */
public abstract class BaseFragment extends Fragment implements ProgressController {

    private FragmentHelper mFragmentHelper;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getFragmentHelper().onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentHelper().onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //实例化View
        View view = isCreatedContent() ? inflater.inflate(getContentResource(), container, false) : null;

        getFragmentHelper().onCreateView(view);

        //初始化
        initData();
        initView(view);

        return view;
    }

    /**
     * @return 是否实例化布局文件
     */
    public boolean isCreatedContent() {
        return getContentResource() != 0;
    }

    /**
     * 要显示的布局id
     *
     * @return 返回0则表示不显示布局
     */
    @LayoutRes
    public abstract int getContentResource();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getFragmentHelper().onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        getFragmentHelper().onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getFragmentHelper().onResume();
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getFragmentHelper().onPause();
        if (getUserVisibleHint()) {
            onUserInVisible();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getFragmentHelper().onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getFragmentHelper().onDestroyView();
        mFragmentHelper = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentHelper().onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getFragmentHelper().onDetach();
    }

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            onUserVisible();
        } else {
            onUserInVisible();
        }
    }

    /**
     * fragment不可见时调用（切换掉或者onPause）
     */
    public void onUserInVisible() {
    }

    /**
     * fragment可见时调用（切换回来或者onResume）
     */
    public void onUserVisible() {
    }

    public FragmentHelper getFragmentHelper() {
        if (null == mFragmentHelper) {
            mFragmentHelper = FragmentHelper.create(this);
        }
        return mFragmentHelper;
    }

    @Override
    public void hideProgress() {
        FragmentActivity activity = getActivity();
        if (activity instanceof ProgressController) {
            ((ProgressController) activity).hideProgress();
        }
    }

    @Override
    public ProgressDialog showProgress() {
        FragmentActivity activity = getActivity();
        return activity instanceof ProgressController ? ((ProgressController) activity).showProgress() : null;
    }

    @Override
    public ProgressDialog showProgress(@StringRes int resId) {
        FragmentActivity activity = getActivity();
        return activity instanceof ProgressController ? ((ProgressController) activity).showProgress(resId) : null;
    }

    @Override
    public ProgressDialog showProgress(CharSequence text) {
        FragmentActivity activity = getActivity();
        return activity instanceof ProgressController ? ((ProgressController) activity).showProgress(text) : null;
    }

    public void showToast(CharSequence msg) {
        AKToast.show(getActivity(), msg);
    }

    public void showToast(@StringRes int resId) {
        AKToast.show(getActivity(), resId);
    }

    /**
     * 初始化View,在initData()后调用
     *
     * @param view {@link BaseFragment#getContentResource()}实例化出来的view
     */
    protected abstract void initView(View view);

    /**
     * 初始化数据
     */
    protected void initData() {
    }
}
