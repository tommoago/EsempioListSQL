package com.example.esempiolistsql.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.adapters.ToDoAdapter;
import com.example.esempiolistsql.database.ToDoDB;
import com.example.esempiolistsql.database.ToDoTableHelper;
import com.example.esempiolistsql.fragments.ConfirmDialogFragment;
import com.example.esempiolistsql.fragments.ConfirmDialogFragmentListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ConfirmDialogFragmentListener {

    final String tableName = ToDoTableHelper.TABLE_NAME;
    final String sortOrder = ToDoTableHelper.DATE + " ASC ";

    ListView toDoList;
    Button newToDoButton,listUsersButton;

    ToDoAdapter toDoAdapter;

    ToDoDB toDoDatabase;
    SQLiteDatabase database;
    Cursor toDoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.activities_list);

        toDoDatabase = new ToDoDB(this);

        toDoList = findViewById(R.id.listViewToDo);
        newToDoButton = findViewById(R.id.buttonNew);
        listUsersButton=findViewById(R.id.buttonListUsers);
        listUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listUserIntent = new Intent(MainActivity.this, ListUtsersActivity.class);
                startActivity(listUserIntent);
            }
        });

        newToDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insertToDoIntent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(insertToDoIntent);
            }
        });

        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateToDo(position);
            }
        });

        toDoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment(getString(R.string.delete_todo_title),
                        getString(R.string.delete_todo_message),
                        id);
                dialogFragment.show(fragmentManager, ConfirmDialogFragment.class.getName());
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        database = toDoDatabase.getReadableDatabase();

        if (database != null) {
            loadToDos();
        } else {
            //TODO show an empty view inside ListView
            newToDoButton.setEnabled(false);
            Toast.makeText(this, R.string.database_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (toDoItems != null)
            toDoItems.close();
        if (database != null)
            database.close();
    }

    @Override
    public void onPositivePressed(long toDoId) {
        deleteToDo(toDoId);
    }

    @Override
    public void onNegativePressed() {

    }

    private void loadToDos() {
        toDoItems = database.query(tableName, null, null, null,
                null, null, sortOrder);
        if (toDoItems != null) {
            if (toDoAdapter == null) {
                toDoAdapter = new ToDoAdapter(this, toDoItems);
                toDoList.setAdapter(toDoAdapter);
            } else {
                toDoAdapter.changeCursor(toDoItems);
                toDoAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateToDo(int position) {
        if (toDoAdapter != null) {
            Cursor toDoCursor = (Cursor) toDoAdapter.getItem(position);
            if (toDoCursor != null) {
                boolean isDone = toDoCursor.getInt(toDoCursor.getColumnIndex(ToDoTableHelper.DONE)) == 1;
                if (isDone) {
                    Toast.makeText(MainActivity.this, R.string.todo_already_done, Toast.LENGTH_SHORT).show();
                } else if (database != null) {
                    Date today = new Date();
                    String todayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(today);
                    int id = toDoCursor.getInt(toDoCursor.getColumnIndex(ToDoTableHelper._ID));
                    String creationDate = toDoCursor.getString(toDoCursor.getColumnIndex(ToDoTableHelper.DATE));
                    String description = toDoCursor.getString(toDoCursor.getColumnIndex(ToDoTableHelper.DESCRIPTION));

                    String tableName = ToDoTableHelper.TABLE_NAME;
                    String whereClause = ToDoTableHelper._ID + "=?";
                    String[] whereArgs = new String[] { String.valueOf(id) };

                    ContentValues updatedToDoValues = new ContentValues();
                    updatedToDoValues.put(ToDoTableHelper.DATE, creationDate);
                    updatedToDoValues.put(ToDoTableHelper.DESCRIPTION, description);
                    updatedToDoValues.put(ToDoTableHelper.DATE_DONE, todayFormat.toString());
                    updatedToDoValues.put(ToDoTableHelper.DONE, 1);
                    int updatedRows = database.update(tableName, updatedToDoValues, whereClause, whereArgs);
                    if (updatedRows > 0) {
                        Toast.makeText(MainActivity.this, R.string.todo_update_success, Toast.LENGTH_SHORT).show();
                        loadToDos();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.todo_update_error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, R.string.todo_update_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void deleteToDo(long toDoId) {
        if (toDoId > 0 && database != null) {
            String whereClause = ToDoTableHelper._ID + "=?";
            String[] whereArgs = new String[] { String.valueOf(toDoId) };
            int deletedRows = database.delete(ToDoTableHelper.TABLE_NAME, whereClause, whereArgs);
            if (deletedRows > 0) {
                Toast.makeText(MainActivity.this, R.string.todo_delete_success, Toast.LENGTH_SHORT).show();
                loadToDos();
            } else {
                Toast.makeText(MainActivity.this, R.string.todo_delete_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, R.string.todo_delete_error, Toast.LENGTH_SHORT).show();
        }
    }
}
