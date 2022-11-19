package com.techno71.fireservice;

public class Other_icons {
    String id;
    String title;
    double latitude;
    double longtude;
    String tag_color;

    public Other_icons() {
    }

    public Other_icons(String id, String title, double latitude, double longtude, String tag_color) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longtude = longtude;
        this.tag_color = tag_color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtude() {
        return longtude;
    }

    public void setLongtude(double longtude) {
        this.longtude = longtude;
    }

    public String getTag_color() {
        return tag_color;
    }

    public void setTag_color(String tag_color) {
        this.tag_color = tag_color;
    }
}
