package com.example.esempiolistsql.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.adapters.UserAdapter;
import com.example.esempiolistsql.database.ToDoDB;
import com.example.esempiolistsql.database.ToDoTableHelper;
import com.example.esempiolistsql.database.UserTableHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InsertActivity extends AppCompatActivity {

    TextView dateLabel;
    EditText descriptionInput;
    Button saveButton;
    Spinner spinnerUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        setTitle(R.string.add_new_activity);

        dateLabel = findViewById(R.id.creationDate);
        descriptionInput = findViewById(R.id.descriptionInput);
        saveButton = findViewById(R.id.saveButton);
        spinnerUsers=findViewById(R.id.spinnerUsers);
        popolateSpinnerUsers();
        spinnerUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        Date today = new Date();
        String todayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(today);

        dateLabel.setText(todayFormat);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertToDo();
            }
        });
    }

    private void popolateSpinnerUsers() {
        ToDoDB db = new ToDoDB(this);
        SQLiteDatabase toDoDatabase = db.getReadableDatabase();
        Cursor cursor=toDoDatabase.query(UserTableHelper.TABLE_NAME,null,null,null,null,null,null);

        UserAdapter adapter=new UserAdapter(this,cursor);
        spinnerUsers.setAdapter(adapter);

    }

    private void insertToDo() {
        String description = null;
        if (!TextUtils.isEmpty(descriptionInput.getText())) {
            description = descriptionInput.getText().toString();
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Inserisci una descrizione valida", Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(dateLabel.getText())) {
                ContentValues toDoValues = new ContentValues();
                toDoValues.put(ToDoTableHelper.DATE, dateLabel.getText().toString());
                toDoValues.put(ToDoTableHelper.DESCRIPTION, description);

                ToDoDB db = new ToDoDB(this);
                SQLiteDatabase toDoDatabase = db.getReadableDatabase();
                if (toDoDatabase != null) {
                    long toDoId = toDoDatabase.insert(ToDoTableHelper.TABLE_NAME, null, toDoValues);
                    if (toDoId > 0) {
                        Toast.makeText(this, "Attività aggiunta con successo", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Qualcosa è andato storto durante l'aggiunta dell'attività", Toast.LENGTH_SHORT).show();
                    }
                    toDoDatabase.close();
                }
            }
        }

    }

}
