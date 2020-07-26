package com.ilanp.simplegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity
{
    private boolean isRunning = true;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("debug", "onCreate() ");
        //setTitle("Ball Game");

        // Hide the Activity Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the Activity Action Bar
        getSupportActionBar().hide();

        // set Activity(screen) Orientation to LANDSCAPE
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int i = 1;
                while(isRunning)
                {
                    Log.d("debug", Thread.currentThread().getName() + " " + i);
                    SystemClock.sleep(1000);
                    i++;
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d("debug", "onStart() ");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("debug", "onResume() ");
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        Log.d("debug", "onRestart() ");
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d("debug", "onPause() ");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d("debug", "onStop() ");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d("debug", "onDestroy() ");

        isRunning = false;  // stop the thread
        thread.interrupt();  // wakeup if sleeping
    }
}