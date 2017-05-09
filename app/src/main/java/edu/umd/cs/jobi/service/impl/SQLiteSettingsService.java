package edu.umd.cs.jobi.service.impl;

import android.app.Notification;
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

import edu.umd.cs.jobi.model.Settings;
import edu.umd.cs.jobi.service.SettingsService;


public class SQLiteSettingsService implements SettingsService {

    private SQLiteDatabase db;

    public SQLiteSettingsService(Context context){
        db = new JobiSettingsDbHelper(context).getWritableDatabase();
    }

    // Private Inner Class SettingsCursorWrapper ///////////////////
    private class SettingsCursorWrapper extends CursorWrapper {

        SettingsCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Settings getSettings() {
            String id = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.ID));
            String status = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.STATUS));
            String notifications_switch = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATIONS_SWITCH));
            String interview = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_INTERVIEW));
            String emails = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_EMAILS));
            String deadlines = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_DEADLINES));

            Settings settings = new Settings();
            settings.setId(id);
            settings.setStatus(Settings.Status.valueOf(status));
            settings.setSwitch(Settings.NotificationSwitch.valueOf(notifications_switch));
            settings.getNotifications().add(Settings.Notifications.INTERVIEWS);
            settings.getNotifications().add(Settings.Notifications.EMAILS);
            settings.getNotifications().add(Settings.Notifications.DEADLINES);

            return settings;
        }

    }

    protected SQLiteDatabase getDatabase() {
        return this.db;
    }

    // queryStories //////////////////////////////////////////////////////////////////////////////
    private List<Settings> querySettings(String whereClause, String[] whereArgs, String orderBy) {
        
        List<Settings> settings_list = new ArrayList<Settings>();

        Cursor cursor = db.query(JobiSettingsDbSchema.SettingsTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        SettingsCursorWrapper settings_cursor = new SettingsCursorWrapper(cursor);
        try {
            settings_cursor.moveToFirst();
            while (!settings_cursor.isAfterLast()) {
                settings_list.add(settings_cursor.getSettings());
                settings_cursor.moveToNext();
            }

        } finally {
            settings_cursor.close();
        }

        return settings_list;
    }

    // getContentValues //////////////////////////////////////////////////////////////////////////////
    private static ContentValues getContentValues(Settings settings) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.ID,settings.getId());
        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.STATUS,settings.getStatus().name());
        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATIONS_SWITCH,settings.getSwitch().name());
        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_INTERVIEW, "");
        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_EMAILS, "");
        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_DEADLINES, "");

        for (Settings.Notifications notif : settings.getNotifications()) {

            if (notif.equals(Settings.Notifications.INTERVIEWS)) {
                contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_INTERVIEW, Settings.Notifications.INTERVIEWS.name());
            } else if (notif.equals(Settings.Notifications.EMAILS)) {
                contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_EMAILS, Settings.Notifications.EMAILS.name());
            } else if (notif.equals(Settings.Notifications.DEADLINES)) {
                contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_DEADLINES, Settings.Notifications.DEADLINES.name());
            }

        }

        return contentValues;
    }

    // update settings //////////////////////////////////////////////////////////////////////////////
    public void updateSettings(Settings settings) {

        // If not present in the list at all, add //
        if (settings == null) {
            db.insert(JobiSettingsDbSchema.SettingsTable.NAME,null,getContentValues(settings));
            // Otherwise if it is then update it //
        } else {
            String[] IDs = new String[]{settings.getId()};
            db.update(JobiSettingsDbSchema.SettingsTable.NAME,getContentValues(settings),"ID=?",IDs);
        }
    }

    @Override
    public boolean getStatus(Settings settings) {
        return false;
    }

    // getSettings //////////////////////////////////////////////////////////////////////////////
    public Settings getSettings(String id) {

        if (id == null) {
            return null;
        } else {
            for (Settings settings : querySettings("ID=?", new String[]{id}, null)) {
                if (settings.getId().equals(id)) {
                    return settings;
                }
            }

            return null;
        }
    }

}