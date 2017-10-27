package com.erasmus.barend.locationtracker.models;

import java.util.Date;

/**
 * Created by Barend Erasmus on 10/27/2017.
 */

public class Location {

    public float Accuracy;
    public double Altitude;
    public double Bearing;
    public float Speed;
    public double Longitude;
    public double Latitude;
    public long Timestamp;

    public Location(float accuracy, double altitude, double bearing, float speed, double longitude, double latitude, long timestamp) {
        Accuracy = accuracy;
        Altitude = altitude;
        Bearing = bearing;
        Speed = speed;
        Longitude = longitude;
        Latitude = latitude;
        Timestamp = timestamp;
    }
}
