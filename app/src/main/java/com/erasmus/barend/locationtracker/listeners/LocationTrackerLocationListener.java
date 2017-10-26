package com.erasmus.barend.locationtracker.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.erasmus.barend.locationtracker.loggers.FileLogger;
import com.erasmus.barend.locationtracker.services.LocationTrackerService;

/**
 * Created by Barend Erasmus on 10/21/2017.
 */

public class LocationTrackerLocationListener implements LocationListener {

    private LocationTrackerService _locationTrackerService;
    private FileLogger _logger;

    public LocationTrackerLocationListener(LocationTrackerService locationTrackerService) {

        _logger = FileLogger.GetLogger("location-tracker");
        _locationTrackerService = locationTrackerService;
    }

    @Override
    public void onLocationChanged(Location location) {
        _locationTrackerService.Log(location.getLongitude(), location.getLatitude(), location.getSpeed());
        _logger.Info(String.format("Location changed"));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        _logger.Info(String.format("%s enabled", provider));
    }

    @Override
    public void onProviderDisabled(String provider) {
        _logger.Info(String.format("%s disabled", provider));
    }
}
