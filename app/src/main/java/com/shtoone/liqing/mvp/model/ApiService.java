package com.shtoone.liqing.mvp.model;

import com.shtoone.liqing.mvp.model.bean.UserInfoBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public interface ApiService {

    @GET("app.do?AppLogin")
    Call<UserInfoBean> login(@Query("userName") String userName, @Query("userPwd") String userPwd, @Query("OSType") String OSType);

}
