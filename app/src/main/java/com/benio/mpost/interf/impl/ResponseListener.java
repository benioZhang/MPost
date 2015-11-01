package com.benio.mpost.interf.impl;

import com.benio.mpost.interf.Response;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 网络回调接口
 * Created by benio on 2015/10/11.
 */
public abstract class ResponseListener implements Response {

    public final SaveListener save() {
        return new SaveListener() {
            @Override
            public void onSuccess() {
                //回调ResponseListener自己的方法
                ResponseListener.this.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                ResponseListener.this.onFailure(i, s);
            }
        };
    }

    public final UpdateListener update() {
        return new UpdateListener() {
            @Override
            public void onSuccess() {
                //回调ResponseListener自己的方法
                ResponseListener.this.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                ResponseListener.this.onFailure(i, s);
            }
        };
    }

    public final DeleteListener delete() {
        return new DeleteListener() {
            @Override
            public void onSuccess() {
                ResponseListener.this.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                ResponseListener.this.onFailure(i, s);
            }
        };
    }
//
//    public FindListener find() {
//        return null;
//    }
}
