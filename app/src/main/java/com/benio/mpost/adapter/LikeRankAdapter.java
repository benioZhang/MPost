package com.benio.mpost.adapter;

import android.content.Context;

import com.benio.mpost.R;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.RecyclerHolder;

import java.util.List;

/**
 * Created by benio on 2015/11/8.
 */
public class LikeRankAdapter extends TimeLineAdapter {

    public LikeRankAdapter(Context context) {
        super(context);
    }

    public LikeRankAdapter(Context context, List<MPost> data) {
        super(context, data);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_like_rank;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, MPost data) {
        super.onBindViewHolder(holder, data);
        holder.getTextView(R.id.tv_item_favor_count).setText("" + data.getFavorCount());
        holder.getTextView(R.id.tv_item_like_count).setText("" + data.getLikeCount());
    }
}
