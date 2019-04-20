package com.silvergruppen.photoblog.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import static java.lang.Math.abs;

public class LineView extends View {
    Paint paint = new Paint();
    Point startPoint, endPoint;

    private void init() {
        paint.setColor(Color.BLACK);
    }

    public LineView(Context context) {
        super(context);
        init();
    }
    public LineView(Context context, Point startPoint, Point endPoint) {
        super(context);
        init();
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        int dx = abs(startPoint.x-endPoint.x);
        int dy = abs(startPoint.y-endPoint.y);
        if(startPoint.x > endPoint.x){
            canvas.drawLine(dx, 0, 0, dy, paint);
        }else{

            canvas.drawLine(0, 0, dx, dy, paint);
        }


    }

}
