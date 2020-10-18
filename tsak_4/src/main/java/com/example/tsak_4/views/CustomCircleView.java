package com.example.tsak_4.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.tsak_4.R;

public class CustomCircleView extends View {
    float w, h, cx, cy, radius;
    private Paint paint;

    public CustomCircleView(Context context) {
        super(context);
    }

    public CustomCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CustomCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    public CustomCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs);
    }


    private void initAttrs(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomCircleView);
        w = 200;
        h = 200;
        cx = w / 2;
        cy = h / 2;

        if (w > h) {
            radius = h / 4;
        } else {
            radius = w / 4;
        }
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(typedArray.getColor(R.styleable.CustomCircleView_circleColor, 0));
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, paint);
        super.onDraw(canvas);
    }
}
