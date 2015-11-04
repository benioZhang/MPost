package com.benio.mpost.consts;

/**
 * 列名
 * Created by benio on 2015/10/11.
 */
public final class Column {

    public interface User extends Base {
        String NAME = "name";
        String PROTRAIT = "portraitUrl";
        String FOLLOW_RELATION = "followRelation";
        String FAV_RELATION = "favRelation";
        String LIKE_RELATION = "likeRelation";
    }

    public interface Post extends Base {
        String CONTENT = "content";
        String PHOTOS = "photoList";
        String AUTHOR = "author";
        String LIKE_COUNT = "likeCount";
        String FAVOR_COUNT = "favorCount";
//        String LIKE_RELATION = "likeRelation";
//        String FAVOR_RELATION = "favorRelation";
        String VISIBILITY = "visibility";
    }

    public interface Base {
        String ID = "objectId";
        String CREATED_AT = "createdAt";
        String UPDATED_AT = "updatedAt";
        String REVERSE_CREATED_AT = "-createdAt";
    }

    public interface Comment extends Base {
        String FROM_USER = "fromUser";
        String TO_USER = "toUser";
        String CONTENT = "content";
        String POST = "post";
    }

}
