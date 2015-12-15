package com.benio.mpost.ui.fragment;

import com.benio.mpost.R;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.util.AKLog;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;
import com.benio.mpost.widget.SwipeRefreshLayout;

import java.util.List;

/**
 * 收藏列表
 * Created by shau-lok on 11/5/15.
 */
public class FavoriteListFragment extends TimeLineFragment {

    @Override
    void getPostList() {
        MPostApi.getMyFavouritePost(AppContext.getInstance().getUser(), new QueryListener<MPost>() {
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
                        getTimeLineAdapter().addAll(list);
                    } else {
                        AKLog.d("xxx", "isRefreshing ");
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
}
