package com.benio.mpost.ui.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.TimeLineAdapter;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.util.AKLog;
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
    private int mPage;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getPostList();
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
        getPostList();
    }

    void getPostList() {
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
                AKLog.d("xxx", "timeLine :" + list.toString());
                if (!Utils.checkListEmpty(list)) {
                    SwipeRefreshLayout refreshLayout = getRefreshLayout();
                    if (refreshLayout.isLoading()) {
                        AKLog.d("xxx", "isLoading ");
                        setLoadingState(false);
                        getAdapter().addAll(list);
                    } else {
                        AKLog.d("xxx", "isRefreshing ");
                        setRefreshingState(false);
                        getAdapter().setData(list);
                    }
                } else {
                    setRefreshingState(false);
                    setLoadingState(false);
                    showToast("没有数据~~");
                }
            }
        }, mPage);
    }


    private TimeLineAdapter getAdapter() {
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
            setAdapter(mAdapter);
        }
        return mAdapter;
    }

}
