package com.example.foodagramapp.foodagram.Profile;

import android.media.Image;

public class Model_profile {

    //    Image menuImage;
//    Image profileImage;
    String menuName;
    String profileName;
    String postTime;
    String menuPrice;



    String location;

//    public Image getMenuImage() {
//        return menuImage;
//    }

    public Model_profile( String menuName, String profileName, String menuPrice , String location) {
//        this.menuImage = menuImage;
//        this.profileImage = profileImage;
        this.menuName = menuName;
        this.profileName = profileName;
        this.menuPrice = menuPrice;
        this.postTime = postTime;
        this.location = location;
    }

//    public void setMenuImage(Image menuImage) {
//        this.menuImage = menuImage;
//    }
//
//    public Image getProfileImage() {
//        return profileImage;
//    }
//
//    public void setProfileImage(Image profileImage) {
//        this.profileImage = profileImage;
//    }


    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}