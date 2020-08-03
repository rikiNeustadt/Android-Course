package com.yaelne_rivkano.ex2;

import android.graphics.Canvas;
import android.graphics.Paint;
import static android.graphics.Color.rgb;

public class Brick {
    private float leftX, topY, rightX, bottomY;    // Brick coordinates
    private int color;
    private Paint paint;
    private boolean isVisible;

    public Brick()
    {
        this.leftX = 0;
        this.topY = 0;
        this.bottomY = 0;
        this.rightX = 0;
        this.color = rgb(255, 0, 81);
        isVisible = true;
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void setLeftX(float leftX) {
        this.leftX = leftX;
    }

    public void setBottomY(float bottomY) {
        this.bottomY = bottomY;
    }

    public void setRightX(float rightX) {
        this.rightX = rightX;
    }

    public void setTopY(float topY) {
        this.topY = topY;
    }

    public void draw(Canvas canvas)
    {
        if(isVisible)
            canvas.drawRect(leftX, topY, rightX, bottomY, paint);
    }

    public boolean isCollide(Ball ball)
    {
        // distance
        return (leftX - ball.getRadius() <= ball.getCx() && rightX + ball.getRadius() >= ball.getCx())
                && (topY - ball.getRadius() <= ball.getCy() && bottomY + ball.getRadius() >= ball.getCy());
    }
}