/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoguang.widget.mlkitscanner.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.xiaoguang.widget.R;
import com.xiaoguang.widget.mlkitscanner.model.ScanConfig;
import com.xiaoguang.widget.mlkitscanner.utils.CommonUtils;


/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

    private static final String TAG = "ViewfinderView";
    private final Paint paint;
    private Paint paintResultPoint;
    private Paint paintText;
    private Paint paintTextBg;
    private Paint paintLine;
    private Paint paintLaser;
    private int maskColor;
    private int laserColor;

    private Rect frame;
    private String hintMsg;
    private String hintTextColor = "#FFFFFF";
    private int hintTextSize = 13;
    private int linePosition = 0;
    private int margin;
    private int laserLineW;
    private int cornerLineH;
    private int cornerLineW;
    private int gridColumn;
    private int gridHeight;

    //扫描线风格：0线，1网格
    private ScanConfig.LaserStyle laserStyle = ScanConfig.LaserStyle.Line;

    private ScanConfig mnScanConfig;

    private ValueAnimator anim;
    private boolean needAnimation = true;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintResultPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintTextBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintLaser = new Paint(Paint.ANTI_ALIAS_FLAG);
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        laserColor = resources.getColor(R.color.viewfinder_laser);
        hintMsg = "扫二维码/条形码";
        //文字
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(CommonUtils.sp2px(getContext(), hintTextSize));
        paintText.setTextAlign(Paint.Align.CENTER);
        paintTextBg.setColor(laserColor);
        paintTextBg.setTextAlign(Paint.Align.CENTER);
        //四角
        paintLine.setColor(laserColor);
        //扫描线
        paintLaser.setColor(laserColor);
        paintResultPoint.setColor(laserColor);
        //初始化数据大小
        initSize();
    }

    private void initSize() {
        //间距
        margin = CommonUtils.dip2px(getContext(), 4);
        //扫描线的宽度
        laserLineW = CommonUtils.dip2px(getContext(), 4);
        //四角线块
        cornerLineH = CommonUtils.dip2px(getContext(), 2);
        cornerLineW = CommonUtils.dip2px(getContext(), 14);
        //网格扫描线先关配置
        gridColumn = 24;
        gridHeight = CommonUtils.getScreenWidth(getContext()) * 7 / 10;
    }

    /**
     * 设置颜色
     *
     * @param laserColor
     */
    public void setLaserColor(int laserColor) {
        this.laserColor = laserColor;
        paintLine.setColor(this.laserColor);
        paintLaser.setColor(this.laserColor);
    }

    /**
     * 扫描线的样式
     *
     * @param laserStyle
     */
    public void setLaserStyle(ScanConfig.LaserStyle laserStyle) {
        this.laserStyle = laserStyle;
    }

    /**
     * 背景色
     *
     * @param maskColor
     */
    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    /**
     * 网格扫描列数
     *
     * @param gridColumn
     */
    public void setGridScannerColumn(int gridColumn) {
        if (gridColumn > 0) {
            this.gridColumn = gridColumn;
        }
    }

    /**
     * 网格扫描高度，默认扫描框的高度
     *
     * @param gridHeight
     */
    public void setGridScannerHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }


    public void setScanConfig(ScanConfig scanConfig) {
        this.mnScanConfig = scanConfig;

        //扫描文字配置
        setHintText(mnScanConfig.getScanHintText(), mnScanConfig.getScanHintTextColor(), mnScanConfig.getScanHintTextSize());

        //扫描线相关配置
        if (!TextUtils.isEmpty(mnScanConfig.getScanColor())) {
            setLaserColor(Color.parseColor(mnScanConfig.getScanColor()));
        }
        setLaserStyle(mnScanConfig.getLaserStyle());

        if (!TextUtils.isEmpty(mnScanConfig.getBgColor())) {
            setMaskColor(Color.parseColor(mnScanConfig.getBgColor()));
        }
        setGridScannerColumn(mnScanConfig.getGridScanLineColumn());
        setGridScannerHeight(mnScanConfig.getGridScanLineHeight());
    }

    /**
     * 设置文案
     */
    public void setHintText(String hintMsg, String hintTextColor, int hintTextSize) {
        //文字
        if (!TextUtils.isEmpty(hintMsg)) {
            this.hintMsg = hintMsg;
        } else {
            this.hintMsg = "";
        }
        //文字颜色
        if (!TextUtils.isEmpty(hintTextColor)) {
            this.hintTextColor = hintTextColor;
        }
        //文字大小
        if (hintTextSize > 0) {
            this.hintTextSize = hintTextSize;
        }
        paintText.setColor(Color.parseColor(this.hintTextColor));
        paintText.setTextSize(CommonUtils.sp2px(getContext(), this.hintTextSize));
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        int txtMargin = CommonUtils.dip2px(getContext(), 20);

        int frameWidth = width * 7 / 10;
        if (mnScanConfig != null && mnScanConfig.isFullScreenScan()) {
            frameWidth = width * 9 / 10;
        }
        int left = (width - frameWidth) / 2;
        int top = (height - frameWidth) / 2;
        frame = new Rect(left, top, left + frameWidth, top + frameWidth);

        //重新赋值
        frame.top = (height - (frame.right - frame.left)) / 2;
        frame.bottom = frame.top + (frame.right - frame.left);
        frame.left = (width - (frame.right - frame.left)) / 2;
        frame.right = frame.left + (frame.right - frame.left);

        paintLine.setShader(null);
        //四角线块
        int rectH = cornerLineW;
        int rectW = cornerLineH;
        //判断是不是全屏模式
        if (mnScanConfig != null && mnScanConfig.isFullScreenScan()) {
            //全屏透明
            paint.setColor(Color.TRANSPARENT);
            canvas.drawRect(0, 0, width, height, paint);
            //扫描线的宽度
            laserLineW = CommonUtils.dip2px(getContext(), 4);
        } else {
            //扫描线的宽度
            laserLineW = CommonUtils.dip2px(getContext(), 2);
            // 半透明背景
            paint.setColor(maskColor);

            canvas.drawRect(0, 0, width, frame.top, paint);
            canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
            canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
            canvas.drawRect(0, frame.bottom + 1, width, height, paint);
            //左上角
            canvas.drawRect(frame.left, frame.top, frame.left + rectW, frame.top + rectH, paintLine);
            canvas.drawRect(frame.left, frame.top, frame.left + rectH, frame.top + rectW, paintLine);
            //右上角
            canvas.drawRect(frame.right - rectW, frame.top, frame.right + 1, frame.top + rectH, paintLine);
            canvas.drawRect(frame.right - rectH, frame.top, frame.right + 1, frame.top + rectW, paintLine);
            //左下角
            canvas.drawRect(frame.left, frame.bottom - rectH, frame.left + rectW, frame.bottom + 1, paintLine);
            canvas.drawRect(frame.left, frame.bottom - rectW, frame.left + rectH, frame.bottom + 1, paintLine);
            //右下角
            canvas.drawRect(frame.right - rectW, frame.bottom - rectH, frame.right + 1, frame.bottom + 1, paintLine);
            canvas.drawRect(frame.right - rectH, frame.bottom - rectW, frame.right + 1, frame.bottom + 1, paintLine);
        }

        //带有背景框的文字，暂时不做
//        float textWidth = CommonUtils.getTextWidth(hintMsg, paintText);
//        float textHeight = CommonUtils.getTextHeight(hintMsg, paintText);
//        float startX = (width - textWidth) / 2 - CommonUtils.dip2px(getContext(), 20);
//        float startY = frame.bottom + txtMargin;
//        float endX = startX + textWidth + CommonUtils.dip2px(getContext(), 40);
//        float endY = startY + textHeight + CommonUtils.dip2px(getContext(), 12);
//        RectF rectF = new RectF(startX, startY, endX, endY);
//        canvas.drawRoundRect(rectF, 100, 100, paintTextBg);
//        if (mnScanConfig.isSupportZoom() && mnScanConfig.isShowZoomController() && mnScanConfig.getZoomControllerLocation() == MNScanConfig.ZoomControllerLocation.Bottom) {
//            canvas.drawText(hintMsg, width / 2, frame.top - txtMargin, paintText);
//        } else {
//            canvas.drawText(hintMsg, width / 2, startY + (rectF.height() - textHeight) + (rectF.height() - textHeight) / 2f, paintText);
//        }
        //文字
        canvas.drawText(hintMsg, width / 2, frame.bottom + txtMargin + CommonUtils.getTextHeight(hintMsg, paintText), paintText);

        //中间的线：动画
        if (linePosition <= 0) {
            linePosition = frame.top + margin;
        }
        //扫描线
        if (laserStyle == ScanConfig.LaserStyle.Line) {
            drawLineScanner(canvas, frame);
        } else if (laserStyle == ScanConfig.LaserStyle.Grid) {
            drawGridScanner(canvas, frame);
        }
        //动画刷新
        startAnimation();
    }

    /**
     * 绘制线性式扫描
     *
     * @param canvas
     * @param frame
     */
    private void drawLineScanner(Canvas canvas, Rect frame) {
        //线性渐变
        LinearGradient linearGradient = new LinearGradient(
                frame.left, linePosition,
                frame.left, linePosition + laserLineW,
                shadeColor(laserColor),
                laserColor,
                Shader.TileMode.MIRROR);
        paintLine.setShader(linearGradient);
        RectF rect = new RectF(frame.left + margin, linePosition, frame.right - margin, linePosition + laserLineW);
        canvas.drawOval(rect, paintLaser);
    }

    /**
     * 绘制网格式扫描
     *
     * @param canvas
     * @param frame
     */
    private void drawGridScanner(Canvas canvas, Rect frame) {
        if (gridHeight <= 0) {
            gridHeight = frame.bottom - frame.top;
        }
        int stroke = 2;
        paintLaser.setStrokeWidth(stroke);
        //计算Y轴开始位置
        int startY;
        if (gridHeight > 0 && linePosition - frame.top > gridHeight) {
            startY = linePosition - gridHeight;
        } else {
            startY = frame.top;
        }

        LinearGradient linearGradient = new LinearGradient(frame.left + frame.width() / 2, startY, frame.left + frame.width() / 2, linePosition, new int[]{shadeColor(laserColor), laserColor}, new float[]{0, 1f}, LinearGradient.TileMode.CLAMP);
        //给画笔设置着色器
        paintLaser.setShader(linearGradient);

        float wUnit = frame.width() * 1.0f / gridColumn;
        float hUnit = wUnit;
        //遍历绘制网格纵线
        for (int i = 0; i <= gridColumn; i++) {
            float startX;
            float stopX;
            if (i == 0) {
                startX = frame.left + 1;
            } else if (i == gridColumn) {
                startX = frame.left + i * wUnit - 1;
            } else {
                startX = frame.left + i * wUnit;
            }
            stopX = startX;
            canvas.drawLine(startX, startY, stopX, linePosition, paintLaser);
        }
        int height = gridHeight > 0 && linePosition - frame.top > gridHeight ? gridHeight : linePosition - frame.top;
        //遍历绘制网格横线
        for (int i = 0; i <= height / hUnit; i++) {
            canvas.drawLine(frame.left, linePosition - i * hUnit, frame.right, linePosition - i * hUnit, paintLaser);
        }
    }

    /**
     * 处理颜色模糊
     *
     * @param color
     * @return
     */
    public int shadeColor(int color) {
        String hax = Integer.toHexString(color);
        String result = "01" + hax.substring(2);
        return Integer.valueOf(result, 16);
    }

    public void startAnimation() {
        if (anim != null) {
            return;
        }
        anim = ValueAnimator.ofInt(frame.top - 2, frame.bottom + 2);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setDuration(2400);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!needAnimation) {
                    return;
                }
                linePosition = (int) animation.getAnimatedValue();
                try {
                    postInvalidate(
                            frame.left - 2,
                            frame.top - 2,
                            frame.right + 2,
                            frame.bottom + 2);
                } catch (Exception e) {
                    postInvalidate();
                }
            }
        });
        anim.start();
    }

    public void destroyView() {
        if (anim != null) {
            anim.removeAllUpdateListeners();
            anim.cancel();
            anim.end();
            anim = null;
        }
    }

}
