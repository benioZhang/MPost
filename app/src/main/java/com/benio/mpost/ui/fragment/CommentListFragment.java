package com.benio.mpost.ui.fragment;

import android.os.Handler;
import android.os.Message;
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
import com.benio.mpost.util.AKToast;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;

import java.util.List;

/**
 * Created by shau-lok on 11/5/15.
 */
public class CommentListFragment extends RefreshRecyclerFragment {

    private CommentListAdapter mAdapter;

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
        getCommentList();

    }

    @Override
    public void onRefresh() {
        setRefreshingState(true);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                setRefreshingState(false);
//                AKToast.show(getActivity(), "下拉刷新");
            }
        }.sendEmptyMessageDelayed(1, 2000);
        getCommentList();
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
            }

            @Override
            public void onSuccess(List<Comment> list) {

                if (!Utils.checkListEmpty(list)) {
                    mAdapter = new CommentListAdapter(getActivity(), list);
                    mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            UIHelper.showPostDetail(getActivity(), mAdapter.getItem(position).getPost());
                        }
                    });
                    setAdapter(mAdapter);
                } else {
                    // TODO: 11/5/15 内容为空时
                    showToast("还没收到评论哦~");
                }
            }
        });
    }

}
