package com.yaelne_rivkano.ex3;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ToDoAdapter extends ArrayAdapter<ToDoItem> {
    public ToDoAdapter(Activity context, ArrayList<ToDoItem> toDoItemsList) {
        super(context, 0, toDoItemsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);

        ToDoItem todoItem = getItem(position);

        // set todos title
        TextView txvTitle = convertView.findViewById(R.id.txvTitleId);
        txvTitle.setText(todoItem.getTitle());

        // set todos description
        TextView txvdescription = convertView.findViewById(R.id.txvDescriptionId);
        txvdescription.setText(todoItem.getDescription());

        // set todos time
        TextView txvTime = convertView.findViewById(R.id.txvTimeId);
        TextView txvDate = convertView.findViewById(R.id.txvDateId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(todoItem.getDateTime());
        String taskDate = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        String taskTime = new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
        txvTime.setText(taskTime);
        txvDate.setText(taskDate);

        // Return the whole list item layout (containing 4 TextViews)
        // so that it can be shown in the ListView`
        return convertView;
    }

}