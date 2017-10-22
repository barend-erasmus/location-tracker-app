package com.erasmus.barend.locationtracker.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.erasmus.barend.locationtracker.repositories.models.LocationEntry;

import java.util.Date;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public class LocationRepository extends BaseRepository {

    public LocationRepository(Context context) {
        super(context);
    }

    public void Insert(double longitude, double latitude, Date timestamp) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_NAME_LONGITUDE, longitude);
        values.put(LocationEntry.COLUMN_NAME_LATITUDE, latitude);
        values.put(LocationEntry.COLUMN_NAME_TIMESTAMP, timestamp.getTime());

        long rowId = database.insert(LocationEntry.TABLE_NAME, null, values);
    }
}
