package com.benio.mpost.adapter;

import android.content.Context;

import com.benio.mpost.R;
import com.benio.mpost.bean.ForbiddenUser;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.RecyclerHolder;
import com.benio.mpost.network.ImageLoader;

import java.util.List;

/**
 * Created by benio on 2015/12/6.
 */
public class ForbiddenUserAdapter extends BaseRecyclerAdapter<ForbiddenUser> {
    public ForbiddenUserAdapter(Context context, List<ForbiddenUser> data) {
        super(context, data);
    }

    public ForbiddenUserAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_following_user;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, ForbiddenUser data) {
        MUser user = data.getUser();
        holder.getTextView(R.id.tv_following_user).setText(user.getName());
        if (user.hasPortrait()) {
            ImageLoader.getInstance(getContext()).load(holder.getImageView(R.id.iv_following_user), user.getPortraitUrl());
        } else {
            ImageLoader.getInstance(getContext()).load(holder.getImageView(R.id.iv_following_user), R.mipmap.ic_user_def_2);
        }
    }
}
