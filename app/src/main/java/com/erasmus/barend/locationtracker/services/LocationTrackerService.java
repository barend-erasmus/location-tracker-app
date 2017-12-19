package com.erasmus.barend.locationtracker.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.erasmus.barend.locationtracker.BackgroundService;
import com.erasmus.barend.locationtracker.models.Location;
import com.erasmus.barend.locationtracker.repositories.BaseRepository;
import com.erasmus.barend.locationtracker.repositories.LocationRepository;
import com.erasmus.barend.locationtracker.utilities.FileHelper;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by Barend.Erasmus on 10/26/2017.
 */

public class LocationTrackerService {

    private Button _btnStartService;
    private Button _btnStopService;
    private Button _btnUploadDatabase;
    private Button _btnExportDatabase;
    private TextView _txtNumberOfEntries;
    private TextView _txtMaxSpeed;
    private TextView _txtAvgSpeed;

    private ProgressDialog progress;

    private Context _context;

    private LocationRepository _locationRepository;

    public LocationTrackerService(Context context) {

        _context = context;

        _locationRepository = new LocationRepository(_context);
    }

    public LocationTrackerService(Context context,
                                  Button btnStartService,
                                  Button btnStopService,
                                  Button btnUploadDatabase,
                                  Button btnExportDatabase,
                                  TextView txtNumberOfEntries,
                                  TextView txtMaxSpeed,
                                  TextView txtAvgSpeed) {

        this(context);

        _btnStartService = btnStartService;
        _btnStopService = btnStopService;
        _btnUploadDatabase = btnUploadDatabase;
        _btnExportDatabase = btnExportDatabase;
        _txtNumberOfEntries = txtNumberOfEntries;
        _txtMaxSpeed = txtMaxSpeed;
        _txtAvgSpeed = txtAvgSpeed;

        if (IsServiceRunning(BackgroundService.class)) {
            _btnStartService.setEnabled(false);
            _btnStopService.setEnabled(true);
        }

        ConfigureOnClickListeners();

        _txtNumberOfEntries.setText("Number of Entries: " + _locationRepository.NumberOfEntries());
        _txtMaxSpeed.setText("Max Speed (m/s): " + _locationRepository.MaxSpeed());
        _txtAvgSpeed.setText("Average Speed (m/s): " + _locationRepository.AverageSpeed());
    }

    public void Log(float accuracy, double altitude, double bearing, float speed, double longitude, double latitude) {
        _locationRepository.Insert(accuracy, altitude, bearing, speed, longitude, latitude, new Date());
    }

    public void CloseDatabase() {
        _locationRepository.Close();
    }

    private void StartService() {
        if (!IsServiceRunning(BackgroundService.class) && GPSEnabled()) {
            Intent intent = new Intent(_context,
                    BackgroundService.class);

            _context.startService(intent);

            _btnStartService.setEnabled(false);
            _btnStopService.setEnabled(true);
        }
    }

    private void StopService() {
        if (IsServiceRunning(BackgroundService.class)) {
            Intent intent = new Intent(_context,
                    BackgroundService.class);
            _context.stopService(intent);

            _btnStartService.setEnabled(true);
            _btnStopService.setEnabled(false);
        }
    }

    private void UploadDatabase() {

        TelephonyManager telephonyManager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") final String deviceId = telephonyManager.getDeviceId();

        progress = progress.show(_context, null, "Please wait...");

        Thread t = new Thread() {

            public void run() {

                try {
                    List<Location> locations = _locationRepository.List();

                    for (int i = 0; i < Math.ceil((double) locations.size() / (double) 300); i++) {

                        int startIndex = i * 300;
                        int endIndex = (i + 1) * 300;
                        List<Location> tempLocations = locations.subList(startIndex, endIndex > locations.size() ? locations.size() - 1 : endIndex);

                        UploadDatabase(tempLocations, deviceId);

                        for (Location location : tempLocations) {
                            _locationRepository.MarkAsUploaded(location.Timestamp);
                        }
                    }

                    Handler handler2 = new Handler(Looper.getMainLooper());
                    handler2.post(new Runnable() {
                        public void run() {
                            progress.dismiss();
                            ShowDialog("Success!", "Successfully uploaded database.");
                        }
                    });

                } catch (final Exception e) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            progress.dismiss();
                            ShowDialog("Uh oh!", "An error occurred, please try again later.");
                        }
                    });

                    e.printStackTrace();
                }
            }
        };

        t.start();
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

    private void ExportDatabase() {
        File src = _context.getDatabasePath(BaseRepository.DATABASE_NAME);
        File dest = new File(FileHelper.GetExternalStoragePath(String.format("location-tracker-%s.db", new Date().getTime())));

        FileHelper.Copy(src, dest);

        Toast.makeText(_context, "Successfully exported database.",
                Toast.LENGTH_LONG).show();
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

        _btnUploadDatabase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UploadDatabase();
            }
        });

        _btnExportDatabase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExportDatabase();
            }
        });
    }

    private void UploadDatabase(List<Location> data, String deviceId) throws IOException {

        String json = new Gson().toJson(data);

        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(),
                5000); // Timeout Limit
        HttpResponse response;

        HttpPost post = new HttpPost("http://location-tracker.openservices.co.za/location/create");

        StringEntity se = new StringEntity(json);
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
                "application/json"));
        post.addHeader("x-device-id", deviceId);
        post.setEntity(se);
        response = client.execute(post);

        if (response != null) {
            InputStream in = response.getEntity().getContent();
            final String content = ConvertStreamToString(in);
        }

    }

    private String ConvertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private void ShowDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                _context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

}
