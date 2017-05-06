package edu.umd.cs.jobi.service.impl;


public class JobiPositionDbSchema {

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
            public static final String CONTACTS = "CONTACTS";
            public static final String COMPANY = "COMPANY";
        }
    }

    public static final class ContactTable {
        static final String NAME = "POSITION_CONTACT";

        static final class Columns {
            public static final String ID = "ID";
            public static final String POSITION_ID = "POSITION_ID";
            public static final String JOB_TITLE = "JOB_TITLE";
            public static final String NAME = "NAME";
            public static final String EMAIL = "EMAIL";
            public static final String PHONE = "PHONE";
        }
    }
}
