package com.jenkins.android.presenter;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.github.raedev.swift.rx.Composer;
import com.jenkins.android.api.ApiFactory;
import com.jenkins.android.contract.ApkDownloadListener;
import com.jenkins.android.model.AppInfo;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

/**
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public final class ApkDownloadManager extends FileDownloadListener {

    static final ApkDownloadManager sManager = new ApkDownloadManager();
    private final FileDownloader mDownloader;
    private List<ApkDownloadListener> mListeners = new ArrayList<>();

    public static ApkDownloadManager getInstance() {
        return sManager;
    }


    private ApkDownloadManager() {
        mDownloader = FileDownloader.getImpl();
    }

    public void download(@NonNull AppInfo item) {
        if (TextUtils.isEmpty(item.downloadUrl)) {
            for (ApkDownloadListener listener : mListeners) {
                listener.onDownloadError(item, "文件下载路径为空，请确保数据加载无误");
            }
            return;
        }
        Observable.create(emitter -> {
            File apk = getApkFile(item);
            if (apk.exists()) {
                // 文件已经下载
                AppUtils.installApp(apk.getPath());
                // 回调
                for (ApkDownloadListener listener : mListeners) {
                    listener.onDownloadFinish(item);
                }
                return;
            }
            BaseDownloadTask task = mDownloader.create(item.downloadUrl);
            task.setListener(this);
            task.setForceReDownload(false);
            task.setWifiRequired(true);
            task.setCallbackProgressMinInterval(500);
            task.setPath(getApkTempFile(item).getPath(), false);
            task.setTag(item);
            // query file length
            item.fileLength = ApiFactory.getInstance().queryContentLength(item.downloadUrl);
            item.downloadId = task.start();
            Log.d("rae", "download:" + item);

        }).compose(Composer.mainThread()).subscribe();

    }

    public void cancel(@NonNull AppInfo item) {
        mDownloader.pause(item.downloadId);
    }

    public void register(ApkDownloadListener listener) {
        mListeners.add(listener);
    }

    public void unregister(ApkDownloadListener listener) {
        mListeners.remove(listener);
    }

    // region 下载文件回调

    @Override
    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        Log.d("rae", "pending: " + task.getTag());

    }

    @Override
    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        AppInfo tag = (AppInfo) task.getTag();
        float current = soFarBytes * 0.01f;
        float total = tag.fileLength * 0.01f;
        float p = current / total * 100f;
        int progress = Math.round(p);
        Log.d("Rae", tag.title + "下载中, current=" + current + "; total= " + total + "; progress=" + progress);
        for (ApkDownloadListener listener : mListeners) {
            listener.onDownloadChanged(tag, progress);
        }
    }

    @Override
    protected void completed(BaseDownloadTask task) {
        AppInfo tag = (AppInfo) task.getTag();
        tag.filePath = task.getTargetFilePath();
        if (!FileUtils.isFileExists(tag.filePath)) {
            error(task, new FileNotFoundException("下载文件不存在：" + tag));
            return;
        }

        File file = getApkFile(tag);
        FileUtils.move(tag.filePath, file.getPath());
        tag.filePath = file.getPath();

        // 唤起安装
        Log.i("Rae", "下载完成: " + tag);
        AppUtils.installApp(file.getPath());
        // 回调
        for (ApkDownloadListener listener : mListeners) {
            listener.onDownloadFinish(tag);
        }
    }

    @Override
    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
        Log.w("Rae", "下载暂停: " + task);
    }

    @Override
    protected void error(BaseDownloadTask task, Throwable e) {
        Log.e("Rae", "下载出错：" + task.getTag(), e);
        for (ApkDownloadListener listener : mListeners) {
            AppInfo tag = (AppInfo) task.getTag();
            listener.onDownloadError(tag, e.getMessage());
        }

    }

    @Override
    protected void warn(BaseDownloadTask task) {
        Log.w("Rae", "下载警告: " + task.getTag());
    }


    // endregion

    /**
     * 下载完成的文件名
     * @param m
     * @return
     */
    public static File getApkFile(AppInfo m) {
        File dir = new File(Environment.getExternalStorageDirectory(), "Jenkins");
        FileUtils.createOrExistsDir(dir);
        String downloadUrl = m.downloadUrl;
        int index = downloadUrl.lastIndexOf("/");
        String fileName = downloadUrl.substring(index + 1);
        return new File(dir, fileName);
    }

    /**
     * 下载中的文件名
     * @param m
     * @return
     */
    public static File getApkTempFile(AppInfo m) {
        return new File(getApkFile(m).getPath() + ".jenkins");
    }


}
