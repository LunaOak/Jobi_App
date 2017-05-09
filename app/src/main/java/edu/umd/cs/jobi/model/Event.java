package edu.umd.cs.jobi.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Event implements Serializable {
    private String id;
    private String title;
    private String company;
    private String position;
    private Type type;
    private Date date;
    private String location;
    private List<Contact> contacts;
    private List<Reminder> reminders;

    public Event() {
        id = UUID.randomUUID().toString();
        contacts = new ArrayList<Contact>();
        reminders = new ArrayList<Reminder>();
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

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(int position) {
        switch (position) {
            case 0:
                this.type = Type.INTERVIEW;
                break;
            case 1:
                this.type = Type.EMAIL;
                break;
            case 2:
                this.type = Type.DEADLINE;
                break;
            default:
                this.type = Type.INTERVIEW;
                break;
        }
    }

    public Type getType() {
        return type;
    }

    public int getPositionType() {
        switch (type) {
            case INTERVIEW:
                return 0;
            case EMAIL:
                return 1;
            case DEADLINE:
                return 2;
            default:
                return 0;
        }
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
    }

    public enum Type {
        INTERVIEW, EMAIL, DEADLINE;
    }
}

