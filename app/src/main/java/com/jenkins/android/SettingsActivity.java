package com.jenkins.android;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;

/**
 * @author RAE
 * @date 2022/01/05
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class SettingsActivity extends AppCompatActivity {

    TextView urlView;
    TextView proxyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        urlView = findViewById(R.id.et_url);
        proxyView = findViewById(R.id.et_proxy);
        findViewById(R.id.btn_save).setOnClickListener(this::onSaveClick);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String baseUrl = SPUtils.getInstance().getString("Jenkins.BaseUrl", BuildConfig.JENKINS_URL);
        String proxyUrl = SPUtils.getInstance().getString("Jenkins.Proxy", BuildConfig.JENKINS_PROXY);
        urlView.setText(baseUrl);
        proxyView.setText(proxyUrl);
    }

    private void onSaveClick(View view) {
        String url = urlView.getText().toString();
        String proxy = proxyView.getText().toString();
        if (!url.endsWith("/")) {
            url += "/";
        }
        SPUtils.getInstance().put("Jenkins.BaseUrl", url);
        SPUtils.getInstance().put("Jenkins.Proxy", proxy);
        new AlertDialog.Builder(this).
                setMessage("保存成功，重启后生效")
                .setNegativeButton("Ok", null)
                .setPositiveButton("Exit", (dialog, which) -> {
                    AppUtils.relaunchApp(true);
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
