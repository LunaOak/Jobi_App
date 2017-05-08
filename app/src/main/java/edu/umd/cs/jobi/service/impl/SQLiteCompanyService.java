package edu.umd.cs.jobi.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.model.Story;
import edu.umd.cs.jobi.service.CompanyService;


public class SQLiteCompanyService implements CompanyService {

    private SQLiteDatabase db;

    public SQLiteCompanyService (Context context){
        db = new JobiCompanyDbHelper(context).getWritableDatabase();

    }

    @Override
    public void addCompanyToDb(Company company) {
        SQLiteDatabase compDb = getDatabase();
        List<Company> companies = queryCompanies("", null, null);
        if (companies.contains(company)){
            compDb.update(JobiCompanyDbSchema.CompanyTable.NAME, getContentValues(company), "", null);

        } else {
            compDb.insert(JobiCompanyDbSchema.CompanyTable.NAME, null, getContentValues(company));
        }

    }

    private static ContentValues getContentValues(Company company){
        ContentValues cv = new ContentValues();
        cv.put(JobiCompanyDbSchema.CompanyTable.Columns.COMPANY_ID, company.getId());
        cv.put(JobiCompanyDbSchema.CompanyTable.Columns.TITLE, company.getName());
        cv.put(JobiCompanyDbSchema.CompanyTable.Columns.STATUS, company.getCurrent());
        cv.put(JobiCompanyDbSchema.CompanyTable.Columns.LOCATION, company.getLocation());
        cv.put(JobiCompanyDbSchema.CompanyTable.Columns.DESCRIPTION, company.getDescription());

        return cv;
    }

    @Override
    public Company getCompanyByName(String name) {
        return null;
    }

    @Override
    public Company getCompanyById(String id) {
        if (id == null){
            return null;
        }

        List<Company> companies = queryCompanies(JobiCompanyDbSchema.CompanyTable.Columns.COMPANY_ID + "=?", new String[]{id}, null);
        for (Company company:companies){
            if(company.getId().equals(id)){
                return company;

            }
        }

    return null;

    }

    private List<Company> queryCompanies(String whereClause, String[] whereArgs, String orderBy){
        List<Company> companies = new ArrayList<Company>();

        SQLiteDatabase compDb = getDatabase();

        Cursor cursor = compDb.query(JobiCompanyDbSchema.CompanyTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        CompanyCursoryWrapper wrapper = new CompanyCursoryWrapper(cursor);
        try {
            wrapper.moveToFirst();
            while(!cursor.isAfterLast()){
                companies.add(wrapper.getCompany());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        return companies;


    }

    private class CompanyCursoryWrapper extends CursorWrapper {


        public CompanyCursoryWrapper(Cursor cursor) {
            super(cursor);
        }

        public Company getCompany(){
            String id = getString(getColumnIndex(JobiCompanyDbSchema.CompanyTable.Columns.COMPANY_ID));
            String title = getString(getColumnIndex(JobiCompanyDbSchema.CompanyTable.Columns.TITLE));
            String status = getString(getColumnIndex(JobiCompanyDbSchema.CompanyTable.Columns.STATUS));
            String location = getString(getColumnIndex(JobiCompanyDbSchema.CompanyTable.Columns.LOCATION));
            String description = getString(getColumnIndex(JobiCompanyDbSchema.CompanyTable.Columns.DESCRIPTION));

            Company company = new Company();
            company.setId(id);
            company.setName(title);
            //TODO deal with current & favorites
            company.setCurrent(true);
            company.setLocation(location);
            company.setDescription(description);
            return company;
        }
    }

    @Override
    public List<Company> getAllCompanies() {


        List<Company> companies = queryCompanies("", null, null);
        return companies;
    }

    @Override
    public List<Company> getCurrentCompanies() {


        List<Company> companies = queryCompanies(JobiCompanyDbSchema.CompanyTable.Columns.STATUS + "=?", new String[]{"TRUE"}, null);
        return companies;
    }



    @Override
    public List<Company> getFavoriteCompanies() {
        //Todo
        return null;
    }




    protected SQLiteDatabase getDatabase() {
        return this.db;
    }




}
