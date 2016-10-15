package com.shtoone.liqing.mvp.presenter;

import com.shtoone.liqing.BaseApplication;
import com.shtoone.liqing.common.Constants;
import com.shtoone.liqing.mvp.contract.LoginContract;
import com.shtoone.liqing.mvp.model.HttpHelper;
import com.shtoone.liqing.mvp.model.bean.UserInfoBean;
import com.shtoone.liqing.mvp.presenter.base.BasePresenter;
import com.socks.library.KLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author：leguang on 2016/10/14 0014 13:17
 * Email：langmanleguang@qq.com
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();
    private UserInfoBean mUserInfoBean;


    public LoginPresenter(LoginContract.View mView) {
        super(mView);
    }

    @Override
    public void login(String username, String password) {
        KLog.e("登录……………………");
        HttpHelper.getInstance().initService().login(username, password, Constants.OSTYPE).enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().isSuccess()) {
                        BaseApplication.mUserInfoBean = mUserInfoBean = response.body();
                        getView().savaData();
                        initParameters();
                        getView().setSuccessMessage("登录成功");
                        if ("GL".equals(mUserInfoBean.getType())) {
                            //进入管理层界面
                            getView().go2Main();

                        } else if ("SG".equals(mUserInfoBean.getType())) {
                            //进入施工层界面
                            switch (mUserInfoBean.getUserRole()) {
                                case "1":
                                    break;
                                case "3":
                                    break;
                            }
                        }

                    } else {
                        getView().setErrorMessage("用户名或密码错误");
                    }
                } else {
                    getView().setErrorMessage("服务器异常");
                }
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                //还是得把Context拿过来
                getView().setErrorMessage("访问异常");
            }
        });

    }

    private void initParameters() {
        BaseApplication.mParametersBean.userGroupID = mUserInfoBean.getDepartId();
        BaseApplication.mDepartmentBean.departmentID = mUserInfoBean.getDepartId();
        BaseApplication.mDepartmentBean.departmentName = mUserInfoBean.getDepartName();
    }
}
