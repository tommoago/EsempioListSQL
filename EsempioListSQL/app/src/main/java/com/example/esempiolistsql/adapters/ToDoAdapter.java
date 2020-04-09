package com.example.esempiolistsql.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.database.ToDoDB;
import com.example.esempiolistsql.database.ToDoTableHelper;
import com.example.esempiolistsql.database.UserTableHelper;

public class ToDoAdapter extends CursorAdapter {

    public ToDoAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater vInflater = LayoutInflater.from(context);
        View vView = vInflater.inflate(R.layout.cell_todo, null);
        return vView;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        String creationDate = cursor.getString(cursor.getColumnIndex(ToDoTableHelper.DATE));
        String doneDate = cursor.getString(cursor.getColumnIndex(ToDoTableHelper.DATE_DONE));
        String description = cursor.getString(cursor.getColumnIndex(ToDoTableHelper.DESCRIPTION));
        boolean isDone = cursor.getInt(cursor.getColumnIndex(ToDoTableHelper.DONE)) == 1;
        int id = cursor.getInt(cursor.getColumnIndex(ToDoTableHelper.ID_USER));
        Log.d("asda", "bindView: "+id);

        String username = "";

        SQLiteDatabase db = new ToDoDB(context).getReadableDatabase();
        Cursor datas = db.query(UserTableHelper.TABLE_NAME, new String[]{UserTableHelper.USERNAME, UserTableHelper._ID}, UserTableHelper._ID + " = " + id,
                null, null, null, null);
        datas.moveToNext();
        username = datas.getString(datas.getColumnIndex(UserTableHelper.USERNAME));

        TextView creationDateLabel = view.findViewById(R.id.textViewDataIns),
                doneDateLabel = view.findViewById(R.id.textViewDataDone),
                descriptionLabel = view.findViewById(R.id.textViewDescription),
                usernameLabel = view.findViewById(R.id.textViewUsername);
        ImageView checkImage = view.findViewById(R.id.done);

        usernameLabel.setText(username);
        creationDateLabel.setText(creationDate);
        doneDateLabel.setText(doneDate);
        descriptionLabel.setText(description);
        checkImage.setImageTintList(ColorStateList.valueOf(isDone ? ContextCompat.getColor(context, R.color.colorAccent) : Color.GRAY));
    }

}
