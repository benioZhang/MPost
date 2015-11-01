package com.benio.mpost.widget;

import android.content.Context;
import android.view.View;

import com.benio.mpost.R;
import com.benio.mpost.bean.ImageFolder;

import java.util.List;

/**
 * Created by benio on 2015/10/28.
 */
public class CommentPopupWindow extends BasePopupWindow {

    public static CommentPopupWindow newInstance(Context context, int width, int height, List<ImageFolder> datas) {
        View contentView = View.inflate(context, R.layout.include_comment_input, null);
        CommentPopupWindow popupWindow = new CommentPopupWindow(contentView, width, height);
        return popupWindow;
    }

    private CommentPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        setWindowShowingAlpha(1);
    }



}
