package com.yaelne_rivkano.ex2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
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
    private int score;
    private int lives;
    private float brickWidth;
    private float brickHeigt;
    private Ball ball;
    private Brick [][] bricks;
    private Paddle paddle;
    private Boolean firstTime;
    private boolean moveRight;
    private boolean movePaddle;
    private int numberOfBricks;
    private boolean playState;
    private final MediaPlayer mpVictory;
    private final MediaPlayer mpLoss;
    private final MediaPlayer mpCollide;



    public GameView(Context context, AttributeSet atr){
        super( context, atr);
        float screenWidth = getWidth();
        mpVictory = MediaPlayer.create(context, R.raw.win);
        mpVictory.setVolume(50, 50);
        mpLoss = MediaPlayer.create(context, R.raw.loss);
        mpLoss.setVolume(50, 50);
        mpCollide = MediaPlayer.create(context, R.raw.collide);
        mpCollide.setVolume(50, 50);
        newGame();
    }

    public void newGame(){
        ball = new Ball();
        bricks = new Brick [ROWS][COLS];
        for(int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLS; j++)
                bricks[i][j] = new Brick();
        paddle = new Paddle();
        firstTime = true;
        moveRight = true;
        movePaddle = false;
        playState = false;
        score = 0;
        lives = 3;
        numberOfBricks = COLS*ROWS;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();

        // if touch in right side of the screen - move paddle right
        if(touchX > this.screenWidth/2)
            moveRight = true;
        else
            moveRight = false;
        if(playState)
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    movePaddle = true;
                    break;
                case MotionEvent.ACTION_UP:
                    movePaddle = false;
                    break;
            }
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (lives == 0)
                newGame();
            else
                playState = true;
            invalidate();
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

        float leftPosition = SPACEBRICK;
        // ToDo: replace the following line with the value 250
        float topPosition = 50;
        float rightPosition = SPACEBRICK + brickWidth;
        float bottomPosition = topPosition + brickHeigt;
        float radius = brickHeigt/2;
        Paint paint = new Paint();


        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                bricks[i][j].setLeftX(leftPosition);
                bricks[i][j].setRightX(rightPosition);
                bricks[i][j].setTopY(topPosition);
                bricks[i][j].setBottomY(bottomPosition);
                if( bricks[i][j].isVisble() && bricks[i][j].isCollide(ball)) {
                    bricks[i][j].setVisble(false);
                    mpCollide.start();
                    score += lives*5;
                    numberOfBricks--;
                    if(numberOfBricks == 0){
                        paint.setColor(Color.GREEN);
                        paint.setTextSize(60);
                        canvas.drawText("Game Over - You Win!", 250, screenHeight/2, paint);
                        mpVictory.start();
                        playState = false;
                        return;
                    }
                    ball.changeDirection();
                }
                bricks[i][j].draw(canvas);
                leftPosition = leftPosition + SPACEBRICK + brickWidth;
                rightPosition = rightPosition + SPACEBRICK + brickWidth;
            }
            topPosition = topPosition + brickHeigt + SPACEBRICK;
            leftPosition = SPACEBRICK;
            bottomPosition = bottomPosition + SPACEBRICK + brickHeigt;
            rightPosition = SPACEBRICK + brickWidth;
        }
        if(!firstTime){
            if(movePaddle)
                paddle.move(screenWidth, moveRight);
            leftPosition = paddle.getLeftX();
            topPosition = paddle.getTopY();
            rightPosition = paddle.getRightX();
            bottomPosition = paddle.getBottomY();

            // ball is on the bottom screen
            if(ball.getCy() + ball.getRadius() >= paddle.getTopY()){
                if(paddle.isCollide(ball))
                    ball.changeDirection();
                else {
                    lives--;
                    //paddle.setLeftX();
                    if(lives == 0) {
                        // Game Over
                        paint.setColor(Color.GREEN);
                        paint.setTextSize(60);
                        canvas.drawText("Game Over - You Loss!", 250, screenHeight/2, paint);
                        mpLoss.start();
                    }
                    playState = false;
                    Log.d("debug", "set flag in ball");
                    firstTime = true;
                }
            }
        }
        if(firstTime){
            leftPosition = screenWidth/2 - brickWidth/2;
            topPosition = screenHeight -150 - brickHeigt/2;
            rightPosition = screenWidth/2 + brickWidth/2;
            bottomPosition = screenHeight - 150;
            Log.d("debug", "in first time, left pos is: "+leftPosition);
        }
        paddle.setLeftX(leftPosition);
        paddle.setTopY(topPosition);
        paddle.setRightX(rightPosition);
        paddle.setBottomY(bottomPosition);
        paddle.draw(canvas);

        if(firstTime) {
            leftPosition = screenWidth / 2;
            topPosition = screenHeight - 150 - brickHeigt / 2 - radius;
            Log.d("debug", "ball, first time");
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

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        canvas.drawText("Score: " + score, 10, 25, paint);
        canvas.drawText("Lives: " + lives, screenWidth - 200, 25, paint);

        if(firstTime) {
            paint.setColor(Color.YELLOW);
            paint.setTextSize(60);
            Log.d("debug", "Lives: " + lives);
            Log.d("debug", "playState: "+ playState);
            if(lives > 0){
                canvas.drawText("Click to PLAY!", 250, screenHeight/2, paint);
                //firstTime = false;
            }
        }
        firstTime = false;
        if(!playState)
            return;
        else
            invalidate();
    }




}
