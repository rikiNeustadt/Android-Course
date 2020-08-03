package com.yaelne_rivkano.ex2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RequiresApi;
import static android.graphics.Color.rgb;

public class GameView extends View {
    private int screenHeight;
    private int screenWidth;
    private final int ROWS = 5;
    private final int COLS = 7;
    private int score;
    private int lives;
    private float brickWidth;
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, AttributeSet atr){
        super( context, atr);
        mpVictory = MediaPlayer.create(context, R.raw.win);
        mpVictory.setVolume(50, 50);
        mpLoss = MediaPlayer.create(context, R.raw.loss);
        mpLoss.setVolume(50, 50);
        mpCollide = MediaPlayer.create(context, R.raw.collide);
        mpCollide.setVolume(50, 50);
        newGame();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();

        // if touch in right side of the screen - move paddle right
        moveRight = touchX > this.screenWidth / 2;
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
            if(lives == 0 || numberOfBricks == 0)
                newGame();
            else if(!playState) {
                playState = true;
                ball.setSpeed();
            }
            invalidate();
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas){
        this.screenHeight = getHeight();
        this.screenWidth = getWidth();
        int SPACEBRICK = 5;
        brickWidth = (screenWidth - (COLS + 1)*(SPACEBRICK))/COLS;
        float brickHeigt = screenHeight / 20;

        float leftPosition = SPACEBRICK;
        float topPosition = 170;
        float rightPosition = SPACEBRICK + brickWidth;
        float bottomPosition = topPosition + brickHeigt;
        float radius = brickHeigt /2;
        boolean oneBrickFlag = true;
        Paint paint = new Paint();

        canvas.drawARGB(100,225,179,214);

        for(int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                bricks[i][j].setLeftX(leftPosition);
                bricks[i][j].setRightX(rightPosition);
                bricks[i][j].setTopY(topPosition);
                bricks[i][j].setBottomY(bottomPosition);
                if (bricks[i][j].isVisible() && bricks[i][j].isCollide(ball) && oneBrickFlag) {
                    bricks[i][j].setVisible(false);
                    mpCollide.start();
                    score += lives * 5;
                    numberOfBricks--;
                    if (numberOfBricks == 0) {
                        paint.setColor(rgb(255, 51, 153));
                        paint.setTextSize(60);
                        canvas.drawText("Game Over - You Win!", (float) (brickWidth * 2.3), screenHeight / 2 + 100, paint);
                        mpVictory.start();
                        playState = false;
                    }
                    ball.changeDirection();
                    oneBrickFlag = false;
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
            if(ball.getCy() + ball.getRadius() > paddle.getTopY()){
                if(paddle.isCollide(ball))
                    ball.changeDirection();
                else {
                    lives--;
                    if(lives == 0) {
                        // Game Over
                        paint.setColor(rgb(255, 51, 153));
                        paint.setTextSize(70);
                        canvas.drawText("Game Over - You Loss!", (float) (brickWidth*2.3), screenHeight/2, paint);
                        mpLoss.start();
                        ball.setCx(ball.getCx() + ball.getDx() * 10);
                        ball.setCy(ball.getCy() + ball.getDy() * 10);
                    }
                    else
                        firstTime = true;
                    playState = false;
                }
            }
        }
        if(firstTime){
            leftPosition = screenWidth/2 - brickWidth/2;
            topPosition = screenHeight -150 - brickHeigt /2;
            rightPosition = screenWidth/2 + brickWidth/2;
            bottomPosition = screenHeight - 150;
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

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(rgb(102, 0, 51));
        paint.setTextSize(50);
        paint.setFakeBoldText(true);
        canvas.drawText("Score: " + score, 100, 100, paint);
        canvas.drawText("Lives: " + lives, screenWidth - 275, 100, paint);
        if(firstTime) {
            paint.setColor(rgb(255, 51, 153));
            paint.setTextSize(60);
            if(lives > 0)
                canvas.drawText("Click to PLAY!", (float) (brickWidth*2.8), screenHeight/2 + 100, paint);
        }
        if((!playState) && lives == 3 && numberOfBricks == COLS*ROWS){
            canvas.drawText("Click to PLAY!", (float) (brickWidth*2.8), screenHeight/2 + 100, paint);
        }
        firstTime = false;

        if(!playState)
            return;
        else
            invalidate();
    }
}