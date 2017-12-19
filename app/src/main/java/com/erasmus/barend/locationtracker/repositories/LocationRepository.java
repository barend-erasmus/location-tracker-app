package com.erasmus.barend.locationtracker.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.erasmus.barend.locationtracker.models.Location;
import com.erasmus.barend.locationtracker.repositories.models.LocationEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public class LocationRepository extends BaseRepository {

    private SQLiteDatabase _writableDatabase;
    private SQLiteDatabase _readableDatabase;

    public LocationRepository(Context context) {

        super(context);

        _writableDatabase = getWritableDatabase();
        _readableDatabase = getReadableDatabase();
    }


    public void Insert(float accuracy, double altitude, double bearing, float speed, double longitude, double latitude, Date timestamp) {
        if (_writableDatabase == null) {
            _writableDatabase = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_NAME_ACCURACY, accuracy);
        values.put(LocationEntry.COLUMN_NAME_ALTITUDE, altitude);
        values.put(LocationEntry.COLUMN_NAME_BEARING, bearing);
        values.put(LocationEntry.COLUMN_NAME_SPEED, speed);
        values.put(LocationEntry.COLUMN_NAME_LONGITUDE, longitude);
        values.put(LocationEntry.COLUMN_NAME_LATITUDE, latitude);
        values.put(LocationEntry.COLUMN_NAME_UPLOADED, false);
        values.put(LocationEntry.COLUMN_NAME_TIMESTAMP, timestamp.getTime());

        long rowId = _writableDatabase.insert(LocationEntry.TABLE_NAME, null, values);
    }

    public void MarkAsUploaded(long timestamp) {

        if (_writableDatabase == null) {
            _writableDatabase = getWritableDatabase();
        }

        ContentValues values = new ContentValues();
        values.put(LocationEntry.COLUMN_NAME_UPLOADED, true);

        String selection = LocationEntry.COLUMN_NAME_TIMESTAMP + " = ?";
        String[] selectionArgs = { Long.toString(timestamp) };

        int count = _writableDatabase.update(
                LocationEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }

    public List<Location> List() {

        if (_readableDatabase == null) {
            _readableDatabase = getReadableDatabase();
        }

        String selection = LocationEntry.COLUMN_NAME_UPLOADED + " = ?";
        String[] selectionArgs = { "0" };

        Cursor cursor = _readableDatabase.query(LocationEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        List<Location> result = new ArrayList<Location>();

        if (cursor.moveToFirst()) {
            do {
                Location location = new Location(
                        cursor.getFloat(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_ACCURACY)),
                        cursor.getDouble(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_ALTITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_BEARING)),
                        cursor.getFloat(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_SPEED)),
                        cursor.getDouble(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LATITUDE)),
                        cursor.getDouble(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_LONGITUDE)),
                        cursor.getLong(cursor.getColumnIndex(LocationEntry.COLUMN_NAME_TIMESTAMP))
                );

                result.add(location);
            } while (cursor.moveToNext());
        }

        return result;
    }

    public int MaxSpeed() {

        if (_readableDatabase == null) {
            _readableDatabase = getReadableDatabase();
        }

        Cursor cursor = _readableDatabase.query(LocationEntry.TABLE_NAME, new String[] { "MAX(" + LocationEntry.COLUMN_NAME_SPEED + ")" }, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public int AverageSpeed() {

        if (_readableDatabase == null) {
            _readableDatabase = getReadableDatabase();
        }

        Cursor cursor = _readableDatabase.query(LocationEntry.TABLE_NAME, new String[] { "AVG(" + LocationEntry.COLUMN_NAME_SPEED + ")" }, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public int NumberOfEntries() {

        if (_readableDatabase == null) {
            _readableDatabase = getReadableDatabase();
        }

        Cursor cursor = _readableDatabase.query(LocationEntry.TABLE_NAME, new String[] { "COUNT(*)" }, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(0);
        }

        return -1;
    }

    public void Close() {
        if (_writableDatabase != null) {
            _writableDatabase.close();
            _writableDatabase = null;
        }

        if (_readableDatabase != null) {
            _readableDatabase.close();
            _readableDatabase = null;
        }
    }
}
