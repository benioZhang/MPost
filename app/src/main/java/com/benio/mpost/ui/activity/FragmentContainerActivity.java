package com.benio.mpost.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;

import com.benio.mpost.R;
import com.benio.mpost.bean.container.Container;
import com.benio.mpost.util.AKLog;
import com.benio.mpost.util.AKView;

import java.lang.ref.WeakReference;

/**
 * Fragment容器
 * Created by benio on 2015/10/10.
 */
public class FragmentContainerActivity extends BaseActivity {
    private static final String TAG = FragmentContainerActivity.class.getSimpleName();
    /**
     * 传参key:负责传递containerId
     */
    private static final String BUNDLE_KEY_CONTAINER = "BUNDLE_KEY_CONTAINER";
    /**
     * 传参key:负责传递传入Fragment的参数
     */
    private static final String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";

    /** 页面fragment */
    protected WeakReference<Fragment> mFragment;
    /** 页面containerId */
    protected int mContainerId;

    /**
     * 构造启动FragmentContainerActivity的intent方法
     *
     * @param context     上下文
     * @param containerId 页面id
     * @param args        传入的参数
     * @return 启动FragmentContainerActivity的intent
     */
    public static Intent newIntent(Context context, @Container.ContainerType int containerId, Bundle args) {
        Intent intent = new Intent(context, FragmentContainerActivity.class);
        intent.putExtra(BUNDLE_KEY_CONTAINER, containerId);
        if (null != args) {
            intent.putExtra(BUNDLE_KEY_ARGS, args);
        }
        return intent;
    }

    @Override
    public int getContentResource() {
        return R.layout.activity_fragment_container;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        mContainerId = getIntent().getIntExtra(BUNDLE_KEY_CONTAINER, mContainerId);
    }

    @Override
    protected void initView(View view) {
        AKView.setToolbar(this, R.id.toolbar);

//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }

        Container container = getContainer(mContainerId);
        if (null == container) {
            AKLog.e(TAG, "No container found here , please provide a right id");
            return;
        }

        if (container.hasTitle()) {
            setTitle(container.getTitle());
        }

        Bundle args = getIntent().getBundleExtra(BUNDLE_KEY_ARGS);
        Fragment fragment = createFragment(container, args);
        replaceFragment(fragment);
    }

    /**
     * 获取Container
     *
     * @param containerId container对应的id
     * @return null or specific container
     */
    public Container getContainer(int containerId) {
        return Container.getContainerById(containerId);
    }

    /**
     * 根据Container实例化对应的Fragment
     *
     * @param container Fragment对应的的container
     * @param args      传入Fragment的参数
     * @return null or specific fragment
     */
    @Nullable
    protected final Fragment createFragment(Container container, @Nullable Bundle args) {
        if (null == container || null == container.getClazz()) {
            return null;
        }

        try {
            Fragment fragment = container.getClazz().newInstance();
            if (args != null) {
                fragment.setArguments(args);
            }
            return fragment;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Generate fragment error");
        }
    }

    /**
     * 替换Fragment
     *
     * @param fragment 替换到容器的fragment
     */
    protected final void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, false);
    }

    /**
     * 替换Fragment，并且设置fragment是否加到back栈中
     *
     * @param addToBackStack 是否添加到back栈
     * @param fragment       替换到容器的fragment
     */
    protected final void replaceFragment(Fragment fragment, boolean addToBackStack) {
        if (null == fragment) {
            return;
        }

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.fl_container, fragment, TAG);
        if (addToBackStack) {
            trans.addToBackStack(null);
        }
        trans.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragment = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AKLog.d(TAG, " requestCode: " + requestCode + " resultCode: " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
