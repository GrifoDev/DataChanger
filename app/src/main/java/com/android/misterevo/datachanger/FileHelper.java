package com.android.misterevo.datachanger;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SyncFailedException;

/**
 * Created by Lars on 15.02.2017.
 */

public class FileHelper {
    final static String filename = "file.txt";
    final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dc/";
    final static String TAG = FileHelper.class.getName();

    public static String ReadFile ( Context context) {
        String line = null;

        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            FileInputStream fileInputStream = new FileInputStream(new File(dir, filename));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();
            bufferedReader.close();
        } catch(FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        } catch (IOException ex ) {
            Log.d(TAG, ex.getMessage());
        }
        return line;
    }

    public static boolean saveToFile (String data) {
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(dir, filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());
            return true;
        }catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
        }catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
        }
        return false;
    }
}
