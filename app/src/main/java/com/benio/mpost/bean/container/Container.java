package com.benio.mpost.bean.container;

import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import com.benio.mpost.R;
import com.benio.mpost.ui.fragment.AlbumFragment;
import com.benio.mpost.ui.fragment.ForbiddenUserFragment;
import com.benio.mpost.ui.fragment.LikeRankFragment;
import com.benio.mpost.ui.fragment.LoginFragment;
import com.benio.mpost.ui.fragment.PostDetailFragment;
import com.benio.mpost.ui.fragment.PostVisibilityFragment;
import com.benio.mpost.ui.fragment.PublishPostFragment;
import com.benio.mpost.ui.fragment.RegisterFragment;
import com.benio.mpost.ui.fragment.SearchFragment;
import com.benio.mpost.ui.fragment.UserAdminFragment;

/**
 * FragmentContainer
 * Created by benio on 2015/10/11.
 */
public class Container {
    /** 默认值 */
    //public static final int NONE = -1;
    /**
     * 登陆
     */
    public static final int LOGIN = 1;
    /**
     * 注册
     */
    public static final int REGISTER = 2;
    /**
     * 相册
     */
    public static final int ALBUM = 3;
    /**
     * 发帖
     */
    public static final int PUBLISH_POST = 4;
    /**
     * 搜索
     */
    public static final int SEARCH = 5;
    /**
     * 设置帖子可见性
     */
    public static final int POST_VISIBLE = 6;
    /**
     * 帖子详情
     */
    public static final int POST_DETAIL = 7;
    /**
     * 点赞榜
     */
    public static final int RANKING_LIST = 8;

    /**
     * 查看禁止发帖用户
     */
    public static final int FORBIDDEN_USER = 9;

    /**
     * 用户管理，用于屏蔽用户
     */
    public static final int USER_ADMIN = 10;

    /**
     * 防止使用不是Container的id
     */
    @IntDef({LOGIN, REGISTER, ALBUM, PUBLISH_POST, SEARCH, POST_VISIBLE, POST_DETAIL,
            RANKING_LIST, FORBIDDEN_USER, USER_ADMIN})
    public @interface ContainerType {
    }

    /**
     * fragment class
     */
    private Class<? extends Fragment> clazz;
    /**
     * 页面title
     */
    private int title;
    /**
     * 为了可以复用同一个Container
     */
    private static Container container = new Container();

    public int getTitle() {
        return title;
    }

    public boolean hasTitle() {
        return title != 0;
    }

    public Class<? extends Fragment> getClazz() {
        return clazz;
    }

    public static Container getContainerById(@ContainerType int id) {
        switch (id) {
            case LOGIN:
                container.clazz = LoginFragment.class;
                container.title = R.string.title_login;
                break;

            case REGISTER:
                container.clazz = RegisterFragment.class;
                container.title = R.string.title_register;
                break;

            case ALBUM:
                container.clazz = AlbumFragment.class;
                container.title = R.string.title_album;
                break;

            case PUBLISH_POST:
                container.clazz = PublishPostFragment.class;
                container.title = R.string.title_publish_post;
                break;

            case SEARCH:
                container.clazz = SearchFragment.class;
//                container.title = R.string.title_search;
                break;

            case POST_VISIBLE:
                container.clazz = PostVisibilityFragment.class;
                container.title = R.string.title_post_visible;
                break;

            case POST_DETAIL:
                container.clazz = PostDetailFragment.class;
                container.title = R.string.title_post_detail;
                break;

            case RANKING_LIST:
                container.clazz = LikeRankFragment.class;
                container.title = R.string.title_like_rank;
                break;

            case FORBIDDEN_USER:
                container.clazz = ForbiddenUserFragment.class;
                container.title = R.string.title_forbidden_user;
                break;

            case USER_ADMIN:
                container.clazz = UserAdminFragment.class;
                container.title = R.string.title_user_admin;
                break;

            default:
                break;
        }

        return container;
    }

    private Container() {
    }

    private Container(Class<? extends Fragment> clazz, @StringRes int title) {
        this.clazz = clazz;
        this.title = title;
    }
}
