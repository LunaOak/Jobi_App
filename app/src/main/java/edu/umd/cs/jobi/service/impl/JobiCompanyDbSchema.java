package edu.umd.cs.jobi.service.impl;


public class JobiCompanyDbSchema {

    public static final class CompanyTable {
        static final String NAME = "COMPANY";

        static final class Columns {
            public static final String COMPANY_ID = "COMPANY_ID";
            public static final String TITLE = "TITLE";
            public static final String STATUS = "STATUS";
            public static final String LOCATION = "LOCATION";
            public static final String DESCRIPTION = "DESCRIPTION";
        }
    }

    public static final class EventTable {
        static final String NAME = "EVENT";

        static final class Columns {
            public static final String ID = "ID";
            public static final String TITLE = "TITLE";
            public static final String COMPANY_ID = "COMPANY_ID";
            public static final String POSITION_ID = "POSITION_ID";
            public static final String TYPE = "TYPE";
            public static final String DATE = "DATE";
            public static final String LOCATION = "LOCATION";
        }
    }

    public static final class ContactTable {
        static final String NAME = "CONTACT";

        static final class Columns {
            public static final String ID = "ID";
            public static final String EVENT_ID = "EVENT_ID";
            public static final String COMPANY_ID = "COMPANY_ID";
            public static final String POSITION_ID = "POSITION_ID";
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
            public static final String EVENT_ID = "EVENT_ID";
            public static final String COMPANY_ID = "COMPANY_ID";
            public static final String POSITION_ID = "POSITION_ID";
            public static final String TITLE = "TITLE";
            public static final String DATE = "DATE";
        }
    }

    public static final class PositionTable {
        static final String NAME = "POSITION";

        static final class Columns {
            public static final String ID = "ID";
            public static final String TITLE = "TITLE";
            public static final String STATUS = "STATUS";
            public static final String LOCATION = "LOCATION";
            public static final String DESCRIPTION = "DESCRIPTION";
            public static final String FAVORITE = "FAVORITE";
            public static final String TYPE = "TYPE";
            public static final String COMPANY_ID = "COMPANY_ID";
        }
    }
}