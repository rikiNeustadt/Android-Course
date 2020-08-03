package com.yaelne_rivkano.ex2;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import static android.graphics.Color.rgb;

public class Ball
{
    private float cx, cy;    // ball location (center point)
    private float radius;
    private int color;
    private Paint paint;
    private float dx, dy; // for animation move
    private static int[] availableSpeed = {-2, -3, -4, -5, 2, 3, 4, 5};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Ball()
    {
        this.cx = 0;
        this.cy = 0;
        setSpeed();
        this.radius = 0;
        this.color = rgb(26, 0, 13);
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    public void setSpeed(){
        int rnd = new Random().nextInt(availableSpeed.length);
        int speed = availableSpeed[rnd];
        this.dx = speed;
        this.dy = speed;
    }

    public float getCx() {
        return cx;
    }

    public void setCx(float cx) {
        this.cx = cx;
    }

    public float getCy() {
        return cy;
    }

    public void setCy(float cy) {
        this.cy = cy;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public void move(float canvasWidth, float canvasHeight)
    {
        cx += dx;  // move right 3 pixel
        cy += dy;    // move down

        // check if in canvas border

        // Left or Right check
        if( (cx-radius <= 0) || (cx+radius >= canvasWidth))
            dx = -dx;  // reverse direction

        // Up or Down check
        if( (cy-radius <= 0) || (cy+radius >= canvasHeight))
            dy = -dy;  // reverse direction
    }

    public void draw(Canvas canvas)
    {
        canvas.drawCircle(cx, cy, radius, paint);
    }

    public void changeDirection(){
        dy = -dy;
    }
}