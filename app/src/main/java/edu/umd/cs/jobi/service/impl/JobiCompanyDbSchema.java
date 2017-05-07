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
}
