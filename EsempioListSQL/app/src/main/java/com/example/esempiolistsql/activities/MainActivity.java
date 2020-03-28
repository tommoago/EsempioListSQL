package com.example.esempiolistsql.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.adapters.ToDoAdapter;
import com.example.esempiolistsql.database.ToDoDB;
import com.example.esempiolistsql.database.ToDoTableHelper;

public class MainActivity extends AppCompatActivity {

    ListView mList;
    Button mButtonNew;

    ToDoAdapter mToDoAdapter;

    SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.activities_list);

        mButtonNew = findViewById(R.id.buttonNew);
        mList = findViewById(R.id.listViewToDo);

        mButtonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insertToDoIntent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(insertToDoIntent);
            }
        });

        ToDoDB toDoDatabase = new ToDoDB(this);
        mDatabase =  toDoDatabase.getReadableDatabase();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDatabase != null) {
            loadToDos();
        } else {
            //TODO show an empty view inside ListView
            mButtonNew.setEnabled(false);
            Toast.makeText(this, R.string.database_error, Toast.LENGTH_SHORT).show();
        }

    }

    private void loadToDos() {
        final String tableName = ToDoTableHelper.TABLE_NAME;
        final String sortOrder = ToDoTableHelper.DATE + " ASC ";
        final Cursor toDoItems = mDatabase.query(tableName, null, null, null,
                null, null, sortOrder);
        if (toDoItems != null) {
            if (mToDoAdapter == null) {
                mToDoAdapter = new ToDoAdapter(this, toDoItems);
                mList.setAdapter(mToDoAdapter);
            } else {
                mToDoAdapter.changeCursor(toDoItems);
                mToDoAdapter.notifyDataSetChanged();
            }
        }
    }
}

