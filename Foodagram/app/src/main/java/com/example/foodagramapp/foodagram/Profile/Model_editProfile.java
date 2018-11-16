package com.example.foodagramapp.foodagram.Profile;

public class Model_editProfile  {


    String name;
    String userId;
    String description;
    String email;
    String sex;
    String birthDate;

    Model_editProfile(){

    }

    public Model_editProfile(String name, String userId, String description, String email, String sex, String birthDate) {
        this.name = name;
        this.userId = userId;
        this.description = description;
        this.email = email;
        this.sex = sex;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}