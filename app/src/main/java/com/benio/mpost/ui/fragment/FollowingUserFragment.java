package com.benio.mpost.ui.fragment;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.FollowingUserAdapter;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;
import com.benio.mpost.widget.SwipeRefreshLayout;

import java.util.List;

/**
 * Created by benio on 2015/12/6.
 */
public class FollowingUserFragment extends RefreshRecyclerFragment {
    private int mPage;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getFollowingUserList();
        }
    };
    private FollowingUserAdapter mAdapter;

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
    public BaseRecyclerAdapter onCreateAdapter() {
        return null;
    }

    @Override
    protected void initData() {
        super.initData();
        getFollowingUserList();
    }

    void getFollowingUserList() {
        MPostApi.getFollowingUserList(AppContext.getInstance().getUser(), new QueryListener<MUser>() {
            @Override
            public void onFailure(int code, String msg) {
                ErrorLog.log(code, msg);
                showToast(R.string.info_access_error);
                setRefreshingState(false);
                setLoadingState(false);
            }

            @Override
            public void onSuccess(List<MUser> list) {
                if (!Utils.checkListEmpty(list)) {
                    SwipeRefreshLayout refreshLayout = getRefreshLayout();
                    if (refreshLayout.isLoading()) {
                        setLoadingState(false);
                        getAdapter().addAll(list);
                    } else {
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

    FollowingUserAdapter getAdapter() {
        if (null == mAdapter) {
            mAdapter = new FollowingUserAdapter(getActivity());
            mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    UIHelper.showUserDetail(getActivity(), mAdapter.getItem(position));
                }
            });
            setAdapter(mAdapter);
        }
        return mAdapter;
    }

}
