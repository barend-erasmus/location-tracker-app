package com.erasmus.barend.locationtracker.services;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.erasmus.barend.locationtracker.BackgroundService;
import com.erasmus.barend.locationtracker.loggers.FileLogger;
import com.erasmus.barend.locationtracker.repositories.BaseRepository;
import com.erasmus.barend.locationtracker.repositories.LocationRepository;
import com.erasmus.barend.locationtracker.utilities.FileHelper;

import java.io.File;
import java.util.Date;

/**
 * Created by Barend.Erasmus on 10/26/2017.
 */

public class LocationTrackerService {

    private FileLogger _logger;

    private Button _btnStartService;
    private Button _btnStopService;
    private Button _btnExportDatabase;

    private Context _context;

    private LocationRepository _locationRepository;

    public LocationTrackerService(Context context) {
        _logger = FileLogger.GetLogger("location-tracker");

        _context = context;

        _locationRepository = new LocationRepository(_context);
    }

    public LocationTrackerService(Context context, Button btnStartService, Button btnStopService, Button btnExportDatabase) {

        this(context);

        _btnStartService = btnStartService;
        _btnStopService = btnStopService;
        _btnExportDatabase = btnExportDatabase;

        if (IsServiceRunning(BackgroundService.class)) {
            _btnStartService.setEnabled(false);
            _btnStopService.setEnabled(true);
        }

        ConfigureOnClickListeners();
    }

    public void Log(double longitude, double latitude, double speed) {
        _locationRepository.Insert(longitude, latitude, speed, new Date());
    }

    private void StartService() {
        if (!IsServiceRunning(BackgroundService.class) && GPSEnabled()) {
            Intent intent = new Intent(_context,
                    BackgroundService.class);

            _context.startService(intent);

            _btnStartService.setEnabled(false);
            _btnStopService.setEnabled(true);

            _logger.Info("Service Started");
        }
    }

    private void StopService() {
        if (IsServiceRunning(BackgroundService.class)) {
            Intent intent = new Intent(_context,
                    BackgroundService.class);
            _context.stopService(intent);

            _btnStartService.setEnabled(true);
            _btnStopService.setEnabled(false);

            _logger.Info("Service Stopped");
        }
    }

    private void ExportDatabase() {
        File src = _context.getDatabasePath(BaseRepository.DATABASE_NAME);
        File dest = new File(FileHelper.GetExternalStoragePath(String.format("location-tracker-backup-%s.db", new Date().getTime())));

        FileHelper.Copy(src, dest);

        Toast.makeText(_context, "Successfully exported database.",
                Toast.LENGTH_LONG).show();
    }

    private boolean GPSEnabled() {
        LocationManager locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OpenLocationSourceSettings();

            return false;
        }

        return true;
    }

    private void OpenLocationSourceSettings() {
        Intent locationSourceSettingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        _context.startActivity(locationSourceSettingsIntent);
    }

    private boolean IsServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) _context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void ConfigureOnClickListeners() {
        _btnStartService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StartService();
            }
        });

        _btnStopService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StopService();
            }
        });

        _btnExportDatabase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExportDatabase();
            }
        });
    }

}
