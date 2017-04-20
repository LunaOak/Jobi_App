package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AgileAndroidDbHelper extends SQLiteOpenHelper {


    public AgileAndroidDbHelper (Context c) {
        super(c,"agile_android.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + AgileAndroidDbSchema.StoryTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                AgileAndroidDbSchema.StoryTable.Columns.ID + ", " +
                AgileAndroidDbSchema.StoryTable.Columns.SUMMARY + ", " +
                AgileAndroidDbSchema.StoryTable.Columns.ACCEPTANCE_CRITERIA + ", " +
                AgileAndroidDbSchema.StoryTable.Columns.STORY_POINTS + ", " +
                AgileAndroidDbSchema.StoryTable.Columns.PRIORITY + ", " +
                AgileAndroidDbSchema.StoryTable.Columns.STATUS + ", " +
                AgileAndroidDbSchema.StoryTable.Columns.TIME_CREATED + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
