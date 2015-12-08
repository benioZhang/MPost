package com.benio.mpost.controller;

import android.content.Context;

import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.Comment;
import com.benio.mpost.bean.ForbiddenUser;
import com.benio.mpost.bean.MPost;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.bean.PostVisibility;
import com.benio.mpost.consts.Column;
import com.benio.mpost.consts.Constant;
import com.benio.mpost.interf.Response;
import com.benio.mpost.interf.impl.ResponseListener;
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

    public static void updatePostVisiblity(MPost post, boolean visible, final Response listener) {
        post.setVisibility(visible ? PostVisibility.PUBLIC.getVisibility() : PostVisibility.PRIVATE.getVisibility());
        post.update(getContext(), listener.update());
    }

    /**
     * 查询用户是否被屏蔽
     *
     * @param me
     * @param listener
     */
    public static void isForbidden(MUser me, FindListener<ForbiddenUser> listener) {
        BmobQuery<ForbiddenUser> query = new BmobQuery<>();
        query.addWhereEqualTo(Column.ForbiddenUser.USER, new BmobPointer(me));
        query.include(Column.ForbiddenUser.USER);
        query.findObjects(getContext(), listener);
    }

    /**
     * 获取关注用户列表
     *
     * @param me
     * @param listener
     * @param page
     */
    public static void getFollowingUserList(MUser me, FindListener<MUser> listener, int page) {
        BmobQuery<MUser> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.FOLLOW_RELATION, new BmobPointer(me));
        query.include(Column.Post.AUTHOR);
        query.setSkip(page * Constant.QUERY_LIMIT);
        query.setLimit(Constant.QUERY_LIMIT);
        query.findObjects(getContext(), listener);
    }

    /**
     * 获取@我的评论/我发的评论
     *
     * @param me
     * @param listener
     */
    public static void getMyCommentList(MUser me, FindListener<Comment> listener, int page) {
        BmobQuery<Comment> q1 = new BmobQuery<>();
        q1.addWhereEqualTo(Column.Comment.TO_USER, new BmobPointer(me));

        BmobQuery<Comment> q2 = new BmobQuery<>();
        q2.addWhereEqualTo(Column.Comment.FROM_USER, new BmobPointer(me));

        List<BmobQuery<Comment>> queries = new ArrayList<>(2);
        queries.add(q1);
        queries.add(q2);

        BmobQuery<Comment> query = new BmobQuery<>();
        query.or(queries);
        query.include(Column.Comment.FROM_USER + "," + Column.Comment.POST + "," + Column.Comment.TO_USER);
        query.setSkip(page * Constant.QUERY_LIMIT);
        query.setLimit(Constant.QUERY_LIMIT);
        query.order(Column.Base.REVERSE_CREATED_AT);
        query.findObjects(getContext(), listener);
    }

    /**
     * 获取点赞榜列表
     *
     * @param listener
     */
    public static void getLikeRankList(FindListener<MPost> listener) {
        BmobQuery<MPost> query = new BmobQuery<>();
        query.include(Column.Post.AUTHOR);
        query.setLimit(Constant.LIKE_RANK_NUM);
        query.order(Column.Post.REVERSE_LIKE_COUNT + "," + Column.Post.REVERSE_CREATED_AT);
        query.addWhereNotEqualTo(Column.Post.VISIBILITY, PostVisibility.PRIVATE.getVisibility());
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
        query.addWhereNotEqualTo(Column.Post.VISIBILITY, PostVisibility.PRIVATE.getVisibility());
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
        MUser me = AppContext.getInstance().getUser();
        if (!me.equals(user)) {
            query.addWhereNotEqualTo(Column.Post.VISIBILITY, PostVisibility.PRIVATE.getVisibility());
        }
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
    public static void likePost(final MUser user, final MPost post, final boolean isLiked, final Response listener) {
        BmobRelation relation = new BmobRelation();
        if (isLiked) {
            relation.add(post);
        } else {
            relation.remove(post);
        }
        user.setLikeRelation(relation);
        user.update(getContext(), new ResponseListener() {

            @Override
            public void onFailure(int code, String msg) {
                listener.onFailure(code, msg);
            }

            @Override
            public void onSuccess() {
                updatePostLikedCount(post, isLiked, listener);
            }
        }.update());
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

    /**
     * 收藏帖/收藏赞
     *
     * @param user
     * @param post
     * @param listener
     */
    public static void favorPost(final MUser user, final MPost post, final boolean isFavored, final Response listener) {
        BmobRelation favorRelation = new BmobRelation();
        if (isFavored) {
            favorRelation.add(post);
        } else {
            favorRelation.remove(post);
        }
        user.setFavRelation(favorRelation);
        user.update(getContext(), new ResponseListener() {

            @Override
            public void onFailure(int code, String msg) {
                listener.onFailure(code, msg);
            }

            @Override
            public void onSuccess() {
                /** 更新post表fav数 **/
                updatePostFavoredCount(post, isFavored, listener);
            }
        }.update());
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
     * 显示个人点赞post
     *
     * @param user
     * @param listener
     */
    public static void getMyLikePost(MUser user, FindListener<MPost> listener, int page) {
        BmobQuery<MPost> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.LIKE_RELATION, new BmobPointer(user));
        query.addWhereNotEqualTo(Column.Post.VISIBILITY, PostVisibility.PRIVATE.getVisibility());
        query.include(Column.Post.AUTHOR);
        query.order(Column.Post.REVERSE_CREATED_AT);
        int num = Constant.QUERY_LIMIT;
        query.setSkip(page * num);
        query.setLimit(num);
        query.findObjects(getContext(), listener);
    }

    /**
     * 显示个人收藏post
     *
     * @param user
     * @param listener
     */
    public static void getMyFavouritePost(MUser user, FindListener<MPost> listener, int page) {
        BmobQuery<MPost> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.FAV_RELATION, new BmobPointer(user));
        query.addWhereNotEqualTo(Column.Post.VISIBILITY, PostVisibility.PRIVATE.getVisibility());
        query.include(Column.Post.AUTHOR);
        query.order(Column.Post.REVERSE_CREATED_AT);
        int num = Constant.QUERY_LIMIT;
        query.setSkip(page * num);
        query.setLimit(num);
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
     * 查询用户是否赞帖
     *
     * @param user
     * @param post
     * @param listener
     */
    public static void isLikedPost(final MUser user, final MPost post, final FindListener<MPost> listener) {
        BmobQuery<MPost> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.LIKE_RELATION, new BmobPointer(user));
        query.findObjects(getContext(), listener);
    }

    /**
     * 是否关注用户
     *
     * @param user
     * @param me       当前登陆用户
     * @param listener
     */
    public static void isFollowingUser(final MUser user, final MUser me, final FindListener<MUser> listener) {
        BmobQuery<MUser> query = new BmobQuery<>();
        query.addWhereRelatedTo(Column.User.FOLLOW_RELATION, new BmobPointer(me));
        query.findObjects(getContext(), listener);
    }

    /**
     * 查询用户是否收藏该贴
     *
     * @param user
     * @param post
     */
    public static void isFavoredPost(final MUser user, final MPost post, final FindListener<MPost> listener) {
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
        //去除私有的贴
        query.addWhereNotEqualTo(Column.Post.VISIBILITY, PostVisibility.PRIVATE.getVisibility());
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
     * 搜索贴列表
     * @param content
     * @param listener
     */
    public static void searchPosts(String content,FindListener<MPost> listener){
        BmobQuery<MPost> query = new BmobQuery<>();
        query.include(Column.Post.AUTHOR);
        query.addWhereContains(Column.Post.CONTENT, content);
        query.order(Column.Post.REVERSE_CREATED_AT);
        query.findObjects(getContext(), listener);
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
        user.setCanNotPost(false);
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

}
