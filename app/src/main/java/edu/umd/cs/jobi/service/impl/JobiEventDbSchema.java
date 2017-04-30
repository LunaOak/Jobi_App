package edu.umd.cs.jobi.service.impl;

/**
 * Created by Juan on 4/28/2017.
 */

public class JobiEventDbSchema {

    public static final class EventTable {
        static final String NAME = "EVENT";

        static final class Columns {
            public static final String ID = "ID";
            public static final String TITLE = "TITLE";
            public static final String POSITION = "POSITION";
            public static final String DATE = "DATE";
            public static final String ADDRESS = "ADDRESS";
            public static final String CITY = "CITY";
            public static final String STATE = "STATE";
        }
    }

    public static final class ContactTable {
        static final String NAME = "EVENT_CONTACT";

        static final class Columns {
            public static final String ID = "ID";
            public static final String JOB_TITLE = "JOB_TITLE";
            public static final String NAME = "NAME";
            public static final String EMAIL = "EMAIL";
            public static final String PHONE = "PHONE";
        }
    }

    public static final class ReminderTable {
        static final String NAME = "EVENT_REMINDER";

        static final class Columns {
            public static final String ID = "ID";
            public static final String TITLE = "TITLE";
            public static final String DATE = "DATE";
        }
    }
}
