package com.benio.mpost.adapter;

import android.content.Context;

import com.benio.mpost.R;
import com.benio.mpost.bean.RecyclerHolder;
import com.benio.mpost.bean.PostVisibility;

import java.util.List;

/**
 * 帖子可见性
 * Created by benio on 2015/10/21.
 */
public class PostVisibilityAdapter extends BaseRecyclerAdapter<PostVisibility> {
    public PostVisibilityAdapter(Context context) {
        super(context);
    }

    public PostVisibilityAdapter(Context context, List<PostVisibility> data) {
        super(context, data);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_text;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, PostVisibility data) {
        holder.getTextView(R.id.tv_item_text).setText(data.getDescription());
    }
}
