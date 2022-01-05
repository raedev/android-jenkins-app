package com.jenkins.android;

import android.app.Application;

import com.github.raedev.swift.AppSwift;
import com.jenkins.android.presenter.ApkDownloadCreator;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;

/**
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class JenkinsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppSwift.init(this);
        DownloadMgrInitialParams.InitCustomMaker maker = FileDownloader.setupOnApplicationOnCreate(this);
        maker.connectionCreator(new ApkDownloadCreator());
    }
}
