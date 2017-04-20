package edu.umd.cs.jobi.model;


import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Story implements Serializable {
    private String id;
    private String summary;
    private String acceptanceCriteria;
    private double storyPoints;
    private Priority priority = Priority.LATER;
    private Status status = Status.TODO;
    private Date timeCreated;

    public Story() {
        id = UUID.randomUUID().toString();
        timeCreated = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(String acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public double getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(double storyPoints) {
        this.storyPoints = storyPoints;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setPriorityCurrent() {
        this.priority = Priority.CURRENT;
    }

    public void setPriorityNext() {
        this.priority = Priority.NEXT;
    }

    public void setPriorityLater() {
        this.priority = Priority.LATER;
    }

    public Status getStatus() {
        return status;
    }

    public int getStatusPosition() {
        switch (status) {
            case TODO:
                return 0;
            case IN_PROGRESS:
                return 1;
            case DONE:
                return 2;
            default:
                return 0;
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(int position) {
        switch (position) {
            case 0:
                this.status = Status.TODO;
                break;
            case 1:
                this.status = Status.IN_PROGRESS;
                break;
            case 2:
                this.status = Status.DONE;
                break;
            default:
                this.status = Status.TODO;
                break;
        }
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public String toString() {
        int displayLength = 10;
        return summary.substring(0, summary.length() >= displayLength ? displayLength : summary.length()) + " : " + storyPoints + " : " + priority + " : " + status;
    }

    public enum Priority {
        CURRENT, NEXT, LATER
    }

    public enum Status {
        TODO, IN_PROGRESS, DONE;
    }
}
