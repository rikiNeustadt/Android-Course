package com.yaelne_rivkano.ex2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ThreadLocalRandom;



public class Ball
{
    private float cx, cy;    // ball location (center point)
    private float radius;
    private int color;
    private Paint paint;
    private boolean isVisble;
    private float dx,dy; // for animation move
    private int speed;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Ball()
    {
        speed = ThreadLocalRandom.current().nextInt(2, 6);
        this.cx = 0;
        this.cy = 0;
        this.dx = speed;
        this.dy = speed;
        this.radius = 0;
        this.color = Color.BLACK;
        isVisble = true;
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    public boolean isVisble() {
        return isVisble;
    }

    public void setVisble(boolean visble) {
        isVisble = visble;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
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
        if(isVisble)
            canvas.drawCircle(cx, cy, radius, paint);
    }

    public boolean isPointInside(float tx, float ty)
    {
        double dist = Math.sqrt((cx-tx)*(cx-tx) + (cy-ty)*(cy-ty));

        if(dist <= radius)
            return true;
        return false;
    }

    public boolean isCollide(Ball other)
    {
        // distance
        double dist = Math.sqrt((cx-other.cx)*(cx-other.cx) + (cy-other.cy)*(cy-other.cy));

        if(dist <= radius + other.radius)
            return true;

        return false;
    }

    public boolean validLocation(Paddle paddle)
    {
        if(cy - radius <= paddle.getTopY())
            return paddle.getLeftX() <= cx && paddle.getRightX() >= cx;
        return true;
    }

    public void changeDirection(){
        Log.d("debug", "in change direction, dx is: "+dx);
        Log.d("debug", "in change direction, dy is: "+dy);
        //dx = -dx;
        dy = -dy;
        Log.d("debug", "in change direction after reverse, dx is: "+dx);
        Log.d("debug", "in change direction after reverse, dy is: "+dy);
    }

    public void stop()
    {
        dx = 0;
        dy = 0;
    }
}
