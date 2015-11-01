package com.benio.mpost.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.benio.mpost.R;
import com.benio.mpost.bean.RecyclerHolder;
import com.benio.mpost.network.ImageLoader;
import com.benio.mpost.util.AKView;

import java.util.List;

/**
 * 略缩图
 * Created by benio on 2015/10/21.
 */
public class ThumbnailAdapter extends BaseRecyclerAdapter<String> {
    /**
     * 添加略缩图按钮
     */
    private static final String ADD_THUMBNAIL = "add_thumbnail";
    /**
     * 添加略缩图按钮位置
     */
    private static final int ADD_THUMBNAIL_POSITION = 0;

    public ThumbnailAdapter(Context context) {
        super(context);
    }

    public ThumbnailAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_photo;
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
    public void onBindViewHolder(RecyclerHolder holder, String data) {
        ImageView iv = holder.getImageView(R.id.iv_item_photo);
        ImageLoader imageLoader = ImageLoader.getInstance(getContext());
        if (ADD_THUMBNAIL.equals(data)) {
            imageLoader.load(iv, R.mipmap.btn_add);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            imageLoader.load(iv, data, R.mipmap.ic_placeholder);
        }
    }

    /**
     * 添加 打开相册
     */
    public void addAlbum() {
        add(ADD_THUMBNAIL_POSITION, ADD_THUMBNAIL);
    }

    public void removeAlbum() {
        if (containsAlbum()) {
            remove(ADD_THUMBNAIL_POSITION);
        }
    }

    public boolean containsAlbum() {
        return ADD_THUMBNAIL.equals(getItem(ADD_THUMBNAIL_POSITION));
    }

    public boolean isAlbum(int position) {
        return position >= 0 && position < getItemCount() && ADD_THUMBNAIL.equals(getItem(position));
    }
}
