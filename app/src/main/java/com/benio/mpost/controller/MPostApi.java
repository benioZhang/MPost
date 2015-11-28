package com.benio.mpost.controller;

import android.content.Context;

import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.Comment;
import com.benio.mpost.bean.MLike;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.consts.Column;
import com.benio.mpost.consts.Constant;
import com.benio.mpost.interf.Response;
import com.benio.mpost.interf.impl.UploadFilesListener;
import com.benio.mpost.util.AKLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;

/**
 * api
 * Created by benio on 2015/10/11.
 */
public class MPostApi {

    static final String TAG = MPostApi.class.getSimpleName();

    /**
     * 获取点赞榜列表
     *
     * @param listener
     */
    public static void getLikeRankList(FindListener<MPost> listener) {
        BmobQuery<MPost> query = new BmobQuery<>();
        query.include(Column.Post.AUTHOR);
//        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.setLimit(Constant.LIKE_RANK_NUM);
        query.order(Column.Post.REVERSE_LIKE_COUNT + "," + Column.Post.REVERSE_CREATED_AT);
        query.findObjects(getContext(), listener);
    }

    /**
     * 获取点赞榜列表
     * 全部，一周，今天内
     *
     * @param listener
     * @param from     开始时间戳
     * @param to       截止时间戳
     */
    public static void getLikeRankList(FindListener<MPost> listener, long from, long to) {
        BmobQuery<MPost> q1 = new BmobQuery<>();
        BmobDate fromDate = new BmobDate(new Date(from));
        AKLog.d("xxxx", "from date:" + fromDate.getDate());
        q1.addWhereGreaterThanOrEqualTo(Column.Post.CREATED_AT, fromDate);
        BmobQuery<MPost> q2 = new BmobQuery<>();
        BmobDate toDate = new BmobDate(new Date(to));
        AKLog.d("xxxx", "to date:" + toDate.getDate());
        q2.addWhereLessThanOrEqualTo(Column.Post.CREATED_AT, toDate);

        List<BmobQuery<MPost>> queries = new ArrayList<>(2);
        queries.add(q1);
        queries.add(q2);

        BmobQuery<MPost> query = new BmobQuery<>();
        query.and(queries);
        query.include(Column.Post.AUTHOR);
        query.setLimit(Constant.LIKE_RANK_NUM);
//        query.order(Column.Post.REVERSE_LIKE_COUNT + "," + Column.Post.REVERSE_CREATED_AT);
        query.findObjects(getContext(), listener);
    }

    /**
     * 获取用户的发帖列表
     *
     * @param user
     * @param listener
     */
    public static void getUserPostList(MUser user, FindListener<MPost> listener) {
        BmobQuery<MPost> query = new BmobQuery<>();
        query.addWhereEqualTo(Column.Post.AUTHOR, new BmobPointer(user));
        query.include(Column.Post.AUTHOR);
        query.setLimit(Constant.QUERY_LIMIT);
        query.order(Column.Post.REVERSE_CREATED_AT);
        query.findObjects(getContext(), listener);
    }

    /**
     * 评论/回复评论
     */
    public static void comment(Comment comment, Response listener) {
        comment.save(getContext(), listener.save());
    }

    /**
     * 赞帖/取消赞
     *
     * @param user
     * @param post
     * @param isLiked
     * @param listener
     */
    public static void likePost(final MUser user, final MPost post, boolean isLiked, Response listener) {
//        BmobRelation relation = new BmobRelation();
//        if (isLiked) {
//            relation.add(user);
//        } else {
//            relation.remove(user);
//        }
//        post.setLikeRelation(relation);
//        post.increment(Column.Post.LIKE_COUNT, isLiked ? 1 : -1);//赞数 +1 or -1
//        post.update(getContext(), listener.update());
        BmobRelation relation = new BmobRelation();
        if (isLiked) {
            relation.add(post);
        } else {
            relation.remove(user);
        }
        user.setLikeRelation(relation);
        user.update(getContext(), listener.update());
    }

    /**
     * 更新post 点赞数
     *
     * @param post
     * @param isLiked
     * @param listener
     */
    public static void updatePostLikedCount(MPost post, boolean isLiked, Response listener) {
        post.increment(Column.Post.LIKE_COUNT, isLiked ? +1 : -1);//点赞数 +1 or -1
        post.update(getContext(), listener.update());
    }

    public static void updateLike(MUser user, MPost post, boolean isLiked, Response listener) {
        MLike mLike = new MLike();
        mLike.setFromUser(user);
        mLike.setToUser(post.getAuthor());
        mLike.setPost(post);
        mLike.save(getContext(), listener.save());
    }

    /**
     * 显示个人点赞 post
     *
     * @param user
     * @param listener
     */
    public static void getMyLikePost(MUser user, FindListener<MPost> listener) {
        BmobQuery<MPost> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.LIKE_RELATION, new BmobPointer(user));
        query.include(Column.Post.AUTHOR);
        query.order(Column.Post.REVERSE_CREATED_AT);
        query.setLimit(Constant.QUERY_LIMIT);
        query.findObjects(getContext(), listener);
    }


    /**
     * 收藏帖/收藏赞
     *
     * @param user
     * @param post
     * @param listener
     */
    public static void favorPost(final MUser user, final MPost post, boolean isFavored, Response listener) {
//        BmobRelation favorRelation = new BmobRelation();
//        if (isFavored) {
//            favorRelation.add(user);
//        } else {
//            favorRelation.remove(user);
//        }
//        post.setFavorRelation(favorRelation);
//        post.increment(Column.Post.FAVOR_COUNT, isFavored ? +1 : -1);//收藏数 +1 or -1
//        post.update(getContext(), listener.update());
        BmobRelation favorRelation = new BmobRelation();
        if (isFavored) {
            favorRelation.add(post);
        } else {
            favorRelation.remove(post);
        }
        user.setFavRelation(favorRelation);
        user.update(getContext(), listener.update());
    }

    /**
     * 收藏完毕更新post表fav数
     *
     * @param post
     * @param isFavored
     * @param listener
     */
    public static void updatePostFavoredCount(final MPost post, boolean isFavored, Response listener) {
        post.increment(Column.Post.FAVOR_COUNT, isFavored ? +1 : -1);//收藏数 +1 or -1
        post.update(getContext(), listener.update());
    }


    /**
     * 显示个人收藏post
     *
     * @param user
     * @param listener
     */
    public static void getMyFavouritePost(MUser user, FindListener<MPost> listener) {
        BmobQuery<MPost> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.FAV_RELATION, new BmobPointer(user));
        query.include(Column.Post.AUTHOR);
        query.order(Column.Post.REVERSE_CREATED_AT);
        query.setLimit(Constant.QUERY_LIMIT);
        query.findObjects(getContext(), listener);
    }

    /**
     * 关注/取消关注
     *
     * @param user
     * @param me
     * @param isFollowed
     * @param listener
     */
    public static void followUser(final MUser user, final MUser me, boolean isFollowed, Response listener) {
        BmobRelation relation = new BmobRelation();
        if (isFollowed) {
            relation.add(user);
        } else {
            relation.remove(user);
        }
        me.setFollowRelation(relation);
        me.update(getContext(), listener.update());
    }

    /**
     * 个人正在关注的用户
     *
     * @param user
     * @param listener
     */
    public static void getMyFollowingUser(MUser user, FindListener<MUser> listener) {
        BmobQuery<MUser> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.FOLLOW_RELATION, new BmobPointer(user));
        query.setLimit(Constant.QUERY_LIMIT);
        query.order(Column.Base.REVERSE_CREATED_AT);
        query.findObjects(getContext(), listener);
    }

    /**
     * 查询用户是否赞帖
     *
     * @param user
     * @param post
     * @param listener
     */
    public static void isLikedPost(final MUser user, final MPost post, final FindListener<MPost> listener) {
//        BmobQuery<MUser> query = new BmobQuery<>();
//        query.addWhereRelatedTo(Column.Post.LIKE_RELATION, new BmobPointer(post));
//        query.findObjects(getContext(), listener);
        BmobQuery<MPost> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.LIKE_RELATION, new BmobPointer(user));
//        query.addWhereEqualTo(Column.Post.ID, post.getObjectId());
        query.findObjects(getContext(), listener);
    }

    /**
     * 是否关注用户
     *
     * @param user
     * @param follower 当前登陆用户
     * @param listener
     */
    public static void isFollowingUser(final MUser user, final MUser follower, final FindListener<MUser> listener) {
        BmobQuery<MUser> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.FOLLOW_RELATION, new BmobPointer(user));
        query.findObjects(getContext(), listener);
    }

    /**
     * 查询用户是否收藏该贴
     *
     * @param user
     * @param post
     */
    public static void isFavoredPost(final MUser user, final MPost post, final FindListener<MPost> listener) {
//        BmobQuery<MUser> query = new BmobQuery<>();
//        query.addWhereRelatedTo(Column.Post.FAVOR_RELATION, new BmobPointer(post));
//        query.findObjects(getContext(), listener);
        BmobQuery<MPost> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.FAV_RELATION, new BmobPointer(user));
        query.findObjects(getContext(), listener);
    }

    /**
     * 获取贴列表
     *
     * @param listener
     */
    public static void getPostList(final FindListener<MPost> listener, int page) {
        BmobQuery<MPost> query = new BmobQuery<>();
        query.include(Column.Post.AUTHOR);
        int num = Constant.QUERY_LIMIT;
        query.setSkip(page * num);
        query.setLimit(num);
        query.order(Column.Post.REVERSE_CREATED_AT);
//        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 先从缓存获取数据，如果没有，再从网络获取。
        query.findObjects(getContext(), listener);
    }

    /**
     * 获取帖子的评论列表
     *
     * @param post
     * @param listener
     */
    public static void getCommentList(MPost post, final FindListener<Comment> listener) {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.include(Column.Comment.FROM_USER + "," + Column.Comment.TO_USER);
        query.addWhereEqualTo(Column.Comment.POST, new BmobPointer(post));
        query.findObjects(getContext(), listener);
    }

    /**
     * 发帖
     *
     * @param author
     * @param content
     * @param photoList
     * @param visibility
     * @param listener
     */
    public static void publishPost(MUser author, String content, List<String> photoList, int visibility, final Response listener) {
        final MPost post = new MPost();
        post.setContent(content);
        post.setVisibility(visibility);
        post.setAuthor(author);
        post.setLikeCount(0);
        post.setFavorCount(0);

        final Context context = getContext();
        String[] photos = photoList.toArray(new String[photoList.size()]);
        Bmob.uploadBatch(context, photos, new UploadFilesListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls, boolean isDone) {
                if (isDone) {
                    //保存图片成功后设置post的图片
                    post.setPhotoList(urls);
                    AKLog.d("xxxxx", post.getPhotoList().toString());
                    //保存post
                    post.save(context, listener.save());
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                listener.onFailure(statuscode, errormsg);
            }
        });
    }


    /**
     * 注册
     *
     * @param account
     * @param password
     * @param listener
     */
    public static void register(String account, String password, Response listener) {
        MUser user = new MUser();
        user.setUsername(account);
        user.setPassword(password);
        user.setName(account);
        user.signUp(getContext(), listener.save());
    }

    /**
     * 登陆
     *
     * @param account
     * @param password
     * @param listener
     */
    public static void login(String account, String password, Response listener) {
        MUser user = new MUser();
        user.setUsername(account);
        user.setPassword(password);
        user.login(getContext(), listener.save());
    }

    private static Context getContext() {
        return AppContext.getInstance();
    }

    private MPostApi() {
    }


    /**
     * 获取@我的评论
     *
     * @param user
     * @param listener
     */
    public static void getMyCommentList(MUser user, FindListener<Comment> listener) {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo(Column.Comment.TO_USER, new BmobPointer(user));
        query.include(Column.Comment.FROM_USER + "," + Column.Comment.POST + "," + Column.Comment.TO_USER);
        query.setLimit(Constant.QUERY_LIMIT);
        query.order(Column.Base.REVERSE_CREATED_AT);
        query.findObjects(AppContext.getInstance(), listener);
    }
}
