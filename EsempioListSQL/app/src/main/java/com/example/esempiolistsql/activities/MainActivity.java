package com.example.esempiolistsql.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.adapters.ToDoAdapter;
import com.example.esempiolistsql.database.ToDoDB;
import com.example.esempiolistsql.database.ToDoTableHelper;

public class MainActivity extends AppCompatActivity {

    ListView mList;
    Button mButtonNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = findViewById(R.id.listViewToDo);
        mButtonNew = findViewById(R.id.buttonNew);

        Cursor vElements = new ToDoDB(this).getReadableDatabase().query(ToDoTableHelper.TABLE_NAME,
                null, null, null, null, null, null);

        ToDoAdapter vAdapter = new ToDoAdapter(this, vElements);

        mList.setAdapter(vAdapter);
    }
}
