package edu.umd.cs.jobi.service.impl;


public class JobiDbSchema {

    public static final class StoryTable {
        static final String NAME = "NAME";

        static final class Columns {
            public static final String ID = "ID";
            public static final String SUMMARY = "SUMMARY";
            public static final String ACCEPTANCE_CRITERIA = "ACCEPTANCE_CRITERIA";
            public static final String STORY_POINTS = "STORY_POINTS";
            public static final String PRIORITY = "PRIORITY";
            public static final String STATUS = "STATUS";
            public static final String TIME_CREATED = "TIME_CREATED";
        }
    }
}
