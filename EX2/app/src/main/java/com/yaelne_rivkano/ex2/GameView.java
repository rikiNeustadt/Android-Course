package com.yaelne_rivkano.ex2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
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


   public GameView(Context context, AttributeSet atr){
       super( context, atr);

       this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
       this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
       brickWidth = (screenHeight - (COLS + 1)*(SPACEBRICK))/COLS;
       brickHeigt = screenWidth/20;

       Log.d("debug", "screenHeight" + screenHeight);
       Log.d("debug", "screenWidth" + screenWidth);
       Log.d("debug", "brickWidth" + brickWidth);
       Log.d("debug", "brickheight" + brickHeigt);

   }

   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
   @Override
    protected void onDraw(Canvas canvas){
       this.screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
       this.screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
       brickWidth = (screenHeight - (COLS + 1)*(SPACEBRICK))/COLS;
       brickHeigt = screenWidth/20;
       Paint pen = brickPaint();
       float leftPosition = SPACEBRICK;
       float topPosition = 250;
       float rightPosition = brickWidth;
       float bottomPosition = brickHeigt;
       float radius = brickHeigt/2;

       for(int i = 0; i < ROWS; i++) {
           for (int j = 0; j < COLS; j++) {
               Log.d("debug", "left Position" + leftPosition);
               //canvas.drawRect(leftPosition, topPosition, rightPosition, bottomPosition, pen);
               leftPosition = leftPosition + SPACEBRICK + brickWidth;
               break;
           }
           Log.d("debug", "top Position" + topPosition);
           topPosition = topPosition + brickHeigt + SPACEBRICK;
           leftPosition = SPACEBRICK;
           break;

       }


       pen = paddlePaint();
       leftPosition = screenHeight/2 - brickWidth/2;
       topPosition = screenHeight -150 - brickHeigt/2;
       rightPosition = brickWidth;
       bottomPosition = brickHeigt/2;
       canvas.drawRoundRect(leftPosition, topPosition, rightPosition, bottomPosition, 0,0, pen);
       Log.d("debug", "left Position paddle" + leftPosition);
       Log.d("debug", "top Position paddle" + topPosition);

       pen = ballPaint();
       leftPosition = screenWidth/2;
       topPosition = screenHeight -150 - brickHeigt/2 - radius;
       canvas.drawCircle(leftPosition, topPosition, radius ,pen);

   }

    private Paint paddlePaint(){
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);
        return p;
    }

    private Paint brickPaint(){
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        return p;
    }

   private Paint ballPaint(){
       Paint p = new Paint();
       p.setColor(Color.BLACK);
       p.setStyle(Paint.Style.FILL);
       return p;
   }
}
