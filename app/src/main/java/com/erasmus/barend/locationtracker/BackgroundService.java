package com.erasmus.barend.locationtracker;

import android.Manifest;
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
import com.erasmus.barend.locationtracker.services.LocationTrackerService;

public class BackgroundService extends Service {

    private LocationManager _locationManager;
    private LocationListener _locationListener;

    private LocationTrackerService _locationTrackerService;

    public BackgroundService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        _locationTrackerService = new LocationTrackerService(BackgroundService.this.getApplicationContext());

        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        _locationListener = new LocationTrackerLocationListener(_locationTrackerService);

        ConfigureLocationManager();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        _locationManager.removeUpdates(_locationListener);
        _locationManager = null;

        super.onDestroy();
    }

    private void ConfigureLocationManager() {
        Criteria criteria = new Criteria();

        String providerName = _locationManager.getBestProvider(criteria, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
        }

        _locationManager.requestLocationUpdates(providerName, 2000, 10, _locationListener);
    }
}
