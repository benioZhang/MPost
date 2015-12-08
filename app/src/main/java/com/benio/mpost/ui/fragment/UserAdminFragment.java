package com.benio.mpost.ui.fragment;

import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.UserAdapter;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.interf.impl.ResponseListener;
import com.benio.mpost.util.AKDialog;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;
import com.benio.mpost.widget.SwipeRefreshLayout;

import java.util.List;

/**
 * Created by benio on 2015/12/8.
 */
public class UserAdminFragment extends FollowingUserFragment {
    private static final String[] ACTION = {"屏蔽"};

    @Override
    void getUserList() {
        MPostApi.getUserList(new QueryListener<MUser>() {
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

    @Override
    UserAdapter getAdapter() {
        if (null == mAdapter) {
            mAdapter = new UserAdapter(getActivity());
            mAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AKDialog.getSelectDialog(getActivity(), ACTION, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                MUser user = mAdapter.getItem(position);
                                MPostApi.forbidUser(user, new ResponseListener() {
                                    @Override
                                    public void onFailure(int code, String msg) {
                                        ErrorLog.log(code, msg);
                                        showToast(R.string.info_access_error);
                                    }

                                    @Override
                                    public void onSuccess() {
                                        showToast("已限制该用户");
                                    }
                                });
                            }
                        }
                    }).show();
                    return false;
                }
            });
            setAdapter(mAdapter);
        }
        return mAdapter;
    }
}
