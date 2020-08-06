package com.ilanp.sqlitedbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// ===========================================================
// Lesson 6 - SQLite DB Demo. (29.7.20)
// Ilan Perez (ilanperets@gmail.com)
// ===========================================================
public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String MY_DB_NAME = "contacts.db";

    private SQLiteDatabase contactsDB = null;
    private Button btnCreateDB, btnDelDB, btnAddContact, btnDelContact, btnShowContacts;
    private EditText edtName, edtEmail, edtId, edtShow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateDB = findViewById(R.id.btnCreateDBID);
        btnDelDB = findViewById(R.id.btnDelDBID);

        btnAddContact = findViewById(R.id.btnAddContactID);
        btnDelContact = findViewById(R.id.btnDelContactID);
        btnShowContacts = findViewById(R.id.btnShowContactsID);

        edtName = findViewById(R.id.edtNameID);
        edtEmail = findViewById(R.id.edtEmailID);
        edtId = findViewById(R.id.edtIdID);
        edtShow = findViewById(R.id.edtShowID);

        btnCreateDB.setOnClickListener(this);
        btnDelDB.setOnClickListener(this);
        btnAddContact.setOnClickListener(this);
        btnDelContact.setOnClickListener(this);
        btnShowContacts.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnCreateDBID:
                createDB();
                break;

            case R.id.btnDelDBID:
                deleteDB();
                break;

            case R.id.btnAddContactID:
                addContact();
                break;

            case R.id.btnDelContactID:
                deleteContact();
                break;

            case R.id.btnShowContactsID:
                showContacts();
                break;
        }
    }

    public void createDB()
    {
        try
        {
            // Opens a current database or creates it
            // Pass the database name, designate that only this app can use it
            // and a DatabaseErrorHandler in the case of database corruption
            contactsDB = openOrCreateDatabase(MY_DB_NAME, MODE_PRIVATE, null);

            // build an SQL statement to create 'contacts' table (if not exists)
            String sql = "CREATE TABLE IF NOT EXISTS contacts (id INTEGER primary key, name VARCHAR, email VARCHAR);";
            contactsDB.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.d("debug", "Error Creating Database");
        }

        // Make buttons clickable since the database was created
        btnAddContact.setEnabled(true);
        btnDelContact.setEnabled(true);
        btnShowContacts.setEnabled(true);
        btnDelDB.setEnabled(true);
        btnCreateDB.setEnabled(false);
    }

    public void deleteDB()
    {
        // Delete database
        deleteDatabase(MY_DB_NAME);

        btnAddContact.setEnabled(false);
        btnDelContact.setEnabled(false);
        btnShowContacts.setEnabled(false);
        btnDelDB.setEnabled(false);
        btnCreateDB.setEnabled(true);

        edtShow.setText("");
        edtName.setText("");
        edtId.setText("");
        edtEmail.setText("");
    }

    public void addContact()
    {
        // Get the contact name and email entered
        String contactName = edtName.getText().toString();
        String contactEmail = edtEmail.getText().toString();

        // Execute SQL statement to insert new data
        String sql = "INSERT INTO contacts (name, email) VALUES ('" + contactName + "', '" + contactEmail + "');";
        contactsDB.execSQL(sql);
        Toast.makeText(this, contactName + " was insert!", Toast.LENGTH_SHORT).show();
    }

    public void showContacts()
    {
        // A Cursor provides read and write access to database results
        String sql = "SELECT * FROM contacts";
        Cursor cursor = contactsDB.rawQuery(sql, null);

        // Get the index for the column name provided
        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int emailColumn = cursor.getColumnIndex("email");

        String contactList = "";

        // Move to the first row of results & Verify that we have results
        if (cursor.moveToFirst()) {
            do {
                // Get the results and store them in a String
                String id = cursor.getString(idColumn);
                String name = cursor.getString(nameColumn);
                String email = cursor.getString(emailColumn);

                contactList = contactList + id + ", " + name + ", " + email + "\n";

                // Keep getting results as long as they exist
            } while (cursor.moveToNext());

            edtShow.setText(contactList);

        } else {

            Toast.makeText(this, "No Results to Show", Toast.LENGTH_SHORT).show();
            edtShow.setText("");
        }
    }

    public void deleteContact()
    {
        // Get the id to delete
        String id = edtId.getText().toString();

        // Delete matching id in database
        String sql = "DELETE FROM contacts WHERE id = " + id + ";";
        contactsDB.execSQL(sql);
        Toast.makeText(this, id + " was delete!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        contactsDB.close();
        super.onDestroy();
    }
}