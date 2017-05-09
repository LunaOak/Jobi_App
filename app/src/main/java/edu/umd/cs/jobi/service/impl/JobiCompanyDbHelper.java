package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class JobiCompanyDbHelper extends SQLiteOpenHelper {


    public JobiCompanyDbHelper(Context c) {
        super(c,"jobi_companies.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table " + JobiCompanyDbSchema.CompanyTable.NAME + "(" +
                JobiCompanyDbSchema.CompanyTable.Columns.COMPANY_ID + ", " +
                JobiCompanyDbSchema.CompanyTable.Columns.TITLE + ", " +
                JobiCompanyDbSchema.CompanyTable.Columns.STATUS + ", " +
                JobiCompanyDbSchema.CompanyTable.Columns.LOCATION + ", " +
                JobiCompanyDbSchema.CompanyTable.Columns.DESCRIPTION + ")");

        database.execSQL("create table " + JobiCompanyDbSchema.ContactTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiCompanyDbSchema.ContactTable.Columns.ID + ", " +
                JobiCompanyDbSchema.ContactTable.Columns.EVENT_ID + ", " +
                JobiCompanyDbSchema.ContactTable.Columns.COMPANY_ID + ", " +
                JobiCompanyDbSchema.ContactTable.Columns.POSITION_ID + ", " +
                JobiCompanyDbSchema.ContactTable.Columns.JOB_TITLE + ", " +
                JobiCompanyDbSchema.ContactTable.Columns.NAME + ", " +
                JobiCompanyDbSchema.ContactTable.Columns.EMAIL + ", " +
                JobiCompanyDbSchema.ContactTable.Columns.PHONE +
                ")");

        database.execSQL("create table " + JobiCompanyDbSchema.EventTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiCompanyDbSchema.EventTable.Columns.ID + ", " +
                JobiCompanyDbSchema.EventTable.Columns.TITLE + ", " +
                JobiCompanyDbSchema.EventTable.Columns.COMPANY_ID + ", " +
                JobiCompanyDbSchema.EventTable.Columns.POSITION_ID + ", " +
                JobiCompanyDbSchema.EventTable.Columns.TYPE + ", " +
                JobiCompanyDbSchema.EventTable.Columns.DATE + ", " +
                JobiCompanyDbSchema.EventTable.Columns.LOCATION + ")");

        database.execSQL("create table " + JobiCompanyDbSchema.ReminderTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiCompanyDbSchema.ReminderTable.Columns.ID + ", " +
                JobiCompanyDbSchema.ReminderTable.Columns.EVENT_ID + ", " +
                JobiCompanyDbSchema.ReminderTable.Columns.COMPANY_ID + ", " +
                JobiCompanyDbSchema.ReminderTable.Columns.POSITION_ID + ", " +
                JobiCompanyDbSchema.ReminderTable.Columns.TITLE + ", " +
                JobiCompanyDbSchema.ReminderTable.Columns.DATE +
                ")");

        database.execSQL("create table " + JobiCompanyDbSchema.PositionTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                JobiCompanyDbSchema.PositionTable.Columns.ID + ", " +
                JobiCompanyDbSchema.PositionTable.Columns.TITLE + ", " +
                JobiCompanyDbSchema.PositionTable.Columns.STATUS + ", " +
                JobiCompanyDbSchema.PositionTable.Columns.LOCATION + ", " +
                JobiCompanyDbSchema.PositionTable.Columns.DESCRIPTION + ", " +
                JobiCompanyDbSchema.PositionTable.Columns.FAVORITE + ", " +
                JobiCompanyDbSchema.PositionTable.Columns.TYPE + ", " +
                JobiCompanyDbSchema.PositionTable.Columns.COMPANY_ID + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
