package com.example.recycling;

public class Driver {
    private String city;

    private String first_name;
    private String last_name;
    private String uid;

    private String email;


    public Driver() {

    }

    public Driver(String city, String first_name, String last_name, String uid, String email) {
        this.city = city;
        this.first_name = first_name;
        this.last_name = last_name;
        this.uid = uid;
        this.email = email;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return  "city=" + city + "\n" +
                "firstName=" + first_name + "\n" +
                "lastName=" + last_name + "\n" +
                "email=" + email + "\n" +
                "uid=" + uid;
    }
}
