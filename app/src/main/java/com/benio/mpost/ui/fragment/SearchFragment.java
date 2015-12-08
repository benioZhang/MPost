package com.benio.mpost.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.benio.mpost.R;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;
import com.benio.mpost.widget.SwipeRefreshLayout;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 搜索
 * Created by benio on 2015/10/21.
 */
public class SearchFragment extends TimeLineFragment {

    SearchView editText;

    String mSearchContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        editText = (SearchView) menu.findItem(R.id.action_search).getActionView();
        editText.setIconified(false);
        editText.setIconifiedByDefault(false);
        editText.onActionViewExpanded();
        editText.setFocusable(true);
        editText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchContent = query;
                getPostList();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }


    @Override
    void getPostList() {
        if (!TextUtils.isEmpty(mSearchContent)) {
            MPostApi.searchPosts(mSearchContent, new FindListener<MPost>() {
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

                @Override
                public void onError(int i, String s) {
                    ErrorLog.log(i, s);
                    showToast(R.string.info_access_error);
                    setRefreshingState(false);
                    setLoadingState(false);
                }
            }, mPage);
        } else {
            setRefreshingState(false);
            setLoadingState(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
