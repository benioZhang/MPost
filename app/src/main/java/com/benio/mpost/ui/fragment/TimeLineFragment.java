package com.benio.mpost.ui.fragment;

import android.os.Handler;
import android.os.Message;
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
import com.benio.mpost.util.AKToast;
import com.benio.mpost.util.ErrorLog;

import java.util.List;

/**
 * 时间线
 * Created by benio on 2015/10/12.
 */
public class TimeLineFragment extends RefreshRecyclerFragment {

    private TimeLineAdapter mAdapter;

    @Override
    public BaseRecyclerAdapter onCreateAdapter() {
        return null;
    }

    @Override
    public void onLoad() {
        setLoadingState(true);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setLoadingState(false);
                AKToast.show(getActivity(), "加载更多");
            }
        }.sendEmptyMessageDelayed(1, 2000);
        getPostList();
    }

    @Override
    public void onRefresh() {
        setRefreshingState(true);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setRefreshingState(false);
                AKToast.show(getActivity(), "下拉刷新");
            }
        }.sendEmptyMessageDelayed(1, 2000);
        getPostList();
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
            }

            @Override
            public void onSuccess(List<MPost> list) {
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
                setAdapter(mAdapter);
            }
        });
    }

}
