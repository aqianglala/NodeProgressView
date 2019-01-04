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

public class NodeVerticalProgressView extends View {

    private int mLineWidth;
    private int mLineColor;
    private int mCircleRadius;
    private int mCircleColor;
    private int mCircleTextColor;
    private int mCircleTextSize;
    private int mRightTextMarginLeft;
    private int mRightTextColor;
    private int mRightTextSize;
    private int mInterval;
    private Paint mPaint;
    private TextPaint mTextPaint;
    private int mHeight;
    private List<NodeData> mData;

    public NodeVerticalProgressView(Context context) {
        this(context, null);
    }

    public NodeVerticalProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int defaultTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
        int defaultLineWidth = dip2px(5);
        int defaultCircleRadius = dip2px(20);
        int defaultRightTextMarginLeft = dip2px(20);
        int defaultInterval = dip2px(40);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NodeVerticalProgressView);
        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.NodeVerticalProgressView_lineWidth, defaultLineWidth);
        mLineColor = typedArray.getColor(R.styleable.NodeVerticalProgressView_lineColor, Color.YELLOW);
        mCircleRadius = typedArray.getDimensionPixelSize(R.styleable.NodeVerticalProgressView_circleRadius, defaultCircleRadius);
        mCircleColor = typedArray.getColor(R.styleable.NodeVerticalProgressView_circleColor, Color.YELLOW);
        mCircleTextColor = typedArray.getColor(R.styleable.NodeVerticalProgressView_circleTextColor, Color.WHITE);
        mCircleTextSize = typedArray.getDimensionPixelSize(R.styleable.NodeVerticalProgressView_circleTextSize, defaultTextSize);
        mRightTextMarginLeft = typedArray.getDimensionPixelSize(R.styleable.NodeVerticalProgressView_rightTextMarginLeft, defaultRightTextMarginLeft);
        mRightTextColor = typedArray.getColor(R.styleable.NodeVerticalProgressView_rightTextColor, Color.BLACK);
        mRightTextSize = typedArray.getDimensionPixelSize(R.styleable.NodeVerticalProgressView_rightTextSize, defaultTextSize);
        mInterval = typedArray.getDimensionPixelSize(R.styleable.NodeVerticalProgressView_interval, defaultInterval);
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
            int height = mInterval * (mData.size() - 1) + mCircleRadius * 2;
            setMeasuredDimension(width, height);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData == null || mData.size() == 0) return;
        // 画线
        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawLine(mCircleRadius, mCircleRadius, mCircleRadius, mHeight - mCircleRadius * 2, mPaint);

        for (int i = 0; i < mData.size(); i++) {
            String index = mData.get(i).getIndex();
            String content = mData.get(i).getContent();
            // 画圆
            mPaint.setColor(mCircleColor);
            canvas.drawCircle(mCircleRadius, mCircleRadius + mInterval * i, mCircleRadius, mPaint);
            // 画圆中的数字
            mTextPaint.setColor(mCircleTextColor);
            mTextPaint.setTextSize(mCircleTextSize);
            int indexBaseX = (int) (mCircleRadius - mTextPaint.measureText(index) / 2);
            int indexBaseY = (int) (mCircleRadius + mInterval * i - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
            canvas.drawText(index, indexBaseX, indexBaseY, mTextPaint);

            // 画右边的文字
            mTextPaint.setColor(mRightTextColor);
            mTextPaint.setTextSize(mRightTextSize);
            int contentBaseX = mCircleRadius * 2 + mRightTextMarginLeft;
            int contentBaseY = (int) (mCircleRadius + mInterval * i - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
            canvas.drawText(content, contentBaseX, contentBaseY, mTextPaint);
        }
    }

    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    public void setData(List<NodeData> data) {
        this.mData = data;
    }
}
