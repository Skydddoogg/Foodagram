package com.example.foodagramapp.foodagram;

import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {

    private String description;
    private String placeName;
    private String latitude;
    private String longitude;
    private String address;
    private String menu_image_url;
    private String menu_name;
    private Double menu_price;
    private Double timestamp;
    private String owner;

    public Post() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(description);
        dest.writeString(placeName);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(address);
        dest.writeString(menu_image_url);
        dest.writeString(menu_name);
        dest.writeDouble(menu_price);
        dest.writeDouble(timestamp);
        dest.writeString(owner);
    }

    protected Post(Parcel in) {
        description = in.readString();
        placeName = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        address = in.readString();
        menu_image_url = in.readString();
        menu_name = in.readString();
        if (in.readByte() == 0) {
            menu_price = null;
        } else {
            menu_price = in.readDouble();
        }
        if (in.readByte() == 0) {
            timestamp = null;
        } else {
            timestamp = in.readDouble();
        }
        owner = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String location) {
        this.placeName = placeName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMenuImageURL() {
        return menu_image_url;
    }

    public void setMenuImageURL(String menu_image_url) {
        this.menu_image_url = menu_image_url;
    }

    public String getMenuName() {
        return menu_name;
    }

    public void setMenuName(String menu_name) {
        this.menu_name = menu_name;
    }

    public double getMenuPrice() {
        return menu_price;
    }

    public void setMenuPrice(double menu_price) {
        this.menu_price = menu_price;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}