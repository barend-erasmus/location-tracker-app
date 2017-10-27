package com.erasmus.barend.locationtracker.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.erasmus.barend.locationtracker.services.LocationTrackerService;

/**
 * Created by Barend Erasmus on 10/21/2017.
 */

public class LocationTrackerLocationListener implements LocationListener {

    private LocationTrackerService _locationTrackerService;

    public LocationTrackerLocationListener(LocationTrackerService locationTrackerService) {
        _locationTrackerService = locationTrackerService;
    }

    @Override
    public void onLocationChanged(Location location) {

        float accuracy = location.hasAccuracy()? location.getAccuracy() : -1;
        float speed = location.hasSpeed()? location.getSpeed() : -1;
        double altitude = location.hasAltitude()? location.getAltitude() : -1;
        double bearing = location.hasBearing()? location.getBearing() : -1;

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        _locationTrackerService.Log(accuracy, altitude, bearing, speed, longitude, latitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
