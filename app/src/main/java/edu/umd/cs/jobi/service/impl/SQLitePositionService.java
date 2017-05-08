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

import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.service.PositionService;


public class SQLitePositionService implements PositionService {

    private SQLiteDatabase db;
    private SQLiteDatabase contactDb;

    public SQLitePositionService (Context context) {
        db = new JobiPositionDbHelper(context).getWritableDatabase();
        contactDb = new JobiPositionContactDbHelper(context).getWritableDatabase();
    }

    protected SQLiteDatabase getPositionDatabase() {
        return db;
    }

    protected SQLiteDatabase getContactDatabase() {
        return contactDb;
    }

    @Override
    public void addPositionToDb(Position position) {
        if(getPositionById(position.getId()) == null) {
            db.insert(JobiPositionDbSchema.PositionTable.NAME, null, getContentValues(position));
        } else {
            db.update(JobiPositionDbSchema.PositionTable.NAME, getContentValues(position), JobiPositionDbSchema.PositionTable.Columns.ID + "=?", new String[]{position.getId()});
        }

        contactDb.delete(JobiPositionDbSchema.ContactTable.NAME, JobiPositionDbSchema.ContactTable.Columns.POSITION_ID + "=?", new String[] {position.getId()});

        for(Contact contact : position.getContacts()) {
            contactDb.insert(JobiPositionDbSchema.ContactTable.NAME, null, getContactContentValues(position.getId(), contact));
        }
    }

    @Override
    public Position getPositionById(String id) {
        if (id == null) {
            return null;
        }

        List<Position> positions = queryPositions(JobiPositionDbSchema.PositionTable.Columns.ID + "=?", new String[]{id}, null);

        if (positions.size() == 0) {
            return null;
        }

        return positions.get(0);
    }

    private List<Position> queryPositions(String whereClause, String[] whereArgs, String orderBy) {
        List<Position> positions = new ArrayList<Position>();

        Cursor cursorRaw = db.query(JobiPositionDbSchema.PositionTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        PositionCursorWrapper cursor = new PositionCursorWrapper(cursorRaw);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                positions.add(cursor.getJobiPosition());

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            cursorRaw.close(); // Might need to delete
        }

        for(Position position : positions) {
            position.getContacts().addAll(queryContacts(JobiPositionDbSchema.ContactTable.Columns.POSITION_ID + "=?", new String[]{position.getId()}, null));
        }
        return positions;
    }

    private List<Contact> queryContacts(String whereClause, String[] whereArgs, String orderBy) {
        List<Contact> contacts = new ArrayList<Contact>();

        Cursor cursorRaw = contactDb.query(JobiPositionDbSchema.ContactTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        PositionCursorWrapper cursor = new PositionCursorWrapper(cursorRaw);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                contacts.add(cursor.getContact());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            cursorRaw.close();
        }
        return contacts;
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
    public List<Position> getFavoritePositions() {
        List<Position> positions = queryPositions(JobiPositionDbSchema.PositionTable.Columns.FAVORITE + "=?", new String[]{Position.Favorite.YES.name()}, null);

        if (positions.size() == 0) {
            return null;
        }

        return positions;
    }

    @Override
    public List<Position> getPositionsByCompany(String name) {
        List<Position> positions = queryPositions(JobiPositionDbSchema.PositionTable.Columns.COMPANY + "=?",
                new String[]{name}, null);

        return positions;
    }

    @Override
    public Contact getContactById(String id) {
        if (id == null) {
            return null;
        }

        List<Contact> contacts = queryContacts(JobiPositionDbSchema.ContactTable.Columns.ID + "=?", new String[]{id}, null);

        if (contacts.size() == 0) {
            return null;
        }

        return contacts.get(0);
    }

    @Override
    public List<Contact> getContactsByPosition(Position position) {
        if (position == null)
            return null;
        List<Contact> contacts = queryContacts(JobiPositionDbSchema.ContactTable.Columns.POSITION_ID +
                "=?", new String[]{position.getId()}, null);
        return contacts;
    }


    @Override
    public boolean deletePositionById(String id) {
        //todo delete all events related as well...
        for (Contact contact : getPositionById(id).getContacts()){
            deleteContactById(contact.getId());
        }
        if (db.delete(JobiPositionDbSchema.PositionTable.NAME, JobiPositionDbSchema.PositionTable.Columns.ID + "=?", new String[]{id}) == 0) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteContactById(String id) {
        if (contactDb.delete(JobiPositionDbSchema.ContactTable.NAME, JobiPositionDbSchema.ContactTable.Columns.ID + "=?", new String[] {id}) == 0) {
            return false;
        }
        return true;
    }

    private class PositionCursorWrapper extends CursorWrapper {

        public PositionCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Position getJobiPosition() {
            String id = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.ID));
            String title = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.TITLE));
            String status = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.STATUS));
            Position.Status enumStatus = Position.Status.valueOf(status);
            String location = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.LOCATION));
            String description = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.DESCRIPTION));
            String fav = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.FAVORITE));
            Position.Favorite enumFav = Position.Favorite.valueOf(fav);
            String type = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.TYPE));
            Position.Type enumType = Position.Type.valueOf(type);
            String company = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.COMPANY));

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

        public Contact getContact() {
            String id = getString(getColumnIndex(JobiPositionDbSchema.ContactTable.Columns.ID));
            String jobTitle = getString(getColumnIndex(JobiPositionDbSchema.ContactTable.Columns.JOB_TITLE));
            String name = getString(getColumnIndex(JobiPositionDbSchema.ContactTable.Columns.NAME));
            String email = getString(getColumnIndex(JobiPositionDbSchema.ContactTable.Columns.EMAIL));
            String phone = getString(getColumnIndex(JobiPositionDbSchema.ContactTable.Columns.PHONE));

            Contact contact = new Contact(name, jobTitle, email, phone);
            contact.setId(id);

            return contact;
        }
    }

    // getContentValues //////////////////////////////////////////////////////////////////////////////
    private static ContentValues getContentValues(Position position) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.ID, position.getId());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.TITLE, position.getTitle());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.STATUS, position.getStatus().toString());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.LOCATION, position.getLocation());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.DESCRIPTION, position.getDescription());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.FAVORITE, position.getFavorite().name());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.TYPE, position.getType().name());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.COMPANY, position.getCompany());

        return contentValues;
    }

    private static ContentValues getContactContentValues(String id, Contact contact) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiPositionDbSchema.ContactTable.Columns.ID, contact.getId());
        contentValues.put(JobiPositionDbSchema.ContactTable.Columns.POSITION_ID, id);
        contentValues.put(JobiPositionDbSchema.ContactTable.Columns.JOB_TITLE, contact.getJobTitle());
        contentValues.put(JobiPositionDbSchema.ContactTable.Columns.NAME, contact.getName());
        contentValues.put(JobiPositionDbSchema.ContactTable.Columns.EMAIL, contact.getEmail());
        contentValues.put(JobiPositionDbSchema.ContactTable.Columns.PHONE, contact.getPhone());

        return contentValues;
    }
}
