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


/**
 * Created by Lars on 15.02.2017.
 */

public class FileHelper {
    final static String filename = "others.xml";
    final static String path = (Environment.getRootDirectory().getAbsolutePath() + "/csc/");
    final static String TAG = FileHelper.class.getName();

    public static String ReadFile ( Context context) {
        String line = null;

        try {
            Process p;
            p = Runtime.getRuntime().exec("su");

            FileInputStream fileInputStream = new FileInputStream(new File(path, filename));
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

    public static String investInput (String input) {

        if (!input.contains("<CscFeature_SystemUI_ConfigOverrideDataIcon>")) {
            //Add parameter
            try {
                Process su, mount;
                File file = new File(path, filename);

                su = Runtime.getRuntime().exec("su");
                mount = Runtime.getRuntime().exec("mount -o rw,remount /system");
                FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                fileOutputStream.write(("<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>" + System.getProperty("line.separator")).getBytes());

                return "4G";
            }catch (FileNotFoundException ex) {
                Log.d(TAG, ex.getMessage());
            }catch (IOException ex) {
                Log.d(TAG, ex.getMessage());
            }

        } else {
            //Read its value
            if(input.contains("<CscFeature_SystemUI_ConfigOverrideDataIcon>LTE</CscFeature_SystemUI_ConfigOverrideDataIcon>")) {
                return "LTE";
            }
            else if (input.contains("<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>")) {
                return "4G";
            }


        }
        return "Nothing";
    }
}
