package com.erasmus.barend.locationtracker;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.erasmus.barend.locationtracker.services.LocationTrackerService;

public class MainActivity extends Activity {

    private final int REQUEST_LOCATION_PERMISSIONS_RESULT_CODE = 4857;

    private LocationTrackerService _locationTrackerService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CheckAllPermissions();

        Button btnStartService = (Button) findViewById(R.id.btn_start_service);
        Button btnStopService = (Button) findViewById(R.id.btn_stop_service);
        Button btnUploadDatabase = (Button) findViewById(R.id.btn_upload_database);
        Button btnExportDatabase = (Button) findViewById(R.id.btn_export_database);

        TextView txtNumberOfEntries = (TextView) findViewById(R.id.txt_number_of_entries);
        TextView txtMaxSpeed = (TextView) findViewById(R.id.txt_max_speed);
        TextView txtAvgSpeed = (TextView) findViewById(R.id.txt_avg_speed);

        _locationTrackerService = new LocationTrackerService(MainActivity.this, btnStartService, btnStopService, btnUploadDatabase, btnExportDatabase, txtNumberOfEntries, txtMaxSpeed, txtAvgSpeed);
    }


    @Override
    protected void onDestroy() {
        _locationTrackerService.CloseDatabase();

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void CheckAllPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                    checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_LOCATION_PERMISSIONS_RESULT_CODE);
            }
        }
    }
}
