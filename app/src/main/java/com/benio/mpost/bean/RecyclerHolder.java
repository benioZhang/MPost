package com.benio.mpost.bean;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * RecyclerView viewHolder
 * Created by benio on 2015/10/14.
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public RecyclerHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * 根据输入的viewId，初始化view<p>
     * 将初始化后的view存入SparseArray类型的mViews中
     *
     * @param viewId id resource
     * @return null or specific view
     */
    public View getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (null == view) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    public TextView getTextView(@IdRes int viewId) {
        return (TextView) getView(viewId);
    }

    public ImageView getImageView(@IdRes int viewId) {
        return (ImageView) getView(viewId);
    }

    public Button getButton(@IdRes int viewId) {
        return (Button) getView(viewId);
    }
}
