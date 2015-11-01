package com.benio.mpost.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.benio.mpost.R;
import com.benio.mpost.bean.ImageFile;
import com.benio.mpost.bean.RecyclerHolder;
import com.benio.mpost.network.ImageLoader;
import com.benio.mpost.util.AKView;

import java.util.ArrayList;
import java.util.List;

/**
 * AlbumFragment adapter
 * Created by benio on 2015/10/16.
 */
public class AlbumAdapter extends BaseRecyclerAdapter<ImageFile> {

    public AlbumAdapter(Context context) {
        super(context);
    }

    public AlbumAdapter(Context context, List<ImageFile> data) {
        super(context, data);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_album;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerHolder holder = super.onCreateViewHolder(parent, viewType);
        //重新计算每个图片的宽高
        int count = getContext().getResources().getInteger(R.integer.thumbnail_span_count);
        int size = parent.getWidth() / count;
        AKView.updateLayoutParams(holder.itemView, size, size);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, ImageFile data) {
        ImageView iv = holder.getImageView(R.id.iv_item_photo);
        ImageLoader.getInstance(getContext()).load(iv, data.getPath(), R.mipmap.ic_placeholder);

        //设置选中状态
        View v = holder.getView(R.id.iv_item_photo_selected);
        if (data.isSelected()) {
            v.setVisibility(View.VISIBLE);
            v.setBackgroundColor(getContext().getResources().getColor(R.color.mask));
        } else {
            v.setVisibility(View.INVISIBLE);
            v.setBackgroundDrawable(null);
        }
    }

    public boolean isSelected(int position) {
        return position >= 0 && position < getItemCount() && getItem(position).isSelected();
    }

    public void setSelected(int position, boolean selected) {
        if (position >= 0 && position < getItemCount()) {
            getItem(position).setSelected(selected);
            notifyItemChanged(position);
        }
    }

    public List<ImageFile> getSelectedImageFile() {
        List<ImageFile> result = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++) {
            ImageFile file = getItem(i);
            if (file.isSelected()) {
                result.add(file);
            }
        }
        return result;
    }

    public void setCamera(int position) {
        add(position, null);
    }

    public boolean isCamera(ImageFile file) {
        return file != null && TextUtils.isEmpty(file.getPath());
    }

    /**
     * 判断是否为Camera
     *
     * @param position
     * @return
     */
    public boolean isCamera(int position) {
        return isCamera(getItem(position));
    }
}
