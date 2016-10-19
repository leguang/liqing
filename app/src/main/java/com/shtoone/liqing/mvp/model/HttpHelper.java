package com.shtoone.liqing.mvp.model;

import com.shtoone.liqing.BaseApplication;
import com.shtoone.liqing.BuildConfig;
import com.shtoone.liqing.common.Constants;
import com.shtoone.liqing.utils.NetworkUtils;
import com.socks.library.KLog;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
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

    /**
     * 初始化OkHttp
     */
    private void initOkHttpClient() {
        if (null == mOkHttpClient) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (builder.interceptors() != null) {
                builder.interceptors().clear();
            }

            File cacheFile = new File(Constants.PATH_CACHE);
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

            Interceptor cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();

                    //打印url信息
                    KLog.e(request.toString());

                    Response mResponse = chain.proceed(request);
                    //打印响应信息
                    KLog.e(mResponse.toString());
                    KLog.json(mResponse.body().string());


                    if (!NetworkUtils.isConnected(BaseApplication.mContext)) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    int tryCount = 0;

                    Response response = chain.proceed(request);

                    while (!response.isSuccessful() && tryCount < 3) {

                        KLog.e("interceptRequest is not successful - :{}", tryCount);

                        tryCount++;

                        // retry the request
                        response = chain.proceed(request);
                    }

                    if (NetworkUtils.isConnected(BaseApplication.mContext)) {
                        int maxAge = 0;
                        // 有网络时, 不缓存, 最大保存时长为0
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Pragma")
                                .build();
                    } else {
                        // 无网络时，设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .removeHeader("Pragma")
                                .build();
                    }
                    return response;
                }
            };

            //设置缓存
            builder.addNetworkInterceptor(cacheInterceptor);
            builder.addInterceptor(cacheInterceptor);
            builder.cache(cache);
            //设置超时
            builder.connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            //错误重连
            builder.retryOnConnectionFailure(true);

            //DEBUG模式下配Log
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(loggingInterceptor);
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
