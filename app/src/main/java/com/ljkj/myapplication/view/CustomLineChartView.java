package com.ljkj.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;


import com.ljkj.myapplication.bean.ChartDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名：LineChartView
 * 作者：Turbo
 * 时间： 12/29/20 2:06 PM
 * 蚁穴虽小，溃之千里。
 */

public class CustomLineChartView extends View {

    private Paint linePaint;//曲线画笔
    private Paint pointPaint;//曲线上锚点画笔
    private Paint tablePaint;//表格画笔
    private Paint textRulerPaint;//标尺文本画笔
//    private Paint textPointPaint;//曲线上锚点文本画笔

    private int paintType = -1;
    private Path linePath;//曲线路径
    private Path tablePath;//表格路径

    private int mWidth, mHeight;

    private List<ChartDataBean> dataList = new ArrayList<>();
    private List<String> mXDataList = new ArrayList<>();

    private Point[] linePoints;
    private int stepStart;
    private int stepEnd;
    private int stepSpace;
    private int stepSpaceDefault = 10;
    private int stepSpaceDP = stepSpaceDefault;//item宽度默认dp
    private int topSpace, bottomSpace;
    private int tablePadding;
    private int tablePaddingDP = 10;//view四周padding默认dp

    private int maxValue, minValue;
    private int rulerValueDefault = 20;
    private int rulerValue = rulerValueDefault;//刻度单位跨度
    private int rulerValuePadding;//刻度单位与轴的间距
    private int rulerValuePaddingDP = 8;//刻度单位与轴的间距默认dp
    private float heightPercent = 0.618f;

    private int lineColor = Color.parseColor("#A2A2A2");//曲线颜色
    private float lineWidthDP = 2f;//曲线宽度dp

    private int pointColor = lineColor;//锚点颜色
    private float pointWidthDefault = 8f;
    private float pointWidthDP = pointWidthDefault;//锚点宽度dp

    private int tableColor = Color.parseColor("#A2A2A2");//表格线颜色
    private float tableWidthDP = 0.5f;//表格线宽度dp

    private int rulerTextColor = tableColor;//表格标尺文本颜色
    private float rulerTextSizeSP = 10f;//表格标尺文本大小

    private int pointTextColor = Color.parseColor("#A2A2A2");//锚点文本颜色
    private float pointTextSizeSP = 10f;//锚点文本大小

    private boolean isInitialized = false;

    public CustomLineChartView(Context context) {
        this(context, null);
    }

    public CustomLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    private void setupView() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);//抗锯齿
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(dip2px(lineWidthDP));//边框宽度

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(lineColor);
        pointPaint.setStrokeWidth(dip2px(pointWidthDP));

        tablePaint = new Paint();
        tablePaint.setAntiAlias(true);
        tablePaint.setStyle(Paint.Style.STROKE);
        tablePaint.setColor(tableColor);
        tablePaint.setStrokeWidth(dip2px(tableWidthDP));

        textRulerPaint = new Paint();
        textRulerPaint.setAntiAlias(true);
        textRulerPaint.setStyle(Paint.Style.FILL);
        textRulerPaint.setTextAlign(Paint.Align.CENTER);
        textRulerPaint.setColor(tableColor);//文本颜色
        textRulerPaint.setTextSize(sp2px(rulerTextSizeSP));//字体大小

//        textPointPaint = new Paint();
//        textPointPaint.setAntiAlias(true);
//        textPointPaint.setStyle(Paint.Style.FILL);
//        textPointPaint.setTextAlign(Paint.Align.CENTER);
//        textPointPaint.setColor(lineColor);//文本颜色
//        textPointPaint.setTextSize(sp2px(pointTextSizeSP));//字体大小

        linePath = new Path();
        tablePath = new Path();

        resetParam();
    }

    private void resetParam() {
        linePath.reset();
        tablePath.reset();
        stepSpace = dip2px(stepSpaceDP);
        tablePadding = dip2px(tablePaddingDP);
        rulerValuePadding = dip2px(rulerValuePaddingDP);
        stepStart = tablePadding * 2;
        stepEnd = stepStart + stepSpace * (dataList.size() - 1);
        topSpace = bottomSpace = tablePadding;
        linePoints = new Point[dataList.size()];

        isInitialized = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = tablePadding + getTableEnd() + getPaddingLeft() + getPaddingRight();//计算自己的宽度
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);//父类期望的高度
        if (MeasureSpec.EXACTLY == heightMode) {
            height = getPaddingTop() + getPaddingBottom() + height;
        }
        setMeasuredDimension(width, height);//设置自己的宽度和高度
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);//绘制背景颜色
        canvas.translate(0f, mHeight / 2f + (getViewDrawHeight() + topSpace + bottomSpace) / 2f);//设置画布中心点垂直居中

        if (!isInitialized) {
            setupLine();
        }

        drawTable(canvas);//绘制表格
//        linePath.reset();
//        linePoints = new Point[dataList.size()];
//        setupLine();
        drawLine(canvas);//绘制曲线
        drawLinePoints(canvas);//绘制曲线上的点
    }

    private void drawText(Canvas canvas, Paint textPaint, String text, float x, float y) {
        canvas.drawText(text, x, y, textPaint);
    }

    /**
     * 绘制标尺x轴文本
     *
     * @param canvas
     * @param text
     * @param x
     * @param y
     */
    private void drawRulerXText(Canvas canvas, String text, float x, float y) {
        textRulerPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = textRulerPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offsetY = fontTotalHeight / 2 - fontMetrics.bottom;
        float newY = y + offsetY + rulerValuePadding;
        drawText(canvas, textRulerPaint, text, x, newY);
    }

//    /**
//     * 绘制曲线上锚点文本
//     *
//     * @param canvas
//     * @param text
//     * @param x
//     * @param y
//     */
//    private void drawLinePointText(Canvas canvas, String text, float x, float y) {
//        textPointPaint.setTextAlign(Paint.Align.CENTER);
//        float newY = y - rulerValuePadding;
//        drawText(canvas, textPointPaint, text, x, newY);
//    }

    private int getTableStart() {
        return stepStart + tablePadding;
    }

    private int getTableEnd() {
        return stepEnd + tablePadding;
    }

    private int margin = 0;

    /**
     * 绘制背景表格
     *
     * @param canvas
     */
    private void drawTable(Canvas canvas) {
        int tableEnd = getTableEnd();

        int rulerCount = maxValue / rulerValue;
        int rulerMaxCount = maxValue % rulerValue > 0 ? rulerCount + 1 : rulerCount;
        int rulerMax = rulerValue * rulerMaxCount + rulerValueDefault;
//        tablePath.moveTo(stepStart, -getValueHeight(rulerMax));//加上顶部的间隔
//        tablePath.lineTo(stepStart, 0);//标尺y轴
        tablePath.moveTo(stepStart + dpToPx(10), 0);//加上顶部的间隔
////        tablePath.lineTo(stepStart, 0);//标尺y轴
        tablePath.lineTo(tableEnd, 0);//标尺x轴
        margin = (int) (getMeasuredHeight() * (1 - heightPercent) / 2);
        if (listener != null) {
            listener.onInit(margin);
        }
        int startValue = minValue - (minValue > 0 ? 0 : minValue % rulerValue);
        int endValue = (maxValue + rulerValue);

        //标尺y轴连接线
        do {
            int startHeight = -getValueHeight(startValue);
            tablePath.moveTo(stepStart, startHeight);
            startValue += rulerValue;
        } while (startValue < endValue);
        canvas.drawPath(tablePath, tablePaint);
        //绘制x轴刻度单位
        drawRulerXValue(canvas);
    }

    /**
     * 绘制标尺x轴上所有文本
     *
     * @param canvas
     */
    private void drawRulerXValue(Canvas canvas) {
//        if (linePoints == null) return;
//        for (int i = 0; i < linePoints.length; i++) {
//            Point point = linePoints[i];
//            if (point == null) break;
//            drawRulerXText(canvas, String.valueOf(i), linePoints[i].x, 0)
//        }


        if (mXDataList == null) return;
        for (int i = 0; i < mXDataList.size(); i++) {
            drawRulerXText(canvas, mXDataList.get(i), linePoints[i].x, 0);
        }
    }

    /**
     * 绘制曲线
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        if (paintType == 2) {
            linePaint.setPathEffect(new DashPathEffect(new float[]{dpToPx(6), dpToPx(6)}, 0));
        }
        canvas.drawPath(linePath, linePaint);
    }

    /**
     * 绘制曲线上的锚点
     *
     * @param canvas
     */
    private void drawLinePoints(Canvas canvas) {
        if (linePoints == null) return;

        float pointWidth = dip2px(pointWidthDP) / 2;
        int pointCount = linePoints.length;
        for (int i = 0; i < pointCount; i++) {
            Point point = linePoints[i];
            if (point == null) break;
            canvas.drawCircle(point.x, point.y, pointWidth, pointPaint);
            //绘制点的文本
//            drawLinePointText(canvas, String.valueOf(dataList.get(i)), point.x, point.y);
        }
    }

    /**
     * 获取value值所占的view高度
     *
     * @param value
     * @return
     */
    private int getValueHeight(int value) {
        float valuePercent = Math.abs(value - minValue) * 100f / (Math.abs(maxValue - minValue) * 100f);//计算value所占百分比
        return (int) (getViewDrawHeight() * valuePercent + bottomSpace + 0.5f);//底部加上间隔

    }

    /**
     * 获取绘制区域高度
     *
     * @return
     */
    private float getViewDrawHeight() {
        return getMeasuredHeight() * heightPercent;
    }

    private int geLinetValueHeight(int value) {
        float valuePercent = Math.abs(value - minValue) * 100f / ((maxValue - minValue) * 100f);//计算value所占百分比
        return (int) (getLineDrawHeight() * valuePercent);//底部加上间隔

    }

    private float getLineDrawHeight() {
        margin = (int) (getMeasuredHeight() * (1 - heightPercent) / 2);
        return (float) ((getMeasuredHeight() - margin + dpToPx(10)) * 1.0f / 10.5 * 10);
    }


    /**
     * 初始化曲线数据
     */
    private void setupLine() {
        if (dataList.isEmpty()) return;
        int stepTemp = getTableStart();
        Point pre = new Point();
        pre.set(stepTemp, -geLinetValueHeight(dataList.get(0).getValue()));//坐标系从0,0默认在第四象限绘制
        linePoints[0] = pre;
        linePath.moveTo(pre.x, pre.y);

        if (dataList.size() == 1) {
            isInitialized = true;
            return;
        }

        for (int i = 1; i < dataList.size(); i++) {
            Point next = new Point();
            next.set(stepTemp += stepSpace, -geLinetValueHeight(dataList.get(i).getValue()));
            linePath.lineTo(next.x, next.y);
            linePoints[i] = next;
        }
        isInitialized = true;
    }

    private int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * dp转化成为px
     *
     * @param dp
     * @return
     */
    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }


    private void refreshLayout() {
        resetParam();
        requestLayout();
        postInvalidate();
    }

    /*-------------可操作方法---------------*/

    public void setMaxMin(int max, int min) {
        maxValue = max;
        minValue = min;
    }

    public void setXData(List<String> dataList) {
        if (dataList == null) {
            throw new RuntimeException("dataList cannot is null!");
        }
        if (dataList.isEmpty()) return;


        this.mXDataList.clear();
        this.mXDataList = new ArrayList<>();
        this.mXDataList.addAll(dataList);

        refreshLayout();
    }

    /**
     * 设置数据
     *
     * @param dataList
     */
    public void setData(List<ChartDataBean> dataList) {
        if (dataList == null) {
            throw new RuntimeException("dataList cannot is null!");
        }
        if (dataList.isEmpty()) return;
        this.dataList.clear();
        this.dataList = new ArrayList<>();
        this.dataList.addAll(dataList);
//        maxValue = Collections.max(this.dataList, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o1 - o2;
//            }
//        });
//
//        minValue = Collections.min(this.dataList, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o1 - o2;
//            }
//        });

        refreshLayout();
    }

    public List<ChartDataBean> getData() {
        return dataList;
    }

    /**
     * 设置曲线点的间距，标尺x轴间距
     *
     * @param dp
     */
    public void setStepSpace(int dp) {
        if (dp < stepSpaceDefault) {
            dp = stepSpaceDefault;
        }
        this.stepSpaceDP = dp;
        refreshLayout();
    }

    public void setPaintType(int type) {
        this.paintType = type;
        refreshLayout();
    }

    public static interface BottomMarginListener {
        void onInit(int value);
    }

    BottomMarginListener listener;

    public void setOnBottomMarginListener(BottomMarginListener listener) {
        this.listener = listener;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        setupView();
        refreshLayout();
    }
}
