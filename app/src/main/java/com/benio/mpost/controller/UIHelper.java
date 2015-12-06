package com.benio.mpost.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.container.Container;
import com.benio.mpost.ui.activity.FragmentContainerActivity;
import com.benio.mpost.ui.activity.MainActivity;
import com.benio.mpost.ui.activity.UserDetailActivity;
import com.benio.mpost.ui.fragment.AlbumFragment;
import com.benio.mpost.ui.fragment.PostDetailFragment;

/**
 * 控制显示页面
 * Created by benio on 2015/10/10.
 */
public class UIHelper {


    public static void showLikeRankingList(Activity activity) {
        showFragmentContainer(activity, Container.RANKING_LIST);
    }

    /**
     * 显示用户详情
     */
    public static void showUserDetail(Activity activity, MUser user) {
        Intent intent = UserDetailActivity.newIntent(activity, user);
        activity.startActivity(intent);
    }

    /**
     * 显示帖子详情
     *
     * @param activity
     * @param post
     */
    public static void showPostDetail(Activity activity, MPost post) {
        Bundle args = new Bundle();
        args.putSerializable(PostDetailFragment.BUNDLE_KEY_POST, post);
        showFragmentContainer(activity, Container.POST_DETAIL, args);
    }

    /**
     * 显示帖子可见性选择
     *
     * @param fragment
     * @param requestCode
     */
    public static void showPostVisibleForResult(Fragment fragment, int requestCode) {
        showFragmentContainerForResult(fragment, requestCode, Container.POST_VISIBLE);
    }

    /**
     * 显示相册
     *
     * @param fragment
     */
    public static void showAlbumForResult(Fragment fragment, int requestCode) {
        showAlbumForResult(fragment, requestCode, 1);
    }

    /**
     * 显示相册
     *
     * @param fragment
     * @param maxCount 最大选择图片数量
     */
    public static void showAlbumForResult(Fragment fragment, int requestCode, int maxCount) {
        Bundle args = new Bundle();
        args.putInt(AlbumFragment.BUNDLE_KEY_MAX_COUNT, maxCount);
        showFragmentContainerForResult(fragment, requestCode, Container.ALBUM, args);
    }

    /**
     * 显示搜索页面
     *
     * @param activity
     */
    public static void showSearch(Activity activity) {
        showFragmentContainer(activity, Container.SEARCH);
    }

    /**
     * 发帖页
     *
     * @param activity
     */
    public static void showPublishPost(Activity activity) {
        showFragmentContainer(activity, Container.PUBLISH_POST);
    }

    public static void showPublishPostForResult(Activity activity, int requestCode) {
        showFragmentContainerForResult(activity, requestCode, Container.PUBLISH_POST);
    }

    /**
     * 显示主页
     *
     * @param activity
     */
    public static void showMain(Activity activity) {
        startActivity(activity, MainActivity.class);
    }

    /**
     * 显示注册页面
     *
     * @param activity
     */
    public static void showRegister(Activity activity) {
        showFragmentContainer(activity, Container.REGISTER);
    }

    /**
     * 显示登陆页面
     *
     * @param activity
     */
    public static void showLogin(Activity activity) {
        showFragmentContainer(activity, Container.LOGIN);
    }

    /**
     * 显示FragmentContainer页面
     *
     * @param fragment 启动的fragment
     * @param id       详情见{@link com.benio.mpost.bean.container.Container.ContainerType }
     */
    public static void showFragmentContainerForResult(Fragment fragment, int requestCode, @Container.ContainerType int id) {
        showFragmentContainerForResult(fragment, requestCode, id, null);
    }

    /**
     * 显示FragmentContainer页面
     *
     * @param fragment 启动的fragment
     * @param id       详情见{@link com.benio.mpost.bean.container.Container.ContainerType }
     * @param args     传入Fragment的参数
     */
    public static void showFragmentContainerForResult(Fragment fragment, int requestCode, @Container.ContainerType int id, Bundle args) {
        Intent intent = FragmentContainerActivity.newIntent(fragment.getActivity(), id, args);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 显示FragmentContainer页面
     *
     * @param activity 启动的activity
     * @param id       详情见{@link com.benio.mpost.bean.container.Container.ContainerType }
     */
    public static void showFragmentContainerForResult(Activity activity, int requestCode, @Container.ContainerType int id) {
        showFragmentContainerForResult(activity, requestCode, id, null);
    }

    /**
     * 显示FragmentContainer页面
     *
     * @param activity 启动的activity
     * @param id       详情见{@link com.benio.mpost.bean.container.Container.ContainerType }
     * @param args     传入Fragment的参数
     */
    public static void showFragmentContainerForResult(Activity activity, int requestCode, @Container.ContainerType int id, Bundle args) {
        Intent intent = FragmentContainerActivity.newIntent(activity, id, args);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 显示FragmentContainer页面
     *
     * @param activity 启动的activity
     * @param id       详情见{@link com.benio.mpost.bean.container.Container.ContainerType }
     */
    public static void showFragmentContainer(Activity activity, @Container.ContainerType int id) {
        showFragmentContainer(activity, id, null);
    }

    /**
     * 显示FragmentContainer页面
     *
     * @param activity 启动的activity
     * @param id       详情见{@link com.benio.mpost.bean.container.Container.ContainerType }
     * @param args     传入Fragment的参数
     */
    public static void showFragmentContainer(Activity activity, @Container.ContainerType int id, Bundle args) {
        Intent intent = FragmentContainerActivity.newIntent(activity, id, args);
        activity.startActivity(intent);
    }

    public static void startActivity(Activity activity, Class<?> clazz) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
    }

    private UIHelper() {
    }
}
