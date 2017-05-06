package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class JobiPositionDbHelper extends SQLiteOpenHelper {


    public JobiPositionDbHelper(Context c) {
        super(c,"jobi_positions.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + JobiPositionDbSchema.PositionTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiPositionDbSchema.PositionTable.Columns.ID + ", " +
                JobiPositionDbSchema.PositionTable.Columns.TITLE + ", " +
                JobiPositionDbSchema.PositionTable.Columns.STATUS + ", " +
                JobiPositionDbSchema.PositionTable.Columns.LOCATION + ", " +
                JobiPositionDbSchema.PositionTable.Columns.DESCRIPTION + ", " +
                JobiPositionDbSchema.PositionTable.Columns.FAVORITE + ", " +
                JobiPositionDbSchema.PositionTable.Columns.TYPE + ", " +
                JobiPositionDbSchema.PositionTable.Columns.COMPANY + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
