package com.jenkins.android.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.github.raedev.swift.mvp.BasePresenter;
import com.jenkins.android.api.ApiFactory;
import com.jenkins.android.api.IJenkinsApi;
import com.jenkins.android.api.IJenkinsDataSource;
import com.jenkins.android.api.JenkinsDataSource;
import com.jenkins.android.contract.JenkinsAppContract;
import com.jenkins.android.model.AppInfo;
import com.jenkins.android.model.JobBean;
import com.jenkins.android.model.JobDetailBean;

import java.text.Collator;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;

/**
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class JenkinsAppPresenter extends BasePresenter<JenkinsAppContract.View> implements JenkinsAppContract.Presenter {

    final IJenkinsApi mJenkinsApi;
    final IJenkinsDataSource mJenkinsDataSource = new JenkinsDataSource();

    public JenkinsAppPresenter(@NonNull JenkinsAppContract.View view) {
        super(view);
        mJenkinsApi = ApiFactory.getInstance().getJenkinsApi();
    }

    @Override
    public void loadData() {
        Log.d("rae", "加载数据");
        Observable.create((ObservableOnSubscribe<List<AppInfo>>) emitter -> {
            List<AppInfo> data = mJenkinsDataSource.queryAppList().blockingFirst();
            for (AppInfo item : data) {
                // query job
                JobBean job = mJenkinsApi.queryJob(item.jobName).blockingFirst();
                // query detail
                JobDetailBean detail = mJenkinsApi.queryJobDetail(item.jobName, String.valueOf(job.getLastSuccessBuildNumber())).blockingFirst();
                item.buildNumber = detail.id;
                item.log = detail.getLog();
                item.downloadUrl = detail.getDownloadUrl();
                item.buildDate = detail.getBuildDate();
                item.currentVersionName = AppUtils.getAppVersionName(item.packageName);
                item.hasDownload = ApkDownloadManager.getApkFile(item).exists();
            }
            Collections.sort(data, (o1, o2) -> Collator.getInstance().compare(o1.title, o2.title));
            emitter.onNext(data);
            emitter.onComplete();

        }).compose(uiThread()).subscribe(data -> {
            getView().onLoadData(data);
        }, error -> {
            Log.e("Jenkins", error.getMessage(), error);
            getView().onLoadError(error.getMessage());
        });
    }

}
