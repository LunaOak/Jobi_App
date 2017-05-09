package edu.umd.cs.jobi.service.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.model.Reminder;
import edu.umd.cs.jobi.service.CompanyService;


public class SQLiteCompanyService implements CompanyService {




    private SQLiteDatabase db;

    public SQLiteCompanyService (Context context){
        db = new JobiCompanyDbHelper(context).getWritableDatabase();

    }

    // ADDING METHODS

    @Override
    public void addCompanyToDb(Company company) {

        if(getCompanyById(company.getId()) == null) {
            db.insert(JobiCompanyDbSchema.CompanyTable.NAME, null, getContentValues(company));
        } else {
            db.update(JobiCompanyDbSchema.CompanyTable.NAME, getContentValues(company), JobiCompanyDbSchema.CompanyTable.Columns.COMPANY_ID + "=?", new String[]{company.getId()});
        }

    }

    @Override
    public void addEventToDb(Event event){
        if(getEventById(event.getId()) == null) {
            db.insert(JobiCompanyDbSchema.EventTable.NAME, null, getEventContentValues(event));
        } else {
            db.update(JobiCompanyDbSchema.EventTable.NAME, getEventContentValues(event), JobiCompanyDbSchema.EventTable.Columns.ID + "=?", new String[]{event.getId()});
        }
    }

    @Override
    public void addPositionToDb(Position position) {
        if(getPositionById(position.getId()) == null) {
            db.insert(JobiCompanyDbSchema.PositionTable.NAME, null, getContentValues(position));
        } else {
            db.update(JobiCompanyDbSchema.PositionTable.NAME, getContentValues(position), JobiCompanyDbSchema.PositionTable.Columns.ID + "=?", new String[]{position.getId()});
        }

    }

    @Override
    public void addReminderToDb(Reminder reminder) {
        if(getReminderById(reminder.getId()) == null) {
            db.insert(JobiCompanyDbSchema.ReminderTable.NAME, null, getReminderContentValues(reminder));
        } else {
            db.update(JobiCompanyDbSchema.ReminderTable.NAME, getReminderContentValues(reminder), JobiCompanyDbSchema.ReminderTable.Columns.ID + "=?", new String[]{reminder.getId()});
        }
    }

    @Override
    public void addContactToDb(Contact contact) {
        if(getContactById(contact.getId()) == null) {
            db.insert(JobiCompanyDbSchema.ContactTable.NAME, null, getContactContentValues(contact));
        } else {
            db.update(JobiCompanyDbSchema.ContactTable.NAME, getContactContentValues(contact), JobiCompanyDbSchema.ReminderTable.Columns.ID + "=?", new String[]{contact.getId()});
        }
    }

    // GET METHODS

    @Override
    public List<Event> getAllEvents() {
        List<Event> prioritizedEvents = queryEvents(null, null, null);

        Collections.sort(prioritizedEvents, new Comparator<Event>() {

            @Override
            public int compare(Event event1, Event event2) {
                return event1.getDate().compareTo(event2.getDate());
            }
        });

        return prioritizedEvents;
    }

    @Override
    public List<Event> getEventsByPositionId(String id) {
        if (id == null)
            return null;
        List<Event> events = queryEvents(JobiCompanyDbSchema.EventTable.Columns.POSITION_ID + "=?", new String[]{id}, null);
        return events;
    }

    @Override
    public List<Event> getEventsByCompanyId(String id) {
        if (id == null)
            return null;
        List<Event> events = queryEvents(JobiCompanyDbSchema.EventTable.Columns.COMPANY_ID + "=?", new String[]{id}, null);
        return events;
    }

    @Override
    public Reminder getReminderById(String id) {
        if (id == null){
            return null;
        }

        List<Reminder> reminders = queryReminders(JobiCompanyDbSchema.ReminderTable.Columns.ID + "=?", new String[]{id}, null);
        for (Reminder reminder:reminders){
            if(reminder.getId().equals(id)){
                return reminder;

            }
        }

        return null;
    }

    @Override
    public Contact getContactById(String id) {
        if (id == null){
            return null;
        }

        List<Contact> contacts = queryContacts(JobiCompanyDbSchema.ContactTable.Columns.ID + "=?", new String[]{id}, null);
        for (Contact contact:contacts){
            if(contact.getId().equals(id)){
                return contact;

            }
        }

        return null;
    }

    @Override
    public List<Contact> getContactsByPositionId(String id) {
        if (id == null){
            return null;
        }

        List<Contact> contacts= queryContacts(JobiCompanyDbSchema.ContactTable.Columns.POSITION_ID + "=?", new String[]{id}, null);
        return contacts;
    }

    @Override
    public List<Contact> getContactsByCompanyId(String id) {
        if (id == null){
            return null;
        }

        List<Contact> contacts= queryContacts(JobiCompanyDbSchema.ContactTable.Columns.COMPANY_ID + "=?", new String[]{id}, null);
        return contacts;
    }

    @Override
    public List<Contact> getContactsByEventId(String id) {
        if (id == null){
            return null;
        }

        List<Contact> contacts= queryContacts(JobiCompanyDbSchema.ContactTable.Columns.EVENT_ID + "=?", new String[]{id}, null);
        return contacts;
    }

    @Override
    public List<Reminder> getRemindersByEvent(String id) {
        if (id == null){
            return null;
        }

        List<Reminder> reminders= queryReminders(JobiCompanyDbSchema.ReminderTable.Columns.EVENT_ID + "=?", new String[]{id}, null);
        return reminders;
    }

    @Override
    public Position getPositionById(String id) {
        if (id == null){
            return null;
        }

        List<Position> positions = queryPositions(JobiCompanyDbSchema.PositionTable.Columns.ID + "=?", new String[]{id}, null);
        for (Position position:positions){
            if(position.getId().equals(id)){
                return position;

            }
        }

        return null;
    }

    @Override
    public List<Position> getAllPositions() {
        List<Position> prioritizedPositions = queryPositions(null, null, null);

        Collections.sort(prioritizedPositions, new Comparator<Position>() {

            @Override
            public int compare(Position pos1, Position pos2) {
                if (pos1.getTitle().compareTo(pos2.getTitle()) == 0) {
                    return pos1.getCompany().compareTo(pos2.getCompany());
                }
                return pos1.getTitle().compareTo(pos2.getTitle());
            }
        });

        return prioritizedPositions;
    }

    @Override
    public List<Position> getPositionsByCompanyId(String id) {
        if (id == null){
            return null;
        }
        List<Position> positions = queryPositions(JobiCompanyDbSchema.PositionTable.Columns.COMPANY_ID + "=?", new String[]{id}, null);
        return positions;
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = queryCompanies(null, null, null);
        return companies;
    }

    @Override
    public List<Company> getCurrentCompanies() {
        List<Company> companies = queryCompanies(JobiCompanyDbSchema.CompanyTable.Columns.STATUS + "=?", new String[]{"TRUE"}, null);
        return companies;
    }

    @Override
    public Event getEventById(String id) {
        if (id == null) {
            return null;
        }

        List<Event> events = queryEvents(JobiCompanyDbSchema.EventTable.Columns.ID + "=?", new String[]{id}, null);

        if (events.size() == 0) {
            return null;
        }

        return events.get(0);
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

    // DESCRIPTOR GETTERS FOR NAMES
    @Override
    public String getCompanyNameById(String id){
        if (id == null){
            return null;
        }
        List<Company> companies = queryCompanies(JobiCompanyDbSchema.CompanyTable.Columns.COMPANY_ID + "=?", new String[]{id}, null);
        if (companies == null || companies.size() == 0){
            return null;
        }
        return companies.get(0).getName();

    }

    @Override
    public String getPositionNameById(String id){
        if (id == null){
            return null;
        }
        List<Position> positions = queryPositions(JobiCompanyDbSchema.PositionTable.Columns.ID + "=?", new String[]{id}, null);
        if (positions == null || positions.size() == 0){
            return null;
        }
        return positions.get(0).getTitle();
    }
    // DELETE METHODS

    @Override
    public boolean deleteEventById(String id) {
        if (id != null) {
            if (db.delete(JobiCompanyDbSchema.EventTable.NAME, JobiCompanyDbSchema.EventTable.Columns.ID + "=?", new String[]{id}) == 0) {
                return false;
            }
            // If event is deleted, associated reminders will be deleted
            if (db.delete(JobiCompanyDbSchema.ReminderTable.NAME, JobiCompanyDbSchema.ReminderTable.Columns.EVENT_ID + "=?", new String[] {id}) == 0) {
                //return false;
            }
            // If event is deleted, associated contacts will not be deleted in case user wants to save for reference
        }
        return true;
    }

    @Override
    public boolean deleteReminderById(String id) {
        if (id != null) {
            if (db.delete(JobiCompanyDbSchema.ReminderTable.NAME, JobiCompanyDbSchema.ReminderTable.Columns.ID + "=?", new String[] {id}) == 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean deleteContactById(String id) {
        if (id != null) {
            if (db.delete(JobiCompanyDbSchema.ContactTable.NAME, JobiCompanyDbSchema.ContactTable.Columns.ID + "=?", new String[]{id}) == 0) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean deletePositionById(String id) {
        if (id != null) {
            if (db.delete(JobiCompanyDbSchema.PositionTable.NAME, JobiCompanyDbSchema.PositionTable.Columns.ID + "=?", new String[]{id}) == 0) {
                return false;
            }
            // If position is deleted, associated events will be deleted
            if (db.delete(JobiCompanyDbSchema.EventTable.NAME, JobiCompanyDbSchema.EventTable.Columns.POSITION_ID + "=?", new String[]{id}) == 0) {
                //return false;
            }
            // If position is deleted, associated contacts will not be deleted, in case user wants to save for reference
        }
        return true;
    }

    @Override
    public boolean deleteCompanyById(String id){
        if (getCompanyById(id) != null){
            if (db.delete(JobiCompanyDbSchema.CompanyTable.NAME, JobiCompanyDbSchema.CompanyTable.Columns.COMPANY_ID + "=?", new String[]{id}) == 0){
                return false;
            }
            // If company is deleted, associated positions will be deleted
            if (db.delete(JobiCompanyDbSchema.PositionTable.NAME, JobiCompanyDbSchema.PositionTable.Columns.COMPANY_ID + "=?", new String[]{id}) == 0) {
                //return false;
            }
            // If company is deleted associated events will be deleted
            if (db.delete(JobiCompanyDbSchema.EventTable.NAME, JobiCompanyDbSchema.EventTable.Columns.COMPANY_ID + "=?", new String[]{id}) == 0) {
                //return false;
            }
            // If company is deleted associated reminders will be deleted
            if (db.delete(JobiCompanyDbSchema.ReminderTable.NAME, JobiCompanyDbSchema.ReminderTable.Columns.COMPANY_ID + "=?", new String[] {id}) == 0) {
                //return false;
            }
            // If company is deleted all contacts associated will be deleted
            if (db.delete(JobiCompanyDbSchema.ContactTable.NAME, JobiCompanyDbSchema.ContactTable.Columns.ID + "=?", new String[]{id}) == 0) {
               // return false;
            }
        }
        return true;
    }

    // Methods to check whether items exist
    @Override
    public String getCompanyIdWithName(String name){
        if (name == null){
            return null;
        }
        List<Company> companies = queryCompanies(JobiCompanyDbSchema.CompanyTable.Columns.TITLE + "=?", new String[]{name}, null);
        for (Company company:companies){
            if(company.getName().equals(name)){
                return company.getId();

            }
        }
        return null;
    }

    @Override
    public String getPositionIdWithName(String name, String companyId){
        if (name == null){
            return null;
        }
        List<Position> positions = queryPositions(JobiCompanyDbSchema.PositionTable.Columns.COMPANY_ID
                + "=?" + " and " + JobiCompanyDbSchema.PositionTable.Columns.TITLE + "=?", new String[]{companyId,name}, null);
        for (Position position:positions){
            if(position.getTitle().equals(name)){
                return position.getId();

            }
        }
        return null;
    }


/*
    @Override
    public Company getCompanyByName(String name) {
        if (name == null){
            return null;
        }
        List<Company> companies = queryCompanies(JobiCompanyDbSchema.CompanyTable.Columns.TITLE + "=?", new String[]{name}, null);
        for (Company company:companies){
            if(company.getName().equals(name)){
                return company;

            }
        }

        return null;
    } */



    @Override
    public List<Company> getFavoriteCompanies() {
        //Todo
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

    @Override
    public List<Position> getFavoritePositions() {
        List<Position> positions = queryPositions(JobiCompanyDbSchema.PositionTable.Columns.FAVORITE + "=?", new String[]{Position.Favorite.YES.name()}, null);

//        if (positions.size() == 0) {
//            return null;
//        }

        return positions;
    }

    private List<Event> queryEvents(String whereClause, String[] whereArgs, String orderBy){
        List<Event> events = new ArrayList<Event>();
        SQLiteDatabase compDb = getDatabase();

        Cursor cursorRaw = compDb.query(JobiCompanyDbSchema.EventTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        CompanyCursoryWrapper cursor = new CompanyCursoryWrapper(cursorRaw);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                events.add(cursor.getEvent());

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            cursorRaw.close(); // Might need to delete
        }

        return events;
    }

    private List<Reminder> queryReminders(String whereClause, String[] whereArgs, String orderBy){
        List<Reminder> reminders = new ArrayList<Reminder>();

        SQLiteDatabase compDb = getDatabase();

        Cursor cursor = compDb.query(JobiCompanyDbSchema.ReminderTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        CompanyCursoryWrapper wrapper = new CompanyCursoryWrapper(cursor);
        try {
            wrapper.moveToFirst();
            while(!cursor.isAfterLast()){
                reminders.add(wrapper.getReminder());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        return reminders;


    }

    private List<Contact> queryContacts(String whereClause, String[] whereArgs, String orderBy){
        List<Contact> contacts = new ArrayList<Contact>();

        SQLiteDatabase compDb = getDatabase();

        Cursor cursor = compDb.query(JobiCompanyDbSchema.ContactTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        CompanyCursoryWrapper wrapper = new CompanyCursoryWrapper(cursor);
        try {
            wrapper.moveToFirst();
            while(!cursor.isAfterLast()){
                contacts.add(wrapper.getContact());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        return contacts;
    }

    private List<Position> queryPositions(String whereClause, String[] whereArgs, String orderBy){
        List<Position> positions = new ArrayList<Position>();

        SQLiteDatabase compDb = getDatabase();

        Cursor cursor = compDb.query(JobiCompanyDbSchema.PositionTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        CompanyCursoryWrapper wrapper = new CompanyCursoryWrapper(cursor);
        try {
            wrapper.moveToFirst();
            while(!cursor.isAfterLast()){
                positions.add(wrapper.getJobiPosition());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        return positions;


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

        public Event getEvent() {
            String id = getString(getColumnIndex(JobiCompanyDbSchema.EventTable.Columns.ID));
            String title = getString(getColumnIndex(JobiCompanyDbSchema.EventTable.Columns.TITLE));
            String company = getString(getColumnIndex(JobiCompanyDbSchema.EventTable.Columns.COMPANY_ID));
            String position = getString(getColumnIndex(JobiCompanyDbSchema.EventTable.Columns.POSITION_ID));
            String type = getString(getColumnIndex(JobiCompanyDbSchema.EventTable.Columns.TYPE));
            Date date = new Date(Long.parseLong(getString(getColumnIndex(JobiCompanyDbSchema.EventTable.Columns.DATE))));
            String location = getString(getColumnIndex(JobiCompanyDbSchema.EventTable.Columns.LOCATION));

            Event event = new Event();
            event.setId(id);
            event.setTitle(title);
            event.setCompany(company);
            event.setPosition(position);
            event.setType(Event.Type.valueOf(type));
            event.setDate(date);
            event.setLocation(location);

            return event;
        }

        public Contact getContact() {
            String id = getString(getColumnIndex(JobiCompanyDbSchema.ContactTable.Columns.ID));
            String eventId = getString(getColumnIndex(JobiCompanyDbSchema.ContactTable.Columns.EVENT_ID));
            String companyId = getString(getColumnIndex(JobiCompanyDbSchema.ContactTable.Columns.COMPANY_ID));
            String positionId = getString(getColumnIndex(JobiCompanyDbSchema.ContactTable.Columns.POSITION_ID));
            String jobTitle = getString(getColumnIndex(JobiCompanyDbSchema.ContactTable.Columns.JOB_TITLE));
            String name = getString(getColumnIndex(JobiCompanyDbSchema.ContactTable.Columns.NAME));
            String email = getString(getColumnIndex(JobiCompanyDbSchema.ContactTable.Columns.EMAIL));
            String phone = getString(getColumnIndex(JobiCompanyDbSchema.ContactTable.Columns.PHONE));

            Contact contact = new Contact(name, jobTitle, email, phone);
            contact.setId(id);
            contact.setEventId(eventId);
            contact.setCompanyId(companyId);
            contact.setPositionId(positionId);

            return contact;
        }

        public Reminder getReminder() {
            String id = getString(getColumnIndex(JobiCompanyDbSchema.ReminderTable.Columns.ID));
            String eventId = getString(getColumnIndex(JobiCompanyDbSchema.ReminderTable.Columns.EVENT_ID));
            String companyId = getString(getColumnIndex(JobiCompanyDbSchema.ReminderTable.Columns.COMPANY_ID));
            String positionId = getString(getColumnIndex(JobiCompanyDbSchema.ReminderTable.Columns.POSITION_ID));
            String title = getString(getColumnIndex(JobiCompanyDbSchema.ReminderTable.Columns.TITLE));
            Date date = new Date(getLong(getColumnIndex(JobiCompanyDbSchema.ReminderTable.Columns.DATE)));

            Reminder reminder = new Reminder(title, date);
            reminder.setId(id);
            reminder.setEventId(eventId);
            reminder.setCompanyId(companyId);
            reminder.setPositionId(positionId);

            return reminder;
        }

        public Position getJobiPosition() {
            String id = getString(getColumnIndex(JobiCompanyDbSchema.PositionTable.Columns.ID));
            String title = getString(getColumnIndex(JobiCompanyDbSchema.PositionTable.Columns.TITLE));
            String status = getString(getColumnIndex(JobiCompanyDbSchema.PositionTable.Columns.STATUS));
            Position.Status enumStatus = Position.Status.valueOf(status);
            String location = getString(getColumnIndex(JobiCompanyDbSchema.PositionTable.Columns.LOCATION));
            String description = getString(getColumnIndex(JobiCompanyDbSchema.PositionTable.Columns.DESCRIPTION));
            String fav = getString(getColumnIndex(JobiCompanyDbSchema.PositionTable.Columns.FAVORITE));
            Position.Favorite enumFav = Position.Favorite.valueOf(fav);
            String type = getString(getColumnIndex(JobiCompanyDbSchema.PositionTable.Columns.TYPE));
            Position.Type enumType = Position.Type.valueOf(type);
            String company = getString(getColumnIndex(JobiCompanyDbSchema.PositionTable.Columns.COMPANY_ID));

            Position position = new Position();
            position.setId(id);
            position.setTitle(title);
            position.setStatus(enumStatus);
            position.setLocation(location);
            position.setDescription(description);
            position.setFavorite(enumFav);
            position.setType(enumType);
            position.setCompany(company);

            return position;
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

    private static ContentValues getEventContentValues(Event event) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiCompanyDbSchema.EventTable.Columns.ID, event.getId());
        contentValues.put(JobiCompanyDbSchema.EventTable.Columns.TITLE, event.getTitle());
        contentValues.put(JobiCompanyDbSchema.EventTable.Columns.COMPANY_ID, event.getCompany());
        contentValues.put(JobiCompanyDbSchema.EventTable.Columns.POSITION_ID, event.getPosition());
        contentValues.put(JobiCompanyDbSchema.EventTable.Columns.TYPE, event.getType().name());
        contentValues.put(JobiCompanyDbSchema.EventTable.Columns.DATE, String.valueOf(event.getDate().getTime()));
        contentValues.put(JobiCompanyDbSchema.EventTable.Columns.LOCATION, event.getLocation());

        return contentValues;
    }

    private static ContentValues getContactContentValues( Contact contact) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiCompanyDbSchema.ContactTable.Columns.ID, contact.getId());
        contentValues.put(JobiCompanyDbSchema.ContactTable.Columns.EVENT_ID, contact.getEventId());
        contentValues.put(JobiCompanyDbSchema.ContactTable.Columns.COMPANY_ID, contact.getCompanyId());
        contentValues.put(JobiCompanyDbSchema.ContactTable.Columns.POSITION_ID, contact.getPositionId());
        contentValues.put(JobiCompanyDbSchema.ContactTable.Columns.JOB_TITLE, contact.getJobTitle());
        contentValues.put(JobiCompanyDbSchema.ContactTable.Columns.NAME, contact.getName());
        contentValues.put(JobiCompanyDbSchema.ContactTable.Columns.EMAIL, contact.getEmail());
        contentValues.put(JobiCompanyDbSchema.ContactTable.Columns.PHONE, contact.getPhone());

        return contentValues;
    }

    private static ContentValues getReminderContentValues(Reminder reminder) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiCompanyDbSchema.ReminderTable.Columns.ID, reminder.getId());
        contentValues.put(JobiCompanyDbSchema.ReminderTable.Columns.EVENT_ID, reminder.getEventId());
        contentValues.put(JobiCompanyDbSchema.ReminderTable.Columns.COMPANY_ID, reminder.getCompanyId());
        contentValues.put(JobiCompanyDbSchema.ReminderTable.Columns.POSITION_ID, reminder.getPositionId());
        contentValues.put(JobiCompanyDbSchema.ReminderTable.Columns.TITLE, reminder.getTitle());
        contentValues.put(JobiCompanyDbSchema.ReminderTable.Columns.DATE, reminder.getDate().getTime());

        return contentValues;
    }

    private static ContentValues getContentValues(Position position) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiCompanyDbSchema.PositionTable.Columns.ID, position.getId());
        contentValues.put(JobiCompanyDbSchema.PositionTable.Columns.TITLE, position.getTitle());
        contentValues.put(JobiCompanyDbSchema.PositionTable.Columns.STATUS, position.getStatus().toString());
        contentValues.put(JobiCompanyDbSchema.PositionTable.Columns.LOCATION, position.getLocation());
        contentValues.put(JobiCompanyDbSchema.PositionTable.Columns.DESCRIPTION, position.getDescription());
        contentValues.put(JobiCompanyDbSchema.PositionTable.Columns.FAVORITE, position.getFavorite().name());
        contentValues.put(JobiCompanyDbSchema.PositionTable.Columns.TYPE, position.getType().name());
        contentValues.put(JobiCompanyDbSchema.PositionTable.Columns.COMPANY_ID, position.getCompany());

        return contentValues;
    }









    protected SQLiteDatabase getDatabase() {
        return this.db;
    }




}
