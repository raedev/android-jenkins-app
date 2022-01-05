package com.jenkins.android.model;

import com.blankj.utilcode.util.TimeUtils;

import java.util.Date;

/**
 * @author RAE
 * @date 2021/12/31
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class JobChangeItem {
    public String msg;
    public String date;
    public AuthorBean author;

    @Override
    public String toString() {
//        String authorName = author == null ? "无名氏" : author.fullName;
//        Date date = TimeUtils.string2Date(this.date, "yyyy-MM-dd HH:mm:ss");
//        String time = date == null ? "" : TimeUtils.date2String(date);
        return msg; // + "(" + authorName + " at " + time + ")";
    }
}
