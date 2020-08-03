package com.yaelne_rivkano.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String MY_DB_NAME = "todo.db";

    private SQLiteDatabase todoDB = null;
    private Button btnLogin;
    private EditText edtUserName, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createDB();
        btnLogin = findViewById(R.id.btnLoginId);
        btnLogin.setEnabled(true);
    }

    @Override
    public void onClick(View v)
    {
        EditText userName = findViewById(R.id.edtUserNamedId);
        EditText password = findViewById(R.id.edtPassowrdId);
        String sql = "SELECT username FROM users";
        boolean userExists = false;
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
        if(!userExists)
            addUser(userName.getText().toString(), password.getText().toString());
        else
            checkCredentials(userName.getText().toString(), password);
    }


    public void checkCredentials(String userName, EditText password){
        String sql = "SELECT password FROM users WHERE username =" + userName;
        String originalPassword = "";
        Cursor cursor = todoDB.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            cursor.moveToNext();
            originalPassword = cursor.getString(0);
        }
        if(originalPassword.equals(password.getText().toString()))
            Log.d("debug", "move to next activity");
        Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show();
        password.setText("");
    }

    public void addUser(String userName, String password)
    {
        // Execute SQL statement to insert new data
        String sql = "INSERT INTO contacts (username, password) VALUES ('" + userName + "', '" + password + "');";
        todoDB.execSQL(sql);
        Toast.makeText(this, userName + " was insert!", Toast.LENGTH_SHORT).show();
        Log.d("debug", "move to next activity");
    }

    public void createDB()
    {
        try
        {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            todoDB = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);

            // build an SQL statement to create 'contacts' table (if not exists)
            String sql = "CREATE TABLE IF NOT EXISTS users (username VARCHAR primary key, password VARCHAR);";
            todoDB.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating Database");
        }
    }


}