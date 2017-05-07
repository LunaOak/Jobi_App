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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
