package com.erasmus.barend.locationtracker.listeners;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.erasmus.barend.locationtracker.LocationTrackerService;
import com.erasmus.barend.locationtracker.MainActivity;

/**
 * Created by Barend Erasmus on 10/21/2017.
 */

public class LocationTrackerLocationListener implements LocationListener {

    private Context _context;

    public LocationTrackerLocationListener(Context context) {
        _context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(_context, location.getLatitude() + ", " + location.getLongitude(),
                Toast.LENGTH_LONG).show();
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
