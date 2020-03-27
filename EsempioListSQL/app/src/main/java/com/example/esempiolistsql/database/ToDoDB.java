package com.example.esempiolistsql.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ToDoDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "todo.db";
    public static final int VERSION = 1;

    public ToDoDB(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ToDoTableHelper.CREATE);

        for (int i = 0; i < 10; i++) {
            ContentValues vValues = new ContentValues();
            vValues.put(ToDoTableHelper.DATE, "2" + i + "/2/2020");
            vValues.put(ToDoTableHelper.DATE_DONE, "2" + (i + 4) + "/2/2020");
            vValues.put(ToDoTableHelper.DESCRIPTION, "descrizione" + i);
            if (i % 2 == 0) {
                vValues.put(ToDoTableHelper.DONE, 1);
            }
            db.insert(ToDoTableHelper.TABLE_NAME, null, vValues);

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
