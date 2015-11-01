package com.benio.mpost.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.benio.mpost.R;
import com.benio.mpost.bean.Comment;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.RecyclerHolder;
import com.benio.mpost.util.AKLog;

import java.util.List;

/**
 * 评论Adapter
 * Created by benio on 2015/10/26.
 */
public class CommentAdapter extends BaseRecyclerAdapter<Comment> {

    public CommentAdapter(Context context) {
        super(context);
    }

    public CommentAdapter(Context context, List<Comment> data) {
        super(context, data);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_comment;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, Comment data) {
        AKLog.d("xxxx", data.toString());
        TextView fromUserTextView = holder.getTextView(R.id.tv_item_from_user);
        fromUserTextView.setText(data.getFromUser().getName());

        TextView toUserTextView = holder.getTextView(R.id.tv_item_to_user);
        MUser toUser = data.getToUser();
        if (null == toUser) {//没有回复对象则隐藏
            fromUserTextView.append(": ");//a: xxxxx
            holder.getView(R.id.tv_item_reply).setVisibility(View.GONE);
            toUserTextView.setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_item_reply).setVisibility(View.VISIBLE);
            toUserTextView.setVisibility(View.VISIBLE);
            toUserTextView.setText(toUser.getName() + ":");//a回复b: xxxxx
        }

        holder.getTextView(R.id.tv_item_content).setText(data.getContent());
    }
}
