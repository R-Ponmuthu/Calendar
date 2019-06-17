package com.it.calendar.meizu_calendarview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.it.calendar.calendarview.Calendar;
import com.it.calendar.calendarview.WeekView;
import com.it.calendar.R;


public class MeizuWeekView extends WeekView {

    private Paint mTextPaint = new Paint();
    private Paint mSchemeBasicPaint = new Paint();
    private float mRadio;
    private int mPadding;
    private float mSchemeBaseLine;
    private int mRadius;

    public MeizuWeekView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 10));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        //mTextPaint.setFakeBoldText(true);

        mSchemeBasicPaint.setAntiAlias(true);
        //mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setColor(0xffed5353);
        //mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setTextSize(dipToPx(context, 10));

        mCurDayTextPaint.setColor(Color.RED);

        mRadio = dipToPx(getContext(), 8);
        mPadding = dipToPx(getContext(), 5);
        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mRadio - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
        //4.0以上硬件加速会导致无效
        mSchemeBasicPaint.setMaskFilter(new BlurMaskFilter(25, BlurMaskFilter.Blur.SOLID));
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return true 则绘制onDrawScheme，因为这里背景色不是是互斥的
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        //canvas.drawRect(x + mPadding, mPadding, x + mItemWidth - mPadding, mItemHeight - mPadding, mSelectedPaint);

        mRadius = Math.min(mItemWidth, mItemHeight) / 10 * 4;

        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx, cy - 18, mRadius, mSelectedPaint);

        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {
        mSchemeBasicPaint.setColor(calendar.getSchemeColor());


        Bitmap bitmap;
        if (calendar.getScheme().equals("சுமு")) {
            bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.subamuhurtham), 30, 30, true);
        } else if (calendar.getScheme().equals("பௌ")) {
            bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icons_pournami), 30, 30, true);
        } else {
            bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icons_amavasai), 30, 30, true);
        }
        canvas.drawBitmap(bitmap, x + mItemWidth - mPadding - mRadio / 2 - getTextWidth(calendar.getScheme()) / 2, mPadding + mSchemeBaseLine, mSchemeBasicPaint);

        //canvas.drawCircle(x + mItemWidth - mPadding - mRadio / 2, mPadding + mRadio, mRadio, mSchemeBasicPaint);
        //canvas.drawText(calendar.getScheme(), x + mItemWidth - mPadding - mRadio / 2 - getTextWidth(calendar.getScheme()) / 2, mPadding + mSchemeBaseLine, mSchemeBasicPaint);
    }

    private float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        //int cx = x + mItemWidth / 2;
        //int top = -mItemHeight / 30;

        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        int top = -mItemHeight / 6;

        if (calendar.isCurrentDay() && !isSelected) {
            canvas.drawCircle(cx, cy, mRadius, mSelectTextPaint);
        }

        boolean isInRange = isInRange(calendar);

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentMonth() && isInRange ? mSchemeTextPaint : mOtherMonthTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top, calendar.isCurrentDay() && isInRange ? mCurDayTextPaint : calendar.isCurrentMonth() && isInRange ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
