package com.yaelne_rivkano.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private ArrayList<ToDoItem> toDosList;
    private ToDoAdapter toDoAdapter;
    private ListView listViewToDos;
    private String currentUserName;
    private DBManager todoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        todoDB = new DBManager(ToDoListActivity.this);
        // set title with current user name
        SharedPreferences setting = getApplicationContext().getSharedPreferences("settings", 0);
        currentUserName = setting.getString("currentUser", "");
        setTitle("ToDo List (" +currentUserName + ")");

        // listener for flouting button
        fab = findViewById(R.id.fabID);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent in = new Intent(ToDoListActivity.this, EditorActivity.class);
            startActivity(in);
            }
        });
        // connect todos array list to list view
        toDosList = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(this, toDosList);
        listViewToDos = findViewById(R.id.listViewToDoId);
        listViewToDos.setAdapter(toDoAdapter);
        // display all toDos for current user
        toDosList = todoDB.searchText("", currentUserName);


        SearchView simpleSearchView = findViewById(R.id.searchViewId);
        // perform set on query text listener event
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // when text in search field changed, display found items
                toDosList = todoDB.searchText(query, currentUserName);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // when text in search field changed, display found items
                toDosList = todoDB.searchText(query, currentUserName);
                return true;
            }
        });
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