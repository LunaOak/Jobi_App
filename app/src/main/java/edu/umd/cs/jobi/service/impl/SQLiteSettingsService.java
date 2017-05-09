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
            String status = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.STATUS));
            String notifications_switch = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATIONS_SWITCH));
            String interview = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_INTERVIEW));
            String emails = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_EMAILS));
            String deadlines = getString(getColumnIndex(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_DEADLINES));

            Settings settings = new Settings();
            settings.setStatus(Settings.Status.valueOf(status));
            settings.setSwitch(Settings.NotificationSwitch.valueOf(notifications_switch));

            if (!interview.equals("")) {
                settings.getNotifications().add(Settings.Notifications.INTERVIEWS);
            }
            if (!emails.equals("")) {
                settings.getNotifications().add(Settings.Notifications.EMAILS);
            }
            if (!deadlines.equals("")) {
                settings.getNotifications().add(Settings.Notifications.DEADLINES);
            }
            return settings;
        }

    }

    protected SQLiteDatabase getDatabase() {
        return this.db;
    }

    // queryStories //////////////////////////////////////////////////////////////////////////////
    private Settings querySettings() {
        
        Settings settings = new Settings();
        Cursor cursor = db.query(JobiSettingsDbSchema.SettingsTable.NAME, null, null, null, null, null, null);
        SettingsCursorWrapper settings_cursor = new SettingsCursorWrapper(cursor);
        try {
            settings_cursor.moveToFirst();
            while (!settings_cursor.isAfterLast()) {
                settings = settings_cursor.getSettings();
            }

        } finally {
            settings_cursor.close();
        }

        return settings;
    }

    // getContentValues //////////////////////////////////////////////////////////////////////////////
    private static ContentValues getContentValues(Settings settings) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.STATUS,settings.getStatus().toString());
        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATIONS_SWITCH,settings.getSwitch().toString());
        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_INTERVIEW, "");
        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_EMAILS, "");
        contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_DEADLINES, "");

        for (Settings.Notifications notif : settings.getNotifications()) {

            if (notif.equals(Settings.Notifications.INTERVIEWS)) {
                contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_INTERVIEW, Settings.Notifications.INTERVIEWS.toString());
            } else if (notif.equals(Settings.Notifications.EMAILS)) {
                contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_EMAILS, Settings.Notifications.EMAILS.toString());
            } else if (notif.equals(Settings.Notifications.DEADLINES)) {
                contentValues.put(JobiSettingsDbSchema.SettingsTable.Columns.NOTIFICATION_DEADLINES, Settings.Notifications.DEADLINES.toString());
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
            db.update(JobiSettingsDbSchema.SettingsTable.NAME,getContentValues(settings),null,null);
        }
    }

    @Override
    public boolean getStatus(Settings settings) {
        return false;
    }

    // getSettings //////////////////////////////////////////////////////////////////////////////
    public Settings getSettings() {
        Settings settings = querySettings();
        return settings;
    }

}