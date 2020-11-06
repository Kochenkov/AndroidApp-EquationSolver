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

    public DrawView(Context context) {
        super(context);
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

    public void drawDiagram(ArrayList<Float> points) {
        this.points = points;
        //points = new float[]{onePoint, twoPoint};
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        float[] pointsArr = new float[points.size()];
        for (int i=0; i<points.size(); i++) {
            pointsArr[i] = (float) points.get(i);
        }

        canvas.drawPoints(pointsArr, paint);
    }
}
