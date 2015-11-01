package com.benio.mpost.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * popupWindow基类
 * Created by benio on 2015/9/5.
 */
public class BasePopupWindow extends PopupWindow {
    /**
     * popupWindow显示时窗口alpha值
     */
    private static final float WINDOW_SHOWING_ALPHA = 0.3F;
    /**
     * popupWindow默认窗口alpha值
     */
    private static final float WINDOW_DEFAULT_ALPHA = 1F;
    /**
     * 当前窗口显示alpha值
     */
    private float mShowingAlpha = WINDOW_SHOWING_ALPHA;

    public BasePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height, true);
        init(contentView);
    }

    /**
     * 设置popupWindow显示时窗口alpha值
     */
    public void setWindowShowingAlpha(float alpha) {
        this.mShowingAlpha = alpha;
    }

    /**
     * @return popupWindow显示时窗口alpha值
     */
    public float getWindowShowingAlpha() {
        return mShowingAlpha;
    }

    public View findById(int id) {
        return null == getContentView() ? null : getContentView().findViewById(id);
    }

    public Context getContext() {
        return null == getContentView() ? null : getContentView().getContext();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        changeWindowAlpha(WINDOW_DEFAULT_ALPHA);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        changeWindowAlpha(mShowingAlpha);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        changeWindowAlpha(mShowingAlpha);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        changeWindowAlpha(mShowingAlpha);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        changeWindowAlpha(mShowingAlpha);
    }

    protected void initView(View contentView) {
    }

    protected void initData() {
    }

    private void init(View contentView) {
        //点击空白处dismiss
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        initData();
        initView(contentView);
    }

    private void changeWindowAlpha(float alpha) {
        Context context = getContext();
        if (context != null && context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.alpha = alpha;
            window.setAttributes(params);
        }
    }
}
