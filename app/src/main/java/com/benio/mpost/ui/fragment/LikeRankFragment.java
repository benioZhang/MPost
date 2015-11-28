package com.benio.mpost.ui.fragment;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.AdapterView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.LikeRankAdapter;
import com.benio.mpost.adapter.TimeLineAdapter;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.util.AKTime;
import com.benio.mpost.util.ErrorLog;

import java.util.List;

import butterknife.Bind;

/**
 * 点赞榜
 * Created by benio on 2015/11/8.
 */
public class LikeRankFragment extends RecyclerFragment implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    LikeRankAdapter mAdapter;

    @Override
    public int getContentResource() {
        return R.layout.fragment_like_rank;
    }

    @Override
    public BaseRecyclerAdapter onCreateAdapter() {
        return null;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mTabLayout.addTab(mTabLayout.newTab().setText("全部"));
        mTabLayout.addTab(mTabLayout.newTab().setText("一周"));
        mTabLayout.addTab(mTabLayout.newTab().setText("今天"));
        mTabLayout.setOnTabSelectedListener(this);
        //默认选中全部
        onTabSelected(mTabLayout.getTabAt(0));
    }

    QueryListener<MPost> mListener = new QueryListener<MPost>() {
        @Override
        public void onFailure(int code, String msg) {
            hideProgress();
            ErrorLog.log(code, msg);
            showToast(R.string.info_access_error);
        }

        @Override
        public void onSuccess(List<MPost> list) {
            hideProgress();
            if (null == mAdapter) {
                mAdapter = new LikeRankAdapter(getActivity(), list);
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
            } else {
                mAdapter.setData(list);
                if (list.isEmpty()) {
                    showToast("no data here");
                }
            }
        }
    };

    /**
     * 获取某个时间段的点赞列表
     *
     * @param from
     * @param to
     */
    void getLikeRankList(long from, long to) {
        showProgress();
        MPostApi.getLikeRankList(mListener, from, to);
    }

    /**
     * 获取全部的点赞列表
     */
    void getLikeRandList() {
        showProgress();
        MPostApi.getLikeRankList(mListener);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int pos = tab.getPosition();
        long from = 0, to = 0;
        switch (pos) {
            case 0://全部
                getLikeRandList();
                break;

            case 1://一周
                to = System.currentTimeMillis();
                from = AKTime.getMillisDaysBefore(to, 7);
                getLikeRankList(from, to);
                break;

            case 2://今天
                to = System.currentTimeMillis();
                from = AKTime.getMillisDaysBefore(to, 1);
                getLikeRankList(from, to);
                break;

            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
