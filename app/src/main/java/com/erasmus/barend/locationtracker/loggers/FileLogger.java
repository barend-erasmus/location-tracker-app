package com.erasmus.barend.locationtracker.loggers;

import android.os.Environment;

import com.erasmus.barend.locationtracker.utilities.FileHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public class FileLogger {

    private String _name;

    public FileLogger(String name) {
        _name = name;
    }

    public static FileLogger GetLogger(String name) {
        return new FileLogger(name);
    }

    public void Info(String message) {

        try {
            File sdCardPath = Environment.getExternalStorageDirectory();

            FileOutputStream fileOutputStream = new FileOutputStream(FileHelper.GetExternalStoragePath(_name + ".log"), true);

            fileOutputStream.write(message.getBytes());
            fileOutputStream.write(System.getProperty("line.separator").getBytes());

            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}