package com.example.authapplication;

public class LocationMap {
    private String longitude;
    private String latitude;

    public LocationMap(){}
    public LocationMap(String longitude, String latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}
