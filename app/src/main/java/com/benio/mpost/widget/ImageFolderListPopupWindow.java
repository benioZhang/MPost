package com.benio.mpost.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.ImageFolderAdapter;
import com.benio.mpost.bean.ImageFolder;

import java.util.List;

/**
 * 相册的popupWindow
 * Created by benio on 2015/9/6.
 */
public class ImageFolderListPopupWindow extends BasePopupWindow {

    private ImageFolderAdapter mAdapter;

    private OnImageFolderClickListener mOnImageFolderClickListener;

    /**
     * 文件夹是否选中
     *
     * @param position
     * @return
     */
    public boolean isFolderSelected(int position) {
        return mAdapter != null && mAdapter.isSelected(position);
    }

    /**
     * 实例化PopupWindow
     */
    public static ImageFolderListPopupWindow newInstance(Context context, int width, int height, List<ImageFolder> datas) {
        View contentView = View.inflate(context, R.layout.recycler_view, null);
        //这里直接new的时候已经执行了initView方法
        //所以后面直接重设adapter的数据即可
        ImageFolderListPopupWindow popupWindow = new ImageFolderListPopupWindow(contentView, width, height);
        popupWindow.mAdapter.setData(datas);
        return popupWindow;
    }

    /**
     * FolderList点击回调接口
     */
    public interface OnImageFolderClickListener {
        void onImageFolderClick(ImageFolder imageFolder, int position);
    }

    public void setOnImageFolderClickListener(OnImageFolderClickListener listener) {
        this.mOnImageFolderClickListener = listener;
    }

    @Override
    protected void initView(View contentView) {
        mAdapter = new ImageFolderAdapter(getContext());
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnImageFolderClickListener != null) {
                    mOnImageFolderClickListener.onImageFolderClick(mAdapter.getItem(position), position);
                }
                //选中position对应的文件夹
                mAdapter.select(position);
            }
        });

        final Context context = getContext();
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
    }

    private ImageFolderListPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }
}
