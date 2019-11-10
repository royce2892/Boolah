package com.royce.tripbotify.database;

import io.realm.RealmObject;

public class RealmTrackFlight extends RealmObject {

    String departureCode, arrivalCode, date, price, flightCode;

    public String getDepartureCode() {
        return departureCode;
    }

    public void setDepartureCode(String departureCode) {
        this.departureCode = departureCode;
    }

    public String getAirportCode() {
        return flightCode == null ? departureCode + " - " + arrivalCode : departureCode + " - " + arrivalCode + ", " + flightCode;
    }

    public String getArrivalCode() {
        return arrivalCode;
    }

    public void setArrivalCode(String arrivalCode) {
        this.arrivalCode = arrivalCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(String flightCode) {
        this.flightCode = flightCode;
    }

    @Override
    public String toString() {
        String r = "Date -> " + date + "\n" + "Price -> " + price + "\n" + "Departure -> " + departureCode + "\n" + "Arrival -> " + arrivalCode + "\n";
        return flightCode == null ? r : r + "Code -> " + flightCode;
    }
}
