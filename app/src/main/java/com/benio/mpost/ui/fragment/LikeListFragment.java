package com.benio.mpost.ui.fragment;

import com.benio.mpost.R;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;
import com.benio.mpost.widget.SwipeRefreshLayout;

import java.util.List;

/**
 * 查看用户曾经点赞的贴
 * Created by benio on 2015/11/30.
 */
public class LikeListFragment extends TimeLineFragment {

    void getPostList(String filter) {
        MPostApi.getMyLikePost(AppContext.getInstance().getUser(), new QueryListener<MPost>() {
            @Override
            public void onFailure(int code, String msg) {
                ErrorLog.log(code, msg);
                showToast(R.string.info_access_error);
                setRefreshingState(false);
                setLoadingState(false);
            }

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
        }, mPage);
    }

}
