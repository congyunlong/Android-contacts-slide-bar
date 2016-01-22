package com.cong.cly.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cong.cly.R;

/**
 * Created by cong on 16/1/21.
 */
public class SlideBar extends View {
    private String tag = "SlideBar";
    private String az[] = null;
    private Paint mPaint = new Paint();
    private int pointIndex;
    private OnTouchLetterChangeListener mOnTouchLetterChangeListener;
    private TextView popTextView;
    private int mOnTouchBackgroudColor;

    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (az == null) return;
        int width = getWidth();
        int height = getHeight();
        float aHeight = (float) height / az.length;
        for (int i = 0; i < az.length; i++) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(getResources().getColor(android.support.design.R.color.material_blue_grey_800));
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(48);
            String t = az[i];
            float mw = mPaint.measureText(t);
            float x = (width - mw) / 2;
            float y = aHeight * i + aHeight - mw / 2;
            canvas.drawText(t, x, y, mPaint);
            mPaint.reset();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(mOnTouchBackgroudColor);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (az == null) break;
                if (event.getY() > 0) {
                    int index = (int) (event.getY() * az.length / getHeight());
                    if (pointIndex != index) {
                        pointIndex = index;
                        if (mOnTouchLetterChangeListener != null) {
                            if (pointIndex < az.length) {
                                String t = az[pointIndex];
                                mOnTouchLetterChangeListener.onTouchLetterChange(t);
                                popTextView(t);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundResource(android.R.color.white);
                dimissPopTextView();
                pointIndex = 0;
                break;
            case MotionEvent.ACTION_CANCEL:
                setBackgroundResource(android.R.color.white);
                dimissPopTextView();
                pointIndex = 0;
                break;
        }
        return true;
    }

    public void setOnTouchBackgroudColor(int color) {
        this.mOnTouchBackgroudColor = color;
    }

    private void popTextView(String t) {
        if (popTextView != null) {
            popTextView.setText(t);
            popTextView.setVisibility(View.VISIBLE);
        }
    }

    private void dimissPopTextView() {
        if (popTextView != null)
            popTextView.setVisibility(View.INVISIBLE);
    }

    public void setOnTouchLetterChangeListener(OnTouchLetterChangeListener mOnTouchLetterChangeListener) {
        this.mOnTouchLetterChangeListener = mOnTouchLetterChangeListener;
    }

    public void setPopTextView(TextView popTextView) {
        this.popTextView = popTextView;
    }

    public void setAz(String[] az) {
        this.az = az;
    }
}
