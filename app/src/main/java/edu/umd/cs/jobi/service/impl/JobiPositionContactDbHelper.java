package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pauline on 5/5/2017.
 */

public class JobiPositionContactDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "jobi_position_contact.db";
    private static final int VERSION = 1;

    public JobiPositionContactDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + JobiPositionDbSchema.ContactTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiPositionDbSchema.ContactTable.Columns.ID + ", " +
                JobiPositionDbSchema.ContactTable.Columns.POSITION_ID + ", " +
                JobiPositionDbSchema.ContactTable.Columns.JOB_TITLE + ", " +
                JobiPositionDbSchema.ContactTable.Columns.NAME + ", " +
                JobiPositionDbSchema.ContactTable.Columns.EMAIL + ", " +
                JobiPositionDbSchema.ContactTable.Columns.PHONE +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
