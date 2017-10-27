package com.erasmus.barend.locationtracker.repositories;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.erasmus.barend.locationtracker.repositories.models.LocationEntry;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public class BaseRepository extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "location-tracker.db";


    public BaseRepository(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LocationEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LocationEntry.DROP_TABLE);
        db.execSQL(LocationEntry.CREATE_TABLE);
    }
}
