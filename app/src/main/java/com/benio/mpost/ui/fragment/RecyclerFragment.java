package com.benio.mpost.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;

/**
 * RecyclerView Fragment
 * Created by benio on 2015/10/14.
 */
public abstract class RecyclerFragment extends BaseFragment {
    private RecyclerView mRecyclerView;

    @Override
    public int getContentResource() {
        return R.layout.fragment_recycler;
    }

    public RecyclerView.ItemAnimator onCreateItemAnimator() {
        return null;
    }

    /**
     * 默认没有分隔线
     *
     * @return
     */
    public RecyclerView.ItemDecoration onCreateItemDecoration() {
        return null;
    }

    /**
     * 默认流式布局
     *
     * @return
     */
    public RecyclerView.LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setAdapter(BaseRecyclerAdapter adapter) {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    public abstract BaseRecyclerAdapter onCreateAdapter();

    @Override
    protected void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //设置adapter
        RecyclerView.Adapter adapter = onCreateAdapter();
        if (adapter != null) {
            mRecyclerView.setAdapter(adapter);
        }

        //设置布局管理器
        RecyclerView.LayoutManager layout = onCreateLayoutManager();
        if (layout != null) {
            mRecyclerView.setLayoutManager(layout);
        }

        //添加分割线
        RecyclerView.ItemDecoration decor = onCreateItemDecoration();
        if (decor != null) {
            mRecyclerView.addItemDecoration(decor);
        }

        //设置Item增加、移除动画
        RecyclerView.ItemAnimator animator = onCreateItemAnimator();
        if (animator != null) {
            mRecyclerView.setItemAnimator(animator);
        }
    }

}
