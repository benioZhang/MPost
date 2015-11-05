package com.benio.mpost.adapter;

import android.content.Context;

import com.benio.mpost.R;
import com.benio.mpost.bean.Comment;
import com.benio.mpost.bean.RecyclerHolder;

import java.util.Collection;
import java.util.List;

/**
 * Created by shau-lok on 11/5/15.
 */
public class CommentListAdapter extends BaseRecyclerAdapter<Comment> {
    public CommentListAdapter(Context context) {
        super(context);
    }

    public CommentListAdapter(Context context, List<Comment> data) {
        super(context, data);
    }

    public CommentListAdapter(Context context, Collection<? extends Comment> collection) {
        super(context, collection);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_list_comment;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, Comment data) {
        holder.getTextView(R.id.tv_user_name).setText(data.getFromUser().getUsername());
        holder.getTextView(R.id.tv_item_time).setText(data.getCreatedAt());
        holder.getTextView(R.id.tv_comment_content).setText("回复了你: " + data.getContent());
        holder.getTextView(R.id.tv_commented_content).setText(data.getPost().getContent());

    }
}
