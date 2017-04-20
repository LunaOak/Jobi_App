package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class JobiDbHelper extends SQLiteOpenHelper {


    public JobiDbHelper(Context c) {
        super(c,"jobi_positions.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + JobiDbSchema.PositionTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiDbSchema.PositionTable.Columns.ID + ", " +
                JobiDbSchema.PositionTable.Columns.TITLE + ", " +
                JobiDbSchema.PositionTable.Columns.STATUS + ", " +
                JobiDbSchema.PositionTable.Columns.LOCATION + ", " +
                JobiDbSchema.PositionTable.Columns.DESCRIPTION + ", " +
                JobiDbSchema.PositionTable.Columns.FAVORITE + ", " +
                JobiDbSchema.PositionTable.Columns.TYPE + ", " +
                JobiDbSchema.PositionTable.Columns.CONTACTS + ", " +
                JobiDbSchema.PositionTable.Columns.COMPANY + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
