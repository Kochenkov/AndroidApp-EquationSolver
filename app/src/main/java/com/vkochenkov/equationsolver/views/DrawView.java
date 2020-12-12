package com.vkochenkov.equationsolver.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawView extends View {

    ArrayList<Float> points;
    float max;
    float min;
    //int screenWidth;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, int screenWidth, ArrayList<Float> points) {
        super(context);
        this.max = screenWidth;
        this.min = 0f;
        this.points = points;
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    public void drawDiagram(ArrayList<Float> points) {
//        this.points = points;
//        //points = new float[]{onePoint, twoPoint};
//        invalidate();
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawAxes(canvas);
        drawPoints(canvas);
    }

    private void drawPoints(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);

        //todo ось y нужно инвертировать, отображается не в ту сторону
        float[] pointsArr = new float[points.size()];
        for (int i=0; i<points.size(); i++) {
            pointsArr[i] = ((float) points.get(i))*10 + max/2;
        }

        canvas.drawPoints(pointsArr, paint);
    }

    private void drawAxes(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        float horisontalX1 = min;
        float horisontalY1 = max/2;
        float horisontalX2 = max;
        float horisontalY2 = horisontalY1;

        float verticalX1 = max/2;
        float verticalY1 = min;
        float verticalX2 = verticalX1;
        float verticalY2 = max;

        canvas.drawLine(horisontalX1,horisontalY1, horisontalX2, horisontalY2, paint);
        canvas.drawLine(verticalX1,verticalY1, verticalX2,verticalY2, paint);
    }
}
