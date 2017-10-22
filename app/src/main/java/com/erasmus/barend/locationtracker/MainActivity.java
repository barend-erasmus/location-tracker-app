package com.erasmus.barend.locationtracker;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.erasmus.barend.locationtracker.loggers.FileLogger;
import com.erasmus.barend.locationtracker.repositories.BaseRepository;
import com.erasmus.barend.locationtracker.utilities.FileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class MainActivity extends Activity {

    private final int REQUEST_LOCATION_PERMISSIONS_CODE = 4857;

    private FileLogger _logger;
    private Button _btnStartService;
    private Button _btnStopService;
    private Button _btnExportDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _logger = FileLogger.GetLogger("location-tracker");


        CheckAllPermissions();

        _btnStartService = (Button) findViewById(R.id.btn_start_service);
        _btnStopService = (Button) findViewById(R.id.btn_stop_service);
        _btnExportDatabase = (Button) findViewById(R.id.btn_export_database);

        if (IsServiceRunning(LocationTrackerService.class)) {
            _btnStartService.setEnabled(false);
            _btnStopService.setEnabled(true);
        }

        ConfigureOnClickListeners();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void StartService() {
        if (!IsServiceRunning(LocationTrackerService.class) && GPSEnabled()) {
            Intent intent = new Intent(MainActivity.this,
                    LocationTrackerService.class);

            MainActivity.this.startService(intent);

            _btnStartService.setEnabled(false);
            _btnStopService.setEnabled(true);

            _logger.Info("Service Started");
        }
    }

    private void StopService() {
        if (IsServiceRunning(LocationTrackerService.class)) {
            Intent intent = new Intent(MainActivity.this,
                    LocationTrackerService.class);
            MainActivity.this.stopService(intent);

            _btnStartService.setEnabled(true);
            _btnStopService.setEnabled(false);

            _logger.Info("Service Stopped");
        }
    }

    private void ExportDatabase() {
        File src = getDatabasePath(BaseRepository.DATABASE_NAME);
        File dest = new File(FileHelper.GetExternalStoragePath(String.format("location-tracker-backup-%s.db", new Date().getTime())));

        FileHelper.Copy(src, dest);

        Toast.makeText(MainActivity.this, "Successfully exported database.",
                Toast.LENGTH_LONG).show();
    }

    private boolean GPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OpenLocationSourceSettings();

            return false;
        }

        return true;
    }

    private void OpenLocationSourceSettings() {
        Intent locationSourceSettingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(locationSourceSettingsIntent);
    }

    private boolean IsServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
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

    private void CheckAllPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, REQUEST_LOCATION_PERMISSIONS_CODE);
            }
        }
    }
}
