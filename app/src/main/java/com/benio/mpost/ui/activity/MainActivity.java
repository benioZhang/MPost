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
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.network.ImageLoader;
import com.benio.mpost.ui.fragment.CommentListFragment;
import com.benio.mpost.ui.fragment.FavoriteListFragment;
import com.benio.mpost.ui.fragment.TimeLineFragment;
import com.benio.mpost.util.AKToast;
import com.benio.mpost.util.AKView;

import java.lang.ref.WeakReference;

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
                mFragment = new WeakReference<Fragment>(new TimeLineFragment());
                replaceFragment(mFragment.get());
                break;

            case R.id.action_comment:
                mFragment = new WeakReference<Fragment>(new CommentListFragment());
                replaceFragment(mFragment.get());
                break;

            case R.id.action_like:
                AKToast.show(MainActivity.this, "工程师在研发中");
                break;

            case R.id.action_star:
                mFragment = new WeakReference<Fragment>(new FavoriteListFragment());
                replaceFragment(mFragment.get());
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
            ImageLoader.getInstance(this).load(mUserImageView, user.getPortraitUrl(),R.mipmap.ic_default_image);
        } else {
            ImageLoader.getInstance(this).load(mUserImageView, R.mipmap.user_default_header);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mFragment!=null){
            if(mFragment.get() instanceof TimeLineFragment ){
                ((TimeLineFragment) mFragment.get()).onRefresh();
            }
        }
    }
}
