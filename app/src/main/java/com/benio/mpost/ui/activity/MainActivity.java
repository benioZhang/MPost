package com.benio.mpost.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.benio.mpost.R;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.event.UpdateMainActivityEvent;
import com.benio.mpost.event.UserAvatarUploadEvent;
import com.benio.mpost.interf.impl.ResponseListener;
import com.benio.mpost.network.ImageLoader;
import com.benio.mpost.ui.fragment.CommentListFragment;
import com.benio.mpost.ui.fragment.FavoriteListFragment;
import com.benio.mpost.ui.fragment.FollowingUserFragment;
import com.benio.mpost.ui.fragment.LikeListFragment;
import com.benio.mpost.ui.fragment.TimeLineFragment;
import com.benio.mpost.util.AKLog;
import com.benio.mpost.util.AKToast;
import com.benio.mpost.util.AKView;
import com.benio.mpost.util.Utils;
import com.benio.mpost.util.image.CircleTransform;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_PUBLISH_POST = 1;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;
    @Bind(R.id.tv_name)
    TextView mNameTextView;
    @Bind(R.id.iv_user)
    ImageView mUserImageView;

    WeakReference<Fragment> mFragment;

    Uri mUri;
    public static final int ACTIVITY_RESULT_ALBUM_CODE = 2;
    public static final int ACTIVITY_RESULT_CAMERA_CODE = 3;

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

            case R.id.action_ranking_list:
                UIHelper.showLikeRankingList(this);
                return true;
            //搜索
            case R.id.action_search:
                UIHelper.showSearch(this);
                return true;

            //发帖
            case R.id.action_publish_post:
                UIHelper.showPublishPostForResult(this, REQUEST_PUBLISH_POST);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        Fragment fragment = null;
        switch (id) {
            case R.id.action_home:
                fragment = new TimeLineFragment();
                break;

            case R.id.action_comment:
                fragment = new CommentListFragment();
                break;

            case R.id.action_follow:
                fragment = new FollowingUserFragment();
                break;

            case R.id.action_star:
                fragment = new FavoriteListFragment();
                break;

            case R.id.action_like:
                fragment = new LikeListFragment();
                break;

            case R.id.action_logout:
                AppContext.getInstance().logout();
                UIHelper.showLogin(this);
                finish();
                break;

            default:
                break;
        }
        if (fragment != null) {
            mFragment = new WeakReference<Fragment>(fragment);
            replaceFragment(fragment);
        }
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }

    /**
     * 点击名字进入个人详情
     */
    @OnClick(R.id.tv_name)
    void onUserNameClickEvent() {
        mDrawerLayout.closeDrawers();
        MUser user = AppContext.getInstance().getUser();
        UIHelper.showUserDetail(this, user);
    }

    @OnClick(R.id.iv_user)
    void onUserLogoClickEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更换头像")
                .setItems(R.array.camera_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            camera();
                        } else {
                            photo();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void camera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mUri = Utils.getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, MainActivity.ACTIVITY_RESULT_CAMERA_CODE);
    }

    private void photo() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, MainActivity.ACTIVITY_RESULT_ALBUM_CODE);
    }


    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView(View view) {
        AKView.setToolbar(this, R.id.toolbar);
        AKView.setHomeIndicator(this, R.mipmap.ic_menu);

        mNavigationView.setNavigationItemSelectedListener(this);
        //选中首页
        MenuItem item = mNavigationView.getMenu().findItem(R.id.action_home);
        onNavigationItemSelected(item);
        setupUser();

        EventBus.getDefault().register(this);

//        mFragment = new WeakReference<Fragment>(new TimeLineFragment());
//        replaceFragment(mFragment.get());
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
            ImageLoader.getInstance(this).load(mUserImageView, user.getPortraitUrl(), R.mipmap.user_default_header, new CircleTransform(getApplicationContext()));
        } else {
            ImageLoader.getInstance(this).load(mUserImageView, R.mipmap.user_default_header, new CircleTransform(getApplicationContext()));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (resultCode != RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (requestCode) {
            case ACTIVITY_RESULT_CAMERA_CODE:
                AppContext.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mUri != null) {
//                            AKToast.show(MainActivity.this, mUri.getPath());
                            String filePath = Utils.getCompressImage(mUri.getPath());
                            EventBus.getDefault().post(new UserAvatarUploadEvent(filePath));
                        }
                    }
                });
                break;
            case ACTIVITY_RESULT_ALBUM_CODE:
                if (data != null) {
                    AppContext.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            Uri fileUri = data.getData();
                            String mFile = Utils.getRealPathFromURI(MainActivity.this, fileUri);
//                            AKToast.show(MainActivity.this, fileUri.getPath());
                            String filePath = Utils.getCompressImage(mFile);
                            EventBus.getDefault().post(new UserAvatarUploadEvent(filePath));
                        }
                    });
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onEventMainThread(final UserAvatarUploadEvent event) {
        if (event != null && !TextUtils.isEmpty(event.path)) {
            MPostApi.uploadAvatar(AppContext.getInstance().getUser(), event.path, new ResponseListener() {
                @Override
                public void onFailure(int code, String msg) {
                    AKLog.d("失败 " + "code " + code + "  msg  " + msg);
                    AKToast.show(MainActivity.this, "上传失败");
                }

                @Override
                public void onSuccess() {
                    ImageLoader.getInstance(MainActivity.this).load(mUserImageView, event.path, new CircleTransform(getApplicationContext()));
                    AKToast.show(MainActivity.this, "上传成功");
                }
            });
        }
    }

    public void onEventMainThread(final UpdateMainActivityEvent event) {
        if (event != null) {
            if (mFragment != null) {
                if (mFragment.get() instanceof TimeLineFragment) {
                    ((TimeLineFragment) mFragment.get()).onRefresh();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
