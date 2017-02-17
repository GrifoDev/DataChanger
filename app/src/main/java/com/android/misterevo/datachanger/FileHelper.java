package com.android.misterevo.datachanger;


import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * Created by Lars on 15.02.2017.
 */

public class FileHelper {

    final static String filename = "others.xml";
    final static String path = (Environment.getRootDirectory().getAbsolutePath() + "/csc/");
    final static String TAG = FileHelper.class.getName();
    public static File xmlText = new File(path, filename);

    public static String readFile () {
        String content;
        try {
            //Acquire root-access
            Process p;
            p = Runtime.getRuntime().exec("su");

            //FileInputStream loads the xmlText- File
            //InputStreamReader reads the bytes coming from the FIS
            //BufferedReader is converting to text?? -- Needs confirmation
            //StringBuilder puts the single lines to one whole String together

            FileInputStream fileInputStream = new FileInputStream(xmlText);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Checking if there is a line to read
            //Stringbuilder adds the line to the Stringbuilder and adds a new line

            while ( (content = bufferedReader.readLine()) != null) {
                stringBuilder.append(content + System.getProperty("line.separator"));
            }

            //Close all IO parts

            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();

            //Stores the whole new Stringbuilder content in a String
            content = stringBuilder.toString();

        } catch(FileNotFoundException ex) {
            content = "File not Found - " + ex.getMessage();
            Log.d(TAG, ex.getMessage());
            return content;
        } catch (IOException ex ) {
            content = "IO exception - " + ex.getMessage();
            Log.d(TAG, ex.getMessage());
            return content;
        }
        return content;
    }

    public static String investInput (String input) {

        if (!input.contains("<CscFeature_SystemUI_ConfigOverrideDataIcon>")) {
            //Add parameter xml code
            try {
                Process su, mount;

                su = Runtime.getRuntime().exec("su");
                //Trying to get RW Status for root folders
                mount = Runtime.getRuntime().exec("mount -o rw,remount /system");


                FileOutputStream fileOutputStream = new FileOutputStream(xmlText, true);        //Crashes atm --> Permission denied
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.append(("<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>" + System.getProperty("line.separator")));


                outputStreamWriter.close();
                fileOutputStream.flush();
                fileOutputStream.close();

                //fileOutputStream.write(("<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>" + System.getProperty("line.separator")).getBytes());
                //When my code is added, the icon will be 4G
                return "4G";
            }catch (FileNotFoundException ex) {
                Log.d(TAG, ex.getMessage());
                return ex.getMessage();
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
