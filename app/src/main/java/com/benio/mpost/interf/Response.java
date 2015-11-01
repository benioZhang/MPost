package com.benio.mpost.interf;

import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 网络回调接口
 * Created by benio on 2015/10/12.
 */
public interface Response {
    SaveListener save();

    UpdateListener update();

    DeleteListener delete();

    void onFailure(int code, String msg);

    void onSuccess();
}
