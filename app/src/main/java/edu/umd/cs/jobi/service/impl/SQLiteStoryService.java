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

import edu.umd.cs.jobi.model.Story;
import edu.umd.cs.jobi.service.StoryService;
import edu.umd.cs.jobi.service.impl.AgileAndroidDbHelper;
import edu.umd.cs.jobi.service.impl.AgileAndroidDbSchema;


public class SQLiteStoryService implements StoryService {

    private SQLiteDatabase db;


    // Private Inner Class StoryCursorWrapper ///////////////////
    private class StoryCursorWrapper extends CursorWrapper {

        StoryCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Story getStory() {
            String id = getString(getColumnIndex(AgileAndroidDbSchema.StoryTable.Columns.ID));
            String summary = getString(getColumnIndex(AgileAndroidDbSchema.StoryTable.Columns.SUMMARY));
            String acceptanceCriteria = getString(getColumnIndex(AgileAndroidDbSchema.StoryTable.Columns.ACCEPTANCE_CRITERIA));
            double storyPoints = getDouble(getColumnIndex(AgileAndroidDbSchema.StoryTable.Columns.STORY_POINTS));
            String priority = getString(getColumnIndex(AgileAndroidDbSchema.StoryTable.Columns.PRIORITY));
            String status = getString(getColumnIndex(AgileAndroidDbSchema.StoryTable.Columns.STATUS));
            long timeCreated = getLong(getColumnIndex(AgileAndroidDbSchema.StoryTable.Columns.TIME_CREATED));

            Story story = new Story();
            story.setId(id);
            story.setSummary(summary);
            story.setAcceptanceCriteria(acceptanceCriteria);
            story.setStoryPoints(storyPoints);
            story.setPriority(Story.Priority.valueOf(priority));
            story.setStatus(Story.Status.valueOf(status));
            story.setTimeCreated(new Date(timeCreated));

            return story;
        }

    }


    // Constructor and Methods ///////////////////////////////////
    public SQLiteStoryService (Context c) {
        db = new AgileAndroidDbHelper(c).getWritableDatabase();
    }

    protected SQLiteDatabase getDatabase() {
        return this.db;
    }

    // queryStories //////////////////////////////////////////////////////////////////////////////
    private List<Story> queryStories(String whereClause, String[] whereArgs, String orderBy) {

        List<Story> list = new ArrayList<Story>();

        Cursor cursor = db.query(AgileAndroidDbSchema.StoryTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        StoryCursorWrapper story_cursor = new StoryCursorWrapper(cursor);
        try {
            story_cursor.moveToFirst();
            while (!story_cursor.isAfterLast()) {
                list.add(story_cursor.getStory());
                story_cursor.moveToNext();
            }

        } finally {
            story_cursor.close();
        }

        return list;
    }

    // getContentValues //////////////////////////////////////////////////////////////////////////////
    private static ContentValues getContentValues(Story story) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(AgileAndroidDbSchema.StoryTable.Columns.ID,story.getId());
        contentValues.put(AgileAndroidDbSchema.StoryTable.Columns.SUMMARY,story.getSummary());
        contentValues.put(AgileAndroidDbSchema.StoryTable.Columns.ACCEPTANCE_CRITERIA,story.getAcceptanceCriteria());
        contentValues.put(AgileAndroidDbSchema.StoryTable.Columns.STORY_POINTS, story.getStoryPoints());
        contentValues.put(AgileAndroidDbSchema.StoryTable.Columns.PRIORITY, story.getPriority().toString());
        contentValues.put(AgileAndroidDbSchema.StoryTable.Columns.STATUS, story.getStatus().toString());
        contentValues.put(AgileAndroidDbSchema.StoryTable.Columns.TIME_CREATED, story.getTimeCreated().getTime());

        return contentValues;
    }

    // addStoryToBacklog //////////////////////////////////////////////////////////////////////////////
    public void addStoryToBacklog(Story story) {

        String[] IDs = new String[]{story.getId()};
        //IDs[0] = story.getId();

        // If not present in the list at all, add //
        if (getStoryById(story.getId()) == null) {
            db.insert(AgileAndroidDbSchema.StoryTable.NAME,null,getContentValues(story));
        // Otherwise if it is then update it //
        } else {
            db.update(AgileAndroidDbSchema.StoryTable.NAME,getContentValues(story),"ID=?",IDs);
        }
    }

    // getStoryById //////////////////////////////////////////////////////////////////////////////
    public Story getStoryById(String id) {

        if (id == null) {
            return null;
        } else {
            for (Story story : queryStories("ID=?", new String[]{id}, null)) {
                if (story.getId().equals(id)) {
                    return story;
                }
            }
            return null;
        }
    }

    // getAllStories //////////////////////////////////////////////////////////////////////////////
    public List<Story> getAllStories() {
        List<Story> prioritizedStories = queryStories(null,null,null);

        Collections.sort(prioritizedStories, new Comparator<Story>() {
            @Override
            public int compare(Story story1, Story story2) {
                if (story1.getPriority().equals(story2.getPriority())) {
                    if (story1.getStatus().equals(story2.getStatus())) {
                        return story1.getTimeCreated().compareTo(story2.getTimeCreated());
                    } else {
                        return story1.getStatus().compareTo(story2.getStatus());
                    }
                } else {
                    return story1.getPriority().compareTo(story2.getPriority());
                }
            }
        });

        return prioritizedStories;
    }


    // getCurrentSprintStories //////////////////////////////////////////////////////////////////////////////
    public List<Story> getCurrentSprintStories() {

        String[] priority = new String[]{Story.Priority.CURRENT.toString()};
        //priority[0] = Story.Priority.CURRENT.toString();

        return queryStories("PRIORITY=?",priority,null);
    }

}
