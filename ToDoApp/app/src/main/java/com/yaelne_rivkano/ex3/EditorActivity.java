package com.yaelne_rivkano.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditorActivity extends AppCompatActivity {

    private DBManager todoDB;
    private String currentUser;
    private int currentToDoId;
    private EditText edtDescription;
    private EditText edtTitle;
    private EditText edtTime;
    private EditText edtDate;
    private AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        todoDB = new DBManager(EditorActivity.this);
        setTitle("ToDo Editor");
        edtDescription = findViewById(R.id.edtDescriptionId);
        edtTitle = findViewById(R.id.edtTitleId);
        edtTime = findViewById(R.id.edtTimeId);
        edtDate = findViewById(R.id.edtDateId);
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        currentUser = settings.getString("currentUser", "");
        // new todo or exisiting one
        currentToDoId = settings.getInt("currentToDo", -1);
        // get the System Alarm Manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Set up the Notification Broadcast Intent.
        Intent intent = new Intent(this, AlarmReceiver.class);
        if(currentToDoId != -1){
            ToDoItem currentToDo = todoDB.getToDo(currentToDoId);
            edtTitle.setText(currentToDo.getTitle());
            edtDescription.setText(currentToDo.getDescription());
            TextView txvTitlePage = findViewById(R.id.txvAddId);
            txvTitlePage.setText("Edit ToDo");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm");
            Long dateTime = currentToDo.getDateTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateTime);
            edtTime.setText(dateFormatter.format(calendar.getTime()));
            edtDate.setText(timeFormatter.format(calendar.getTime()));
        }
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

                EditText edtDate = findViewById(R.id.edtDateId);
                edtDate.setText(simpleDateFormat.format(calendar.getTime()));
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
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Log.d("debug","Selected Time: " + simpleDateFormat.format(calendar.getTime()));

                EditText edtTime = findViewById(R.id.edtTimeId);
                edtTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true).show();

    }

    public void addToDo(View view) {
        String[] date;
        String[] time;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(edtDate.getText().toString().trim());
            date  = edtDate.getText().toString().split("/", 3);
        } catch (ParseException pe) {
            Toast.makeText(this, "invalid time format. please enter date with the following format: DD/MM/YYYY", Toast.LENGTH_LONG);
            edtDate.setText("");
            return;
        }
        try {
            dateFormat.parse(edtTime.getText().toString().trim());
            time = edtTime.getText().toString().split(":", 2);
        } catch (ParseException pe) {
            Toast.makeText(this, "invalid time format. please enter time with the following format: HH:MM", Toast.LENGTH_LONG);
            edtTime.setText("");
            return;
        }
        if(edtDescription.getText().toString().equals("")){
            Toast.makeText(this, "Please insert text in description ToDo field", Toast.LENGTH_LONG);
            return;
        }
        if(edtTitle.getText().toString().equals("")){
            Toast.makeText(this, "Please insert text in title ToDo field", Toast.LENGTH_LONG);
            return;
        }
        // convert date and time to millis
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0);
        long dateTime = calendar.getTimeInMillis();
        currentToDoId = todoDB.addOrUpdateToDo(currentToDoId, currentUser, edtDescription.getText().toString(), edtTitle.getText().toString(),dateTime);
        // get current Data & Time
        Calendar alarmTime = Calendar.getInstance();
        // set alarmTime to current time + 10sec
        alarmTime.setTimeInMillis(dateTime);
        // create one time alarm at alarmTime
        oneTimeAlarm(alarmTime);
        Intent in = new Intent(EditorActivity.this, ToDoListActivity.class);
        startActivity(in);
    }


    public void oneTimeAlarm(Calendar alarmTime)
    {
        Log.d("debug", "oneTimeAlarm()");

        // Date formatter
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("HH:mm:ss");
        //new SimpleDateFormat("HH:mm:ss dd/MM/yyyy")
        String alarmTimeStr = dateTimeFormatter.format(alarmTime.getTime());

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("title", edtTitle.getText().toString());
        intent.putExtra("msg", edtDescription.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, currentToDoId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long wakeupTime = alarmTime.getTimeInMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeupTime, pendingIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeupTime, pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, wakeupTime, pendingIntent);
    }
}