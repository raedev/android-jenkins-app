package com.jenkins.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * @author RAE
 * @date 2021/12/30
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class AppInfo implements Parcelable {
    public String title;
    public String icon;
    public String jobName;
    public String packageName;

    // region 从Jenkins获取赋值的字段

    public String buildNumber;
    public String log;
    public String downloadUrl;
    public String buildDate;
    public String filePath;
    public long fileLength;
    public int downloadId;
    public int progress;
    public String currentVersionName;
    public boolean hasDownload;

    // endregion

    protected AppInfo(Parcel in) {
        title = in.readString();
        icon = in.readString();
        jobName = in.readString();
        packageName = in.readString();
        buildNumber = in.readString();
        log = in.readString();
        downloadUrl = in.readString();
        buildDate = in.readString();
        filePath = in.readString();
        fileLength = in.readLong();
        downloadId = in.readInt();
        progress = in.readInt();
        currentVersionName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(icon);
        dest.writeString(jobName);
        dest.writeString(packageName);
        dest.writeString(buildNumber);
        dest.writeString(log);
        dest.writeString(downloadUrl);
        dest.writeString(buildDate);
        dest.writeString(filePath);
        dest.writeLong(fileLength);
        dest.writeInt(downloadId);
        dest.writeInt(progress);
        dest.writeString(currentVersionName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppInfo appInfo = (AppInfo) o;
        return Objects.equals(jobName, appInfo.jobName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobName);
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "title='" + title + '\'' +
                ", jobName='" + jobName + '\'' +
                ", downloadId='" + downloadId + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }

}
