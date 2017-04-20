package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class JobiDbHelper extends SQLiteOpenHelper {


    public JobiDbHelper(Context c) {
        super(c,"jobi.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + JobiDbSchema.StoryTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiDbSchema.StoryTable.Columns.ID + ", " +
                JobiDbSchema.StoryTable.Columns.SUMMARY + ", " +
                JobiDbSchema.StoryTable.Columns.ACCEPTANCE_CRITERIA + ", " +
                JobiDbSchema.StoryTable.Columns.STORY_POINTS + ", " +
                JobiDbSchema.StoryTable.Columns.PRIORITY + ", " +
                JobiDbSchema.StoryTable.Columns.STATUS + ", " +
                JobiDbSchema.StoryTable.Columns.TIME_CREATED + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
