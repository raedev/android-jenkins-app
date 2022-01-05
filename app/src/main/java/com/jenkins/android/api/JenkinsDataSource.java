package com.jenkins.android.api;

import com.blankj.utilcode.util.ResourceUtils;
import com.github.raedev.swift.utils.JsonUtils;
import com.github.raedev.swift.utils.TextUtils;
import com.jenkins.android.model.AppInfo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * Jenkins 数据来源
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class JenkinsDataSource implements IJenkinsDataSource {

    @Override
    public Observable<List<AppInfo>> queryAppList() {
        return Observable.create(emitter -> {
            // 数据来源从asset文件中获取
            String json = ResourceUtils.readAssets2String("apps.json");
            if (TextUtils.isEmpty(json)) {
                emitter.onError(new NullPointerException("apps.json in assets dir not found."));
            }
            List<AppInfo> data = JsonUtils.toList(json, AppInfo.class);
            emitter.onNext(data);
            emitter.onComplete();
        });
    }
}
