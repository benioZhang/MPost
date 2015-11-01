package com.benio.mpost.adapter;

import android.content.Context;

import com.benio.mpost.R;
import com.benio.mpost.bean.RecyclerHolder;

import java.util.List;

/**
 * 简单数组adapter
 * Created by benio on 2015/10/21.
 */
public class SimpleArrayAdapter extends BaseRecyclerAdapter<String> {

    public SimpleArrayAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_text;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, String data) {
        holder.getTextView(R.id.tv_item_text).setText(data);
    }
}
