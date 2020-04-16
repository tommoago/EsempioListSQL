package com.example.esempiolistsql.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.adapters.UserAdapter;
import com.example.esempiolistsql.database.ToDoDB;
import com.example.esempiolistsql.database.UserTableHelper;
import com.example.esempiolistsql.fragments.ConfirmDialogFragment;
import com.example.esempiolistsql.fragments.ConfirmDialogFragmentListener;

import java.util.List;

public class ListUtsersActivity extends AppCompatActivity  implements ConfirmDialogFragmentListener {

    final String tableName = UserTableHelper.TABLE_NAME;
    final String sortOrder = UserTableHelper.SURNAME + " ASC ";


    ListView list;
    UserAdapter adapter;
    ToDoDB toDoDB;
    SQLiteDatabase db;
    Button newUser;
    Cursor users;
    public static final String USER_ID = "USER_ID";


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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent(ListUtsersActivity.this, UpdateUserActivity.class);
                intent.putExtra(USER_ID, l);
                startActivity(intent);

            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                users.moveToPosition(position);
                String username = users.getString(users.getColumnIndex(UserTableHelper.USERNAME));
                ConfirmDialogFragment dialogFragment = new ConfirmDialogFragment("ATTENZIONE","Sei sicuro di voler cancellare l'utente " +username +" ?",id);
                dialogFragment.show(getSupportFragmentManager(),null);
                return true;
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

    @Override
    public void onPositivePressed(long toDoId) {
        db = toDoDB.getWritableDatabase();
        db.delete(UserTableHelper.TABLE_NAME,UserTableHelper._ID + " = ? ",new String[] {String.valueOf(toDoId)} );
        onResume();
        Toast.makeText(this,"Utente Eliminato",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNegativePressed() {
        Toast.makeText(this,"Operazione annullata",Toast.LENGTH_LONG).show();
    }
}
