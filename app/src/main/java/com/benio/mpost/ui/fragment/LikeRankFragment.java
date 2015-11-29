package com.benio.mpost.ui.fragment;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.TimeLineAdapter;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.interf.impl.ResponseListener;
import com.benio.mpost.util.AKTime;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;

import java.util.List;

import butterknife.Bind;

/**
 * 点赞榜
 * Created by benio on 2015/11/8.
 */
public class LikeRankFragment extends RecyclerFragment implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    TimeLineAdapter mAdapter;

    @Override
    public int getContentResource() {
        return R.layout.fragment_like_rank;
    }

    @Override
    public BaseRecyclerAdapter onCreateAdapter() {
        return null;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
        mTabLayout.addTab(mTabLayout.newTab().setText("一周"));
        mTabLayout.addTab(mTabLayout.newTab().setText("今天"));
        mTabLayout.setOnTabSelectedListener(this);
        //默认选中全部
        onTabSelected(mTabLayout.getTabAt(0));
    }

    QueryListener<MPost> mListener = new QueryListener<MPost>() {
        @Override
        public void onFailure(int code, String msg) {
            hideProgress();
            ErrorLog.log(code, msg);
            showToast(R.string.info_access_error);
        }

        @Override
        public void onSuccess(List<MPost> list) {
            hideProgress();
            if (null == mAdapter) {
                mAdapter = new TimeLineAdapter(getActivity(), list);
                mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UIHelper.showPostDetail(getActivity(), mAdapter.getItem(position));
                    }
                });
                mAdapter.setOnAuthorClickListener(new TimeLineAdapter.OnAuthorClickListener() {
                    @Override
                    public void onAuthorClick(MUser author) {
                        UIHelper.showUserDetail(getActivity(), author);
                    }
                });
                mAdapter.setOnFavorPostListener(new TimeLineAdapter.OnFavorPostListener() {
                    @Override
                    public void onFavorPost(View view, MPost post) {
                        favorPost(view, post);
                    }
                });
                mAdapter.setOnLikePostListener(new TimeLineAdapter.OnLikePostListener() {
                    @Override
                    public void onLikePost(final View view, final MPost post) {
                        likePost(view, post);
                    }
                });
                setAdapter(mAdapter);
            } else {
                mAdapter.setData(list);
                if (list.isEmpty()) {
                    showToast("没有数据~~");
                }
            }
        }
    };

    /**
     * 获取某个时间段的点赞列表
     *
     * @param from
     * @param to
     */
    void getLikeRankList(long from, long to) {
        showProgress();
        MPostApi.getLikeRankList(mListener, from, to);
    }

    /**
     * 获取全部的点赞列表
     */
    void getLikeRandList() {
        showProgress();
        MPostApi.getLikeRankList(mListener);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        long from = 0, to = 0;
        switch (pos) {
            case 0://全部
                getLikeRandList();
                break;

            case 1://一周
                to = System.currentTimeMillis();
                from = AKTime.getMillisDaysBefore(to, 7);
                getLikeRankList(from, to);
                break;

            case 2://今天
                to = System.currentTimeMillis();
                from = AKTime.getMillisDaysBefore(to, 1);
                getLikeRankList(from, to);
                break;

            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    void likePost(final View view, final MPost post) {
        final MUser user = AppContext.getInstance().getUser();
        //先检查当前用户是否已经赞贴
        MPostApi.isLikedPost(user, post, new QueryListener<MPost>() {
            @Override
            public void onSuccess(List<MPost> result) {
                post.setLiked(Utils.isPostInList(post, result));
                if (post.isLiked()) {
                    //已点赞，但是view的点赞状态没更新
                    if (!view.isSelected()) {
                        view.setSelected(true);
                        showToast("已点赞");
                        return;
                    }
                }
                //未点赞，则更新点赞数据
                final boolean isLiked = !post.isLiked();
                MPostApi.likePost(user, post, isLiked, new ResponseListener() {
                    @Override
                    public void onFailure(int code, String msg) {
                        ErrorLog.log(code, msg);
                        showToast(R.string.info_like_failed);
                    }

                    @Override
                    public void onSuccess() {
                        post.setLiked(isLiked);
                        post.increaseLike(isLiked ? +1 : -1);//赞则赞数+1，否则-1
                        view.setSelected(isLiked);
                        ((TextView) view).setText(String.valueOf(post.getLikeCount()));
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                ErrorLog.log(i, s);
                showToast(R.string.info_like_failed);
            }
        });
    }

    void favorPost(final View view, final MPost post) {
        final MUser user = AppContext.getInstance().getUser();
        //先检查当前用户是否已经收藏
        MPostApi.isFavoredPost(user, post, new QueryListener<MPost>() {
            @Override
            public void onSuccess(List<MPost> result) {
                post.setFavored(Utils.isPostInList(post, result));
                if (post.isFavored()) {
                    //已收藏，但是view的收藏状态没更新
                    if (!view.isSelected()) {
                        view.setSelected(true);
                        showToast("已收藏");
                        return;
                    }
                }
                //未收藏，则更新收藏数据
                final boolean isFavored = !post.isFavored();
                MPostApi.favorPost(user, post, isFavored, new ResponseListener() {
                    @Override
                    public void onFailure(int code, String msg) {
                        showToast(R.string.info_favor_failed);
                        ErrorLog.log(code, msg);
                    }

                    @Override
                    public void onSuccess() {
                        post.setFavored(isFavored);
                        post.increaseFavor(isFavored ? +1 : -1);//收藏则收藏数+1，否则-1
                        view.setSelected(isFavored);
                        ((TextView) view).setText(String.valueOf(post.getFavorCount()));
                    }
                });
            }

            @Override
            public void onFailure(int i, String s) {
                ErrorLog.log(i, s);
                showToast(R.string.info_like_failed);
            }
        });
    }
}
