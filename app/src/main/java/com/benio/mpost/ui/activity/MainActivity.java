package com.benio.mpost.ui.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.benio.mpost.R;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.network.ImageLoader;
import com.benio.mpost.ui.fragment.TimeLineFragment;
import com.benio.mpost.util.AKView;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;
    @Bind(R.id.tv_name)
    TextView mNameTextView;
    @Bind(R.id.iv_user)
    ImageView mUserImageView;

    WeakReference<Fragment> mFragment;

    @Override
    public int getContentResource() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            //搜索
            case R.id.action_search:
                UIHelper.showSearch(this);
                return true;

            //发帖
            case R.id.action_publish_post:
                UIHelper.showPublishPostForResult(this, 0x001);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            //home
            case R.id.action_home:
                break;

            case R.id.action_comment:
                break;

            case R.id.action_like:
                break;

            case R.id.action_star:
                MPostApi.getMyFavouritePost(AppContext.getInstance().getUser(), new QueryListener<MPost>() {
                    @Override
                    public void onSuccess(List<MPost> list) {
                        // TODO: 11/4/15 处理显示个人收藏的post 
                    }

                    @Override
                    public void onFailure(int code, String msg) {

                    }
                });
                break;

            case R.id.action_logout:
                AppContext.getInstance().logout();
                UIHelper.showLogin(this);
                finish();
                break;

            default:
                break;
        }
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }

    /**
     * 点击头像进入个人详情
     */
    @OnClick(R.id.ll_header)
    void onUserClickEvent() {
        MUser user = AppContext.getInstance().getUser();
        UIHelper.showUserDetail(this, user);
    }

    @Override
    protected void initView(View view) {
        AKView.setToolbar(this, R.id.toolbar);
        AKView.setHomeIndicator(this, R.mipmap.ic_menu);

        mNavigationView.setNavigationItemSelectedListener(this);

        setupUser();
        mFragment = new WeakReference<Fragment>(new TimeLineFragment());
        replaceFragment(mFragment.get());
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
        trans.replace(R.id.fl_container, fragment);
        if (addToBackStack) {
            trans.addToBackStack(null);
        }
        trans.commit();
    }

    /**
     * 设置用户信息
     */
    private void setupUser() {
        MUser user = AppContext.getInstance().getUser();
        mNameTextView.setText(user.getName());
        if (user.hasPortrait()) {
            ImageLoader.getInstance(this).load(mUserImageView, user.getPortraitUrl());
        } else {
            ImageLoader.getInstance(this).load(mUserImageView, R.mipmap.ic_user_def);
        }
    }
}
