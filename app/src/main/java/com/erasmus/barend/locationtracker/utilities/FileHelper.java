package com.erasmus.barend.locationtracker.utilities;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Barend Erasmus on 10/22/2017.
 */

public class FileHelper {

    public static void Copy(File src, File dst) {
        try (InputStream inputStream = new FileInputStream(src)) {
            try (OutputStream outputStream = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String GetExternalStoragePath(String path) {
        return JoinPath(GetExternalStoragePath(), path);
    }

    public static String GetExternalStoragePath() {
        return Environment.getExternalStorageDirectory().toString();
    }

    public static String JoinPath(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }
}
