package com.example.esempiolistsql.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.esempiolistsql.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InsertActivity extends AppCompatActivity {

    TextView dateLabel;
    EditText descriptionInput;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        setTitle(R.string.add_new_activity);

        dateLabel = findViewById(R.id.textData);

        Date today = new Date();
        String todayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(today);

        dateLabel.setText(todayFormat);
    }
}
