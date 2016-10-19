package com.shtoone.liqing.common;

import com.shtoone.liqing.BaseApplication;
import com.shtoone.liqing.utils.DirectoryUtils;

import java.io.File;

public class Constants {

    /**
     * 不允许new
     */
    private Constants() {
        throw new Error("Do not need instantiate!");
    }

    //SD卡路径
    public static final String PATH_DATA = DirectoryUtils.getDiskCacheDirectory(BaseApplication.mContext, "data").getAbsolutePath();
    public static final String PATH_CACHE = PATH_DATA + File.separator + "NetCache";

    //基地址
    public static final String BASE_URL = "http://192.168.10.35:8080/lqqms/";


    //登录地址
    public static final String LOGIN_URL = BASE_URL + "app.do?AppLogin&userName=%1&userPwd=%2&OSType=2";

    public static final String DOMAIN_1 = "shtoone.com";
    public static final String DOMAIN_2 = "sh-toone";
    public static final String ISFIRSTENTRY = "is_first_entry";
    public static final String ISFIRSTGUIDE = "is_first_guide";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final int DEFAULT_TIMEOUT = 5;


    //作为登录的参数，固定这个写法
    public static final String OSTYPE = "2";
    public static final String PRESS_AGAIN = "再按一次退出";
    public static final String ENCRYPT_KEY = "leguang";

    public static final String PARAMETERS = "parameters";
    public static final String USERGROUPID = "usergroupid";


    public static final String ABOUTAPP = "http://note.youdao.com/share/?id=37e5d8602c49af15d7589d7f91bd548b&type=note";
    public static final String ABOUTCOMPANY = "http://en.ccccltd.cn/ccccltd/";

}
