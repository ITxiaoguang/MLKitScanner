package com.xiaoguang.widget.mlkitscanner.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xiaoguang.widget.mlkitscanner.ScanManager;
import com.xiaoguang.widget.mlkitscanner.R;
import com.xiaoguang.widget.mlkitscanner.callback.CustomViewBindCallback;
import com.xiaoguang.widget.mlkitscanner.callback.act.ScanCallback;
import com.xiaoguang.widget.mlkitscanner.model.ScanConfig;

import java.util.ArrayList;

import top.defaults.colorpicker.ColorPickerPopup;

public class CustomConfigActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 是否显示相册功能
     */
    private CheckBox mCbPhoto;
    /**
     * 是否显示闪光灯
     */
    private CheckBox mCbLight;
    /**
     * 是否需要全屏扫描识别（默认）
     */
    private CheckBox mCbFullscreenScan;
    /**
     * 是否开启扫描完成震动提醒
     */
    private CheckBox mCbVibrate;
    /**
     * 是否开启扫描完成声音提醒
     */
    private CheckBox mCbBeep;
    /**
     * 是否完全自定义遮罩层
     */
    private CheckBox mCbCustomView;
    /**
     * 输入自定义提示文案
     */
    private EditText mEtHintText;
    /**
     * 文字大小(sp)
     */
    private EditText mEtHintTextSize;
    /**
     * 网格扫描高度
     */
    private EditText mEtGridlineHeight;
    /**
     * 网格扫描列数
     */
    private EditText mEtGridlineNum;

    private TextView mBtnColorPickerText;
    private TextView mBtnColorPickerLine;
    private TextView mBtnColorPickerBg;
    /**
     * 线性
     */
    private RadioButton mRbScanlineLine;
    /**
     * 网格
     */
    private RadioButton mRbScanlineGrid;


    private String colorText = "#22CE6B";
    private String colorLine = "#22CE6B";
    private String colorBackground = "#22FF0000";
    private String colorStatusBar = "#00000000";
    private String colorResultPoint = "#CC22CE6B";
    private String colorResultPointStroke = "#FFFFFFFF";

    /**
     * 是否支持手势缩放
     */
    private CheckBox mCbSupportZoom;
    /**
     * 是否状态栏黑色字体
     */
    private CheckBox mCbStatusDark;
    private TextView mBtnColorStatusbarBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_config);
        initView();
    }

    private void initView() {
        mCbPhoto = (CheckBox) findViewById(R.id.cb_photo);
        mCbLight = (CheckBox) findViewById(R.id.cb_light);
        mCbFullscreenScan = (CheckBox) findViewById(R.id.cb_fullscreen_scan);
        mCbVibrate = (CheckBox) findViewById(R.id.cb_vibrate);
        mCbBeep = (CheckBox) findViewById(R.id.cb_beep);
        mCbCustomView = (CheckBox) findViewById(R.id.cb_custom_view);
        mEtHintText = (EditText) findViewById(R.id.et_hint_text);
        mEtHintTextSize = (EditText) findViewById(R.id.et_hint_text_size);
        mEtGridlineHeight = (EditText) findViewById(R.id.et_gridline_height);
        mEtGridlineNum = (EditText) findViewById(R.id.et_gridline_num);
        mBtnColorPickerText = (TextView) findViewById(R.id.btn_color_picker_text);
        mBtnColorPickerText.setOnClickListener(this);
        mBtnColorPickerLine = (TextView) findViewById(R.id.btn_color_picker_line);
        mBtnColorPickerLine.setOnClickListener(this);
        mBtnColorPickerBg = (TextView) findViewById(R.id.btn_color_picker_bg);
        mBtnColorPickerBg.setOnClickListener(this);
        mRbScanlineLine = (RadioButton) findViewById(R.id.rb_scanline_line);
        mRbScanlineGrid = (RadioButton) findViewById(R.id.rb_scanline_grid);
        mCbSupportZoom = (CheckBox) findViewById(R.id.cb_support_zoom);
        mCbStatusDark = (CheckBox) findViewById(R.id.cb_status_dark);
        mBtnColorStatusbarBg = (TextView) findViewById(R.id.btn_color_statusbar_bg);
        mBtnColorStatusbarBg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_color_picker_text:
                new ColorPickerPopup.Builder(this)
                        .initialColor(Color.parseColor(colorText))
                        .enableBrightness(true)
                        .enableAlpha(false)
                        .okTitle("选择颜色")
                        .cancelTitle("取消")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(mBtnColorPickerText, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                colorText = getHexString(color);
                                mBtnColorPickerText.setBackgroundColor(color);
                            }
                        });
                break;
            case R.id.btn_color_picker_line:
                new ColorPickerPopup.Builder(this)
                        .initialColor(Color.parseColor(colorLine))
                        .enableBrightness(true)
                        .enableAlpha(false)
                        .okTitle("选择颜色")
                        .cancelTitle("取消")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(mBtnColorPickerLine, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                colorLine = getHexString(color);
                                mBtnColorPickerLine.setBackgroundColor(color);
                            }
                        });
                break;
            case R.id.btn_color_picker_bg:
                new ColorPickerPopup.Builder(this)
                        .initialColor(Color.parseColor(colorBackground))
                        .enableBrightness(true)
                        .enableAlpha(true)
                        .okTitle("选择颜色")
                        .cancelTitle("取消")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(mBtnColorPickerBg, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                colorBackground = getHexString(color);
                                mBtnColorPickerBg.setBackgroundColor(color);
                            }
                        });
                break;
            case R.id.btn_color_statusbar_bg:
                new ColorPickerPopup.Builder(this)
                        .initialColor(Color.parseColor(colorStatusBar))
                        .enableBrightness(true)
                        .enableAlpha(true)
                        .okTitle("选择颜色")
                        .cancelTitle("取消")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(mBtnColorStatusbarBg, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                colorStatusBar = getHexString(color);
                                mBtnColorStatusbarBg.setBackgroundColor(color);
                            }
                        });
                break;
        }
    }

    private String getHexString(int color) {
        String format = String.format("#%X", color);
        Log.e("=====", "format:" + format);
        if ("#0".equals(format)) {
            format = "#00000000";
            Log.e("=====", "format:" + format);
        }
        return format;
    }

    public void scanCode(View view) {
        //需要判断有没有权限
        ScanConfig scanConfig = new ScanConfig.Builder()
                //设置完成震动
                .isShowVibrate(mCbVibrate.isChecked())
                //扫描完成声音
                .isShowBeep(mCbBeep.isChecked())
                //显示相册功能
                .isShowPhotoAlbum(mCbPhoto.isChecked())
                //显示闪光灯
                .isShowLightController(mCbLight.isChecked())
                //打开扫描页面的动画
                .setActivityOpenAnime(R.anim.activity_anmie_in)
                //退出扫描页面动画
                .setActivityExitAnime(R.anim.activity_anmie_out)
                //自定义文案
                .setScanHintText(mEtHintText.getText().toString())
                .setScanHintTextColor(colorText)
                .setScanHintTextSize(TextUtils.isEmpty(mEtHintTextSize.getText().toString()) ? 14 : Integer.parseInt(mEtHintTextSize.getText().toString()))
                //扫描线的颜色
                .setScanColor(colorLine)
                //是否支持手势缩放
                .setSupportZoom(mCbSupportZoom.isChecked())
                //扫描线样式
                .setLaserStyle(mRbScanlineGrid.isChecked() ? ScanConfig.LaserStyle.Grid : ScanConfig.LaserStyle.Line)
                //背景颜色
                .setBgColor(colorBackground)
                //网格扫描线的列数
                .setGridScanLineColumn(TextUtils.isEmpty(mEtGridlineNum.getText().toString()) ? 30 : Integer.parseInt(mEtGridlineNum.getText().toString()))
                //网格高度
                .setGridScanLineHeight(TextUtils.isEmpty(mEtGridlineHeight.getText().toString()) ? 0 : Integer.parseInt(mEtGridlineHeight.getText().toString()))
                //是否全屏扫描,默认全屏
                .setFullScreenScan(mCbFullscreenScan.isChecked())
                //单位dp
                .setResultPointConfigs(36, 12, 3, colorResultPointStroke, colorResultPoint)
                //状态栏设置
                .setStatusBarConfigs(colorStatusBar, mCbStatusDark.isChecked())
                //自定义遮罩
                .setCustomShadeViewLayoutID(mCbCustomView.isChecked() ? R.layout.layout_custom_view : 0, new CustomViewBindCallback() {
                    @Override
                    public void onBindView(View customView) {
                        if (customView == null) {
                            return;
                        }
                        ImageView iv_back = customView.findViewById(R.id.iv_back);
                        ImageView iv_photo = customView.findViewById(R.id.iv_photo);
                        LinearLayout btn_scan_light = customView.findViewById(R.id.btn_scan_light);
                        final ImageView iv_scan_light = customView.findViewById(R.id.iv_scan_light);
                        final TextView tv_scan_light = customView.findViewById(R.id.tv_scan_light);
                        LinearLayout btn_my_card = customView.findViewById(R.id.btn_my_card);
                        LinearLayout btn_scan_record = customView.findViewById(R.id.btn_scan_record);
                        iv_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //关闭扫描页面
                                ScanManager.closeScanPage();
                            }
                        });
                        btn_scan_light.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //手电筒
                                if (ScanManager.isLightOn()) {
                                    ScanManager.closeScanLight();
                                    iv_scan_light.setImageResource(R.drawable.icon_custom_light_close);
                                    tv_scan_light.setText("开启手电筒");
                                } else {
                                    ScanManager.openScanLight();
                                    iv_scan_light.setImageResource(R.drawable.icon_custom_light_open);
                                    tv_scan_light.setText("关闭手电筒");
                                }
                            }
                        });
                        iv_photo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //打开相册扫描
                                ScanManager.openAlbumPage();
                            }
                        });
                        btn_my_card.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //我的名片
                                showToast("我的名片");
                            }
                        });
                        btn_scan_record.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //扫码记录
                                showToast("扫码记录");
                            }
                        });
                    }
                })
                .builder();
        ScanManager.startScan(this, scanConfig, new ScanCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                handlerResult(resultCode, data);
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    private void handlerResult(int resultCode, Intent data) {
        switch (resultCode) {
            case ScanManager.RESULT_SUCCESS:
                ArrayList<String> results = data.getStringArrayListExtra(ScanManager.INTENT_KEY_RESULT_SUCCESS);
                StringBuilder resultStr = new StringBuilder();
                for (int i = 0; i < results.size(); i++) {
                    resultStr.append("第" + (i + 1) + "条：");
                    resultStr.append(results.get(i));
                    resultStr.append("\n");
                }
                showToast(resultStr.toString());
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

}
