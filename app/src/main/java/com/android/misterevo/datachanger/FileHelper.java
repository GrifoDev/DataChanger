package com.android.misterevo.datachanger;



import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
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

    final static String TAG = FileHelper.class.getName();


    public static void copyFileToTemp(String cmd) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream outs = new DataOutputStream(p.getOutputStream());
            outs.writeBytes(cmd);
            outs.flush();
        }catch (IOException ex) {
           ex.printStackTrace();
        }
    }

    public static void copyFileToRoot(String cmd) {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream outs = new DataOutputStream(p.getOutputStream());
            //Mount System to Read-Write
            outs.writeBytes("mount -o rw,remount /system\n");
            //Copying the file from temp to root folder
            outs.writeBytes(cmd);
            //Mounting back to Read-Only
            outs.writeBytes("mount -o ro,remount /system\n");
            outs.flush();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static String readFile (File inputFile) {
        String content;
        try {
            //Acquire root-access
            Process p;
            p = Runtime.getRuntime().exec("su");

            //FileInputStream loads the xmlText- File
            //InputStreamReader reads the bytes coming from the FIS
            //BufferedReader is converting to text?? -- Needs confirmation
            //StringBuilder puts the single lines to one whole String together

            FileInputStream fileInputStream = new FileInputStream(inputFile);
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

    public static String investInput (String input, File tempFile) {

        if (!input.contains("<CscFeature_SystemUI_ConfigOverrideDataIcon>")) {
            //Add parameter xml code
            try {
                //Removing empty lines
                input = input.replaceAll("(?m)^[ \t]*\r?\n", "");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(input);


                //Place code right before the last two tags
                stringBuilder.insert(input.length()-39, "<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>\n");

                FileOutputStream fileOutputStream = new FileOutputStream(tempFile, false);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.append(stringBuilder.toString());


                outputStreamWriter.close();
                fileOutputStream.flush();
                fileOutputStream.close();

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
        return null;
    }

    public static boolean Toggled(String input) {
        if (input.contains("<CscFeature_SystemUI_ConfigOverrideDataIcon>LTE</CscFeature_SystemUI_ConfigOverrideDataIcon>")) {
            return true;
        } else if (input.contains("<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>")) {
            return false;
        }
        return false;
    }

    public static String saveFile (String input,File tempFile) {
        try {
            //Removing empty lines
            input = input.replaceAll("(?m)^[ \t]*\r?\n", "");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(input);


            FileOutputStream fileOutputStream = new FileOutputStream(tempFile, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.append(stringBuilder.toString());


            outputStreamWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();

            //When my code is added, the icon will be 4G
            return "4G";
        }catch (FileNotFoundException ex) {
            Log.d(TAG, ex.getMessage());
            return ex.getMessage();
        }catch (IOException ex) {
            Log.d(TAG, ex.getMessage());
            return ex.getMessage();
        }
    }
}
