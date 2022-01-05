package com.jenkins.android.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blankj.utilcode.util.AppUtils;
import com.jenkins.android.AppDetailActivity;
import com.jenkins.android.R;
import com.jenkins.android.adapter.JenkinsAppAdapter;
import com.jenkins.android.contract.ApkDownloadListener;
import com.jenkins.android.contract.JenkinsAppContract;
import com.jenkins.android.model.AppInfo;
import com.jenkins.android.presenter.ApkDownloadManager;
import com.jenkins.android.presenter.JenkinsAppPresenter;

import java.util.List;
import java.util.Objects;

/**
 * Jenkins App List
 * @author RAE
 * @date 2022/01/01
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class JenkinsAppFragment extends Fragment implements JenkinsAppContract.View, ApkDownloadListener {

    private SwipeRefreshLayout mRefreshLayout;
    private JenkinsAppAdapter mAdapter;
    private JenkinsAppPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fm_jenkins_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new JenkinsAppAdapter();
        mPresenter = new JenkinsAppPresenter(this);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this::onRefresh);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        Objects.requireNonNull((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mAdapter.addChildClickViewIds(R.id.btn_download);
        mAdapter.setOnItemChildClickListener((adapter, v, position) -> {
            int id = v.getId();
            if (id == R.id.btn_download) {
                onDownloadItemClick(mAdapter.getItem(position));
            }
        });
        mAdapter.setOnItemClickListener((adapter, v, position) -> {
            Intent intent = new Intent(getContext(), AppDetailActivity.class);
            intent.putExtra("data", mAdapter.getItem(position));
            requireActivity().startActivity(intent);
        });
        this.onRefresh();
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

    private void onDownloadItemClick(AppInfo item) {
        ApkDownloadManager.getInstance().download(item);
    }

    private void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mPresenter.loadData();
    }

    @Override
    public void onLoadData(List<AppInfo> data) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.setList(data);
    }

    @Override
    public void onLoadError(String message) {
        mRefreshLayout.setRefreshing(false);
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onDownloadChanged(AppInfo m, int progress) {
        mAdapter.updateProgress(m.jobName, progress);
    }

    @Override
    public void onDownloadFinish(AppInfo m) {
        mAdapter.updateProgress(m.jobName, 100);
    }

    @Override
    public void onDownloadError(AppInfo m, String message) {
        mAdapter.updateProgress(m.jobName, 0);
        new AlertDialog.Builder(getContext())
                .setMessage("下载错误：" + message)
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .show();
    }
}
