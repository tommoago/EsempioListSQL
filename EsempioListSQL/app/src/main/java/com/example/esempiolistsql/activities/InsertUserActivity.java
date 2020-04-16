package com.example.esempiolistsql.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.database.ToDoDB;
import com.example.esempiolistsql.database.UserTableHelper;

public class InsertUserActivity extends AppCompatActivity {
    EditText name, surname, username;
    Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user);

        name = findViewById(R.id.editTextName);
        surname = findViewById(R.id.editTextSuername);
        username = findViewById(R.id.editTextUsername);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("") || surname.getText().toString().equals("") || username.getText().toString().equals("")) {
                    Toast.makeText(InsertUserActivity.this, "Devi riempire tutti i campi", Toast.LENGTH_SHORT).show();
                } else {
                    doInsert();
                }
            }
        });
    }

    private void doInsert() {
        SQLiteDatabase db = new ToDoDB(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTableHelper.NAME, name.getText().toString());
        values.put(UserTableHelper.SURNAME, surname.getText().toString());
        values.put(UserTableHelper.USERNAME, username.getText().toString());
        long result=db.insert(UserTableHelper.TABLE_NAME, null, values);
        if(result>0){
            Toast.makeText(this, "inserimento eseguito", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(this, "ops c'è stato un errore", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
