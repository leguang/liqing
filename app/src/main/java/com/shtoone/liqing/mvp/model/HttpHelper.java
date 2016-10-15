package com.shtoone.liqing.mvp.model;

import com.shtoone.liqing.BuildConfig;
import com.shtoone.liqing.common.Constants;
import com.socks.library.KLog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class HttpHelper {

    private static final String TAG = HttpHelper.class.getSimpleName();
    public static final String BASE_URL = Constants.BASE_URL;
    private static OkHttpClient mOkHttpClient;
    private static Retrofit mRetrofit;
    private volatile static HttpHelper INSTANCE;

    //构造方法私有
    private HttpHelper() {
        initRetrofit();
    }

    //获取单例
    public static HttpHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpHelper();
                }
            }
        }
        return INSTANCE;
    }

    private void initOkHttpClient() {
        if (null == mOkHttpClient) {
            //手动创建一个OkHttpClient并设置超时时间
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (builder.interceptors() != null) {
                builder.interceptors().clear();
            }
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request mRequest = chain.request();
                    //打印url信息
                    KLog.e(mRequest.toString());

                    Response mResponse = chain.proceed(mRequest);
                    //打印响应信息
                    KLog.e(mResponse.toString());
                    KLog.json(mResponse.body().string());

                    return chain.proceed(mRequest);
                }
            })
                    .retryOnConnectionFailure(true)
                    .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

            //DEBUG模式下配Log
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }
            mOkHttpClient = builder.build();
        }
    }

    private void initRetrofit() {
        if (null == mRetrofit) {
            initOkHttpClient();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(mOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
    }

    public ApiService initService() {
        return mRetrofit.create(ApiService.class);
    }
}
