package com.example.esempiolistsql.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.esempiolistsql.R;
import com.example.esempiolistsql.database.ToDoTableHelper;

public class ToDoAdapter extends CursorAdapter {

    public ToDoAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater vInflater = LayoutInflater.from(context);
        View vView = vInflater.inflate(R.layout.call_todo, null);
        return vView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String vDateIns = cursor.getString(cursor.getColumnIndex(ToDoTableHelper.DATE));
        String vDateDone = cursor.getString(cursor.getColumnIndex(ToDoTableHelper.DATE_DONE));
        String vDescription = cursor.getString(cursor.getColumnIndex(ToDoTableHelper.DESCRIPTION));
        boolean vDone = (cursor.getInt(cursor.getColumnIndex(ToDoTableHelper.DONE)) == 1);
        TextView vDateInsT = view.findViewById(R.id.textViewDataIns),
                vDateDoneT = view.findViewById(R.id.textViewDataDone),
                vDescriptionT = view.findViewById(R.id.textViewDescription);
        CheckBox vCheck = view.findViewById(R.id.done);

        vDateInsT.setText(vDateIns);
        vDateDoneT.setText(vDateDone);
        vDescriptionT.setText(vDescription);
        vCheck.setChecked(vDone);


    }
}
