package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Juan on 4/29/2017.
 */

public class JobiEventReminderDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "jobi_event_reminder.db";
    private static final int VERSION = 1;

    public JobiEventReminderDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + JobiEventDbSchema.ReminderTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiEventDbSchema.ReminderTable.Columns.ID + ", " +
                JobiEventDbSchema.ReminderTable.Columns.TITLE + ", " +
                JobiEventDbSchema.ReminderTable.Columns.DATE +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
