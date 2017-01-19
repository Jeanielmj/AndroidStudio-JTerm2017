package com.example.demouser.fractaltreepainting;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by demouser on 1/12/17.
 */

public class FractalTreeView extends View {

    private static final int FIRST_BRANCH_LENGTH = 8;
    private static final int MIN_BRANCH_LENGTH = 2;
    private static final int NUM_CHILDREN = 8;
    public static final int BLOSSOM_DIAM = 5;
    //private static final int

    private Paint mBackgroundPaint;
    private Paint mPaint;

    public FractalTreeView(Context context) {
        super(context);
        init();
    }

    public FractalTreeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(); // configure view independent of which constructor is used
    }

    public FractalTreeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FractalTreeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(getResources().getColor(R.color.background_color));
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas){
        // drawing a black background
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);

        // drawing a trunk
//        mPaint.setColor(Color.rgb(160,82,45));
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeWidth(80f);
//
        int startX = (getWidth() - 0)/2;
        int startY = getHeight() -  250; // (int) 0.5*getHeight();
//
//        int endX = startX;
//        int endY = startY - 200; //(int) 0.2*getHeight();
//
//        canvas.drawLine(startX, startY, endX, endY, mPaint);

        // draw the leaves
        drawBranch(canvas, startX, startY, 0, FIRST_BRANCH_LENGTH);
    }

    private void drawBranch(Canvas canvas, int x, int y, double angle, int length){

        // base case
        if(x < 0 || y < 0|| x > getWidth() || y > getHeight() || length < MIN_BRANCH_LENGTH){
            int color = Color.rgb(225, 225, 225);
            mPaint.setColor(color);
            //canvas.drawCircle(x, y, BLOSSOM_DIAM, mPaint);
        }else{
            //

        }

        int nextX = 0;
        int nextY = 0;
        int nextLength = (int) 0.75*length;
        double nextAngle = (double) 0.5 * angle;

        // recursive call
        drawBranch(canvas, nextX, nextY, nextAngle, nextLength);

    }

}
