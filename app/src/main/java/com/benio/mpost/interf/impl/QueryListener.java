package com.benio.mpost.interf.impl;

import cn.bmob.v3.listener.FindListener;

/**
 * Created by benio on 2015/10/22.
 */
public abstract class QueryListener<T> extends FindListener<T> {

    public abstract void onFailure(int code, String msg);

    @Override
    public final void onError(int i, String s) {
        onFailure(i, s);
    }
}
