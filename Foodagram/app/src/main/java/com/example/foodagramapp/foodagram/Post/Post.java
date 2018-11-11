package com.example.foodagramapp.foodagram.Post;

public class Post {

    private String description;
    private String location;
    private String menu_image_url;
    private String menu_name;
    private Double menu_price;
    private Double timestamp;

    public Post(String description, String location, String menu_image_url, String menu_name, Double menu_price, Double timestamp){
        this.description = description;
        this.location = location;
        this.menu_image_url = menu_image_url;
        this.menu_name = menu_name;
        this.menu_price = menu_price;
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMenu_image_url() {
        return menu_image_url;
    }

    public void setMenu_image_url(String menu_image_url) {
        this.menu_image_url = menu_image_url;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public double getMenu_price() {
        return menu_price;
    }

    public void setMenu_price(double menu_price) {
        this.menu_price = menu_price;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }
}
