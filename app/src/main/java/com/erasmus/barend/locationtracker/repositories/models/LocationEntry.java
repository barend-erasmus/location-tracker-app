package com.erasmus.barend.locationtracker.repositories.models;

import android.provider.BaseColumns;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public final class LocationEntry implements BaseColumns {

    public static final String CREATE_TABLE =
            "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                    LocationEntry._ID + " INTEGER PRIMARY KEY," +
                    LocationEntry.COLUMN_NAME_LATITUDE + " REAL," +
                    LocationEntry.COLUMN_NAME_LONGITUDE + " REAL," +
                    LocationEntry.COLUMN_NAME_SPEED + " REAL," +
                    LocationEntry.COLUMN_NAME_TIMESTAMP + " NUMERIC)";

    public static final String TABLE_NAME = "location";
    public static final String COLUMN_NAME_LATITUDE = "latitude";
    public static final String COLUMN_NAME_LONGITUDE = "longitude";
    public static final String COLUMN_NAME_SPEED = "speed";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
}
