package com.example.recycling;

public class Client {
    private String first_name;
    private String last_name;
    private String uid;

    private String email;

    public Client() {
    }

    public Client(String first_name, String last_name, String uid, String email) {
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

    @Override
    public String toString() {
        return  "firstName=" + first_name + "\n" +
                "lastName=" + last_name + "\n" +
                "uid=" + uid + "\n" +
                "email=" + email + "\n";
    }
}
