package com.jenkins.android.model;

/**
 * @author RAE
 * @date 2021/12/31
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class JobBean {
    public String description;
    public String displayName;
    public JobBuildBean lastSuccessfulBuild;


    public int getLastSuccessBuildNumber() {
        return lastSuccessfulBuild == null ? 0 : lastSuccessfulBuild.number;
    }
}
