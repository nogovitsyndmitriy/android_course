package com.example.tsak_4.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.tsak_4.R;

import java.util.Random;

public class CustomSliceView extends View {

    public static final int RADIUS = 80;
    private Paint paint;
    private Paint paint2;
    private Paint paint3;
    private Paint paint4;
    private Paint paint5;
    private RectF oval;
    private String colorName;
    int centerY;
    float radius = 300;
    int centerX;
    int resolvedCenterX;
    int resolvedCenterY;
    int resolvedRadius;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private final Random random = new Random();

    public CustomSliceView(Context context) {
        super(context);
    }

    public CustomSliceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public CustomSliceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    public CustomSliceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomSliceView);
        paint = setPaintStyle(paint, getResources().getColor(R.color.green));
        paint2 = setPaintStyle(paint2, getResources().getColor(R.color.yellow));
        paint3 = setPaintStyle(paint3, getResources().getColor(R.color.blue));
        paint4 = setPaintStyle(paint4, getResources().getColor(R.color.red));
        paint5 = setPaintStyle(paint5, getResources().getColor(R.color.violet));
        oval = new RectF();
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        centerX = MeasureSpec.getSize(widthMeasureSpec) / 2;
        centerY = MeasureSpec.getSize(heightMeasureSpec) / 2;

        float left = centerX - (float) (WIDTH / 2);
        float right = centerX + (float) WIDTH / 2;
        float top = centerY - (float) HEIGHT / 2;
        float bottom = centerY + (float) HEIGHT / 2;

        resolvedCenterX = resolveSize(HEIGHT, widthMeasureSpec) / 2;
        resolvedCenterY = resolveSize(WIDTH, heightMeasureSpec) / 2;
        resolvedRadius = resolveSize((int) radius, heightMeasureSpec);

        oval.set(left, top, right, bottom);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(oval, 270, 90, true, paint);
        canvas.drawArc(oval, 0, 90, true, paint2);
        canvas.drawArc(oval, 90, 90, true, paint3);
        canvas.drawArc(oval, 180, 90, true, paint4);
        canvas.drawCircle(centerX, centerY, RADIUS, paint5);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if ((x > centerX && x <= centerX + RADIUS && y < centerY && y > centerY - RADIUS)
                || (x < centerX && x > centerX - RADIUS && y < centerY && y > centerY - RADIUS)
                || (x < centerX && x > centerX - RADIUS && y > centerY && y < centerY + RADIUS)
                || (x > centerX && x <= centerX + RADIUS && y > centerY && y < centerY + RADIUS)) {
            switchColor(event);
        } else if (x > centerX && x < centerX + (float) WIDTH / 2 && y < centerY && y > centerY - (float) HEIGHT / 2) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setColorToPaint(paint, paint);
            }
        } else if (x < centerX && x > centerX - (float) WIDTH / 2 && y < centerY && y > centerY - (float) HEIGHT / 2) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setColorToPaint(paint, paint4);
            }
        } else if (x < centerX && x > centerX - (float) WIDTH / 2 && y > centerY && y < centerY + (float) HEIGHT / 2) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setColorToPaint(paint, paint3);
            }
        } else if (x > centerX && x < centerX + (float) WIDTH / 2 && y > centerY && y < centerY + (float) HEIGHT / 2) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                setColorToPaint(paint, paint2);
            }
        }
        Toast toast = Toast.makeText(getContext(), colorName, Toast.LENGTH_SHORT);
        toast.show();
        return super.onTouchEvent(event);
    }

    private void setColorToPaint(Paint paint, Paint paint2) {
        int randomColor;
        randomColor = getRandomColor(paint);
        paint2.setColor(randomColor);
        invalidate();
    }

    private void switchColor(MotionEvent event) {
        int randomColor;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            randomColor = getRandomColor(paint);
            paint.setColor(randomColor);
            randomColor = getRandomColor(paint2);
            paint2.setColor(randomColor);
            randomColor = getRandomColor(paint3);
            paint3.setColor(randomColor);
            randomColor = getRandomColor(paint4);
            paint4.setColor(randomColor);
            invalidate();
        }
    }

    private int getRandomColor(Paint paint) {
        int color = random.nextInt(4);
        int randomColor;
        switch (color) {
            case 0:
                randomColor = ContextCompat.getColor(getContext(), R.color.red);
                colorName = getResources().getResourceEntryName(R.color.red);
                break;
            case 1:
                randomColor = ContextCompat.getColor(getContext(), R.color.yellow);
                colorName = getResources().getResourceEntryName(R.color.yellow);
                break;
            case 2:
                randomColor = ContextCompat.getColor(getContext(), R.color.green);
                colorName = getResources().getResourceEntryName(R.color.green);
                break;
            case 3:
                randomColor = ContextCompat.getColor(getContext(), R.color.blue);
                colorName = getResources().getResourceEntryName(R.color.blue);
                break;
            default:
                randomColor = ContextCompat.getColor(getContext(), R.color.black);
        }
        int currentColor = paint.getColor();
        if (randomColor == currentColor) {
            getRandomColor(paint);
        }
        return randomColor;
    }

    public Paint setPaintStyle(Paint paint, int color) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        return paint;
    }
}
