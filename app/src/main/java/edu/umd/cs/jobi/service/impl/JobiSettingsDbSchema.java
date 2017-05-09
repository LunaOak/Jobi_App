package edu.umd.cs.jobi.service.impl;


public class JobiSettingsDbSchema {

    public static final class SettingsTable {
        static final String NAME = "SETTINGS";

        static final class Columns {
            public static final String ID = "ID";
            public static final String STATUS = "STATUS";
            public static final String NOTIFICATIONS_SWITCH = "NOTIFICATIONS_SWITCH";
            public static final String NOTIFICATION_INTERVIEW = "NOTIFICATION_INTERVIEW";
            public static final String NOTIFICATION_EMAILS = "NOTIFICATION_EMAILS";
            public static final String NOTIFICATION_DEADLINES = "NOTIFICATION_DEADLINES";
        }
    }
}
