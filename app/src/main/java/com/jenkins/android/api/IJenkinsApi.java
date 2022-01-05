package com.jenkins.android.api;


import com.jenkins.android.model.JobBean;
import com.jenkins.android.model.JobDetailBean;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Jenkins OpenAPI
 * @author RAE
 * @date 2021/12/31
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public interface IJenkinsApi {


    /**
     * 查询Job任务信息
     * @param jobName JobName
     * @return Job Info
     */
    @GET("/job/{jobName}/api/json")
    Observable<JobBean> queryJob(@Path("jobName") String jobName);

    /**
     * 查询某次构建的任务信息
     * @param jobName JobName
     * @param buildNumber buildNumber
     * @return Job detail
     */
    @GET("/job/{jobName}/{buildNumber}/api/json")
    Observable<JobDetailBean> queryJobDetail(@Path("jobName") String jobName, @Path("buildNumber") String buildNumber);
}
