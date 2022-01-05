package com.jenkins.android.api;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.jenkins.android.BuildConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author RAE
 * @date 2021/12/31
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public final class ApiFactory {

    private static final ApiFactory sFactory = new ApiFactory();

    public static ApiFactory getInstance() {
        return sFactory;
    }

    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;


    public ApiFactory() {
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        HttpLoggingInterceptor logger = new HttpLoggingInterceptor(msg -> Log.d("Jenkins.API", msg));
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(logger)
                .proxy(getProxy());


        String baseUrl = SPUtils.getInstance().getString("Jenkins.BaseUrl", BuildConfig.JENKINS_URL);
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("Jenkins base url can't be null");
        }
        mOkHttpClient = builder.build();
        mRetrofit = new Retrofit.Builder().client(mOkHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    /**
     * 重新加载
     */
    public void reload() {
        if (mRetrofit == null && mOkHttpClient == null) {
            init();
            return;
        }
        mOkHttpClient.connectionPool().evictAll();
        init();
    }

    /**
     * 仅仅获取HTTP Content长度，该方法需运行在线程中
     * @return 长度
     */
    public long queryContentLength(String url) throws IOException {
        Proxy proxy = getProxy();
        URLConnection connection = proxy == null ? new URL(url).openConnection() : new URL(url).openConnection(proxy);
        connection.setRequestProperty("Accept-Encoding", "*");
        connection.connect();
        return connection.getContentLength();
    }


    public IJenkinsApi getJenkinsApi() {
        return mRetrofit.create(IJenkinsApi.class);
    }

    @Nullable
    public Proxy getProxy() {
        // 添加HTTP代理
        String proxyUrl = SPUtils.getInstance().getString("Jenkins.Proxy", BuildConfig.JENKINS_PROXY);
        if (!TextUtils.isEmpty(proxyUrl)) {
            String[] urls = proxyUrl.split(":");
            if (urls.length != 2) {
                throw new IllegalArgumentException("Proxy url is wrong, the url should like 10.0.01:8080");
            }
            SocketAddress sa = new InetSocketAddress(urls[0], com.github.raedev.swift.utils.TextUtils.parseInt(urls[1]));
            return new Proxy(Proxy.Type.HTTP, sa);
        }
        return null;
    }
}
