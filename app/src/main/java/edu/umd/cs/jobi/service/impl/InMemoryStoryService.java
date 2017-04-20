package edu.umd.cs.jobi.service.impl;


import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.umd.cs.jobi.model.Story;
import edu.umd.cs.jobi.service.StoryService;

public class InMemoryStoryService implements StoryService {
    private Context context;
    private List<Story> stories;

    public InMemoryStoryService(Context context) {
        this.context = context;
        this.stories = new ArrayList<Story>();
    }

    public void addStoryToBacklog(Story story) {
        Story currStory = getStoryById(story.getId());
        if (currStory == null) {
            stories.add(story);
        } else {
            currStory.setSummary(story.getSummary());
            currStory.setAcceptanceCriteria(story.getAcceptanceCriteria());
            currStory.setPriority(story.getPriority());
            currStory.setStatus(story.getStatusPosition());
        }
    }

    public Story getStoryById(String id) {
        for (Story story : stories) {
            if (story.getId().equals(id)) {
                return story;
            }
        }

        return null;
    }

    public List<Story> getAllStories() {
        List<Story> prioritizedStories = new ArrayList<Story>(stories);

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

    public List<Story> getCurrentSprintStories() {
        List<Story> currentStories = new ArrayList<Story>();

        for (Story story : stories) {
            if (story.getPriority().equals(Story.Priority.CURRENT)) {
                currentStories.add(story);
            }
        }

        return currentStories;
    }
}
