package com.example.esempiolistsql.activities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.database.ToDoDB;
import com.example.esempiolistsql.database.UserTableHelper;

public class UpdateUserActivity extends AppCompatActivity {

    EditText updateUsername, updateSurname, updateName;
    SQLiteDatabase db;
    long user_id;
    Button buttonUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Bundle extras = getIntent().getExtras();

        updateName = findViewById(R.id.editTextUpdateName);
        updateSurname = findViewById(R.id.editTextUpdateSuername);
        updateUsername = findViewById(R.id.editTextUpdateUsername);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        user_id = getIntent().getLongExtra(ListUtsersActivity.USER_ID, 0 );
        db = new ToDoDB(this).getWritableDatabase();
        Cursor cursor = db.query(UserTableHelper.TABLE_NAME , null, UserTableHelper._ID + " = " + user_id, null, null, null, null);
        cursor.moveToNext();

        updateName.setText(cursor.getString(cursor.getColumnIndex(UserTableHelper.NAME)));
        updateUsername.setText(cursor.getString(cursor.getColumnIndex(UserTableHelper.USERNAME)));
        updateSurname.setText(cursor.getString(cursor.getColumnIndex(UserTableHelper.SURNAME)));

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (updateName.getText().toString().equals("") || updateSurname.getText().toString().equals("") || updateUsername.getText().toString().equals("")) {
                    Toast.makeText(UpdateUserActivity.this, "Devi riempire tutti i campi", Toast.LENGTH_SHORT).show();
                } else {
                    doUpdate();
                }



            }


        });


    }

    private void doUpdate() {

        ContentValues contentValues=  new ContentValues();
        contentValues.put(UserTableHelper.NAME, updateName.getText().toString());
        contentValues.put(UserTableHelper.SURNAME, updateSurname.getText().toString());
        contentValues.put(UserTableHelper.USERNAME, updateUsername.getText().toString());

        int result = db.update(UserTableHelper.TABLE_NAME, contentValues , UserTableHelper._ID + " = " + user_id, null);

        if (result>0){
            Toast.makeText(UpdateUserActivity.this, "Update Successful", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(UpdateUserActivity.this, "Update ERROR", Toast.LENGTH_LONG).show();
        }
        finish();

    }
}
