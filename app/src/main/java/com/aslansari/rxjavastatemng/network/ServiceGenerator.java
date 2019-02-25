package com.aslansari.rxjavastatemng.network;

import android.support.annotation.Nullable;

import com.aslansari.rxjavastatemng.BuildConfig;
import com.aslansari.rxjavastatemng.StateManageApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by etiennelawlor on 6/14/15.
 */
public class ServiceGenerator {

    // region Constants
    private static final int DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    // endregion
    private static final Gson gson = new GsonBuilder()
            .create();

    private static Retrofit.Builder retrofitBuilder
            = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));

    private static OkHttpClient defaultOkHttpClient
            = new OkHttpClient.Builder()
                .cache(getCache())
                .build();

    // No need to instantiate this class.
    private ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        return createService(serviceClass, baseUrl, null,null);
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl, Interceptor networkInterceptor, @Nullable Interceptor cacheInterceptor) {
        OkHttpClient.Builder okHttpClientBuilder = defaultOkHttpClient.newBuilder();

        if(networkInterceptor != null){
            okHttpClientBuilder.addNetworkInterceptor(networkInterceptor);
        }

        if (cacheInterceptor != null){
            okHttpClientBuilder.addNetworkInterceptor(cacheInterceptor);
        }

        OkHttpClient modifiedOkHttpClient = okHttpClientBuilder
                .addInterceptor(getHttpLoggingInterceptor())
                .build();

        retrofitBuilder.client(modifiedOkHttpClient);
        retrofitBuilder.baseUrl(baseUrl);



        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    private static Cache getCache() {

        Cache cache = null;
        // Install an HTTP cache in the application cache directory.
        try {
            File cacheDir = new File(StateManageApplication.getCacheDirectory(), "apiResponses");
            cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }

    private static HttpLoggingInterceptor getHttpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return httpLoggingInterceptor;
    }
}

