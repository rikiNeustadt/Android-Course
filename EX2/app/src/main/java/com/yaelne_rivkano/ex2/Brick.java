package com.yaelne_rivkano.ex2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Brick {
    private float leftX, topY, rightX, bottomY;    // Brick coordinates
    private int color;
    private Paint paint;
    private boolean isVisble;

    public Brick()
    {
        this.leftX = 0;
        this.topY = 0;
        this.bottomY = 0;
        this.rightX = 0;
        this.color = Color.RED;
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

    public boolean isCollide(Ball ball)
    {
        // distance
        // direction of ball is right
        double distX = ball.getCx() - ball.getRadius();
        double distY = ball.getCy() - ball.getRadius();
        if((leftX - ball.getRadius() <= ball.getCx() && rightX + ball.getRadius() >= ball.getCx())
                && (topY - ball.getRadius() <= ball.getCy() && bottomY + ball.getRadius() >= ball.getCy()))
                return true;
        return false;

    }

}
