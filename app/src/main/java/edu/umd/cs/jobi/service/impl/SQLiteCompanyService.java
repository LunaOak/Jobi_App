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

import edu.umd.cs.jobi.model.Story;
import edu.umd.cs.jobi.service.StoryService;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.service.CompanyService;


public class SQLiteCompanyService implements CompanyService {

    private SQLiteDatabase db;

    @Override
    public void addCompanyToDb(Company company) {

    }

    @Override
    public Company getCompanyByName(String name) {
        return null;
    }

    @Override
    public List<Company> getAllCompanies() {
        return null;
    }

    @Override
    public List<Company> getCurrentCompanies() {
        return null;
    }


    // Private Inner Class StoryCursorWrapper ///////////////////
    private class StoryCursorWrapper extends CursorWrapper {

        StoryCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Story getStory() {
//            String id = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.ID));
//            String title = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.TITLE));
//            String status = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.STATUS));
//            double storyPoints = getDouble(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.STORY_POINTS));
//            String priority = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.PRIORITY));
//            String status = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.STATUS));
//            long timeCreated = getLong(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.TIME_CREATED));

            Story story = new Story();
//            story.setId(id);
//            story.setSummary(title);
//            //story.setAcceptanceCriteria(acceptanceCriteria);
//            story.setStoryPoints(storyPoints);
//            story.setPriority(Story.Priority.valueOf(priority));
//            story.setStatus(Story.Status.valueOf(status));
//            story.setTimeCreated(new Date(timeCreated));

            return story;
        }

    }


    // Constructor and Methods ///////////////////////////////////
    public SQLiteCompanyService(Context c) {
        db = new JobiPositionDbHelper(c).getWritableDatabase();
    }

    protected SQLiteDatabase getDatabase() {
        return this.db;
    }

    // queryStories //////////////////////////////////////////////////////////////////////////////
    private List<Story> queryStories(String whereClause, String[] whereArgs, String orderBy) {

        List<Story> list = new ArrayList<Story>();

        Cursor cursor = db.query(JobiPositionDbSchema.PositionTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
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

        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.ID,story.getId());
//        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.SUMMARY,story.getSummary());
//        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.ACCEPTANCE_CRITERIA,story.getAcceptanceCriteria());
//        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.STORY_POINTS, story.getStoryPoints());
//        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.PRIORITY, story.getPriority().toString());
//        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.STATUS, story.getStatus().toString());
//        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.TIME_CREATED, story.getTimeCreated().getTime());

        return contentValues;
    }

    // addStoryToBacklog //////////////////////////////////////////////////////////////////////////////
    public void addStoryToBacklog(Story story) {

        String[] IDs = new String[]{story.getId()};
        //IDs[0] = story.getId();

        // If not present in the list at all, add //
        if (getStoryById(story.getId()) == null) {
            db.insert(JobiPositionDbSchema.PositionTable.NAME,null,getContentValues(story));
        // Otherwise if it is then update it //
        } else {
            db.update(JobiPositionDbSchema.PositionTable.NAME,getContentValues(story),"ID=?",IDs);
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
