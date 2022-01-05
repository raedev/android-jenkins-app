package com.jenkins.android.presenter;

import com.jenkins.android.api.ApiFactory;
import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadHelper;

import java.io.IOException;

/**
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class ApkDownloadCreator implements FileDownloadHelper.ConnectionCreator {

    private FileDownloadUrlConnection.Configuration mConfiguration;

    public ApkDownloadCreator() {
        mConfiguration = new FileDownloadUrlConnection.Configuration();
        mConfiguration.proxy(ApiFactory.getInstance().getProxy());
        mConfiguration.connectTimeout(30000);
        mConfiguration.readTimeout(5 * 60 * 1000);
    }

    @Override
    public FileDownloadConnection create(String url) throws IOException {
        return new FileDownloadUrlConnection(url, mConfiguration);
    }
}
