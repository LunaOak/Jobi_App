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

import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Reminder;
import edu.umd.cs.jobi.service.EventService;

/**
 * Created by Juan on 5/2/2017.
 */

public class SQLiteEventService implements EventService {

    private SQLiteDatabase eventDb;
    private SQLiteDatabase contactDb;
    private SQLiteDatabase reminderDb;


    public SQLiteEventService (Context context) {
        eventDb = new JobiEventDbHelper(context).getWritableDatabase();
        contactDb = new JobiEventContactDbHelper(context).getWritableDatabase();
        reminderDb = new JobiEventReminderDbHelper(context).getWritableDatabase();
    }

    protected SQLiteDatabase getEventDatabase() {
        return eventDb;
    }

    protected SQLiteDatabase getContactDatabase() {
        return contactDb;
    }

    protected SQLiteDatabase getReminderDatabase() {
        return reminderDb;
    }

    private List<Event> queryEvents(String whereClause, String[] whereArgs, String orderBy) {
        List<Event> events = new ArrayList<Event>();

        Cursor cursorRaw = eventDb.query(JobiEventDbSchema.EventTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        EventCursorWrapper cursor = new EventCursorWrapper(cursorRaw);

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

        for(Event event : events) {
            event.getContacts().addAll(queryContacts(JobiEventDbSchema.ContactTable.Columns.EVENT_ID + "=?", new String[]{event.getId()}, null));
            event.getReminders().addAll(queryReminders(JobiEventDbSchema.ReminderTable.Columns.EVENT_ID + "=?", new String[]{event.getId()}, null));
        }
        return events;
    }

    private List<Contact> queryContacts(String whereClause, String[] whereArgs, String orderBy) {
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursorRaw = contactDb.query(JobiEventDbSchema.ContactTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        EventCursorWrapper cursor = new EventCursorWrapper(cursorRaw);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contacts.add(cursor.getContact());

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            cursorRaw.close(); // Might need to delete
        }

        return contacts;
    }

    private List<Reminder> queryReminders(String whereClause, String[] whereArgs, String orderBy) {
        List<Reminder> reminders = new ArrayList<Reminder>();

        Cursor cursorRaw = reminderDb.query(JobiEventDbSchema.ReminderTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        EventCursorWrapper cursor = new EventCursorWrapper(cursorRaw);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                reminders.add(cursor.getReminder());

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            cursorRaw.close(); // Might need to delete
        }

        return reminders;
    }

    @Override
    public void addEventToDb(Event event) {
        if(getEventById(event.getId()) == null) {
            eventDb.insert(JobiEventDbSchema.EventTable.NAME, null, getEventContentValues(event));

        } else {
            eventDb.update(JobiEventDbSchema.EventTable.NAME, getEventContentValues(event), JobiEventDbSchema.EventTable.Columns.ID + "=?", new String[]{event.getId()});
        }

        contactDb.delete(JobiEventDbSchema.ContactTable.NAME, JobiEventDbSchema.ContactTable.Columns.EVENT_ID + "=?", new String[] {event.getId()});
        reminderDb.delete(JobiEventDbSchema.ReminderTable.NAME, JobiEventDbSchema.ReminderTable.Columns.EVENT_ID + "=?", new String[] {event.getId()});

        for(Contact contact : event.getContacts()) {
            contactDb.insert(JobiEventDbSchema.ContactTable.NAME, null, getContactContentValues(event.getId(), contact));
        }

        for(Reminder reminder: event.getReminders()) {
            reminderDb.insert(JobiEventDbSchema.ReminderTable.NAME, null, getReminderContentValues(event.getId(), reminder));
        }
    }


    @Override
    public Event getEventById(String id) {
        if (id == null) {
            return null;
        }

        List<Event> events= queryEvents(JobiEventDbSchema.EventTable.Columns.ID + "=?", new String[]{id}, null);

        if (events.size() == 0) {
            return null;
        }

        /*
        if (stories.size() != 1) {
            throw new IllegalArgumentException();
        }
        */

        return events.get(0);
    }

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
    public Contact getContactById(String id) {
        if (id == null) {
            return null;
        }

        List<Contact> contacts = queryContacts(JobiEventDbSchema.ContactTable.Columns.ID + "=?", new String[]{id}, null);

        if (contacts.size() == 0) {
            return null;
        }

        /*
        if (stories.size() != 1) {
            throw new IllegalArgumentException();
        }
        */

        return contacts.get(0);
    }

    @Override
    public Reminder getReminderById(String id) {
        if (id == null) {
            return null;
        }

        List<Reminder> reminders = queryReminders(JobiEventDbSchema.ReminderTable.Columns.ID + "=?", new String[]{id}, null);

        if (reminders.size() == 0) {
            return null;
        }

        /*
        if (stories.size() != 1) {
            throw new IllegalArgumentException();
        }
        */

        return reminders.get(0);
    }

    @Override
    public List<Event> getEventsByPositionTitle(String title) {
        List<Event> prioritizedEvents = queryEvents(JobiEventDbSchema.EventTable.Columns.POSITION + "=?", new String[]{title}, null);

        Collections.sort(prioritizedEvents, new Comparator<Event>() {

            @Override
            public int compare(Event event1, Event event2) {
                return event1.getDate().compareTo(event2.getDate());
            }
        });

        return prioritizedEvents;
    }

    private static ContentValues getEventContentValues(Event event) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiEventDbSchema.EventTable.Columns.ID, event.getId());
        contentValues.put(JobiEventDbSchema.EventTable.Columns.TITLE, event.getTitle());
        contentValues.put(JobiEventDbSchema.EventTable.Columns.COMPANY, event.getCompany());
        contentValues.put(JobiEventDbSchema.EventTable.Columns.POSITION, event.getPosition());
        contentValues.put(JobiEventDbSchema.EventTable.Columns.TYPE, event.getType().name());
        contentValues.put(JobiEventDbSchema.EventTable.Columns.DATE, event.getDate().getTime());
        contentValues.put(JobiEventDbSchema.EventTable.Columns.ADDRESS, event.getAddress());
        contentValues.put(JobiEventDbSchema.EventTable.Columns.CITY, event.getCity());
        contentValues.put(JobiEventDbSchema.EventTable.Columns.STATE, event.getState());

        return contentValues;
    }

    private static ContentValues getContactContentValues(String id, Contact contact) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiEventDbSchema.ContactTable.Columns.ID, contact.getId());
        contentValues.put(JobiEventDbSchema.ContactTable.Columns.EVENT_ID, id);
        contentValues.put(JobiEventDbSchema.ContactTable.Columns.JOB_TITLE, contact.getJobTitle());
        contentValues.put(JobiEventDbSchema.ContactTable.Columns.NAME, contact.getName());
        contentValues.put(JobiEventDbSchema.ContactTable.Columns.EMAIL, contact.getEmail());
        contentValues.put(JobiEventDbSchema.ContactTable.Columns.PHONE, contact.getPhone());

        return contentValues;
    }

    private static ContentValues getReminderContentValues(String id, Reminder reminder) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiEventDbSchema.ReminderTable.Columns.ID, id);
        contentValues.put(JobiEventDbSchema.ReminderTable.Columns.TITLE, reminder.getTitle());
        contentValues.put(JobiEventDbSchema.ReminderTable.Columns.DATE, reminder.getDate().getTime());

        return contentValues;
    }

    private class EventCursorWrapper extends CursorWrapper {

        public EventCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Event getEvent() {
            String id = getString(getColumnIndex(JobiEventDbSchema.EventTable.Columns.ID));
            String title = getString(getColumnIndex(JobiEventDbSchema.EventTable.Columns.TITLE));
            String company = getString(getColumnIndex(JobiEventDbSchema.EventTable.Columns.COMPANY));
            String position = getString(getColumnIndex(JobiEventDbSchema.EventTable.Columns.POSITION));
            String type = getString(getColumnIndex(JobiEventDbSchema.EventTable.Columns.TYPE));
            Date date = new Date(getLong(getColumnIndex(JobiEventDbSchema.EventTable.Columns.DATE)));
            String address = getString(getColumnIndex(JobiEventDbSchema.EventTable.Columns.ADDRESS));
            String city = getString(getColumnIndex(JobiEventDbSchema.EventTable.Columns.CITY));
            String state = getString(getColumnIndex(JobiEventDbSchema.EventTable.Columns.STATE));

            Event event = new Event();
            event.setId(id);
            event.setTitle(title);
            event.setCompany(company);
            event.setPosition(position);
            event.setType(Event.Type.valueOf(type));
            event.setDate(date);
            event.setAddress(address);
            event.setCity(city);
            event.setState(state);

            return event;
        }

        public Contact getContact() {
            String id = getString(getColumnIndex(JobiEventDbSchema.ContactTable.Columns.ID));
            String jobTitle = getString(getColumnIndex(JobiEventDbSchema.ContactTable.Columns.JOB_TITLE));
            String name = getString(getColumnIndex(JobiEventDbSchema.ContactTable.Columns.NAME));
            String email = getString(getColumnIndex(JobiEventDbSchema.ContactTable.Columns.EMAIL));
            String phone = getString(getColumnIndex(JobiEventDbSchema.ContactTable.Columns.PHONE));

            Contact contact = new Contact(name, jobTitle, email, phone);
            contact.setId(id);

            return contact;
        }

        public Reminder getReminder() {
            String id = getString(getColumnIndex(JobiEventDbSchema.ReminderTable.Columns.ID));
            String title = getString(getColumnIndex(JobiEventDbSchema.ReminderTable.Columns.TITLE));
            Date date = new Date(getLong(getColumnIndex(JobiEventDbSchema.ReminderTable.Columns.DATE)));

            Reminder reminder = new Reminder(title, date);

            return reminder;
        }
    }
}
