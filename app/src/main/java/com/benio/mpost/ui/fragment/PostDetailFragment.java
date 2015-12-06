package com.benio.mpost.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.benio.mpost.R;
import com.benio.mpost.adapter.BaseRecyclerAdapter;
import com.benio.mpost.adapter.CommentAdapter;
import com.benio.mpost.adapter.ThumbnailAdapter;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.Comment;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.PostDetail;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.controller.UIHelper;
import com.benio.mpost.interf.impl.QueryListener;
import com.benio.mpost.interf.impl.ResponseListener;
import com.benio.mpost.network.ImageLoader;
import com.benio.mpost.util.AKLog;
import com.benio.mpost.util.AKView;
import com.benio.mpost.util.ErrorLog;
import com.benio.mpost.util.Utils;
import com.benio.mpost.widget.FullyGridLayoutManager;
import com.benio.mpost.widget.FullyLinearLayoutManager;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 帖子详情
 * Created by benio on 2015/10/23.
 */
public class PostDetailFragment extends BaseFragment {
    /**
     * 传参Key:PostDetail
     */
    public static String BUNDLE_KEY_POST = "BUNDLE_KEY_POST";

    @Bind(R.id.iv_item_author)
    ImageView mAuthorImageView;
    @Bind(R.id.tv_item_author)
    TextView mNameTextView;
    @Bind(R.id.tv_item_time)
    TextView mTimeTextView;
    @Bind(R.id.tv_item_content)
    TextView mContentTextView;
    @Bind(R.id.tv_favor)
    TextView mFavorTextView;
    @Bind(R.id.iv_favor)
    ImageView mFavorImageView;
    @Bind(R.id.tv_like)
    TextView mLikeTextView;
    @Bind(R.id.iv_like)
    ImageView mLikeImageView;
    @Bind(R.id.recycler_view)
    RecyclerView mPhotoRecyclerView;
    @Bind(R.id.recycler_view_comment)
    RecyclerView mCommentRecyclerView;
    @Bind(R.id.rl_comment)
    View mCommentLayout;
    @Bind(R.id.et_comment)
    EditText mCommentEditText;

    private PostDetail mPostDetail;
    private BaseRecyclerAdapter<Comment> mAdapter;
    //回复对象，没有则为Null
    private MUser mToUser;

    @Override
    public int getContentResource() {
        return R.layout.fragment_post_detail;
    }

    @Override
    protected void initView(View view) {
        MPost post = mPostDetail.getPost();
        final MUser author = post.getAuthor();
        mNameTextView.setText(author.getName());
        mTimeTextView.setText(post.getCreatedAt());
        mContentTextView.setText(post.getContent());
        //set author portrait
        if (author.hasPortrait()) {
            ImageLoader.getInstance(this).load(mAuthorImageView, author.getPortraitUrl(), R.mipmap.user_default_header);
        } else {
            ImageLoader.getInstance(this).load(mAuthorImageView, R.mipmap.user_default_header);
        }
        mAuthorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showUserDetail(getActivity(), author);
            }
        });

        // set mPostDetail photos
        if (post.hasPhoto()) {
            int spanCount = getResources().getInteger(R.integer.thumbnail_span_count);
            List<String> photoList = post.getPhotoList();
            ThumbnailAdapter adapter = new ThumbnailAdapter(getActivity(), photoList);
            RecyclerView.LayoutManager layoutManager = new FullyGridLayoutManager(getActivity(), spanCount);
            if (photoList.size() <= spanCount) {
                //因为FullyGridLayoutManager有bug
                //在图片数量为1的时候显示不了
                //layoutManager = new GridLayoutManager(getContext(), spanCount);
            }
            mPhotoRecyclerView.setAdapter(adapter);
            mPhotoRecyclerView.setLayoutManager(layoutManager);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle args = getArguments();
        final MPost post;
        if (null == args || (post = (MPost) args.getSerializable(BUNDLE_KEY_POST)) == null) {
            throw new RuntimeException("you must provide a mPostDetail here");
        }

        showProgress();

        mPostDetail = new PostDetail();
        mPostDetail.setPost(post);

        final MUser user = AppContext.getInstance().getUser();

        //get favor status
        MPostApi.isFavoredPost(user, post, new QueryListener<MPost>() {
            @Override
            public void onSuccess(List<MPost> result) {
                mPostDetail.setFavored(Utils.isPostInList(post, result));
                checkLoadReady();
            }

            @Override
            public void onFailure(int i, String s) {
                ErrorLog.log(i, s);
                mPostDetail.setFavored(false);
                checkLoadReady();
            }
        });

        //get like status
        MPostApi.isLikedPost(user, post, new QueryListener<MPost>() {
            @Override
            public void onSuccess(List<MPost> result) {
                mPostDetail.setLiked(Utils.isPostInList(post, result));
                checkLoadReady();
            }

            @Override
            public void onFailure(int i, String s) {
                ErrorLog.log(i, s);
                mPostDetail.setLiked(false);
                checkLoadReady();
            }
        });

        //get commentList
        MPostApi.getCommentList(post, new QueryListener<Comment>() {
            @Override
            public void onSuccess(List<Comment> list) {
                mPostDetail.setCommentList(list);
                checkLoadReady();
            }

            @Override
            public void onFailure(int code, String msg) {
                ErrorLog.log(code, msg);
                showToast(R.string.info_access_error);
                mPostDetail.setCommentList(null);
                checkLoadReady();
            }
        });
    }

    /**
     * 发表评论
     */
    @OnClick(R.id.btn_send)
    void sendComment() {
        String content = AKView.getText(mCommentEditText);
        final Comment comment = new Comment();
        comment.setFromUser(AppContext.getInstance().getUser());
        comment.setToUser(mToUser);
        comment.setContent(content);
        comment.setPost(mPostDetail.getPost());
        MPostApi.comment(comment, new ResponseListener() {
            @Override
            public void onFailure(int code, String msg) {
                ErrorLog.log(code, msg);
                showToast(R.string.info_comment_failed);
            }

            @Override
            public void onSuccess() {
                if (mAdapter != null) {
                    mAdapter.add(comment);
                }
                //hide comment layout after comment success
                showCommentLayout(false);
                mCommentEditText.setHint("");
                mCommentEditText.setText("");
                AKLog.d(getString(R.string.info_comment_success));
            }
        });
    }

    /**
     * 点击评论显示评论输入事件
     */
    @OnClick(R.id.iv_comment)
    void onShowCommentLayoutEvent() {
        showCommentLayout(mCommentLayout.getVisibility() != View.VISIBLE);
    }

    private void showCommentLayout(boolean show) {
        mCommentLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * 收藏/取消收藏
     */
    @OnClick(R.id.iv_favor)
    void onFavorEvent() {
        showProgress();
        final boolean favor = !mPostDetail.isFavored();
        MPostApi.favorPost(AppContext.getInstance().getUser(), mPostDetail.getPost(), favor, new ResponseListener() {
            @Override
            public void onFailure(int code, String msg) {
                hideProgress();
                showToast(R.string.info_favor_failed);
                ErrorLog.log(code, msg);
            }

            @Override
            public void onSuccess() {
                hideProgress();
                mPostDetail.setFavored(favor);
                mPostDetail.getPost().increaseFavor(favor ? +1 : -1);//收藏则收藏数+1，否则-1
                setupFavor();
            }
        });
    }

    /**
     * 赞/取消赞
     */
    @OnClick(R.id.iv_like)
    void onLikeEvent() {
        final boolean like = !mPostDetail.isLiked();
        showProgress();
        MPostApi.likePost(AppContext.getInstance().getUser(), mPostDetail.getPost(), like, new ResponseListener() {
            @Override
            public void onFailure(int code, String msg) {
                hideProgress();
                showToast(R.string.info_like_failed);
                ErrorLog.log(code, msg);
            }

            @Override
            public void onSuccess() {
                hideProgress();
                mPostDetail.setLiked(like);
                mPostDetail.getPost().increaseLike(like ? +1 : -1);//赞则赞数+1，否则-1
                setupLike();
            }
        });
    }

    /**
     * 检查是否已经加载完成
     */
    private void checkLoadReady() {
        if (mPostDetail.isReady()) {
            hideProgress();
            //set adapter
            mAdapter = new CommentAdapter(getActivity(), mPostDetail.getCommentList());
            mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //show input layout
                    showCommentLayout(true);

                    Comment comment = mAdapter.getItem(position);
                    MUser user = comment.getFromUser();
                    //如果选中自己的评论，则直接返回
                    if (user.equals(AppContext.getInstance().getUser())) {
                        mCommentEditText.setHint("");
                        mCommentEditText.setText("");
                        return;
                    }
                    //获取评论作者 作为 回复的对象
                    mToUser = comment.getFromUser();
                    mCommentEditText.setHint("回复" + mToUser.getName());
                    mCommentEditText.setText("");
                }
            });
            mCommentRecyclerView.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
            mCommentRecyclerView.setAdapter(mAdapter);

            setupLike();
            setupFavor();
        }
    }

    /**
     * 设置赞
     */
    private void setupLike() {
        MPost post = mPostDetail.getPost();
        mLikeTextView.setText(String.valueOf(post.getLikeCount()));
        mLikeImageView.setSelected(mPostDetail.isLiked());
    }

    /**
     * 设置收藏
     */
    private void setupFavor() {
        MPost post = mPostDetail.getPost();
        mFavorTextView.setText(String.valueOf(post.getFavorCount()));
        mFavorImageView.setSelected(mPostDetail.isFavored());
    }
}
