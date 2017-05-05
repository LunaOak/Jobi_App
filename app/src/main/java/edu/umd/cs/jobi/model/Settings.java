package edu.umd.cs.jobi.model;

import java.io.Serializable;

public class Settings implements Serializable {

    private Status status = Status.INTERVIEWING;

    public Settings() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        INTERVIEWING, SEARCHING, NOT_SEARCHING;
    }

    public enum Notifications {
        INTERVIEWS, EMAILS, DEADLINES;
    }
}
