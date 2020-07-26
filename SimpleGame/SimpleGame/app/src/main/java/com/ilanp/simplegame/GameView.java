package com.ilanp.simplegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View
{
    private Paint pen;
    private Ball ball1, ball2, ball3;
    private int numCollistion;
    private boolean isDrag;

    public GameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        pen = new Paint();
        pen.setTextSize(50);
        pen.setColor(Color.YELLOW);

        ball1 = new Ball(300,300, 100, Color.GREEN);
        ball1.setDx(4);
        ball1.setDy(5);

        ball2 = new Ball(500,800, 150, Color.RED);
        ball2.setDx(1);
        ball2.setDy(-1);

        ball3 = new Ball(300,1000, 100, Color.MAGENTA);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        // draw
        canvas.drawColor(Color.DKGRAY);
        canvas.drawText("Num Collistion: " + numCollistion, 50, 100, pen);
        ball1.draw(canvas);
        ball2.draw(canvas);
        ball3.draw(canvas);

        // update
        ball1.move(canvas.getWidth(), canvas.getHeight());
        ball2.move(canvas.getWidth(), canvas.getHeight());

        // check collistion
        if(ball1.isCollide(ball2))
        {
            numCollistion++;
            ball2.setVisble(false);
            ball2.stop();
            ball2.setCx(-ball2.getRadius()*2);
        }

        // do animation loop
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float tx = event.getX();
        float ty = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(ball3.isPointInside(tx,ty))
                    isDrag = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if(isDrag)
                {
                    ball3.setCx(tx);
                    ball3.setCy(ty);
                }
                break;

            case MotionEvent.ACTION_UP:
                isDrag = false;
                break;
        }

//        if(event.getAction() == MotionEvent.ACTION_DOWN)
//        {
//            if (ball2.isPointInside(tx, ty)) {
//                ball2.stop();
//            }
//        }



        return true;
    }
}
