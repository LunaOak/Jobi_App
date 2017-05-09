package edu.umd.cs.jobi;


import android.content.Context;

import edu.umd.cs.jobi.service.CompanyService;
import edu.umd.cs.jobi.service.EventService;
import edu.umd.cs.jobi.service.PositionService;
import edu.umd.cs.jobi.service.StoryService;
import edu.umd.cs.jobi.service.SettingsService;
import edu.umd.cs.jobi.service.impl.InMemoryStoryService;
import edu.umd.cs.jobi.service.impl.InMemorySettingsService;
import edu.umd.cs.jobi.service.impl.SQLiteCompanyService;
import edu.umd.cs.jobi.service.impl.SQLiteEventService;
import edu.umd.cs.jobi.service.impl.SQLitePositionService;
import edu.umd.cs.jobi.service.impl.SQLiteSettingsService;

public class DependencyFactory {

    private static StoryService storyService;
    private static PositionService positionService;
    private static CompanyService companyService;
    private static EventService eventService;
    private static SettingsService settingsService;

    public static StoryService getStoryService(Context context) {
        if (storyService == null) {
            storyService = new InMemoryStoryService(context);
        }
        return storyService;
    }

    public static PositionService getPositionService(Context context) {
        if (positionService == null) {
            positionService = new SQLitePositionService(context);
        }
        return positionService;
    }

    public static CompanyService getCompanyService(Context context) {
        if (companyService == null) {
            companyService = new SQLiteCompanyService(context);
        }
        return companyService;
    }

    public static EventService getEventService(Context context) {
        if (eventService == null) {
            eventService = new SQLiteEventService(context);
        }

        return eventService;
    }

    public static SettingsService getSettingsService(Context context) {
        if (settingsService == null) {
            settingsService = new SQLiteSettingsService(context);
            //settingsService = new InMemorySettingsService(context);
        }
        return settingsService;
    }
}
