package com.benio.mpost.ui.fragment;

import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;

import com.benio.mpost.R;
import com.benio.mpost.app.AppManager;
import com.benio.mpost.controller.MPostApi;
import com.benio.mpost.interf.impl.ResponseListener;
import com.benio.mpost.util.AKDialog;
import com.benio.mpost.util.AKToast;
import com.benio.mpost.util.AKView;
import com.benio.mpost.util.ErrorLog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 注册
 * Created by benio on 2015/10/10.
 */
public class RegisterFragment extends BaseFragment {
    /** 账号输入Layout */
    @Bind(R.id.til_account)
    TextInputLayout mAccountInputLayout;
    /** 密码输入Layout */
    @Bind(R.id.til_pwd)
    TextInputLayout mPwdInputLayout;

    @Override
    public int getContentResource() {
        return R.layout.fragment_register;
    }

    @Override
    protected void initView(@Nullable View view) {
    }

    /** 点击注册按钮事件 */
    @OnClick(R.id.btn_register)
    protected void onRegisterEvent() {
        if (!validateInput()) {
            return;
        }

        String account = AKView.getText(mAccountInputLayout);
        String password = AKView.getText(mPwdInputLayout);

        MPostApi.register(account, password, new ResponseListener() {
            @Override
            public void onSuccess() {
                AKToast.show(getActivity(), R.string.info_register_success);
                //finish this activity
                AppManager.getInstance().finishActivity(getActivity());
            }

            @Override
            public void onFailure(int i, String s) {
                ErrorLog.log(i, s);
                AKDialog.getMessageDialog(getActivity(), getString(R.string.info_register_failed)).show();
            }
        });
    }

    /**
     * 校验账号密码合法性
     *
     * @return true:合法；false:非法
     */
    private boolean validateInput() {
        //校验账号
        if (AKView.isEmpty(mAccountInputLayout)) {
            mAccountInputLayout.setError(getString(R.string.info_account_empty));
            return false;
        } else {
            mAccountInputLayout.setError(null);
        }

        //校验密码
        if (AKView.isEmpty(mPwdInputLayout)) {
            mPwdInputLayout.setError(getString(R.string.info_password_empty));
            return false;
        } else {
            mPwdInputLayout.setError(null);
        }

        return true;
    }
}
