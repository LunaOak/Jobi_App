package edu.umd.cs.jobi.model;

import java.util.Date;

/**
 * Created by Juan on 4/28/2017.
 */

public class Reminder {
    private String title;
    private Date date;

    public Reminder(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }
}
