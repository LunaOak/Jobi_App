package edu.umd.cs.jobi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.*;

public class Settings implements Serializable {

    private String id;
    private Status status = Status.INTERVIEWING;
    private NotificationSwitch notificationSwitch = NotificationSwitch.ON;
    private List<Notifications> notifications = new ArrayList<>();

    public Settings() {
        notifications.add(Notifications.INTERVIEWS);
        notifications.add(Notifications.EMAILS);
        notifications.add(Notifications.DEADLINES);
        id = "1";
    }

    // Settings Status //

    public enum Status {
        INTERVIEWING, SEARCHING, NOT_SEARCHING
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Settings NotificationsSwitch //

    public enum NotificationSwitch {
        ON, OFF
    }

    public NotificationSwitch getSwitch() {
        return notificationSwitch;
    }

    public void setSwitch(NotificationSwitch toggle) {
        notificationSwitch = toggle;
    }

    // Settings Notifications List //

    public enum Notifications {
        INTERVIEWS, EMAILS, DEADLINES
    }

    public List<Notifications> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notifications> new_notifications) {
        notifications.clear();
        notifications = new_notifications;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String i) {
        this.id = i;
    }

}
