package edu.umd.cs.jobi.model;

import java.io.Serializable;
import java.util.UUID;

public class Company implements Serializable {
    private String id;
    private String name;
    private boolean current;
    private String location;
    private Favorite favorite = Favorite.NO;

    public Company(String name, boolean current) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.current = current;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getCurrent() {
        return this.current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public enum Favorite {
        YES, NO;
    }

    @Override
    public String toString() {
        return name;
    }
}