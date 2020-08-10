package com.yaelne_rivkano.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;



import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private ArrayList<ToDoItem> toDosList;
    private ToDoAdapter toDoAdapter;
    private ListView listViewToDos;
    private String currentUserName;
    private DBManager todoDB;
    private AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        todoDB = new DBManager(ToDoListActivity.this);
        // set title with current user name
        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        currentUserName = settings.getString("currentUser", "");
        Log.d("debug","userName "+currentUserName );
        // get the System Alarm Manager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        setTitle("ToDo List (" +currentUserName + ")");

        // listener for flouting button
        fab = findViewById(R.id.fabID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mark current todo as -1 in order to indicate to create new todo, neither load existing todo
                SharedPreferences setting = getApplicationContext().getSharedPreferences("settings", 0);
                SharedPreferences.Editor editor = setting.edit();
                editor.putInt("currentToDo", -1);
                editor.apply();
                Intent in = new Intent(ToDoListActivity.this, EditorActivity.class);
                startActivity(in);
            }
        });
        // connect todos array list to list view
        toDosList = new ArrayList<>();
        // display all toDos for current user
        updateList("");


        SearchView simpleSearchView = findViewById(R.id.searchViewId);
        // perform set on query text listener event
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // when text in search field changed, display found items
                updateList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // when text in search field changed, display found items
                updateList(query);
                return true;
            }
        });

        listViewToDos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("debug", "position " +position + "id " + id);
                int idToDo = toDosList.get(position).getId();
                SharedPreferences setting = getApplicationContext().getSharedPreferences("settings", 0);
                SharedPreferences.Editor editor = setting.edit();
                editor.putInt("currentToDo", idToDo);
                editor.apply();
                Intent in = new Intent(ToDoListActivity.this, EditorActivity.class);
                startActivity(in);
            }
        });

        listViewToDos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long id) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(ToDoListActivity.this);
                myDialog.setTitle("Delet ToDo");
                myDialog.setMessage("Are you sure you want to delete this toDo?");
                myDialog.setCancelable(false);
                myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int idToDo = toDosList.get(position).getId();
                        Intent intent = new Intent(ToDoListActivity.this, AlarmReceiver.class);
                        PendingIntent sender = PendingIntent.getBroadcast(ToDoListActivity.this, idToDo, intent, 0);
                        alarmManager.cancel(sender);
                        todoDB.delToDo(idToDo);
                    }
                });
                myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                myDialog.show();
                return true;
            }
        });
    }

    public void updateList(String query){
        toDosList = todoDB.searchText(query, currentUserName);
        toDoAdapter = new ToDoAdapter(ToDoListActivity.this, toDosList);
        listViewToDos = findViewById(R.id.listViewToDoId);
        listViewToDos.setAdapter(toDoAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create inflate
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // go to previous page on clicking back button
        Intent in = new Intent(ToDoListActivity.this, LoginActivity.class);
        startActivity(in);
        return true;
    }
}