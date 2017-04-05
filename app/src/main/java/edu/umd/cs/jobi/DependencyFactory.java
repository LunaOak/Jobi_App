package edu.umd.cs.jobi;


import android.content.Context;

import edu.umd.cs.jobi.service.StoryService;
import edu.umd.cs.jobi.service.impl.InMemoryStoryService;

public class DependencyFactory {
    private static StoryService storyService;

    public static StoryService getStoryService(Context context) {
        if (storyService == null) {
            storyService = new InMemoryStoryService(context);
        }
        return storyService;
    }
}
