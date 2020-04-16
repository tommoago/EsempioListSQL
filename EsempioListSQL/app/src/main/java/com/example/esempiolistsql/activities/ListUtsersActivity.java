package com.example.esempiolistsql.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.adapters.UserAdapter;
import com.example.esempiolistsql.database.ToDoDB;
import com.example.esempiolistsql.database.UserTableHelper;

public class ListUtsersActivity extends AppCompatActivity {

    final String tableName = UserTableHelper.TABLE_NAME;
    final String sortOrder = UserTableHelper.SURNAME + " ASC ";


    ListView list;
    UserAdapter adapter;
    ToDoDB toDoDB;
    SQLiteDatabase db;
    Button newUser;
    Cursor users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        list = findViewById(R.id.listUsers);
        toDoDB = new ToDoDB(this);
        newUser = findViewById(R.id.buttonNewUser);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListUtsersActivity.this, InsertUserActivity.class));
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        db = toDoDB.getReadableDatabase();
        if (db != null) {
            loadUsers();
        } else {
            //TODO show an empty view inside ListView
            newUser.setEnabled(false);
            Toast.makeText(this, R.string.database_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUsers() {
        users = db.query(UserTableHelper.TABLE_NAME, null, UserTableHelper._ID + " != 0", null,
                null, null, sortOrder);
        if (users != null) {
            if (adapter == null) {
                adapter = new UserAdapter(this, users);
                list.setAdapter(adapter);
            } else {
                adapter.changeCursor(users);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (users != null)
            users.close();
        if (db != null)
            db.close();
    }

}
