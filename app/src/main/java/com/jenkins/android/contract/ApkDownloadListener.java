package com.jenkins.android.contract;

import com.jenkins.android.model.AppInfo;

/**
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface ApkDownloadListener {

    /**
     * 下载变化时回调
     * @param m 下载任务
     * @param progress 进度
     */
    void onDownloadChanged(AppInfo m, int progress);

    /**
     * 下载完成
     * @param m 任务
     */
    void onDownloadFinish(AppInfo m);

    /**
     * 下载错误时候回调
     * @param m 任务
     * @param message 错误信息
     */
    void onDownloadError(AppInfo m, String message);
}
