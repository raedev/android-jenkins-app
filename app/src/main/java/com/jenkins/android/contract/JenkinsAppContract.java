package com.jenkins.android.contract;

import com.github.raedev.swift.mvp.IPresenter;
import com.github.raedev.swift.mvp.IPresenterView;
import com.jenkins.android.model.AppInfo;

import java.util.List;

/**
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface JenkinsAppContract {

    interface Presenter extends IPresenter {

    }

    interface View extends IPresenterView {

        /**
         * 加载成功
         * @param data
         */
        void onLoadData(List<AppInfo> data);

        /**
         * 加载失败
         * @param message
         */
        void onLoadError(String message);
    }
}
