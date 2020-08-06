package com.yaelne_rivkano.ex3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class DBManager {
    public static final String MY_DB_NAME = "todo.db";
    private SQLiteDatabase todoDB = null;
    private Context context;
    public DBManager(Context cntx){
        context = cntx;
        createDB();
    }




    public boolean addUser(String userName, String password)
    {
        // Execute SQL statement to insert new data
        String sql = "INSERT INTO users (username, password) VALUES ('" + userName + "', '" + password + "');";
        todoDB.execSQL(sql);
        return true;
    }


    public void createDB()
    {
        try
        {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            todoDB = context.openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);

            // build an SQL statement to create 'users and todo' table (if not exists)
            String sql = "CREATE TABLE IF NOT EXISTS users (username VARCHAR primary key, password VARCHAR);";
            todoDB.execSQL(sql);
            sql = String.format("CREATE TABLE IF NOT EXISTS todos (_id INTEGER primary key AUTOINCREMENT,FOREIGN KEY(username) REFERENCES users(username),title VARCHAR, description VARCHAR, datetime INTEGER);");
            todoDB.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating Database");
        }
    }


    public boolean checkCredentials(String userName, EditText password){
        // validate username and password by checking in DB
        String sql = "SELECT password FROM users WHERE username=\"" + userName + "\"";
        String originalPassword = "";
        Cursor cursor = todoDB.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            //cursor.moveToNext();
            originalPassword = cursor.getString(0);
        }
        if(originalPassword.equals(password.getText().toString()))
            return true;
        return false;
    }

    public boolean isUserExists(String userName){
        boolean userExists = false;
        String sql = "SELECT username FROM users";
        Cursor cursor = todoDB.rawQuery(sql, null);
        // Move to the first row of results & Verify that we have results
        if (cursor.moveToFirst()) {
            do {
                // Get the results and store them in a String
                String currentUserName = cursor.getString(0);
                if(currentUserName.equals(userName)){
                    userExists = true;
                    break;
                }
                // Keep getting results as long as they exist
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userExists;
    }

    public ArrayList<ToDoItem> searchText(String query, String userName){
        ArrayList toDoItems= new ArrayList<ToDoItem>();
        String sql = "SELECT * FROM todos WHERE username=\"" + userName + "\" and title like '%" + query +"%' or description like '%" + query +"%';";
        Cursor cursor = todoDB.rawQuery(sql, null);
        // Get the index for the column name provided
        int titleColumn = cursor.getColumnIndex("title");
        int descriptionColumn = cursor.getColumnIndex("description");
        int dateTimeColumn = cursor.getColumnIndex("datetime");

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                // Get the results and store them in a String
                String title = cursor.getString(titleColumn);
                String description = cursor.getString(descriptionColumn);
                Long dateTime = cursor.getLong(dateTimeColumn);
                ToDoItem newItem = new ToDoItem(title, description, dateTime);
                toDoItems.add(newItem);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return toDoItems;
    }



}
