package com.royce.tripbotify.database;

public class City {

    private String name, airportCode, languageCode;
    private double north,south,east,west;
    private int resId;

    public City(String name, String airportCode, double north, double south, double east, double west, int resId,String languageCode) {
        this.name = name;
        this.airportCode = airportCode;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.resId = resId;
        this.languageCode = languageCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", airportCode='" + airportCode + '\'' +
                ", north=" + north +
                ", south=" + south +
                ", east=" + east +
                ", west=" + west +
                ", resId=" + resId +
                '}';
    }
}
