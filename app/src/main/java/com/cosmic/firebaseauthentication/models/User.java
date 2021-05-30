package com.cosmic.firebaseauthentication.models;

public class User {

    private String dob, first_name, last_name, uid, email, account_creation_date;

    public User() {

    }

    public User(String dob, String first_name, String last_name, String uid, String email, String account_creation_date) {
        this.dob = dob;
        this.first_name = first_name;
        this.last_name = last_name;
        this.uid = uid;
        this.email = email;
        this.account_creation_date = account_creation_date;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount_creation_date() {
        return account_creation_date;
    }

    public void setAccount_creation_date(String account_creation_date) {
        this.account_creation_date = account_creation_date;
    }

    @Override
    public String toString() {
        return "User{" +
                "dob='" + dob + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", account_creation_date='" + account_creation_date + '\'' +
                '}';
    }

}
