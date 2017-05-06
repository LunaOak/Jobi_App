package edu.umd.cs.jobi.model;

import java.io.Serializable;
import java.util.UUID;

public class Contact implements Serializable {

    private String id;
    private String jobTitle;
    private String name;
    private String email;
    private String phone;

    public Contact(String name, String jobTitle, String email, String phone){
        id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.jobTitle = jobTitle;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }





}