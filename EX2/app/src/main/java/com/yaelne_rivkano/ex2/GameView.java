package com.yaelne_rivkano.ex2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;


public class GameView extends View {
    private int screenHeight;
    private int screenWidth;

    private final int ROWS = 5;
    private final int COLS = 7;
    private final int SPACEBRICK = 5;
    private float brickWidth;
    private float brickHeigt;
    private Ball ball;
    private Brick [][] bricks;
    private Paddle paddle;
    private Boolean firstTime;
    private boolean moveRight;
    private boolean movePaddle;


    public GameView(Context context, AttributeSet atr){
        super( context, atr);
        float screenWidth = getWidth();
        Log.d("debug", "screenWidth" + screenWidth);
        ball = new Ball();
        bricks = new Brick [ROWS][COLS];
        for(int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLS; j++)
                bricks[i][j] = new Brick();
        paddle = new Paddle();
        firstTime = true;
        moveRight = true;
        movePaddle = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();

        // if touch in right side of the screen - move paddle right
        if(touchX > this.screenWidth/2)
            moveRight = true;
        else
            moveRight = false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                movePaddle = true;
                break;
            case MotionEvent.ACTION_UP:
                movePaddle = false;
                break;
        }

        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas){

        this.screenHeight = getHeight();
        this.screenWidth = getWidth();
        brickWidth = (screenWidth - (COLS + 1)*(SPACEBRICK))/COLS;
        brickHeigt = screenHeight/20;

        Log.d("debug", "screenHeight" + screenHeight);
        Log.d("debug", "screenWidth" + screenWidth);
        Log.d("debug", "brickWidth" + brickWidth);
        Log.d("debug", "brickHeight" + brickHeigt);


        float leftPosition = SPACEBRICK;
        // ToDo: replace the following line with the value 250
        float topPosition = 50;
        float rightPosition = SPACEBRICK + brickWidth;
        float bottomPosition = topPosition + brickHeigt;
        float radius = brickHeigt/2;

        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                bricks[i][j].setLeftX(leftPosition);
                bricks[i][j].setRightX(rightPosition);
                bricks[i][j].setTopY(topPosition);
                bricks[i][j].setBottomY(bottomPosition);
                if( bricks[i][j].isCollide(ball))
                    bricks[i][j].setVisble(false);

                bricks[i][j].draw(canvas);
                leftPosition = leftPosition + SPACEBRICK + brickWidth;
                rightPosition = rightPosition + SPACEBRICK + brickWidth;
            }
            topPosition = topPosition + brickHeigt + SPACEBRICK;
            leftPosition = SPACEBRICK;
            bottomPosition = bottomPosition + SPACEBRICK + brickHeigt;
            rightPosition = SPACEBRICK + brickWidth;
        }
        if(firstTime){
            leftPosition = screenWidth/2 - brickWidth/2;
            topPosition = screenHeight -150 - brickHeigt/2;
            rightPosition = screenWidth/2 + brickWidth/2;
            bottomPosition = screenHeight - 150;
        }
        else{
            if(movePaddle)
                paddle.move(screenWidth, moveRight);
            leftPosition = paddle.getLeftX();
            topPosition = paddle.getTopY();
            rightPosition = paddle.getRightX();
            bottomPosition = paddle.getBottomY();
        }
        paddle.setLeftX(leftPosition);
        paddle.setTopY(topPosition);
        paddle.setRightX(rightPosition);
        paddle.setBottomY(bottomPosition);
        paddle.draw(canvas);

        if(firstTime) {
            leftPosition = screenWidth / 2;
            topPosition = screenHeight - 150 - brickHeigt / 2 - radius;
        }
        else {
            ball.move(screenWidth, screenHeight);
            leftPosition = ball.getCx();
            topPosition = ball.getCy();
        }
        ball.setCx(leftPosition);
        ball.setCy(topPosition);
        ball.setRadius(radius);
        ball.draw(canvas);

        firstTime = false;
        invalidate();
    }




}
