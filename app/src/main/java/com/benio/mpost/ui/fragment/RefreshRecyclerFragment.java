package com.benio.mpost.ui.fragment;

import android.view.View;

import com.benio.mpost.R;
import com.benio.mpost.widget.SwipeRefreshLayout;

/**
 * 可刷新的RecyclerView Fragment
 * Created by benio on 2015/10/14.
 */
public abstract class RefreshRecyclerFragment extends RecyclerFragment implements SwipeRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnLoadListener {

    private SwipeRefreshLayout mRefreshLayout;

    @Override
    public int getContentResource() {
        return R.layout.fragment_refresh;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setBottomColor(R.color.blue, R.color.green, R.color.red, R.color.black);
        mRefreshLayout.setTopColor(R.color.blue, R.color.green, R.color.red, R.color.black);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadListener(this);
    }

    /**
     * 设置上啦刷新状态
     *
     * @param loading
     */
    public void setLoadingState(boolean loading) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setLoading(loading);
            // 防止多次重复刷新
            mRefreshLayout.setEnabled(!loading);
        }
    }

    /**
     * 设置下拉刷新状态
     *
     * @param refreshing
     */
    public void setRefreshingState(boolean refreshing) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(refreshing);
            // 防止多次重复刷新
            mRefreshLayout.setEnabled(!refreshing);
        }
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return mRefreshLayout;
    }
//    @Override
//    public void onLoad() {
//        setLoadingState(true);
//    }
//
//    @Override
//    public void onRefresh() {
//        setRefreshingState(true);
//    }
}
