package com.benio.mpost.ui.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.CommentListAdapter;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.Comment;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;
import com.benio.mpost.widget.SwipeRefreshLayout;

import java.util.List;

/**
 * Created by shau-lok on 11/5/15.
 */
public class CommentListFragment extends RefreshRecyclerFragment {

    private CommentListAdapter mAdapter;

    protected int mPage;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getCommentList();
        }
    };

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
        getCommentList();
    }

    @Override
    public BaseRecyclerAdapter onCreateAdapter() {
        return null;
    }

    void getCommentList() {
        MPostApi.getMyCommentList(AppContext.getInstance().getUser(), new QueryListener<Comment>() {
            @Override
            public void onFailure(int code, String msg) {
                ErrorLog.log(code, msg);
                showToast(R.string.info_access_error);
                setRefreshingState(false);
                setLoadingState(false);
            }

            @Override
            public void onSuccess(List<Comment> list) {
                if (!Utils.checkListEmpty(list)) {
                    SwipeRefreshLayout refreshLayout = getRefreshLayout();
                    if (refreshLayout.isLoading()) {
                        setLoadingState(false);
                        getCommentListAdapter().addAll(list);
                    } else {
                        setRefreshingState(false);
                        getCommentListAdapter().setData(list);
                    }
                } else {
                    setRefreshingState(false);
                    setLoadingState(false);
                    showToast("没有数据~~");
                }
            }
        }, mPage);
    }

    CommentListAdapter getCommentListAdapter() {
        if (null == mAdapter) {
            mAdapter = new CommentListAdapter(getActivity());
            mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UIHelper.showPostDetail(getActivity(), mAdapter.getItem(position).getPost());
                }
            });
            setAdapter(mAdapter);
        }
        return mAdapter;
    }

}
