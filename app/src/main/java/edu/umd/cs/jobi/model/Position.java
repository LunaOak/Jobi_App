package edu.umd.cs.jobi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Position implements Serializable{

    private String id;
    private String title;
    private Status status = Status.TODO;
    private String location;
    private String description;
    private Favorite favorite = Favorite.NO;
    private Type type;
    private List<Contact> contacts;
    private String company;

    public Position() {
        id = UUID.randomUUID().toString();
        contacts = new ArrayList<Contact>();
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public Type getType() {
        return type;
    }

    public int getPositionType() {
        switch (type) {
            case FULL_TIME:
                return 0;
            case PART_TIME:
                return 1;
            case INTERNSHIP:
                return 2;
            case VOLUNTEER:
                return 3;
            default:
                return 0;
        }
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setType(int position) {
        switch (position) {
            case 0:
                this.type = Type.FULL_TIME;
                break;
            case 1:
                this.type = Type.PART_TIME;
                break;
            case 2:
                this.type = Type.INTERNSHIP;
                break;
            case 3:
                this.type = Type.VOLUNTEER;
                break;
            default:
                this.status = Status.TODO;
                break;
        }
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void addContact(Contact contact) {
       // this.contacts.append(contact);
    }

    // TODO verify this works
    public void removeContact(Contact contact) {
        for (Iterator<Contact> iter = contacts.listIterator(); iter.hasNext(); ) {
            Contact curr = iter.next();
            if (curr.equals(contact)) {
                iter.remove();
            }
        }
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return title + ", " + company;
    }

    public enum Status {
        TODO, IN_PROGRESS, DONE;
    }

    public enum Favorite {
        YES, NO;
    }

    public enum Type {
        FULL_TIME, PART_TIME, INTERNSHIP, VOLUNTEER;
    }
}