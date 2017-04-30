package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Juan on 4/28/2017.
 */

public class JobiEventDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "jobi_event.db";
    private static final int VERSION = 1;

    public JobiEventDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + JobiEventDbSchema.EventTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiEventDbSchema.EventTable.Columns.ID + ", " +
                JobiEventDbSchema.EventTable.Columns.TITLE + ", " +
                JobiEventDbSchema.EventTable.Columns.POSITION + ", " +
                JobiEventDbSchema.EventTable.Columns.DATE + ", " +
                JobiEventDbSchema.EventTable.Columns.ADDRESS + ", " +
                JobiEventDbSchema.EventTable.Columns.CITY + ", " +
                JobiEventDbSchema.EventTable.Columns.STATE +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
