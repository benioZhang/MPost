package com.benio.mpost.ui.fragment;

import android.os.Handler;
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
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;
import com.benio.mpost.widget.SwipeRefreshLayout;

import java.util.List;

/**
 * 时间线
 * Created by benio on 2015/10/12.
 */
public class TimeLineFragment extends RefreshRecyclerFragment {

    private TimeLineAdapter mAdapter;
    protected int mPage;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getPostList("");
        }
    };

    @Override
    public BaseRecyclerAdapter onCreateAdapter() {
        return null;
    }

    @Override
    public void onLoad() {
        setLoadingState(true);
        mPage++;
        mHandler.post(mRunnable);
    }

    @Override
    public void onRefresh() {
        setRefreshingState(true);
        mPage = 0;
        mHandler.post(mRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void initData() {
        super.initData();
        getPostList("");
    }

    void getPostList(String filter) {
        MPostApi.getPostList(new QueryListener<MPost>() {
            @Override
            public void onFailure(int code, String msg) {
                ErrorLog.log(code, msg);
                showToast(R.string.info_access_error);
                setRefreshingState(false);
                setLoadingState(false);
            }

            @Override
            public void onSuccess(List<MPost> list) {
                if (!Utils.checkListEmpty(list)) {
                    SwipeRefreshLayout refreshLayout = getRefreshLayout();
                    if (refreshLayout.isLoading()) {
                        setLoadingState(false);
                        getTimeLineAdapter().addAll(list);
                    } else {
                        setRefreshingState(false);
                        getTimeLineAdapter().setData(list);
                    }
                } else {
                    setRefreshingState(false);
                    setLoadingState(false);
                    showToast("没有数据~~");
                }
            }
        }, mPage);
    }


    TimeLineAdapter getTimeLineAdapter() {
        if (null == mAdapter) {
            mAdapter = new TimeLineAdapter(getActivity());
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
        }
        return mAdapter;
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
                        getTimeLineAdapter().notifyDataSetChanged();
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
                        getTimeLineAdapter().notifyDataSetChanged();
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
