package com.xiaoguang.widget.mlkitscanner.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xiaoguang.widget.mlkitscanner.ScanManager;
import com.xiaoguang.widget.mlkitscanner.R;
import com.xiaoguang.widget.mlkitscanner.callback.act.ScanCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnScanDefault;
    private Button btnScanCustom;
    private TextView tvResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnScanDefault = (Button) findViewById(R.id.btn_scan_default);
        btnScanCustom = (Button) findViewById(R.id.btn_scan_custom);
        btnScanDefault.setOnClickListener(this);
        btnScanCustom.setOnClickListener(this);
        tvResults = (TextView) findViewById(R.id.tv_results);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_scan_default) {
            ScanManager.startScan(this, new ScanCallback() {
                @Override
                public void onActivityResult(int resultCode, Intent data) {
                    handlerResult(resultCode, data);
                }
            });
        }else if (view.getId() == R.id.btn_scan_custom) {
            //跳转到自定义界面
            startActivity(new Intent(this, CustomConfigActivity.class));
        }
    }

    private void handlerResult(int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (resultCode) {
            default:
                break;
            case ScanManager.RESULT_SUCCESS:
                ArrayList<String> results = data.getStringArrayListExtra(ScanManager.INTENT_KEY_RESULT_SUCCESS);
                StringBuilder resultStr = new StringBuilder();
                for (int i = 0; i < results.size(); i++) {
                    resultStr.append("第" + (i + 1) + "条：");
                    resultStr.append(results.get(i));
                    resultStr.append("\n");
                }
                tvResults.setText(resultStr.toString());
                break;
            case ScanManager.RESULT_FAIL:
                String resultError = data.getStringExtra(ScanManager.INTENT_KEY_RESULT_ERROR);
                showToast(resultError);
                break;
            case ScanManager.RESULT_CANCLE:
                showToast("取消扫码");
                break;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}