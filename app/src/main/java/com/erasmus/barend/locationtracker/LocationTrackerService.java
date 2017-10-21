package com.erasmus.barend.locationtracker;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import com.erasmus.barend.locationtracker.listeners.LocationTrackerLocationListener;

public class LocationTrackerService extends Service {

    private LocationManager _locationManager;
    private LocationListener _locationListener;

    public LocationTrackerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        _locationListener = new LocationTrackerLocationListener(LocationTrackerService.this.getApplicationContext());

        Criteria criteria = new Criteria();

        String providerName = _locationManager.getBestProvider(criteria, false);

        if (!_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OpenLocationSourceSettings();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
        }

        _locationManager.requestLocationUpdates(providerName, 1500, 10, _locationListener);

        return super.onStartCommand(intent, flags, startId);
    }

    private void OpenLocationSourceSettings() {
        Intent locationSourceSettingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(locationSourceSettingsIntent);
    }
}
