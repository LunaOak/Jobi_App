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
        database.execSQL("create table " + JobiDbSchema.StoryTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiDbSchema.StoryTable.Columns.ID + ", " +
                JobiDbSchema.StoryTable.Columns.TITLE + ", " +
                JobiDbSchema.StoryTable.Columns.STATUS + ", " +
                JobiDbSchema.StoryTable.Columns.LOCATION + ", " +
                JobiDbSchema.StoryTable.Columns.DESCRIPTION + ", " +
                JobiDbSchema.StoryTable.Columns.FAVORITE + ", " +
                JobiDbSchema.StoryTable.Columns.TYPE + ", " +
                JobiDbSchema.StoryTable.Columns.CONTACTS + ", " +
                JobiDbSchema.StoryTable.Columns.COMPANY + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
