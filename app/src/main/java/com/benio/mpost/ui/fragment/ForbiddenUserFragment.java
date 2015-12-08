package com.benio.mpost.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.ForbiddenUserAdapter;
import com.benio.mpost.bean.ForbiddenUser;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
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
public class ForbiddenUserFragment extends RefreshRecyclerFragment {
    private int mPage;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            getForbiddenUserList();
        }
    };
    private ForbiddenUserAdapter mAdapter;

    private static final String[] ACTION = {"取消屏蔽"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_forbidden_user, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            UIHelper.showUserAdmin(getActivity());
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        getForbiddenUserList();
    }

    @Override
    public BaseRecyclerAdapter onCreateAdapter() {
        return null;
    }

    void getForbiddenUserList() {
        MPostApi.getForbiddenUserList(new QueryListener<ForbiddenUser>() {
            @Override
            public void onFailure(int code, String msg) {
                ErrorLog.log(code, msg);
                showToast(R.string.info_access_error);
                setRefreshingState(false);
                setLoadingState(false);
            }

            @Override
            public void onSuccess(List<ForbiddenUser> list) {
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


    private ForbiddenUserAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new ForbiddenUserAdapter(getActivity());
            mAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    AKDialog.getSelectDialog(getActivity(), ACTION, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                final ForbiddenUser forbiddenUser = mAdapter.getItem(position);
                                MPostApi.recoverUser(forbiddenUser, new ResponseListener() {
                                    @Override
                                    public void onFailure(int code, String msg) {
                                        ErrorLog.log(code, msg);
                                        showToast(R.string.info_access_error);
                                    }

                                    @Override
                                    public void onSuccess() {
                                        showToast("已恢复该用户");
                                        mAdapter.remove(forbiddenUser);
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
