package com.yaelne_rivkano.ex2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Paddle {
    private float leftX, topY, rightX, bottomY;    // Paddle coordinates
    private int color;
    private Paint paint;
    private boolean isVisble;

    public Paddle()
    {
        this.leftX = 0;
        this.topY = 0;
        this.bottomY = 0;
        this.rightX = 0;
        this.color = Color.BLUE;
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

    public float getLeftX() {
        return leftX;
    }

    public void setLeftX(float leftX) {
        this.leftX = leftX;
    }

    public float getBottomY() {
        return bottomY;
    }

    public void setBottomY(float bottomY) {
        this.bottomY = bottomY;
    }

    public float getRightX() {
        return rightX;
    }

    public void setRightX(float rightX) {
        this.rightX = rightX;
    }

    public float getTopY() {
        return topY;
    }

    public void setTopY(float topY) {
        this.topY = topY;
    }

    public void draw(Canvas canvas)
    {
        if(isVisble)
            canvas.drawRect(leftX, topY, rightX, bottomY, paint);
    }

    public void move(float canvasWidth, Boolean moveRight)
    {
        if(moveRight){
            if(rightX + 1 > canvasWidth)
                return;
            rightX += 10;
            leftX += 10;
        }
        else{
            if(leftX - 1 < 0)
                return;
            leftX -= 10;
            rightX -= 10;
        }
    }
    public boolean isCollide(Ball ball)
    {
        return leftX <= ball.getCx() && rightX >= ball.getCx();
    }

}

