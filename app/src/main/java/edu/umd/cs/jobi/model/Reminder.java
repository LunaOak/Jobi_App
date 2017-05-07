package edu.umd.cs.jobi.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Juan on 4/28/2017.
 */

public class Reminder {
    private String id;
    private String title;
    private Date date;

    public Reminder(String title, Date date) {
        id = UUID.randomUUID().toString();
        this.title = title;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
