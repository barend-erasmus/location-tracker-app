package com.erasmus.barend.locationtracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocationTrackerService extends Service {
    public LocationTrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
