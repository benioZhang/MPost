package com.benio.mpost.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.PostVisibilityAdapter;
import com.benio.mpost.bean.PostVisibility;

import java.util.Arrays;
import java.util.List;

/**
 * 设置Post的可见性
 * Created by benio on 2015/10/21.
 */
public class PostVisibilityFragment extends RecyclerFragment implements AdapterView.OnItemClickListener {

    public static final String BUNDLE_KEY_VISIBILITY = "BUNDLE_KEY_VISIBILITY";
    public static final String BUNDLE_KEY_DESCRIPTION = "BUNDLE_KEY_DESCRIPTION";

    private BaseRecyclerAdapter<PostVisibility> mAdapter;

    @Override
    public BaseRecyclerAdapter onCreateAdapter() {
        List<PostVisibility> list = Arrays.asList(PostVisibility.VALUES);
        mAdapter = new PostVisibilityAdapter(getActivity(), list);
        mAdapter.setOnItemClickListener(this);
        return mAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PostVisibility visibility = mAdapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra(BUNDLE_KEY_VISIBILITY, visibility.getVisibility());
        intent.putExtra(BUNDLE_KEY_DESCRIPTION, visibility.getDescription());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
