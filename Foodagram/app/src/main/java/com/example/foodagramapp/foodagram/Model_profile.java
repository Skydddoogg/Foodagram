package com.example.foodagramapp.foodagram;

import android.media.Image;

public class Model_profile {

    Image menuImage;
    Image profileImage;
    String menuName;
    String profileName;
    String postTime;

    public Image getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(Image menuImage) {
        this.menuImage = menuImage;
    }

    public Image getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Image profileImage) {
        this.profileImage = profileImage;
    }

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
}
