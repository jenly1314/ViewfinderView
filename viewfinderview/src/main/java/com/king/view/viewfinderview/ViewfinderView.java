/*
 * Copyright (C) Jenly
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
package com.king.view.viewfinderview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * 取景视图：主要用于渲染扫描相关的动画效果
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@SuppressWarnings("unused")
public class ViewfinderView extends View {

    /**
     * 默认范围比例，之所以默认为 1.2 是因为内切圆半径和外切圆半径之和的二分之一（即：（1 + √2) / 2 ≈ 1.2）
     */
    private static final float DEFAULT_RANGE_RATIO = 1.2F;
    /**
     * 最大缩放比例
     */
    private static final float MAX_ZOOM_RATIO = 1.2F;
    /**
     * 动画间隔
     */
    private static final int POINT_ANIMATION_INTERVAL = 3000;
    /**
     * 画笔
     */
    private Paint paint;

    /**
     * 文本画笔
     */
    private TextPaint textPaint;
    /**
     * 扫描框外面遮罩颜色
     */
    private int maskColor;
    /**
     * 扫描框域边框颜色
     */
    private int frameColor;
    /**
     * 扫描线颜色
     */
    private int laserColor;
    /**
     * 扫描框四角颜色
     */
    private int frameCornerColor;

    /**
     * 提示文本与扫描框的边距
     */
    private float labelTextPadding;
    /**
     * 提示文本的宽度
     */
    private int labelTextWidth;
    /**
     * 提示文本的位置
     */
    private TextLocation labelTextLocation;
    /**
     * 扫描区域提示文本
     */
    private String labelText;
    /**
     * 扫描区域提示文本颜色
     */
    private int labelTextColor;
    /**
     * 提示文本字体大小
     */
    private float labelTextSize;

    /**
     * 扫描线开始位置
     */
    private float scannerStart;
    /**
     * 扫描线结束位置
     */
    private float scannerEnd;

    /**
     * 扫描框宽
     */
    private int frameWidth;
    /**
     * 扫描框高
     */
    private int frameHeight;
    /**
     * 扫描框圆角半径
     */
    private float frameCornerRadius;
    /**
     * 激光扫描风格
     */
    private LaserStyle laserStyle;

    /**
     * 网格列数
     */
    private int laserGridColumn;
    /**
     * 网格高度
     */
    private float laserGridHeight;
    /**
     * 扫描网格线条的宽
     */
    private float laserGridStrokeWidth;

    /**
     * 扫描框
     */
    private RectF frame;

    /**
     * 扫描框边角的宽
     */
    private float frameCornerStrokeWidth;
    /**
     * 扫描框边角的高
     */
    private float frameCornerSize;
    /**
     * 扫描线每次移动距离
     */
    private float laserMovementSpeed;
    /**
     * 扫描线高度
     */
    private float laserLineHeight;

    /**
     * 扫描动画延迟间隔时间 默认20毫秒
     */
    private int laserAnimationInterval;
    /**
     * 是否完全刷新
     */
    private boolean fullRefresh;

    /**
     * 边框线宽度
     */
    private float frameLineStrokeWidth;

    /**
     * 扫描框占比
     */
    private float frameRatio;

    /**
     * 扫描框内间距
     */
    private float framePaddingLeft;
    private float framePaddingTop;
    private float framePaddingRight;
    private float framePaddingBottom;
    /**
     * 扫描框对齐方式
     */
    private FrameGravity frameGravity;
    /**
     * 扫描框图片
     */
    private Bitmap frameBitmap;

    /**
     * 结果点颜色
     */
    private int pointColor;
    /**
     * 结果点描边颜色
     */
    private int pointStrokeColor;
    /**
     * 结果点图片
     */
    private Bitmap pointBitmap;
    /**
     * 是否显示结果点缩放动画
     */
    private boolean isPointAnimation = true;

    /**
     * 结果点动画间隔时间
     */
    private int pointAnimationInterval;
    /**
     * 结果点半径
     */
    private float pointRadius;
    /**
     * 结果点外圈描边的半径与结果点半径的比例
     */
    private float pointStrokeRatio;
    /**
     * 设置结果点外圈描边的半径
     */
    private float pointStrokeRadius;

    /**
     * 当前缩放比例
     */
    private float currentZoomRatio = 1.0f;
    /**
     * 最后一次缩放比例（即上一次缩放比例）
     */
    private float lastZoomRatio;
    /**
     * 缩放速度
     */
    private float zoomSpeed = 0.02f;

    private int zoomCount;

    /**
     * 结果点有效点击范围半径
     */
    private float pointRangeRadius;

    private Bitmap laserBitmap;

    private float laserBitmapRatio;

    private float laserBitmapWidth;

    private int viewfinderStyle = ViewfinderStyle.CLASSIC;

    private List<Point> pointList;

    private boolean isShowPoints = false;

    private int minDimension;

    private OnItemClickListener onItemClickListener;

    private GestureDetector gestureDetector;

    /**
     * 取景框样式
     */
    @IntDef({ViewfinderStyle.CLASSIC, ViewfinderStyle.POPULAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewfinderStyle {
        /**
         * 经典样式：经典的扫描风格（带扫描框）
         */
        int CLASSIC = 0;
        /**
         * 流行样式：类似于新版的微信全屏扫描（不带扫描框）
         */
        int POPULAR = 1;

    }

    /**
     * 扫描线样式
     */
    public enum LaserStyle {
        /**
         * 无
         */
        NONE(0),
        /**
         * 线条样式
         */
        LINE(1),
        /**
         * 网格样式
         */
        GRID(2),
        /**
         * 图片样式
         */
        IMAGE(3);
        private final int mValue;

        LaserStyle(int value) {
            mValue = value;
        }

        private static LaserStyle getFromInt(int value) {
            for (LaserStyle style : LaserStyle.values()) {
                if (style.mValue == value) {
                    return style;
                }
            }
            return LaserStyle.LINE;
        }
    }

    /**
     * 文字位置
     */
    public enum TextLocation {
        TOP(0), BOTTOM(1);

        private final int mValue;

        TextLocation(int value) {
            mValue = value;
        }

        private static TextLocation getFromInt(int value) {
            for (TextLocation location : TextLocation.values()) {
                if (location.mValue == value) {
                    return location;
                }
            }
            return TextLocation.TOP;
        }
    }

    /**
     * 扫描框对齐方式
     */
    public enum FrameGravity {
        CENTER(0), LEFT(1), TOP(2), RIGHT(3), BOTTOM(4);

        private final int mValue;

        FrameGravity(int value) {
            mValue = value;
        }

        private static FrameGravity getFromInt(int value) {
            for (FrameGravity gravity : values()) {
                if (gravity.mValue == value) {
                    return gravity;
                }
            }
            return CENTER;
        }
    }

    public ViewfinderView(Context context) {
        this(context, null);
    }

    public ViewfinderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewfinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     */
    private void init(@NonNull Context context, AttributeSet attrs) {
        // 初始化自定义属性信息
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewfinderView);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        viewfinderStyle = array.getInt(R.styleable.ViewfinderView_vvViewfinderStyle, ViewfinderStyle.CLASSIC);
        maskColor = array.getColor(R.styleable.ViewfinderView_vvMaskColor, getColor(context, R.color.viewfinder_mask));

        frameColor = array.getColor(R.styleable.ViewfinderView_vvFrameColor, getColor(context, R.color.viewfinder_frame));
        frameWidth = array.getDimensionPixelSize(R.styleable.ViewfinderView_vvFrameWidth, 0);
        frameHeight = array.getDimensionPixelSize(R.styleable.ViewfinderView_vvFrameHeight, 0);
        frameRatio = array.getFloat(R.styleable.ViewfinderView_vvFrameRatio, 0.625f);
        frameLineStrokeWidth = array.getDimension(R.styleable.ViewfinderView_vvFrameLineStrokeWidth, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics));
        framePaddingLeft = array.getDimension(R.styleable.ViewfinderView_vvFramePaddingLeft, 0);
        framePaddingTop = array.getDimension(R.styleable.ViewfinderView_vvFramePaddingTop, 0);
        framePaddingRight = array.getDimension(R.styleable.ViewfinderView_vvFramePaddingRight, 0);
        framePaddingBottom = array.getDimension(R.styleable.ViewfinderView_vvFramePaddingBottom, 0);
        frameGravity = FrameGravity.getFromInt(array.getInt(R.styleable.ViewfinderView_vvFrameGravity, FrameGravity.CENTER.mValue));
        frameCornerColor = array.getColor(R.styleable.ViewfinderView_vvFrameCornerColor, getColor(context, R.color.viewfinder_corner));
        frameCornerSize = array.getDimension(R.styleable.ViewfinderView_vvFrameCornerSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, displayMetrics));
        frameCornerStrokeWidth = array.getDimension(R.styleable.ViewfinderView_vvFrameCornerStrokeWidth, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, displayMetrics));
        frameCornerRadius = array.getDimension(R.styleable.ViewfinderView_vvFrameCornerRadius, 0);
        Drawable frameDrawable = array.getDrawable(R.styleable.ViewfinderView_vvFrameDrawable);

        laserLineHeight = array.getDimension(R.styleable.ViewfinderView_vvLaserLineHeight, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, displayMetrics));
        laserMovementSpeed = array.getDimension(R.styleable.ViewfinderView_vvLaserMovementSpeed, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics));
        laserAnimationInterval = array.getInteger(R.styleable.ViewfinderView_vvLaserAnimationInterval, 20);

        laserGridColumn = array.getInt(R.styleable.ViewfinderView_vvLaserGridColumn, 20);
        laserGridHeight = array.getDimension(R.styleable.ViewfinderView_vvLaserGridHeight, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics));
        laserGridStrokeWidth = array.getDimension(R.styleable.ViewfinderView_vvLaserGridStrokeWidth, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, displayMetrics));

        laserColor = array.getColor(R.styleable.ViewfinderView_vvLaserColor, getColor(context, R.color.viewfinder_laser));
        laserStyle = LaserStyle.getFromInt(array.getInt(R.styleable.ViewfinderView_vvLaserStyle, LaserStyle.LINE.mValue));
        laserBitmapRatio = array.getFloat(R.styleable.ViewfinderView_vvLaserDrawableRatio, 0.625f);
        Drawable laserDrawable = array.getDrawable(R.styleable.ViewfinderView_vvLaserDrawable);

        labelText = array.getString(R.styleable.ViewfinderView_vvLabelText);
        labelTextColor = array.getColor(R.styleable.ViewfinderView_vvLabelTextColor, getColor(context, R.color.viewfinder_label_text));
        labelTextSize = array.getDimension(R.styleable.ViewfinderView_vvLabelTextSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, displayMetrics));
        labelTextPadding = array.getDimension(R.styleable.ViewfinderView_vvLabelTextPadding, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, displayMetrics));
        labelTextWidth = array.getDimensionPixelSize(R.styleable.ViewfinderView_vvLabelTextWidth, 0);
        labelTextLocation = TextLocation.getFromInt(array.getInt(R.styleable.ViewfinderView_vvLabelTextLocation, 1));

        pointColor = array.getColor(R.styleable.ViewfinderView_vvPointColor, getColor(context, R.color.viewfinder_point));
        pointStrokeColor = array.getColor(R.styleable.ViewfinderView_vvPointStrokeColor, getColor(context, R.color.viewfinder_point_stroke));
        pointRadius = array.getDimension(R.styleable.ViewfinderView_vvPointRadius, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, displayMetrics));
        pointStrokeRatio = array.getFloat(R.styleable.ViewfinderView_vvPointStrokeRatio, DEFAULT_RANGE_RATIO);
        Drawable pointDrawable = array.getDrawable(R.styleable.ViewfinderView_vvPointDrawable);

        isPointAnimation = array.getBoolean(R.styleable.ViewfinderView_vvPointAnimation, true);
        pointAnimationInterval = array.getInt(R.styleable.ViewfinderView_vvPointAnimationInterval, POINT_ANIMATION_INTERVAL);
        fullRefresh = array.getBoolean(R.styleable.ViewfinderView_vvFullRefresh, false);

        array.recycle();

        if (frameDrawable != null) {
            frameBitmap = getBitmapFormDrawable(frameDrawable);
        }

        if (laserDrawable != null) {
            laserBitmap = getBitmapFormDrawable(laserDrawable);
        }

        if (pointDrawable != null) {
            pointBitmap = getBitmapFormDrawable(pointDrawable);
            pointRangeRadius = (pointBitmap.getWidth() + pointBitmap.getHeight()) / 4f * DEFAULT_RANGE_RATIO;
        } else {
            pointStrokeRadius = pointRadius * pointStrokeRatio;
            pointRangeRadius = pointStrokeRadius * DEFAULT_RANGE_RATIO;
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setAntiAlias(true);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent event) {
                if (isShowPoints && checkSingleTap(event.getX(), event.getY())) {
                    return true;
                }
                return super.onSingleTapUp(event);
            }
        });

    }

    /**
     * 获取颜色
     */
    @SuppressWarnings("deprecation")
    private int getColor(@NonNull Context context, @ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /**
     * 根据 drawable 获取对应的 bitmap
     */
    private Bitmap getBitmapFormDrawable(@NonNull Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initFrame(getWidth(), getHeight());
    }

    /**
     * 缩放扫描线位图
     */
    private void scaleLaserBitmap() {
        if (laserBitmap != null && laserBitmapWidth > 0) {
            float ratio = laserBitmapWidth / laserBitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.postScale(ratio, ratio);
            int w = laserBitmap.getWidth();
            int h = laserBitmap.getHeight();
            laserBitmap = Bitmap.createBitmap(laserBitmap, 0, 0, w, h, matrix, true);
        }
    }

    /**
     * 初始化扫描框
     */
    private void initFrame(int width, int height) {
        minDimension = Math.min(width, height);
        int size = (int) (minDimension * frameRatio);

        if (laserBitmapWidth <= 0) {
            laserBitmapWidth = minDimension * laserBitmapRatio;
            scaleLaserBitmap();
        }

        if (frameWidth <= 0 || frameWidth > width) {
            frameWidth = size;
        }

        if (frameHeight <= 0 || frameHeight > height) {
            frameHeight = size;
        }

        if (labelTextWidth <= 0) {
            labelTextWidth = width - getPaddingLeft() - getPaddingRight();
        }

        float leftOffsets = (width - frameWidth) / 2f + framePaddingLeft - framePaddingRight;
        float topOffsets = (height - frameHeight) / 2f + framePaddingTop - framePaddingBottom;
        switch (frameGravity) {
            case LEFT:
                leftOffsets = framePaddingLeft;
                break;
            case TOP:
                topOffsets = framePaddingTop;
                break;
            case RIGHT:
                leftOffsets = width - frameWidth + framePaddingRight;
                break;
            case BOTTOM:
                topOffsets = height - frameHeight + framePaddingBottom;
                break;
        }

        frame = new RectF(leftOffsets, topOffsets, leftOffsets + frameWidth, topOffsets + frameHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isShowPoints) {
            // 显示结果点
            drawMask(canvas, getWidth(), getHeight());
            drawResultPoints(canvas, pointList);
            if (isPointAnimation) {
                // 显示动画并且结果点标记的图片为空时，支持缩放动画
                calcPointZoomAnimation();
            }
            return;
        }

        if (frame == null) {
            return;
        }

        if (scannerStart == 0f || scannerEnd == 0f) {
            scannerStart = frame.top;
            scannerEnd = frame.bottom - laserLineHeight;
        }

        // CLASSIC样式：经典样式（带扫描框）
        if (viewfinderStyle == ViewfinderStyle.CLASSIC) {
            // 绘制模糊区域
            drawExterior(canvas, frame, getWidth(), getHeight());
            // 绘制扫描动画
            drawLaserScanner(canvas, frame);
            // 绘制取景区域框
            drawFrame(canvas, frame);
            // 绘制提示信息
            drawTextInfo(canvas, frame);

            if (fullRefresh) {
                // 完全刷新
                postInvalidateDelayed(laserAnimationInterval);
            } else {
                // 局部刷新，更高效
                postInvalidateDelayed(laserAnimationInterval, (int) frame.left, (int) frame.top, (int) frame.right, (int) frame.bottom);
            }
        } else if (viewfinderStyle == ViewfinderStyle.POPULAR) {
            // POPULAR样式：类似于新版的微信全屏扫描（不带扫描框）
            // 绘制扫描动画
            drawLaserScanner(canvas, frame);
            // 绘制提示信息
            drawTextInfo(canvas, frame);

            postInvalidateDelayed(laserAnimationInterval);
        }

    }

    /**
     * 绘制文本
     */
    private void drawTextInfo(Canvas canvas, RectF frame) {
        if (!TextUtils.isEmpty(labelText)) {
            textPaint.setColor(labelTextColor);
            textPaint.setTextSize(labelTextSize);
            textPaint.setTextAlign(Paint.Align.CENTER);

            StaticLayout staticLayout = new StaticLayout(labelText, textPaint, labelTextWidth, Layout.Alignment.ALIGN_NORMAL, 1.2f, 0.0f, true);

            int saveCount = canvas.save();
            try {
                if (labelTextLocation == TextLocation.BOTTOM) {
                    canvas.translate(frame.centerX(), frame.bottom + labelTextPadding);
                } else {
                    canvas.translate(frame.centerX(), frame.top - labelTextPadding - staticLayout.getHeight());
                }
                staticLayout.draw(canvas);
            } finally {
                canvas.restoreToCount(saveCount);
            }
        }
    }

    /**
     * 绘制扫描框边角
     */
    private void drawFrameCorner(Canvas canvas, RectF frame) {
        paint.setColor(frameCornerColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(frameCornerStrokeWidth);

        float padding = (frameCornerStrokeWidth - frameLineStrokeWidth) / 2;
        RectF cornerFrame = new RectF(frame.left + padding, frame.top + padding,
            frame.right - padding, frame.bottom - padding);

        // 绘制圆角
        if (frameCornerRadius > 0f) {
            float diameter = 2 * frameCornerRadius;
            // 左上角（从180度开始，画90度）
            RectF topLeft = new RectF(cornerFrame.left, cornerFrame.top,
                cornerFrame.left + diameter, cornerFrame.top + diameter);
            canvas.drawArc(topLeft, 180, 90, false, paint);

            // 右上角（从270度开始，画90度）
            RectF topRight = new RectF(cornerFrame.right - diameter, cornerFrame.top,
                cornerFrame.right, cornerFrame.top + diameter);
            canvas.drawArc(topRight, 270, 90, false, paint);

            // 右下角（从0度开始，画90度）
            RectF bottomRight = new RectF(cornerFrame.right - diameter, cornerFrame.bottom - diameter,
                cornerFrame.right, cornerFrame.bottom);
            canvas.drawArc(bottomRight, 0, 90, false, paint);

            // 左下角（从90度开始，画90度）
            RectF bottomLeft = new RectF(cornerFrame.left, cornerFrame.bottom - diameter,
                cornerFrame.left + diameter, cornerFrame.bottom);
            canvas.drawArc(bottomLeft, 90, 90, false, paint);
        }

        float length = frameCornerSize - frameCornerRadius;
        // 绘制边角纵横延长线
        if (length > 0f) {
            // 左上
            canvas.drawLine(cornerFrame.left - padding + frameCornerRadius, cornerFrame.top, cornerFrame.left + frameCornerSize, cornerFrame.top, paint);
            canvas.drawLine(cornerFrame.left, cornerFrame.top - padding + frameCornerRadius, cornerFrame.left, cornerFrame.top + frameCornerSize, paint);

            // 右上
            canvas.drawLine(cornerFrame.right - frameCornerSize, cornerFrame.top, cornerFrame.right + padding - frameCornerRadius, cornerFrame.top, paint);
            canvas.drawLine(cornerFrame.right, cornerFrame.top - padding + frameCornerRadius, cornerFrame.right, cornerFrame.top + frameCornerSize, paint);

            // 右下
            canvas.drawLine(cornerFrame.right + padding - frameCornerRadius, cornerFrame.bottom, cornerFrame.right - frameCornerSize, cornerFrame.bottom, paint);
            canvas.drawLine(cornerFrame.right, cornerFrame.bottom + padding - frameCornerRadius, cornerFrame.right, cornerFrame.bottom - frameCornerSize, paint);

            // 左下
            canvas.drawLine(cornerFrame.left + frameCornerSize, cornerFrame.bottom, cornerFrame.left - padding + frameCornerRadius, cornerFrame.bottom, paint);
            canvas.drawLine(cornerFrame.left, cornerFrame.bottom + padding - frameCornerRadius, cornerFrame.left, cornerFrame.bottom - frameCornerSize, paint);
        }
    }


    /**
     * 绘制扫描动画
     */
    private void drawImageScanner(Canvas canvas, RectF frame) {
        if (laserBitmap != null) {
            canvas.drawBitmap(laserBitmap, (getWidth() - laserBitmap.getWidth()) / 2f, scannerStart, paint);
        } else {
            drawLineScanner(canvas, frame);
        }
    }

    /**
     * 绘制激光扫描线
     */
    private void drawLaserScanner(Canvas canvas, RectF frame) {
        if (laserStyle == null) {
            return;
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(laserColor);
        switch (laserStyle) {
            case LINE:// 线
                drawLineScanner(canvas, frame);
                break;
            case GRID:// 网格
                drawGridScanner(canvas, frame);
                break;
            case IMAGE:// 图片
                drawImageScanner(canvas, frame);
                break;
        }
        // 更新扫描位置
        if (scannerStart < scannerEnd) {
            scannerStart += laserMovementSpeed;
        } else {
            scannerStart = frame.top;
        }
        // 清除shader
        paint.setShader(null);
    }

    /**
     * 绘制线性式扫描
     */
    private void drawLineScanner(Canvas canvas, RectF frame) {
        // 线性渐变
        LinearGradient linearGradient = new LinearGradient(
            frame.centerX(), scannerStart,
            frame.centerX(), scannerStart + laserLineHeight,
            new int[]{shadeColor(laserColor), laserColor},
            null,
            Shader.TileMode.CLAMP);

        paint.setShader(linearGradient);
        // 椭圆
        RectF rectF = new RectF(frame.left + frameCornerSize, scannerStart, frame.right - frameCornerSize, scannerStart + laserLineHeight);
        canvas.drawOval(rectF, paint);
    }

    /**
     * 绘制网格式扫描
     */
    private void drawGridScanner(Canvas canvas, RectF frame) {
        paint.setStrokeWidth(laserGridStrokeWidth);
        paint.setStyle(Paint.Style.STROKE);

        // 计算Y轴开始位置
        float startY = (laserGridHeight > 0 && scannerStart - frame.top > laserGridHeight)
            ? scannerStart - laserGridHeight : frame.top;

        // 渐变设置
        LinearGradient linearGradient = new LinearGradient(
            frame.centerX(), startY,
            frame.centerX(), scannerStart,
            new int[]{shadeColor(laserColor), laserColor},
            null,
            Shader.TileMode.CLAMP
        );
        paint.setShader(linearGradient);

        float gridItemSize = frame.width() / laserGridColumn;
        Path gridPath = new Path();

        // 绘制纵向线
        for (int i = 1; i < laserGridColumn; i++) {
            float x = frame.left + i * gridItemSize;
            gridPath.moveTo(x, startY);
            gridPath.lineTo(x, scannerStart);
        }

        // 绘制横向线
        float visibleHeight = scannerStart - startY;
        int horizontalLineCount = (int) Math.ceil(visibleHeight / gridItemSize);
        float padding = frameLineStrokeWidth / 2f;
        for (int i = 0; i <= horizontalLineCount; i++) {
            float y = scannerStart - i * gridItemSize;
            gridPath.moveTo(frame.left + padding, y);
            gridPath.lineTo(frame.right - padding, y);
        }

        canvas.drawPath(gridPath, paint);
    }

    /**
     * 处理颜色模糊
     */
    private static int shadeColor(@ColorInt int color) {
        return (color & 0x00FFFFFF) | 0x01000000;
    }

    /**
     * 绘制扫描框边框
     */
    private void drawFrame(Canvas canvas, RectF frame) {
        paint.setColor(frameColor);
        paint.setStyle(Paint.Style.STROKE);
        if (frameBitmap != null) {
            canvas.drawBitmap(frameBitmap, null, frame, paint);
        } else {
            paint.setStrokeWidth(frameLineStrokeWidth);
            // 绘制圆角边框
            canvas.drawRoundRect(frame, frameCornerRadius, frameCornerRadius, paint);
            // 绘制扫描框边角
            drawFrameCorner(canvas, frame);
        }
    }

    /**
     * 绘制模糊区域
     */
    private void drawExterior(Canvas canvas, RectF frame, int width, int height) {
        if (maskColor == 0) {
            return;
        }
        paint.setColor(maskColor);
        paint.setStyle(Paint.Style.FILL);

        Path path = new Path();
        path.addRect(0, 0, width, height, Path.Direction.CW);

        Path framePath = new Path();
        framePath.addRoundRect(frame, frameCornerRadius, frameCornerRadius, Path.Direction.CW);

        // 使用 DIFFERENCE 模式挖空扫描取景区域
        path.op(framePath, Path.Op.DIFFERENCE);
        // 绘制最终路径
        canvas.drawPath(path, paint);
    }

    /**
     * 绘制遮罩层
     */
    private void drawMask(Canvas canvas, int width, int height) {
        if (maskColor != 0) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(maskColor);
            canvas.drawRect(0, 0, width, height, paint);
        }
    }

    /**
     * 根据结果点集合绘制结果点
     */
    private void drawResultPoints(Canvas canvas, List<Point> points) {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        if (points != null) {
            for (Point point : points) {
                drawResultPoint(canvas, point, currentZoomRatio);
            }
        }
    }

    /**
     * 计算点的缩放动画
     */
    private void calcPointZoomAnimation() {
        if (currentZoomRatio <= 1F) {
            lastZoomRatio = currentZoomRatio;
            currentZoomRatio += zoomSpeed;

            if (zoomCount < 2) {
                // 记住缩放回合次数
                zoomCount++;
            } else {
                zoomCount = 0;
            }
        } else if (currentZoomRatio >= MAX_ZOOM_RATIO) {
            lastZoomRatio = currentZoomRatio;
            currentZoomRatio -= zoomSpeed;
        } else {
            if (lastZoomRatio > currentZoomRatio) {
                lastZoomRatio = currentZoomRatio;
                currentZoomRatio -= zoomSpeed;
            } else {
                lastZoomRatio = currentZoomRatio;
                currentZoomRatio += zoomSpeed;
            }
        }

        // 每间隔3秒触发一套缩放动画，一套动画缩放三个回合(即：每次zoomCount累加到2后重置为0时)
        postInvalidateDelayed(zoomCount == 0 && lastZoomRatio == 1f ? pointAnimationInterval : laserAnimationInterval * 2L);

    }

    /**
     * 绘制结果点
     */
    private void drawResultPoint(Canvas canvas, Point point, float currentZoomRatio) {
        if (pointBitmap != null) {
            float left = point.x - pointBitmap.getWidth() / 2.0f;
            float top = point.y - pointBitmap.getHeight() / 2.0f;
            if (isPointAnimation) {
                int dstW = Math.round(pointBitmap.getWidth() * currentZoomRatio);
                int dstH = Math.round(pointBitmap.getHeight() * currentZoomRatio);
                int dstLeft = point.x - Math.round(dstW / 2.0f);
                int dstTop = point.y - Math.round(dstH / 2.0f);
                Rect dstRect = new Rect(dstLeft, dstTop, dstLeft + dstW, dstTop + dstH);
                canvas.drawBitmap(pointBitmap, null, dstRect, paint);
            } else {
                canvas.drawBitmap(pointBitmap, left, top, paint);
            }
        } else {
            paint.setColor(pointStrokeColor);
            canvas.drawCircle(point.x, point.y, pointStrokeRadius * currentZoomRatio, paint);

            paint.setColor(pointColor);
            canvas.drawCircle(point.x, point.y, pointRadius * currentZoomRatio, paint);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return isShowPoints || super.onTouchEvent(event);
    }

    private boolean checkSingleTap(float x, float y) {
        if (pointList != null) {
            for (int i = 0; i < pointList.size(); i++) {
                Point point = pointList.get(i);
                float distance = getDistance(x, y, point.x, point.y);
                if (distance <= pointRangeRadius) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(i);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取两点之间的距离
     */
    private float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * 是否显示结果点
     *
     * @return 是否显示结果点
     */
    public boolean isShowPoints() {
        return isShowPoints;
    }

    /**
     * 显示扫描动画
     */
    public void showScanner() {
        isShowPoints = false;
        invalidate();
    }

    /**
     * 显示结果点
     *
     * @param points 结果点
     */
    public void showResultPoints(List<Point> points) {
        pointList = points;
        isShowPoints = true;
        zoomCount = 0;
        lastZoomRatio = 0;
        currentZoomRatio = 1;
        invalidate();
    }

    /**
     * 设置 扫描区外遮罩的颜色
     *
     * @param maskColor 遮罩颜色
     */
    public void setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
    }

    /**
     * 设置 扫描框边框的颜色
     *
     * @param frameColor 扫描框边框的颜色
     */
    public void setFrameColor(@ColorInt int frameColor) {
        this.frameColor = frameColor;
    }

    /**
     * 设置扫描区激光线的颜色
     *
     * @param laserColor 激光线的颜色
     */
    public void setLaserColor(@ColorInt int laserColor) {
        this.laserColor = laserColor;
    }

    /**
     * 设置扫描框边角的颜色
     *
     * @param frameCornerColor 扫描框边角的颜色
     */
    public void setFrameCornerColor(@ColorInt int frameCornerColor) {
        this.frameCornerColor = frameCornerColor;
    }

    /**
     * 设置提示文本距离扫描区的间距
     *
     * @param labelTextPadding 提示文本距离扫描框的间距
     */
    public void setLabelTextPadding(float labelTextPadding) {
        this.labelTextPadding = labelTextPadding;
    }

    /**
     * 设置提示文本距离扫描框的间距
     *
     * @param labelTextPadding 提示文本距离扫描框的间距
     * @param unit             单位；比如：{@link TypedValue#COMPLEX_UNIT_DIP}；如需了解更多可查看：{@link TypedValue}
     */
    public void setLabelTextPadding(float labelTextPadding, int unit) {
        this.labelTextPadding = TypedValue.applyDimension(unit, labelTextPadding, getResources().getDisplayMetrics());
    }

    /**
     * 设置提示文本的宽度，默认为View的宽度
     *
     * @param labelTextWidth 提示文本的宽度
     */
    public void setLabelTextWidth(int labelTextWidth) {
        this.labelTextWidth = labelTextWidth;
    }

    /**
     * 设置提示文本显示位置
     *
     * @param labelTextLocation 提示文本显示位置
     */
    public void setLabelTextLocation(TextLocation labelTextLocation) {
        this.labelTextLocation = labelTextLocation;
    }

    /**
     * 设置提示文本信息
     *
     * @param labelText 提示文本信息
     */
    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    /**
     * 设置提示文本字体颜色
     *
     * @param color 提示文本字体颜色
     */
    public void setLabelTextColor(@ColorInt int color) {
        this.labelTextColor = color;
    }

    /**
     * 设置提示文本字体颜色
     *
     * @param id 提示文本字体颜色资源ID
     */
    public void setLabelTextColorResource(@ColorRes int id) {
        this.labelTextColor = getColor(getContext(), id);
    }

    /**
     * 设置提示文本字体大小
     *
     * @param textSize 提示文本字体大小
     */
    public void setLabelTextSize(float textSize) {
        this.labelTextSize = textSize;
    }

    /**
     * 设置提示文本字体大小
     *
     * @param textSize 提示文本字体大小
     * @param unit     单位；比如：{@link TypedValue#COMPLEX_UNIT_SP}；如需了解更多可查看：{@link TypedValue}
     */
    public void setLabelTextSize(float textSize, int unit) {
        this.labelTextSize = TypedValue.applyDimension(unit, textSize, getResources().getDisplayMetrics());
    }

    /**
     * 设置激光样式
     *
     * @param laserStyle 激光样式
     */
    public void setLaserStyle(LaserStyle laserStyle) {
        this.laserStyle = laserStyle;
    }

    /**
     * 设置网格激光扫描列数
     *
     * @param laserGridColumn 网格激光扫描列数
     */
    public void setLaserGridColumn(int laserGridColumn) {
        this.laserGridColumn = laserGridColumn;
    }

    /**
     * 设置网格激光扫描高度，为0时，表示动态铺满
     *
     * @param laserGridHeight 网格激光扫描高度
     */
    public void setLaserGridHeight(int laserGridHeight) {
        this.laserGridHeight = laserGridHeight;
    }

    /**
     * 设置扫描框边角的宽
     *
     * @param frameCornerStrokeWidth 扫描框边角的宽
     */
    public void setFrameCornerStrokeWidth(int frameCornerStrokeWidth) {
        this.frameCornerStrokeWidth = frameCornerStrokeWidth;
    }

    /**
     * 设置扫描框边角的高
     *
     * @param frameCornerSize 扫描框边角的高
     */
    public void setFrameCornerSize(int frameCornerSize) {
        this.frameCornerSize = frameCornerSize;
    }

    /**
     * 设置扫描框边角的高
     *
     * @param frameCornerSize 扫描框边角的高
     * @param unit            单位；比如：{@link TypedValue#COMPLEX_UNIT_DIP}；如需了解更多可查看：{@link TypedValue}
     */
    public void setFrameCornerSize(int frameCornerSize, int unit) {
        this.frameCornerSize = TypedValue.applyDimension(unit, frameCornerSize, getResources().getDisplayMetrics());
    }

    /**
     * 设置扫描框圆角半径
     *
     * @param frameCornerRadius 扫描框圆角半径
     */
    public void setFrameCornerRadius(int frameCornerRadius) {
        this.frameCornerRadius = frameCornerRadius;
    }

    /**
     * 设置扫描框圆角半径
     *
     * @param frameCornerRadius 扫描框圆角半径
     * @param unit              单位；比如：{@link TypedValue#COMPLEX_UNIT_DIP}；如需了解更多可查看：{@link TypedValue}
     */
    public void setFrameCornerRadius(int frameCornerRadius, int unit) {
        this.frameCornerRadius = TypedValue.applyDimension(unit, frameCornerRadius, getResources().getDisplayMetrics());
    }


    /**
     * 设置激光扫描的速度：即：每次移动的距离
     *
     * @param laserMovementSpeed 激光扫描的速度
     */
    public void setLaserMovementSpeed(int laserMovementSpeed) {
        this.laserMovementSpeed = laserMovementSpeed;
    }

    /**
     * 设置扫描线高度
     *
     * @param laserLineHeight 扫描线高度
     */
    public void setLaserLineHeight(int laserLineHeight) {
        this.laserLineHeight = laserLineHeight;
    }

    /**
     * 设置边框线宽度
     *
     * @param frameLineStrokeWidth 边框线宽度
     */
    public void setFrameLineStrokeWidth(int frameLineStrokeWidth) {
        this.frameLineStrokeWidth = frameLineStrokeWidth;
    }

    /**
     * 设置扫描框图片
     *
     * @param drawableResId 扫描框图片资源ID
     */
    public void setFrameDrawable(@DrawableRes int drawableResId) {
        setFrameBitmap(BitmapFactory.decodeResource(getResources(), drawableResId));
    }

    /**
     * 设置扫描框图片
     *
     * @param frameBitmap 扫描框图片
     */
    public void setFrameBitmap(Bitmap frameBitmap) {
        this.frameBitmap = frameBitmap;
    }

    /**
     * 设置扫描动画延迟间隔时间，单位：毫秒
     *
     * @param laserAnimationInterval 扫描动画延迟间隔时间
     */
    public void setLaserAnimationInterval(int laserAnimationInterval) {
        this.laserAnimationInterval = laserAnimationInterval;
    }

    /**
     * 设置结果点的颜色
     *
     * @param pointColor 结果点的颜色
     */
    public void setPointColor(@ColorInt int pointColor) {
        this.pointColor = pointColor;
    }

    /**
     * 设置结果点描边的颜色
     *
     * @param pointStrokeColor 结果点描边的颜色
     */
    public void setPointStrokeColor(@ColorInt int pointStrokeColor) {
        this.pointStrokeColor = pointStrokeColor;
    }

    /**
     * 设置结果点的半径
     *
     * @param pointRadius 结果点的半径
     */
    public void setPointRadius(float pointRadius) {
        this.pointRadius = pointRadius;
    }

    /**
     * 设置结果点的半径
     *
     * @param pointRadius 结果点的半径
     * @param unit        单位；比如：{@link TypedValue#COMPLEX_UNIT_DIP}；如需了解更多可查看：{@link TypedValue}
     */
    public void setPointRadius(float pointRadius, int unit) {
        this.pointRadius = TypedValue.applyDimension(unit, pointRadius, getResources().getDisplayMetrics());
    }

    /**
     * 设置激光扫描自定义图片
     *
     * @param drawableResId 激光扫描自定义图片资源ID
     */
    public void setLaserDrawable(@DrawableRes int drawableResId) {
        setLaserBitmap(BitmapFactory.decodeResource(getResources(), drawableResId));
    }

    /**
     * 设置激光扫描自定义图片
     *
     * @param laserBitmap 激光扫描自定义图片
     */
    public void setLaserBitmap(Bitmap laserBitmap) {
        this.laserBitmap = laserBitmap;
        scaleLaserBitmap();
    }

    /**
     * 设置结果点图片
     *
     * @param drawableResId 结果点图片资源ID
     */
    public void setPointDrawable(@DrawableRes int drawableResId) {
        setPointBitmap(BitmapFactory.decodeResource(getResources(), drawableResId));
    }

    /**
     * 设置结果点图片
     *
     * @param bitmap 结果点图片
     */
    public void setPointBitmap(Bitmap bitmap) {
        pointBitmap = bitmap;
        pointRangeRadius = (pointBitmap.getWidth() + pointBitmap.getHeight()) / 4f * DEFAULT_RANGE_RATIO;
    }

    /**
     * 设置结果点的动画间隔时长；单位：毫秒
     *
     * @param pointAnimationInterval 结果点的动画间隔时长
     */
    public void setPointAnimationInterval(int pointAnimationInterval) {
        this.pointAnimationInterval = pointAnimationInterval;
    }

    /**
     * 设置取景框样式；支持：classic：经典样式（带扫描框那种）、popular：流行样式（不带扫描框）
     *
     * @param viewfinderStyle 取景框样式
     */
    public void setViewfinderStyle(int viewfinderStyle) {
        this.viewfinderStyle = viewfinderStyle;
    }

    /**
     * 设置扫描框的宽度
     *
     * @param frameWidth 扫描框的宽度
     */
    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    /**
     * 设置扫描框的高度
     *
     * @param frameHeight 扫描框的高度
     */
    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    /**
     * 设置扫描框的与视图宽的占比；默认：0.625
     *
     * @param frameRatio 扫描框的与视图宽的占比
     */
    public void setFrameRatio(float frameRatio) {
        this.frameRatio = frameRatio;
    }

    /**
     * 设置扫描框左边的间距
     *
     * @param framePaddingLeft 扫描框左边的间距
     */
    public void setFramePaddingLeft(float framePaddingLeft) {
        this.framePaddingLeft = framePaddingLeft;
    }

    /**
     * 设置扫描框顶部的间距
     *
     * @param framePaddingTop 扫描框顶部的间距
     */
    public void setFramePaddingTop(float framePaddingTop) {
        this.framePaddingTop = framePaddingTop;
    }

    /**
     * 设置扫描框右边的间距
     *
     * @param framePaddingRight 扫描框右边的间距
     */
    public void setFramePaddingRight(float framePaddingRight) {
        this.framePaddingRight = framePaddingRight;
    }

    /**
     * 设置扫描框的间距
     *
     * @param left   扫描框左边的间距
     * @param top    扫描框顶部的间距
     * @param right  扫描框左边的间距
     * @param bottom 扫描框底部的间距
     */
    public void setFramePadding(float left, float top, float right, float bottom) {
        this.framePaddingLeft = left;
        this.framePaddingTop = top;
        this.framePaddingRight = right;
        this.framePaddingBottom = bottom;
    }

    /**
     * 设置扫描框底部的间距
     *
     * @param framePaddingBottom 扫描框底部的间距
     */
    public void setFramePaddingBottom(float framePaddingBottom) {
        this.framePaddingBottom = framePaddingBottom;
    }

    /**
     * 设置扫描框的对齐方式；默认居中对齐；即：{@link FrameGravity#CENTER}
     *
     * @param frameGravity 扫描框的对齐方式
     */
    public void setFrameGravity(FrameGravity frameGravity) {
        this.frameGravity = frameGravity;
    }

    /**
     * 设置是否显示结果点缩放动画；默认为：true
     *
     * @param pointAnimation 是否显示结果点缩放动画
     */
    public void setPointAnimation(boolean pointAnimation) {
        isPointAnimation = pointAnimation;
    }

    /**
     * 设置结果点外圈描边的半径；默认为：{@link #pointRadius} 的 {@link #pointStrokeRatio} 倍
     *
     * @param pointStrokeRadius 结果点外圈描边的半径
     */
    public void setPointStrokeRadius(float pointStrokeRadius) {
        this.pointStrokeRadius = pointStrokeRadius;
    }

    /**
     * 设置显示结果点动画的缩放速度；默认为：0.02 / {@link  #laserAnimationInterval}
     *
     * @param zoomSpeed 显示结果点动画的缩放速度
     */
    public void setZoomSpeed(float zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
    }

    /**
     * 设置结果点有效点击范围半径；默认为：{@link #pointStrokeRadius} 的 {@link #DEFAULT_RANGE_RATIO} 倍；
     * 需要注意的是，因为有效点击范围是建立在结果点的基础之上才有意义的；其主要目的是为了支持一定的容错范围；所以如果在此方法之后；
     * 有直接或间接有调用{@link #setPointBitmap(Bitmap)}方法的话，那么 {@link #pointRangeRadius}的值将会被覆盖。
     *
     * @param pointRangeRadius 结果点有效点击范围半径
     */
    public void setPointRangeRadius(float pointRangeRadius) {
        this.pointRangeRadius = pointRangeRadius;
    }

    /**
     * 设置扫描线位图的宽度比例；默认为：0.625；此方法会改变{@link #laserBitmapWidth}
     *
     * @param laserBitmapRatio 扫描线位图的宽度比例
     */
    public void setLaserBitmapRatio(float laserBitmapRatio) {
        this.laserBitmapRatio = laserBitmapRatio;
        if (minDimension > 0) {
            laserBitmapWidth = minDimension * laserBitmapRatio;
            scaleLaserBitmap();
        }
    }

    /**
     * 设置扫描线位图的宽度
     *
     * @param laserBitmapWidth 扫描线位图的宽度
     */
    public void setLaserBitmapWidth(float laserBitmapWidth) {
        this.laserBitmapWidth = laserBitmapWidth;
        scaleLaserBitmap();
    }

    /**
     * 设置是否完全刷新；适用于当 {@link #viewfinderStyle} 为 {@link ViewfinderStyle#CLASSIC} 时；
     * {@link #fullRefresh}的默认值为：{@code false}；
     * 当设置为{@code false}时，则使用局部刷新；即：视图只刷新扫描区域；
     * 当设置为{@code true}时，则使用完全刷新，即：视图会刷新全部区域。
     *
     * @param fullRefresh 是否完全刷新
     */
    public void setFullRefresh(boolean fullRefresh) {
        this.fullRefresh = fullRefresh;
    }

    /**
     * 设置点击Item监听
     *
     * @param listener 点击Item监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    /**
     * Item点击监听
     */
    public interface OnItemClickListener {
        /**
         * Item点击事件
         *
         * @param position 点击Item的索引位置
         */
        void onItemClick(int position);
    }

}
