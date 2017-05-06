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

import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.service.PositionService;


public class SQLitePositionService implements PositionService {

    private SQLiteDatabase db;
    private SQLiteDatabase contactDb;

    public SQLitePositionService (Context context) {
        db = new JobiPositionDbHelper(context).getWritableDatabase();
//        contactDb = new JobiEventContactDbHelper(context).getWritableDatabase();
    }

    protected SQLiteDatabase getPositionDatabase() {
        return db;
    }

    protected SQLiteDatabase getContactDatabase() {
        return contactDb;
    }

    @Override
    public void addPositionToDb(Position position) {
        if(getPositionById(position.getId()) == null) {
            db.insert(JobiPositionDbSchema.PositionTable.NAME, null, getContentValues(position));
            // Contacts?
        } else {
            db.update(JobiPositionDbSchema.PositionTable.NAME, getContentValues(position), JobiPositionDbSchema.PositionTable.Columns.ID + "=?", new String[]{position.getId()});
            // Contacts?
        }
    }

    @Override
    public Position getPositionById(String id) {
        if (id == null) {
            return null;
        }

        List<Position> positions = queryPositions(JobiPositionDbSchema.PositionTable.Columns.ID + "=?", new String[]{id}, null);

        if (positions.size() == 0) {
            return null;
        }

        return positions.get(0);
    }

    private List<Position> queryPositions(String whereClause, String[] whereArgs, String orderBy) {
        List<Position> positions = new ArrayList<Position>();

        Cursor cursorRaw = db.query(JobiPositionDbSchema.PositionTable.NAME, null, whereClause, whereArgs, null, null, orderBy);
        PositionCursorWrapper cursor = new PositionCursorWrapper(cursorRaw);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                positions.add(cursor.getJobiPosition());

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
            cursorRaw.close(); // Might need to delete
        }
        return positions;
    }

    @Override
    public List<Position> getAllPositions() {
        List<Position> prioritizedPositions = queryPositions(null, null, null);

        Collections.sort(prioritizedPositions, new Comparator<Position>() {

            @Override
            public int compare(Position pos1, Position pos2) {
                if (pos1.getTitle().compareTo(pos2.getTitle()) == 0) {
                    return pos1.getCompany().compareTo(pos2.getCompany());
                }
                return pos1.getTitle().compareTo(pos2.getTitle());
            }
        });

        return prioritizedPositions;
    }

    @Override
    public List<Position> getFavoritePositions() {
        List<Position> positions = queryPositions(JobiPositionDbSchema.PositionTable.Columns.FAVORITE + "=?", new String[]{Position.Favorite.YES.name()}, null);

        if (positions.size() == 0) {
            return null;
        }

        return positions;
    }


    private class PositionCursorWrapper extends CursorWrapper {

        public PositionCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public Position getJobiPosition() {
            String id = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.ID));
            String title = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.TITLE));
            String status = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.STATUS));
            Position.Status enumStatus = Position.Status.valueOf(status);
            String location = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.LOCATION));
            String description = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.DESCRIPTION));
            String fav = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.FAVORITE));
            Position.Favorite enumFav = Position.Favorite.valueOf(fav);
            String type = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.TYPE));
            Position.Type enumType = Position.Type.valueOf(type);
            String company = getString(getColumnIndex(JobiPositionDbSchema.PositionTable.Columns.COMPANY));

            Position position = new Position();
            position.setId(id);
            position.setTitle(title);
            position.setStatus(enumStatus);
            position.setLocation(location);
            position.setDescription(description);
            position.setFavorite(enumFav);
            position.setType(enumType);
            position.setCompany(company);

            return position;
        }
    }

    // getContentValues //////////////////////////////////////////////////////////////////////////////
    private static ContentValues getContentValues(Position position) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.ID, position.getId());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.TITLE, position.getTitle());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.STATUS, position.getStatus().toString());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.LOCATION, position.getLocation());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.DESCRIPTION, position.getDescription());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.FAVORITE, position.getFavorite().name());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.TYPE, position.getType().name());
        contentValues.put(JobiPositionDbSchema.PositionTable.Columns.COMPANY, position.getCompany().toString());

        return contentValues;
    }

    // addStoryToBacklog //////////////////////////////////////////////////////////////////////////////
//    public void addStoryToBacklog(Story story) {
//
//        String[] IDs = new String[]{story.getId()};
//        //IDs[0] = story.getId();
//
//        // If not present in the list at all, add //
//        if (getStoryById(story.getId()) == null) {
//            db.insert(JobiPositionDbSchema.PositionTable.NAME,null,getContentValues(story));
//        // Otherwise if it is then update it //
//        } else {
//            db.update(JobiPositionDbSchema.PositionTable.NAME,getContentValues(story),"ID=?",IDs);
//        }
//    }

    // getStoryById //////////////////////////////////////////////////////////////////////////////
//    public Story getStoryById(String id) {
//
//        if (id == null) {
//            return null;
//        } else {
//            for (Story story : queryStories("ID=?", new String[]{id}, null)) {
//                if (story.getId().equals(id)) {
//                    return story;
//                }
//            }
//            return null;
//        }
//    }

    // getAllStories //////////////////////////////////////////////////////////////////////////////
//    public List<Story> getAllStories() {
//        List<Story> prioritizedStories = queryStories(null,null,null);
//
//        Collections.sort(prioritizedStories, new Comparator<Story>() {
//            @Override
//            public int compare(Story story1, Story story2) {
//                if (story1.getPriority().equals(story2.getPriority())) {
//                    if (story1.getStatus().equals(story2.getStatus())) {
//                        return story1.getTimeCreated().compareTo(story2.getTimeCreated());
//                    } else {
//                        return story1.getStatus().compareTo(story2.getStatus());
//                    }
//                } else {
//                    return story1.getPriority().compareTo(story2.getPriority());
//                }
//            }
//        });
//
//        return prioritizedStories;
//    }


    // getCurrentSprintStories //////////////////////////////////////////////////////////////////////////////
//    public List<Story> getCurrentSprintStories() {
//
//        String[] priority = new String[]{Story.Priority.CURRENT.toString()};
//        //priority[0] = Story.Priority.CURRENT.toString();
//
//        return queryStories("PRIORITY=?",priority,null);
//    }

}
