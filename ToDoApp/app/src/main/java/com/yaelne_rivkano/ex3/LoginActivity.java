package com.yaelne_rivkano.ex3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity{

    private Button btnLogin;
    private EditText edtUserName, edtPassword;
    private DBManager todoDB;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem menuItem1 = menu.add("About");
        MenuItem menuItem2 = menu.add("Exit");

        menuItem1.setOnMenuItemClickListener(new  MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(LoginActivity.this);
                myDialog.setTitle("About ToDo App");
                myDialog.setMessage("This app implement ToDo manager.\nBy Riki Neustadt and Yael Neeman");
                myDialog.show();
                return true;
            }
        });

        menuItem2.setOnMenuItemClickListener(new  MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(LoginActivity.this);
                myDialog.setTitle("Exit ToDo App");
                myDialog.setMessage("Do you really want to exit the app?");
                myDialog.setCancelable(false);
                myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();// destroy this activity
                    }
                });
                myDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.d(“debug”, “No”);
                    }
                });
                myDialog.show();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("ToDo Login");
        todoDB = new DBManager(LoginActivity.this);
        btnLogin = findViewById(R.id.btnLoginId);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                boolean is_valid_user = true;
                edtUserName = findViewById(R.id.edtUserNamedId);
                edtPassword = findViewById(R.id.edtPassowrdId);
                String userName = edtUserName.getText().toString();
                String password = edtPassword.getText().toString();
                if(todoDB.isUserExists(userName)) {
                    is_valid_user = todoDB.checkCredentials(userName, edtPassword);
                }
                else {
                    todoDB.addUser(userName, password);
                    Toast.makeText(LoginActivity.this, userName + " was insert!", Toast.LENGTH_SHORT).show();
                }
                if(is_valid_user) {
                    // save current user for next activity
                    SharedPreferences setting = getApplicationContext().getSharedPreferences("settings", 0);
                    SharedPreferences.Editor editor = setting.edit();
                    editor.putString("currentUser", userName);
                    editor.apply();
                    Log.d("debug","userName "+userName );
                    // move to next activity
                    Intent in = new Intent(LoginActivity.this, ToDoListActivity.class);
                    startActivity(in);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_LONG).show();
                    edtPassword.setText("");
                }
            }
        });
    }




}