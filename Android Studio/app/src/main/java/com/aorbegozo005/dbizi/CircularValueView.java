package com.aorbegozo005.dbizi;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ander on 2016/08/20.
 */
public class CircularValueView extends View implements ValueAnimator.AnimatorUpdateListener {


    private int value;
    private int maxValue;

    private boolean reverse;

    private int currentValue;

    private ValueAnimator valueAnimator;

    private String stringValue;

    private int width;
    private int height;
    private int smallestDimension;

    private Paint backgroundStrokePaint;
    private Paint strokePaint;
    private Paint disabledPaint;
    private Paint backgroundPaint;
    private Paint textPaint;

    private RectF rectF;

    private int textX;
    private int textY;

    public CircularValueView(Context context) {
        super(context);
        init();
    }

    public CircularValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularValueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircularValueView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        disabledPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        disabledPaint.setColor(ContextCompat.getColor(getContext(), R.color.availabilityDisabled));
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(ContextCompat.getColor(getContext(), R.color.foreground_stroke));
        backgroundStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundStrokePaint.setStyle(Paint.Style.STROKE);
        backgroundStrokePaint.setColor(ContextCompat.getColor(getContext(), R.color.background_stroke));
        value = 0;
        stringValue = "";
        setValue(5);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        if (width > height){
            smallestDimension = height;
        }else{
            smallestDimension = width;
        }

        int strokeWidth = smallestDimension / 15;
        strokePaint.setStrokeWidth(strokeWidth);
        backgroundStrokePaint.setStrokeWidth(strokeWidth);

        smallestDimension = smallestDimension - strokeWidth*2;

        rectF = new RectF((width-smallestDimension)/2, (height-smallestDimension)/2, smallestDimension + (width-smallestDimension)/2, smallestDimension + (height-smallestDimension)/2);

        calculateTextSize();
        calculateTextPos();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnabled()){
            canvas.drawCircle(width/2, height/2, smallestDimension/2, backgroundPaint);
            canvas.drawText(stringValue, textX, textY, textPaint);
            canvas.drawArc(rectF, 0, 360, false, backgroundStrokePaint);
            if (reverse){
                canvas.drawArc(rectF, currentValue, 360-currentValue, false, strokePaint);
            }else{
                canvas.drawArc(rectF, 0, currentValue, false, strokePaint);
            }

        }else{
            canvas.drawCircle(width/2, height/2, smallestDimension/2, disabledPaint);
            canvas.drawText("0", textX, textY, textPaint);
        }
    }

    public void setValue(int pValue){
        value = pValue;
        currentValue = 0;
        stringValue = String.valueOf(value);
        backgroundPaint.setColor(ColorManager.getColorFromValue(getContext(), pValue));
        calculateTextPos();
        startAnimation();
        invalidate();
        requestLayout();
    }

    public void setMaxValue(int pMax){
        maxValue = pMax;
        currentValue = 0;
        startAnimation();
        invalidate();
        requestLayout();
    }

    private void calculateTextPos(){
        Rect textRect = new Rect();
        textPaint.getTextBounds(stringValue, 0, stringValue.length(), textRect);
        textX = width/2 - textRect.width()/2;
        textY = height/2 + textRect.height()/2;
    }

    private void calculateTextSize(){
        boolean found = false;
        int size = 3;
        while(!found){
            textPaint.setTextSize(size);
            Rect bounds = new Rect();
            textPaint.getTextBounds("99", 0, 2, bounds);
            if (bounds.width() >= width || bounds.height() >= height){
                found = true;
            }else{
                size++;
            }
        }
        textPaint.setTextSize(size/3);
    }

    public void setReversed(boolean pReverse){
        reverse = pReverse;
    }

    private void startAnimation(){
        if (valueAnimator != null && valueAnimator.isRunning()){
            valueAnimator.cancel();
        }
        int target = 0;
        if (maxValue != 0){
            target = (360*value/maxValue);
        }
        if (reverse){
            valueAnimator = ValueAnimator.ofInt(360, 360-target);
        }else{
            valueAnimator = ValueAnimator.ofInt(0, target);
        }

        valueAnimator.addUpdateListener(this);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        currentValue = (int) valueAnimator.getAnimatedValue();
        invalidate();
    }
}
