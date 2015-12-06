package com.benio.mpost.adapter;

import android.content.Context;

import com.benio.mpost.R;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.Comment;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.RecyclerHolder;
import com.benio.mpost.util.AKLog;

import java.util.List;

/**
 * Created by shau-lok on 11/5/15.
 */
public class CommentListAdapter extends BaseRecyclerAdapter<Comment> {
    private MUser me;

    public CommentListAdapter(Context context) {
        this(context, null);
    }

    public CommentListAdapter(Context context, List<Comment> data) {
        super(context, data);
        me = AppContext.getInstance().getUser();
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_list_comment;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, Comment data) {
        AKLog.d(":xxx", "comment " + data.toString());
        MUser fromUser = data.getFromUser();
        MUser toUser = data.getToUser();
        holder.getTextView(R.id.tv_user_name).setText(fromUser.getUsername());
        holder.getTextView(R.id.tv_item_time).setText(data.getCreatedAt());
        if (toUser != null && toUser.equals(me)) {
            holder.getTextView(R.id.tv_comment_content).setText("回复了你: " + data.getContent());
        } else {
            holder.getTextView(R.id.tv_comment_content).setText("评论内容: " + data.getContent());
        }
        holder.getTextView(R.id.tv_commented_content).setText(data.getPost().getContent());
    }
}
