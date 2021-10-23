package com.xiaoguang.widget.mlkitscanner;

import android.app.Activity;
import android.content.Intent;

import com.xiaoguang.widget.mlkitscanner.callback.act.ActResultRequest;
import com.xiaoguang.widget.mlkitscanner.callback.act.ScanCallback;
import com.xiaoguang.widget.mlkitscanner.model.ScanConfig;
import com.xiaoguang.widget.mlkitscanner.ui.ScanPreviewActivity;

/**
 * Created by maning on 2017/12/7.
 * 启动扫描的主类
 */

public class ScanManager {

    //常量
    public static final int RESULT_SUCCESS = 0;
    public static final int RESULT_FAIL = 1;
    public static final int RESULT_CANCLE = 2;
    public static final String INTENT_KEY_RESULT_SUCCESS = "INTENT_KEY_RESULT_SUCCESS";
    public static final String INTENT_KEY_RESULT_ERROR = "INTENT_KEY_RESULT_ERROR";
    //是否是调试模式
    public static final boolean isDebugMode = false;


    //跳转传入的数据
    public static final String INTENT_KEY_CONFIG_MODEL = "INTENT_KEY_CONFIG_MODEL";


    public static void startScan(Activity activity, ScanCallback scanCallback) {
        startScan(activity, null, scanCallback);
    }

    public static void startScan(Activity activity, ScanConfig mnScanConfig, ScanCallback scanCallback) {
        if (mnScanConfig == null) {
            mnScanConfig = new ScanConfig.Builder().builder();
        }
        Intent intent = new Intent(activity.getApplicationContext(), ScanPreviewActivity.class);
        //传递数据
        intent.putExtra(ScanManager.INTENT_KEY_CONFIG_MODEL, mnScanConfig);
        ActResultRequest actResultRequest = new ActResultRequest(activity);
        actResultRequest.startForResult(intent, scanCallback);
        activity.overridePendingTransition(mnScanConfig.getActivityOpenAnime(), android.R.anim.fade_out);
    }

    /**
     * 关闭当前页面
     */
    public static void closeScanPage() {
        ScanPreviewActivity.closeScanPage();
    }

    /**
     * 打开相册扫描图片
     */
    public static void openAlbumPage() {
        ScanPreviewActivity.openAlbumPage();
    }

    /**
     * 打开手电筒
     */
    public static void openScanLight() {
        ScanPreviewActivity.openScanLight();
    }

    /**
     * 关闭手电筒
     */
    public static void closeScanLight() {
        ScanPreviewActivity.closeScanLight();
    }

    /**
     * 手电筒是否开启
     */
    public static boolean isLightOn() {
        return ScanPreviewActivity.isLightOn();
    }

}
