package com.jenkins.android.model;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.TimeUtils;

import java.util.List;

/**
 * @author RAE
 * @date 2021/12/31
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class JobDetailBean {

    public String url;
    public String id;
    public long timestamp;
    public List<JobArtifacts> artifacts;
    public JobChangeSet changeSet;

    /**
     * 获取日志信息
     * @return
     */
    public String getLog() {
        if (changeSet == null || changeSet.items == null) {
            return "无日志信息";
        }
        StringBuilder sb = new StringBuilder();
        for (JobChangeItem item : changeSet.items) {
            sb.append(item.toString());
        }
        return sb.toString();
    }

    /**
     * 获取下载路径
     */
    @Nullable
    public String getDownloadUrl() {
        if (artifacts == null || url == null || artifacts.size() <= 0) {
            return null;
        }
        for (JobArtifacts artifact : artifacts) {
            if (artifact.fileName.endsWith("apk")) {
                return url + "artifact/" + artifact.relativePath;
            }
        }
        return null;
    }

    /**
     * 构建日期
     */
    public String getBuildDate() {
        return TimeUtils.millis2String(timestamp);
    }
}
