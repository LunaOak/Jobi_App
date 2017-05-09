package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class JobiSettingsDbHelper extends SQLiteOpenHelper {


    public JobiSettingsDbHelper(Context c) {
        super(c,"jobi_settings.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + JobiSettingsDbSchema.SettingsTable.NAME + "(" +
                JobiSettingsDbSchema.SettingsTable.Columns.STATUS + ", " +
                JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATIONS_SWITCH + ", " +
                JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_INTERVIEW + ", " +
                JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_EMAILS+ ", " +
                JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_DEADLINES + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
