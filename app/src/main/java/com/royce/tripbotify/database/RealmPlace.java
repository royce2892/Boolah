package com.royce.tripbotify.database;

public class RealmPlace {

    private double rating, lat, lng;
    private String photo_url, name;
    private String tag;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPromTag() {
        try {
            return tag;
        } catch (Exception ex) {
            return "Sightseeing";
        }
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
