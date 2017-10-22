package com.erasmus.barend.locationtracker.listeners;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.erasmus.barend.locationtracker.LocationTrackerService;
import com.erasmus.barend.locationtracker.MainActivity;
import com.erasmus.barend.locationtracker.loggers.FileLogger;
import com.erasmus.barend.locationtracker.repositories.LocationRepository;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Barend Erasmus on 10/21/2017.
 */

public class LocationTrackerLocationListener implements LocationListener {

    private Context _context;
    private LocationRepository _locationRepository;
    private FileLogger _logger;

    public LocationTrackerLocationListener(Context context) {

        _context = context;
        _locationRepository = new LocationRepository(_context);
        _logger = FileLogger.GetLogger("location-tracker");
    }

    @Override
    public void onLocationChanged(Location location) {
        _locationRepository.Insert(location.getLongitude(), location.getLatitude(), new Date());
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
