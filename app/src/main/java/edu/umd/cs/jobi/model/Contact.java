package edu.umd.cs.jobi.model;

public class Contact {

    private String jobTitle;
    private String name;
    private String email;
    private String address;
    private String phone;

    public Contact(String name, String jobTitle, String email, String address, String phone){
        this.name = name;
        this.email = email;
        this.jobTitle = jobTitle;
        this.address = address;
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }





}