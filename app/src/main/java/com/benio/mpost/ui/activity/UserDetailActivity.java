package com.benio.mpost.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.TimeLineAdapter;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.UserDetail;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.interf.impl.ResponseListener;
import com.benio.mpost.util.AKLog;
import com.benio.mpost.util.AKToast;
import com.benio.mpost.util.AKView;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;

import java.util.List;

import butterknife.Bind;

/**
 * Created by benio on 2015/10/31.
 */
public class UserDetailActivity extends BaseActivity {

    private final static String BUNDLE_KEY_USER = "BUNDLE_KEY_USER";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.fab)
    FloatingActionButton mFloatingActionButton;

    private UserDetail mUserDetail;

    public static Intent newIntent(Context context, MUser user) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(BUNDLE_KEY_USER, user);
        return intent;
    }

    @Override
    public int getContentResource() {
        return R.layout.activity_user_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        final MUser user = (MUser) getIntent().getSerializableExtra(BUNDLE_KEY_USER);
        if (null == user) {
            throw new IllegalArgumentException("you must provide a user to display here");
        }

        showProgress();

        mUserDetail = new UserDetail();
        mUserDetail.setUser(user);

        if (user.getCanNotPost() != null && user.getCanNotPost()) {
            hideProgress();
            removeAllViews();
            AKToast.show(getBaseContext(), "你被禁止状态，无法查看内容，解封请联系管理员");
        } else {
            MPostApi.getUserPostList(user, new QueryListener<MPost>() {
                @Override
                public void onFailure(int code, String msg) {
                    ErrorLog.log(code, msg);
                    AKToast.show(UserDetailActivity.this, R.string.info_access_error);
                    mUserDetail.setPostList(null);
                    checkReady();
                }

                @Override
                public void onSuccess(List<MPost> list) {
                    if (!Utils.checkListEmpty(list)) {
                        mUserDetail.setPostList(list);
                        checkReady();
                    } else {
                        AKToast.show(UserDetailActivity.this, "还没有发过说说哦～");
                    }
                }
            });


            final MUser me = AppContext.getInstance().getUser();
            MPostApi.isFollowingUser(user, me, new QueryListener<MUser>() {
                @Override
                public void onFailure(int code, String msg) {
                    ErrorLog.log(code, msg);
                    AKToast.show(UserDetailActivity.this, R.string.info_access_error);
                    mUserDetail.setFollowing(false);
                    checkReady();
                }

                @Override
                public void onSuccess(List<MUser> list) {
                    AKLog.d("xxxxx", "result list: " + list.toString());
                    mUserDetail.setFollowing(isUserInList(user, list));
                    checkReady();
                }
            });
        }
    }

    private void removeAllViews() {
        mRecyclerView.setVisibility(View.GONE);
        mFloatingActionButton.setVisibility(View.GONE);
    }

    /**
     * 检查是否已经加载完毕
     */
    private void checkReady() {
        if (mUserDetail.isReady()) {
            hideProgress();

            final BaseRecyclerAdapter<MPost> adapter = new TimeLineAdapter(UserDetailActivity.this, mUserDetail.getPostList());
            adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UIHelper.showPostDetail(UserDetailActivity.this, adapter.getItem(position));
                }
            });
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(UserDetailActivity.this));

            setupFollow();
        }
    }

    /**
     * 检查user是否存在list中
     *
     * @param user
     * @param list
     * @return
     */
    private boolean isUserInList(MUser user, List<MUser> list) {
        if (list != null) {
            for (MUser u : list) {
                if (u.equals(user)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void initView(View view) {
        AKView.setToolbar(this, R.id.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(mUserDetail.getUser().getName());
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFollowUserEvent();
            }
        });
    }

    /**
     * 关注/取消关注
     */
    void onFollowUserEvent() {
        MUser me = AppContext.getInstance().getUser();
        if (mUserDetail.getUser().equals(me)) {
            AKToast.show(this, "不能收藏自己");
            return;
        }
        final boolean follow = !mUserDetail.isFollowing();
        MPostApi.followUser(mUserDetail.getUser(), me, follow, new ResponseListener() {
            @Override
            public void onFailure(int code, String msg) {
                ErrorLog.log(code, msg);
                AKToast.show(UserDetailActivity.this, "关注失败");
            }

            @Override
            public void onSuccess() {
                AKLog.d("关注成功");
                mUserDetail.setFollowing(follow);
                setupFollow();
            }
        });
    }

    private void setupFollow() {
        mFloatingActionButton.setImageResource(mUserDetail.isFollowing() ? R.mipmap.btn_like_selected : R.mipmap.btn_like_unselected);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
