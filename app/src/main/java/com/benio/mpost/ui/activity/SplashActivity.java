package com.benio.mpost.ui.activity;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.benio.mpost.R;
import com.benio.mpost.app.AppContext;
import com.benio.mpost.bean.MUser;
import com.benio.mpost.controller.UIHelper;

/**
 * 开机页面
 * Created by benio on 2015/10/22.
 */
public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    public int getContentResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initData() {
        super.initData();
        int mills = getResources().getInteger(R.integer.splash_mills);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MUser user = AppContext.getInstance().getUser();
                if (null == user) {
                    UIHelper.showLogin(SplashActivity.this);
                } else {
                    UIHelper.showMain(SplashActivity.this);
                }
                finish();
            }
        }, mills);
    }

    @Override
    protected void initView(@Nullable View view) {
        ImageView iv = (ImageView) findViewById(R.id.iv_splash);
//        ImageLoader.getInstance(this).load(iv, R.mipmap.splash_bg);
    }
}
