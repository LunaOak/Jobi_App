package edu.umd.cs.jobi.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Event {
    private String id;
    private String title;
    private Position position;
    private Date date;
    private String address;
    private String city;
    private String state;
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

    public void setCompany(Position position) {
        this.position = position;
    }

    public String getCompanyName() {
        return position.getCompany().getName();
    }

    public String getPositionTitle() {
        return position.getTitle();
    }

    public String getLocation() {
        return address + ", " + city + ", " + state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Contact> getContacts() {
        return new ArrayList<Contact>(contacts);
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public List<Reminder> getReminders() {
        return new ArrayList<Reminder>(reminders);
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
    }


}

