package com.benio.mpost.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.ThumbnailAdapter;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.PostVisibility;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.interf.impl.ResponseListener;
import com.benio.mpost.util.AKLog;
import com.benio.mpost.util.AKView;
import com.benio.mpost.widget.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发帖
 * Created by benio on 2015/10/15.
 */
public class PublishPostFragment extends RecyclerFragment implements AdapterView.OnItemClickListener {
    /**
     * 请求码：相册
     */
    private static final int REQUEST_ALBUM = 0x00001;
    /**
     * 请求码：帖子可见性
     */
    private static final int REQUEST_POST_VISIBILITY = 0x00002;

    /**
     * 帖子内容输入框
     */
    @Bind(R.id.et_publish_post_content)
    EditText mPostContentEditText;
    /**
     * 帖子可见性
     */
    @Bind(R.id.tv_publish_post_visibility)
    TextView mVisibilityTextView;

    private ThumbnailAdapter mAdapter;
    /**
     * 帖的图片列表
     */
    private List<String> mPostPhotoList;
    /**
     * 每行显示的图片数量
     */
    private int mSpanCount;
    /**
     * 贴子可见性
     */
    private int mVisibility;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //通知Fragment调用onCreateOptionsMenu()方法
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_commit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_commit) {
            onPublishPostEvent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getContentResource() {
        return R.layout.fragment_publish_post;
    }

    @Override
    public RecyclerView.ItemDecoration onCreateItemDecoration() {
        return new DividerGridItemDecoration(getActivity());
    }

    @Override
    public RecyclerView.LayoutManager onCreateLayoutManager() {
        return new GridLayoutManager(getActivity(), mSpanCount);
    }

    @Override
    public BaseRecyclerAdapter onCreateAdapter() {
        mAdapter = new ThumbnailAdapter(getContext());
        mAdapter.addAlbum();
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.isAlbum(position)) {
            int max = MPost.MAX_PHOTO_COUNT - getPostPhotosCount();
            if (max > 0) {
                //打开相册
                UIHelper.showAlbumForResult(this, REQUEST_ALBUM, max);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ALBUM && data != null) {
            //获取返回结果
            String[] result = data.getStringArrayExtra(AlbumFragment.BUNDLE_KEY_SELECTED_IMAGE_PATH);
            if (result != null) {
                List<String> resultList = Arrays.asList(result);
                mPostPhotoList.addAll(resultList);
                mAdapter.addAll(resultList);
            }

            //帖子图片数量大于等于上限则移除添加按钮
            if (getPostPhotosCount() >= MPost.MAX_PHOTO_COUNT) {
                mAdapter.removeAlbum();
            }
        } else if (requestCode == REQUEST_POST_VISIBILITY && data != null) {
            //更新可见性
            mVisibility = data.getIntExtra(PostVisibilityFragment.BUNDLE_KEY_VISIBILITY, PostVisibility.PUBLIC.getVisibility());
            String desc = data.getStringExtra(PostVisibilityFragment.BUNDLE_KEY_DESCRIPTION);
            mVisibilityTextView.setText(desc);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void initData() {
        super.initData();
        //获取每行显示的图片数量
        mSpanCount = getResources().getInteger(R.integer.thumbnail_span_count);
        mPostPhotoList = new ArrayList<>();
        mVisibility = PostVisibility.PUBLIC.getVisibility();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mVisibilityTextView.setText(PostVisibility.PUBLIC.getDescription());
    }

    @OnClick(R.id.fl_publish_post_visible)
    void onShowPostVisibleEvent() {
        UIHelper.showPostVisibleForResult(this, REQUEST_POST_VISIBILITY);
    }

    /**
     * @return 获取post图片数量
     */
    int getPostPhotosCount() {
        //要除去添加按钮
        return mAdapter != null ? (mAdapter.asList().size() - 1) : 0;
    }

    /**
     * 发帖
     */
    private void onPublishPostEvent() {
        if (getPostPhotosCount() < MPost.MIN_PHOTO_COUNT) {
            showToast("你至少要选择" + MPost.MIN_PHOTO_COUNT + "张图片");
            return;
        }

        showProgress(R.string.publishing);

        String content = AKView.getText(mPostContentEditText);
        MUser author = AppContext.getInstance().getUser();

        MPostApi.publishPost(author, content, mPostPhotoList, mVisibility, new ResponseListener() {
            @Override
            public void onSuccess() {
                hideProgress();
                AKLog.d("发帖成功");
                showToast(R.string.info_publish_post_success);
                getActivity().finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                AKLog.d("失败 " + "code " + code + "  msg  " + msg);
                showToast(R.string.info_publish_post_failure);
            }
        });
    }

}
