package com.jenkins.android.api;

import com.jenkins.android.model.AppInfo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * 数据来源
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface IJenkinsDataSource {

    /**
     * query app list
     * @return list
     */
    Observable<List<AppInfo>> queryAppList();
}
