package com.benio.mpost.adapter;

import android.content.Context;

import com.benio.mpost.R;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.RecyclerHolder;
import com.benio.mpost.network.ImageLoader;
import com.benio.mpost.util.image.CircleTransform;

import java.util.List;

/**
 * Created by benio on 2015/12/6.
 */
public class UserAdapter extends BaseRecyclerAdapter<MUser> {
    public UserAdapter(Context context, List<MUser> data) {
        super(context, data);
    }

    public UserAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_following_user;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, MUser data) {
        holder.getTextView(R.id.tv_following_user).setText(data.getName());
        if (data.hasPortrait()) {
//            ImageLoader.getInstance(getContext()).load(holder.getImageView(R.id.iv_following_user), data.getPortraitUrl());
            ImageLoader.getInstance(getContext()).load(holder.getImageView(R.id.iv_following_user), data.getPortraitUrl(), R.mipmap.user_default_header, new CircleTransform(getContext()));
        } else {
            ImageLoader.getInstance(getContext()).load(holder.getImageView(R.id.iv_following_user), R.mipmap.user_default_header, new CircleTransform(getContext()));
        }
    }
}
