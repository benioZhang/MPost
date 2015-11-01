package com.benio.mpost.adapter;

import android.content.Context;
import android.view.View;

import com.benio.mpost.R;
import com.benio.mpost.bean.ImageFolder;
import com.benio.mpost.bean.RecyclerHolder;
import com.benio.mpost.network.ImageLoader;

import java.util.List;

/**
 * ImageFolder
 * Created by benio on 2015/9/7.
 */
public class ImageFolderAdapter extends BaseRecyclerAdapter<ImageFolder> {

    //上次选中的position
    private int mLastPosition;

    public ImageFolderAdapter(Context context) {
        super(context);
    }

    public ImageFolderAdapter(Context context, List<ImageFolder> data) {
        super(context, data);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_image_folder;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, ImageFolder data) {
        holder.getTextView(R.id.tv_item_image_folder_name).setText(data.getName());
        holder.getTextView(R.id.tv_item_image_folder_count).setText(data.getCount() + "张");
        holder.getView(R.id.iv_item_image_folder_selected).setVisibility(data.isSelected() ? View.VISIBLE : View.INVISIBLE);
        ImageLoader.getInstance(getContext()).load(holder.getImageView(R.id.iv_item_image_folder), data.getFirstImagePath());
    }

    /**
     * 选中第position个文件夹
     *
     * @param position
     */
    public void select(int position) {
        //点击了已经选中的文件夹，直接返回
        if (isSelected(position)) {
            return;
        }

        getItem(position).setSelected(true);//选中当前
        getItem(mLastPosition).setSelected(false);//清除上次选中
        notifyItemChanged(position);
        notifyItemChanged(mLastPosition);
        mLastPosition = position;
    }

    /**
     * 文件夹是否选中
     *
     * @param position
     * @return
     */
    public boolean isSelected(int position) {
        return position >= 0 && position < getItemCount() && getItem(position).isSelected();
    }
}
