package com.benio.mpost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.benio.mpost.bean.RecyclerHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * recyclerView adapter基类
 * Created by benio on 2015/10/14.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

    private List<T> mData;
    private Context mContext;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private AdapterView.OnItemLongClickListener mOnItemLongClickListener;

    public BaseRecyclerAdapter(Context context) {
        this(context, null);
    }

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = (null == data ? new ArrayList<T>() : data);
    }

    public BaseRecyclerAdapter(Context context, Collection<? extends T> collection) {
        this.mContext = context;
        this.mData = (null == collection ? new ArrayList<T>() : new ArrayList<>(collection));
    }

    public abstract int getLayoutRes(int viewType);

    public abstract void onBindViewHolder(final RecyclerHolder holder, T data);

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes = getLayoutRes(viewType);
        View itemView = LayoutInflater.from(mContext).inflate(layoutRes, parent, false);
        return new RecyclerHolder(itemView);
    }

    @Override
    public final void onBindViewHolder(final RecyclerHolder holder, int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(null, v, holder.getLayoutPosition(), holder.getItemId());
                }
            });
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mOnItemLongClickListener.onItemLongClick(null, v, holder.getLayoutPosition(), holder.getItemId());
                }
            });
        }

        onBindViewHolder(holder, getItem(holder.getLayoutPosition()));
    }

    @Override
    public int getItemCount() {
        return null == mData ? 0 : mData.size();
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void clear() {
        int size = mData.size();
        if (size > 0) {
            mData.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public int indexOf(T elem) {
        return mData.indexOf(elem);
    }

    public int lastIndexOf(T elem) {
        return mData.lastIndexOf(elem);
    }

    public boolean contains(T elem) {
        return mData.contains(elem);
    }

    public boolean containsAll(List<T> data) {
        return mData.containsAll(data);
    }

    public boolean add(T elem) {
        int lastIndex = mData.size();
        if (mData.add(elem)) {
            notifyItemInserted(lastIndex);
            return true;
        }
        return false;
    }

    public void add(int position, T elem) {
        mData.add(position, elem);
        notifyItemInserted(position);
    }

    public boolean addAll(int position, Collection<? extends T> collection) {
        if (mData.addAll(position, collection)) {
            notifyItemRangeChanged(position, collection.size());
            return true;
        }
        return false;
    }

    public boolean addAll(Collection<? extends T> collection) {
        int lastIndex = mData.size();
        if (mData.addAll(collection)) {
            notifyItemRangeChanged(lastIndex, collection.size());
            return true;
        }
        return false;
    }

    public T remove(int position) {
        T item = mData.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public boolean remove(T elem) {
        int index = indexOf(elem);
        if (mData.remove(elem)) {
            notifyItemRemoved(index);
            return true;
        }
        return false;
    }

    public List<T> asList() {
        return mData;
    }

    public Context getContext() {
        return mContext;
    }

    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener l) {
        this.mOnItemLongClickListener = l;
    }
}
