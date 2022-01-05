package com.jenkins.android.adapter;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jenkins.android.R;
import com.jenkins.android.model.AppInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class JenkinsAppAdapter extends BaseQuickAdapter<AppInfo, BaseViewHolder> {

    private Map<String, Integer> mIndexMap = new HashMap<>(4);

    public JenkinsAppAdapter() {
        super(R.layout.item_jenkins_app);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, AppInfo m) {
        holder.setText(R.id.tv_title, m.title);
        holder.setText(R.id.tv_date, m.buildDate);
        holder.setText(R.id.tv_version, "当前安装版本：" + m.currentVersionName);
        String number = TextUtils.isEmpty(m.buildNumber) ? "0" : m.buildNumber;
        ImageView iconView = holder.findView(R.id.img_icon);
        Button btnDownload = holder.findView(R.id.btn_download);
        if (m.progress > 0 && m.progress < 100) {
            btnDownload.setText("下载中" + m.progress + "%");
        } else if (m.progress == 100) {
            btnDownload.setText("下载完成");
        } else {
            String title = m.hasDownload ? "安装" : "下载";
            btnDownload.setText(title + " #" + number);
        }
        Glide.with(iconView).load(m.icon)
                .placeholder(R.drawable.ic_android_apps)
                .error(R.drawable.ic_android_apps)
                .into(iconView);
    }

    @Override
    public void setList(@Nullable Collection<? extends AppInfo> list) {
        super.setList(list);
        // 记录ID和索引关系
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                mIndexMap.put(getItem(i).jobName, i);
            }
        }
    }

    public void updateProgress(String jobName, int progress) {
        Integer index = mIndexMap.get(jobName);
        if (index != null) {
            getItem(index).progress = progress;
            notifyItemChanged(index);
        }
    }
}
