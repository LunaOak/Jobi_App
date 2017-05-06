package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Juan on 4/29/2017.
 */

public class JobiEventContactDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "jobi_event_contact.db";
    private static final int VERSION = 1;

    public JobiEventContactDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + JobiEventDbSchema.ContactTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiEventDbSchema.ContactTable.Columns.ID + ", " +
                JobiEventDbSchema.ContactTable.Columns.EVENT_ID + ", " +
                JobiEventDbSchema.ContactTable.Columns.JOB_TITLE + ", " +
                JobiEventDbSchema.ContactTable.Columns.NAME + ", " +
                JobiEventDbSchema.ContactTable.Columns.EMAIL + ", " +
                JobiEventDbSchema.ContactTable.Columns.PHONE +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
