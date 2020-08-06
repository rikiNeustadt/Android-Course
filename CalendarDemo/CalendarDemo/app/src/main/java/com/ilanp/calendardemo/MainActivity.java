package com.ilanp.calendardemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      // get current Date & Time from the device
      Calendar nowCalendar = Calendar.getInstance();

      // get Date&Time as Milli Seconds
      Long millis = nowCalendar.getTimeInMillis();
      Log.d("debug", ">>>>>>> Date&Time In Millis: " + millis);

      // get DATE & TIME as fields
      int day = nowCalendar.get(Calendar.DAY_OF_MONTH);
      int month = nowCalendar.get(Calendar.MONTH)+1;  // month is zero based: 0 - JANUARY, 1 - FEBRUARY ...
      int year = nowCalendar.get(Calendar.YEAR);
      int hour = nowCalendar.get(Calendar.HOUR);
      int minute = nowCalendar.get(Calendar.MINUTE);
      int second = nowCalendar.get(Calendar.SECOND);
      String time = hour + ":" + minute + ":" + second;
      String date =  day + "/" + month + "/" + year;
      Log.d("debug", ">>>>>>> date: "+ date);
      Log.d("debug", ">>>>>>> time: "+ time);

      // print formatted Date & Time
      Date timeAsDate = nowCalendar.getTime();
      String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(timeAsDate);
      String formattedTime = new SimpleDateFormat("HH:mm:ss").format(timeAsDate);
      Log.d("debug", ">>>>>>> Formatted DATE: " + formattedDate);
      Log.d("debug", ">>>>>>> Formatted TIME: " + formattedTime);

      // create calendar from Milli Seconds date & time
      Calendar copyCalendar = Calendar.getInstance();
      copyCalendar.setTimeInMillis(nowCalendar.getTimeInMillis());
      Log.d("debug", ">>>>>>> Copy Calendar: " + copyCalendar.getTimeInMillis());
   }

   public void showDatePicker(View view)
   {
      final Calendar calendar = Calendar.getInstance();

      DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
      {
         @Override
         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
         {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Log.d("debug","Selected Date: " + simpleDateFormat.format(calendar.getTime()));

            TextView txvTime = findViewById(R.id.txvTimeID);
            txvTime.setText(simpleDateFormat.format(calendar.getTime()));
         }
      };

      new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR),
              calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
   }

   public void showTimePicker(View view)
   {
      final Calendar calendar = Calendar.getInstance();

      TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener()
      {
         @Override
         public void onTimeSet(TimePicker view, int hourOfDay, int minute)
         {
            calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            calendar.set(Calendar.MINUTE,minute);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Log.d("debug","Selected Time: " + simpleDateFormat.format(calendar.getTime()));

            TextView txvTime = findViewById(R.id.txvTimeID);
            txvTime.setText(simpleDateFormat.format(calendar.getTime()));
         }
      };

      new TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
              calendar.get(Calendar.MINUTE), true).show();

   }
}