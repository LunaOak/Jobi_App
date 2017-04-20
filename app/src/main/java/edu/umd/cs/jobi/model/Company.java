package edu.umd.cs.jobi.model;

import java.io.Serializable;

public class Company implements Serializable {
    private String name;
    private boolean current;

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

    @Override
    public String toString() {
        return name;
    }
}