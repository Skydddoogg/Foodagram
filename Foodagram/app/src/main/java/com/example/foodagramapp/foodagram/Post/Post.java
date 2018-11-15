package com.example.foodagramapp.foodagram.Post;

public class Post {

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