package com.jenkins.android;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.jenkins.android.contract.ApkDownloadListener;
import com.jenkins.android.model.AppInfo;
import com.jenkins.android.presenter.ApkDownloadManager;

/**
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class AppDetailActivity extends AppCompatActivity implements ApkDownloadListener {

    private Button mDownloadButton;
    private AppInfo mCurrent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mCurrent = getIntent().getParcelableExtra("data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView titleView = findViewById(R.id.tv_title);
        TextView buildInfoView = findViewById(R.id.tv_build_info);
        TextView logInfoView = findViewById(R.id.tv_log_info);
        ImageView iconView = findViewById(R.id.img_icon);
        mDownloadButton = findViewById(R.id.btn_download);
        mDownloadButton.setOnClickListener(this::onDownloadClick);
        mDownloadButton.setText(mCurrent.hasDownload ? "立即安装" : "立即下载");
        findViewById(R.id.btn_run).setOnClickListener(this::onRunClick);


        titleView.setText("#" + mCurrent.buildNumber + " " + mCurrent.title);
        buildInfoView.setText(getBuildInfo(mCurrent));
        logInfoView.setText(mCurrent.log);

        Glide.with(iconView).load(mCurrent.icon)
                .placeholder(R.drawable.ic_android_apps)
                .error(R.drawable.ic_android_apps)
                .into(iconView);
    }

    private void onRunClick(View view) {
        AppUtils.launchApp(mCurrent.packageName);
    }

    private void onDownloadClick(View view) {
        ApkDownloadManager.getInstance().download(mCurrent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ApkDownloadManager.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ApkDownloadManager.getInstance().unregister(this);
    }


    private CharSequence getBuildInfo(AppInfo appInfo) {
        StringBuilder sb = new StringBuilder();
        sb.append("构建号：").append(appInfo.buildNumber).append("<br>");
        sb.append("构建时间：").append(appInfo.buildDate).append("<br>");
        sb.append("构建包名：").append(appInfo.packageName).append("<br>");
        sb.append("当前安装版本：").append(appInfo.currentVersionName);
        return Html.fromHtml(sb.toString());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDownloadChanged(AppInfo m, int progress) {
        if (mCurrent.equals(m)) {
            mDownloadButton.setText("下载中" + progress + "%");
        }
    }

    @Override
    public void onDownloadFinish(AppInfo m) {
        if (mCurrent.equals(m)) {
            mDownloadButton.setText("点击安装");
        }
    }

    @Override
    public void onDownloadError(AppInfo m, String message) {
        if (mCurrent.equals(m)) {
            mDownloadButton.setText("下载错误");
        }
    }
}
