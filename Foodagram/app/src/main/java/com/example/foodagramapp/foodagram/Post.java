package com.example.foodagramapp.foodagram;

public class Post {
    private String description,location,menu_image_url,menu_name;
    private double menu_price;

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
}
