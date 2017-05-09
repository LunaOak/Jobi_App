package edu.umd.cs.jobi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Juan on 4/28/2017.
 */

public class Reminder implements Serializable {
    private String id;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    private String eventId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    private String companyId;
    private String positionId;
    private String title;
    private Date date;

    public Reminder() {
        id = UUID.randomUUID().toString();
    }

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
