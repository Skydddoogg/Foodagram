package com.example.foodagramapp.foodagram.user;

import android.support.annotation.NonNull;

public class User implements Comparable<User> {

    private String displayname;
    private String email;
    private String name;
    private String dob;
    private String vitae;
    private String sex;

    public User(){

    }

    public User(String displayname, String email, String name, String dob, String vitae, String sex){
        this.displayname = displayname;
        this.email = email;
        this.name = name;
        this.dob = dob;
        this.vitae = vitae;
        this.sex = sex;
    }

    @Override
    public int compareTo(@NonNull User o) {
        return 0;
    }

    public String getUsername() {
        return displayname;
    }

    public void setUsername(String username) {
        this.displayname = displayname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getVitae() {
        return vitae;
    }

    public void setVitae(String vitae) {
        this.vitae = vitae;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}

