package com.jnu.student.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.jnu.student.R;

import java.util.Calendar;

public class CustomClockView extends View {
    private Bitmap hourHandBitmap;
    private Bitmap minuteHandBitmap;
    private Bitmap secondHandBitmap;
    private Bitmap clockBackgroundBitmap;
    private Handler handler = new Handler();
    private static final int DELAY_MILLIS = 1000; // 刷新间隔，单位毫秒

    public CustomClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 初始化图片资源
        hourHandBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hour2);
        minuteHandBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.minute2);
        secondHandBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.second2);

        // 加载背景图片
        clockBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.clock1);

        // 开始更新时钟
        startClockUpdate();
    }
    private void startClockUpdate() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 更新时间
                invalidate();
                // 安排下一次更新
                handler.postDelayed(this, DELAY_MILLIS);
            }
        }, DELAY_MILLIS);
    }

    // 重写 onDraw 方法，在该方法中实现绘制逻辑
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 获取自定义视图的中心坐标
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // 绘制背景图片
        drawBackground(canvas, clockBackgroundBitmap, centerX, centerY);

//        // 计算旋转角度
//        float hourRotationDegrees = 360 / 12 * Calendar.getInstance().get(Calendar.HOUR);
//        float minuteRotationDegrees = 360 / 60 * Calendar.getInstance().get(Calendar.MINUTE);
//        float secondRotationDegrees = 360 / 60 * Calendar.getInstance().get(Calendar.SECOND);

        // 计算旋转角度
        float hourRotationDegrees = 360 / 12 * hour + (360 / 12) * (minute / 60f);
        float minuteRotationDegrees = 360 / 60 * minute + (360 / 60) * (second / 60f);
        float secondRotationDegrees = 360 / 60 * second;

        // 获取图片宽度和高度
        float hourHandWidth = hourHandBitmap.getWidth();
        float hourHandHeight = hourHandBitmap.getHeight();

        float minuteHandWidth = minuteHandBitmap.getWidth();
        float minuteHandHeight = minuteHandBitmap.getHeight();

        float secondHandWidth = secondHandBitmap.getWidth();
        float secondHandHeight = secondHandBitmap.getHeight();
        // 时针旋转点
        float hourPivotX = hourHandWidth/2;  // 根据实际情况指定旋转点的 X 坐标
        float hourPivotY = hourHandHeight/2;  // 根据实际情况指定旋转点的 Y 坐标

        // 分针旋转点
        float minutePivotX = minuteHandWidth/2;  // 根据实际情况指定旋转点的 X 坐标
        float minutePivotY = minuteHandHeight/2;  // 根据实际情况指定旋转点的 Y 坐标

        // 秒针旋转点
        float secondPivotX = secondHandWidth/2;  // 根据实际情况指定旋转点的 X 坐标
        float secondPivotY = secondHandHeight/2;  // 根据实际情况指定旋转点的 Y 坐标

        // 绘制时针
        drawHand(canvas, hourHandBitmap, centerX, centerY, hourRotationDegrees,hourPivotX, hourPivotY);

        // 绘制分针
        drawHand(canvas, minuteHandBitmap, centerX, centerY, minuteRotationDegrees,minutePivotX, minutePivotY);

        // 绘制秒针
        drawHand(canvas, secondHandBitmap, centerX, centerY, secondRotationDegrees,secondPivotX, secondPivotY);
    }
    private void drawHand(Canvas canvas, Bitmap handBitmap, float centerX, float centerY,
                          float rotationDegrees, float pivotX, float pivotY) {
        // 将画布旋转到指定角度
        canvas.save();
        canvas.rotate(rotationDegrees, centerX, centerY);
        // 计算绘制位置
        float drawX = centerX - pivotX;
        float drawY = centerY - pivotY;
//        float drawX = pivotX;
//        float drawY = pivotY;

        // 绘制图片
        canvas.drawBitmap(handBitmap, drawX, drawY, null);

        // 恢复画布状态
        canvas.restore();
    }
    private void drawBackground(Canvas canvas, Bitmap backgroundBitmap, float centerX, float centerY) {
        // 计算绘制位置
        float drawX = centerX - backgroundBitmap.getWidth() / 2f;
        float drawY = centerY - backgroundBitmap.getHeight() / 2f;

        // 绘制背景图片
        canvas.drawBitmap(backgroundBitmap, drawX, drawY, null);
    }

}
