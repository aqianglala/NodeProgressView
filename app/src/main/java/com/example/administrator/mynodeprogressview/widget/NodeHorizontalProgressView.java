package com.example.administrator.mynodeprogressview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.administrator.mynodeprogressview.NodeData;
import com.example.administrator.mynodeprogressview.R;

import java.util.List;


public class NodeHorizontalProgressView extends View {

    private int mLineWidth;
    private int mLineColor;
    private int mCircleRadius;
    private int mCircleColor;
    private int mCircleTextColor;
    private int mCircleTextSize;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private int mWidth;
    private List<NodeData> mData;
    private int mColumn;
    private int mInterval;// 两个圆之间的圆心距离
    private int mRowCount;

    public NodeHorizontalProgressView(Context context) {
        this(context, null);
    }

    public NodeHorizontalProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int defaultTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
        int defaultLineWidth = dip2px(5);
        int defaultCircleRadius = dip2px(20);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NodeHorizontalProgressView);
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.NodeHorizontalProgressView_lineWidth, defaultLineWidth);
        mLineColor = typedArray.getColor(R.styleable.NodeHorizontalProgressView_lineColor, Color.YELLOW);
        mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.NodeHorizontalProgressView_circleRadius, defaultCircleRadius);
        mCircleColor = typedArray.getColor(R.styleable.NodeHorizontalProgressView_circleColor, Color.YELLOW);
        mCircleTextColor = typedArray.getColor(R.styleable.NodeHorizontalProgressView_circleTextColor, Color.WHITE);
        mCircleTextSize = typedArray.getDimensionPixelSize(R.styleable.NodeHorizontalProgressView_circleTextSize, defaultTextSize);
        mColumn = typedArray.getInteger(R.styleable.NodeHorizontalProgressView_column, 0);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mData == null || mData.size() == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            mInterval = (width - mCircleRadius * 2) / (mColumn - 1);
            mRowCount = (int) Math.ceil(mData.size() / (double) mColumn);
            int height = (mRowCount - 1) * mInterval + mCircleRadius * 2;
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData == null || mData.size() == 0) return;
        drawLines(canvas);
        drawData(canvas);
    }

    private void drawLines(Canvas canvas) {
        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);
        // 画横线
        drawHorizontalLine(canvas);
        // 画竖线
        drawVerticalLine(canvas);
    }

    private void drawVerticalLine(Canvas canvas) {
        int startX;
        int startY;
        int stopX;
        int stopY;
        for (int i = 0; i < mRowCount - 1; i++) {
            if (i % 2 == 0) {// 偶数行竖线画在右边
                startX = mWidth - mCircleRadius;
                startY = mCircleRadius + i * mInterval;
                stopX = mWidth - mCircleRadius;
                stopY = mCircleRadius + (i + 1) * mInterval;
            } else {// 奇数行竖线划在左边
                startX = mCircleRadius;
                startY = mCircleRadius + i * mInterval;
                stopX = mCircleRadius;
                stopY = mCircleRadius + (i + 1) * mInterval;
            }
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        }
    }

    private void drawHorizontalLine(Canvas canvas) {
        int startX;
        int startY;
        int stopX;
        int stopY;
        for (int i = 0; i < mRowCount; i++) {
            if (i == mRowCount - 1) {// 最后一行
                int lastCount = mData.size() % mColumn;
                if (i % 2 == 0) {// 偶数行从左向右画
                    startX = mCircleRadius;
                    startY = mCircleRadius + i * mInterval;
                    stopX = mCircleRadius + mInterval * (lastCount - 1);
                    stopY = mCircleRadius + i * mInterval;
                } else {// 奇数行从右向左画
                    startX = mWidth - mCircleRadius;
                    startY = mCircleRadius + i * mInterval;
                    stopX = mWidth - mCircleRadius - mInterval * (lastCount - 1);
                    stopY = mCircleRadius + i * mInterval;
                }
            } else {
                startX = mCircleRadius;
                startY = mCircleRadius + i * mInterval;
                stopX = mWidth - mCircleRadius;
                stopY = mCircleRadius + i * mInterval;
            }
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        }
    }

    private void drawData(Canvas canvas) {
        for (int i = 0; i < mData.size(); i++) {
            String index = mData.get(i).getIndex();
            int row = i / mColumn;// 算出在第几行
            int num = i % mColumn;// 算出在该行第几个

            if (row % 2 == 0) {// 偶数行从左到右填充数据
                // 画圆
                mPaint.setColor(mCircleColor);
                int cx = mCircleRadius + mInterval * num;
                int cy = mCircleRadius + mInterval * row;
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);
                // 画圆中的数字
                mTextPaint.setColor(mCircleTextColor);
                mTextPaint.setTextSize(mCircleTextSize);
                int indexBaseX = (int) (cx - mTextPaint.measureText(index) / 2);
                int indexBaseY = (int) (cy - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
                canvas.drawText(index, indexBaseX, indexBaseY, mTextPaint);

            } else {// 奇数行从右到左填充数据
                mPaint.setColor(mCircleColor);
                int cx = mWidth - mCircleRadius - mInterval * num;
                int cy = mCircleRadius + mInterval * row;
                canvas.drawCircle(cx, cy, mCircleRadius, mPaint);

                mTextPaint.setColor(mCircleTextColor);
                mTextPaint.setTextSize(mCircleTextSize);
                int indexBaseX = (int) (cx - mTextPaint.measureText(index) / 2);
                int indexBaseY = (int) (cy - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
                canvas.drawText(index, indexBaseX, indexBaseY, mTextPaint);
            }
        }
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    public void setData(List<NodeData> data) {
        this.mData = data;
    }
}
