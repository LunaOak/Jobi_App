package edu.umd.cs.jobi.model;

import java.io.Serializable;

public class Company implements Serializable {
    private String name;
    private boolean current;
    private String location;

    public Company(String name, boolean current) {
        this.name = name;
        this.current = current;
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

    @Override
    public String toString() {
        return name;
    }
}