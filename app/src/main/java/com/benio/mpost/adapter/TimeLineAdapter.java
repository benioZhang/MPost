package com.benio.mpost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.benio.mpost.R;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.RecyclerHolder;
import com.benio.mpost.network.ImageLoader;
import com.benio.mpost.widget.FullyGridLayoutManager;

import java.util.List;

/**
 * TimeLineFragment
 * Created by benio on 2015/10/22.
 */
public class TimeLineAdapter extends BaseRecyclerAdapter<MPost> {
    private int mSpanCount;

    public TimeLineAdapter(Context context) {
        this(context, null);
    }

    public TimeLineAdapter(Context context, List<MPost> data) {
        super(context, data);
        mSpanCount = context.getResources().getInteger(R.integer.thumbnail_span_count);
    }

    @Override
    public int getLayoutRes(int viewType) {
        return R.layout.item_time_line;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, MPost data) {
        holder.getTextView(R.id.tv_item_author).setText(data.getAuthor().getName());
        holder.getTextView(R.id.tv_item_time).setText(data.getCreatedAt());
        holder.getTextView(R.id.tv_item_content).setText(data.getContent());

        ImageView iv = holder.getImageView(R.id.iv_item_author);
        ImageLoader imageLoader = ImageLoader.getInstance(getContext());
        final MUser author = data.getAuthor();
        if (mOnAuthorClickListener != null) {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnAuthorClickListener.onAuthorClick(author);
                }
            });
        }
        if (author.hasPortrait()) {
            imageLoader.load(iv, author.getPortraitUrl(),R.mipmap.user_default_header);
        } else {
            imageLoader.load(iv, R.mipmap.user_default_header);
        }

        if (data.hasPhoto()) {
            //使用ViewHolder写法，避免频繁创建实例
            ThumbnailAdapter adapter = (ThumbnailAdapter) holder.itemView.getTag(R.id.adapter_tag);
            List<String> photoList = data.getPhotoList();
//            AKLog.d("xxxx", photoList.toString());
            if (null == adapter) {
                adapter = new ThumbnailAdapter(getContext(), photoList);
                holder.itemView.setTag(R.id.adapter_tag, adapter);
            } else {
                //这里一定要刷新data，否则数据不对
                adapter.setData(photoList);
            }

            FullyGridLayoutManager manager = (FullyGridLayoutManager) holder.itemView.getTag(R.id.manager_tag);
            if (null == manager) {
                manager = new FullyGridLayoutManager(getContext(), mSpanCount);
                holder.itemView.setTag(R.id.manager_tag, manager);
            }

            RecyclerView recyclerView = (RecyclerView) holder.getView(R.id.recycler_view);
            recyclerView.setEnabled(false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        }
    }


    private OnAuthorClickListener mOnAuthorClickListener;

    public void setOnAuthorClickListener(OnAuthorClickListener onAuthorClickListener) {
        this.mOnAuthorClickListener = onAuthorClickListener;
    }

    public interface OnAuthorClickListener {
        void onAuthorClick(MUser author);
    }

}
