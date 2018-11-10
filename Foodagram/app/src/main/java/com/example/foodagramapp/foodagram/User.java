package com.example.foodagramapp.foodagram;

public class User {
    private String name;

    @Override
    public String toString() {
        return this.name;
    }
    public User(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
