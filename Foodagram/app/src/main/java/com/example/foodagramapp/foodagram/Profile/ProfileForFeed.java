package com.example.foodagramapp.foodagram.Profile;

import android.os.Parcel;
import android.os.Parcelable;

public class ProfileForFeed implements Parcelable{
   private String dob, email, name, profile_img_url, sex, username, vitae;


   public ProfileForFeed (){

   }


    protected ProfileForFeed(Parcel in) {
        dob = in.readString();
        email = in.readString();
        name = in.readString();
        profile_img_url = in.readString();
        sex = in.readString();
        username = in.readString();
        vitae = in.readString();
    }

    public static final Creator<ProfileForFeed> CREATOR = new Creator<ProfileForFeed>() {
        @Override
        public ProfileForFeed createFromParcel(Parcel in) {
            return new ProfileForFeed(in);
        }

        @Override
        public ProfileForFeed[] newArray(int size) {
            return new ProfileForFeed[size];
        }
    };

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
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

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVitae() {
        return vitae;
    }

    public void setVitae(String vitae) {
        this.vitae = vitae;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dob);
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeString(profile_img_url);
        parcel.writeString(sex);
        parcel.writeString(username);
        parcel.writeString(vitae);
    }
}
