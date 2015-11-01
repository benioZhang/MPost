package com.benio.mpost.app;

import android.app.Application;

import com.benio.mpost.R;
import com.benio.mpost.bean.MUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Application
 * Created by benio on 2015/10/12.
 */
public class AppContext extends Application {
    /** Application单例 */
    private static AppContext sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        //初始化Bmob
        Bmob.initialize(this, getString(R.string.bmob_key));
    }

    /**
     * @return Application实例
     */
    public static AppContext getInstance() {
        return sInstance;
    }

    /**
     * 注销
     */
    public void logout() {
        BmobUser.logOut(this);
    }

    /**
     * @return 用户信息
     */
    public MUser getUser() {
        return BmobUser.getCurrentUser(this, MUser.class);
    }
}
