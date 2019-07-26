package com.royce.tripbotify.database;

import com.amadeus.resources.PointOfInterest;

import java.util.Arrays;

import io.realm.RealmList;
import io.realm.RealmObject;

public class RealmPointOfInterest extends RealmObject {

    private String name,category,type,subType;
    private String city;
    private double lat,lon;
    private RealmList<String> tags;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public RealmList<String> getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = new RealmList<>(tags);
    }
}
