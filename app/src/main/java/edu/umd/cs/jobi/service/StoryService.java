package edu.umd.cs.jobi.service;


import java.util.List;

import edu.umd.cs.jobi.model.Story;

public interface StoryService {
    public void addStoryToBacklog(Story story);
    public Story getStoryById(String id);
    public List<Story> getAllStories();
    public List<Story> getCurrentSprintStories();
}
