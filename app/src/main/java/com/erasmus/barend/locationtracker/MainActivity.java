package com.erasmus.barend.locationtracker;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (IsServiceRunning(LocationTrackerService.class)) {
            // Intent i = new Intent(MainActivity.this,
                    // LocationTrackerService.class);
            // MainActivity.this.stopService(i);
            // Toast.makeText(MainActivity.this, "Service stopped",
                    // Toast.LENGTH_LONG).show();

        } else {
            Intent i = new Intent(MainActivity.this,
                    LocationTrackerService.class);
            MainActivity.this.startService(i);
            Toast.makeText(MainActivity.this, "Service started",
                    Toast.LENGTH_LONG).show();
        }
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
}
