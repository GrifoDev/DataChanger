package com.android.misterevo.datachanger;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    //Getting interface
    private TextView Output;
    private Switch swtData, swtRCS;
    private static final int PERM_READ_EXT = 111;
    private static final int PERM_WRITE_EXT = 222;
    private final String TEMP_FILE_NAME = ".other_temp.xml";
    private final String cmdRT = "cat /system/csc/others.xml > " + Environment.getExternalStorageDirectory() + "/.tmp/" + TEMP_FILE_NAME + "\n";
    private final String cmdTR = "cat " +Environment.getExternalStorageDirectory() + "/.tmp/" + TEMP_FILE_NAME + " > /system/csc/others.xml\n";
    private String result;
    private final File tempFile = new File(Environment.getExternalStorageDirectory() + "/.tmp/" + TEMP_FILE_NAME);
    File direct = new File(Environment.getExternalStorageDirectory() + "/.tmp");
    Process p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect elements
        Output = (TextView) findViewById(R.id.tvCSC);
        swtData = (Switch) findViewById(R.id.swtdata);
        swtRCS = (Switch) findViewById(R.id.swtrcs);

        //Acquire Permissions
        final int permissionsCheckRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        final int permissionsCheckWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        try {
            p = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Checking ReadStoragePermission
        if (permissionsCheckRead != PackageManager.PERMISSION_GRANTED) {
            //No access --> OpenRequestDialog
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERM_READ_EXT);

        } else {
            //Permissions is ok


            if(!direct.exists())
            {
               direct.mkdir(); //directory is created;

            }
            //Read switch state
            SharedPreferences shardPrefs = getSharedPreferences("com.misterevo.datachanger", MODE_PRIVATE);
            swtData.setChecked(shardPrefs.getBoolean("DataIcon", true));
            swtRCS.setChecked(shardPrefs.getBoolean("RCS", false));




            FileHelper.copyFileToTemp(cmdRT, p);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    result = FileHelper.readFile(tempFile);
                    FileHelper.investInput(result, tempFile);
                    Output.setText(result);
                    FileHelper.copyFileToRoot(cmdTR, p);
                    //Check if LTE is toggled

                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 2000);
        }

        swtData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (permissionsCheckWrite != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_WRITE_EXT);
                } else {
                    if (isChecked) {
                        result = FileHelper.readFile(tempFile);
                        result = result.replace("<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>", "<CscFeature_SystemUI_ConfigOverrideDataIcon>LTE</CscFeature_SystemUI_ConfigOverrideDataIcon>");
                        FileHelper.saveFile(result, tempFile);
                        FileHelper.copyFileToRoot(cmdTR, p);
                        Toast.makeText(MainActivity.this, "Changed to LTE", Toast.LENGTH_SHORT).show();
                        Output.setText(result);
                        SharedPreferences.Editor editor = getSharedPreferences("com.misterevo.datachanger", MODE_PRIVATE).edit();
                        editor.putBoolean("DataIcon", true);
                        editor.commit();


//                        new AlertDialog.Builder(MainActivity.this)
//                                .setTitle("Reboot phone")
//                                .setMessage("You need to reboot your device to apply changes.\n Do you want to reboot?")
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        try {
//                                            p = Runtime.getRuntime().exec("reboot");
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                })
//                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .show();


                    } else {
                        result = FileHelper.readFile(tempFile);
                        result = result.replace("<CscFeature_SystemUI_ConfigOverrideDataIcon>LTE</CscFeature_SystemUI_ConfigOverrideDataIcon>", "<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>");
                        FileHelper.saveFile(result, tempFile);
                        FileHelper.copyFileToRoot(cmdTR, p);
                        Toast.makeText(MainActivity.this, "Changed to 4G", Toast.LENGTH_SHORT).show();
                        Output.setText(result);
                        SharedPreferences.Editor editor = getSharedPreferences("com.misterevo.datachanger", MODE_PRIVATE).edit();
                        editor.putBoolean("DataIcon", false);
                        editor.commit();

//                        new AlertDialog.Builder(MainActivity.this)
//                                .setTitle("Reboot phone")
//                                .setMessage("You need to reboot your device to apply changes.\n Do you want to reboot?")
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        try {
//                                            p = Runtime.getRuntime().exec("reboot");
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                })
//                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                })
//                                .show();

                    }
                }
            }
        });

        swtRCS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                   //Bak to APK

                    try {
                        DataOutputStream outs = new DataOutputStream(p.getOutputStream());
                        outs.writeBytes("mount -o rw,remount /system\n");
                        outs.writeBytes("mv /system/priv-app/imsservice/imsservice.apk /system/priv-app/imsservice/imsservice.tmp\n");
                        outs.writeBytes("mv /system/priv-app/imsservice/imsservice.bak /system/priv-app/imsservice/imsservice.apk\n");
                        outs.writeBytes("mv /system/priv-app/imsservice/imsservice.tmp /system/priv-app/imsservice/imsservice.bak\n");
                        outs.writeBytes("mount -o ro,remount /system\n");
                        outs.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    SharedPreferences.Editor editor = getSharedPreferences("com.misterevo.datachanger", MODE_PRIVATE).edit();
                    editor.putBoolean("RCS", true);
                    editor.commit();

                } else {
                    try {
                        DataOutputStream outs = new DataOutputStream(p.getOutputStream());
                        outs.writeBytes("mount -o rw,remount /system\n");
                        outs.writeBytes("mv /system/priv-app/imsservice/imsservice.apk /system/priv-app/imsservice/imsservice.tmp\n");
                        outs.writeBytes("mv /system/priv-app/imsservice/imsservice.bak /system/priv-app/imsservice/imsservice.apk\n");
                        outs.writeBytes("mv /system/priv-app/imsservice/imsservice.tmp /system/priv-app/imsservice/imsservice.bak\n");
                        outs.writeBytes("mount -o ro,remount /system\n");
                        outs.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    SharedPreferences.Editor editor = getSharedPreferences("com.misterevo.datachanger", MODE_PRIVATE).edit();
                    editor.putBoolean("RCS", false);
                    editor.commit();

                }

            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERM_READ_EXT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(!direct.exists())
                    {
                        direct.mkdir(); //directory is created;

                    }
                    FileHelper.copyFileToTemp(cmdRT, p);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Output.setText(FileHelper.readFile(tempFile));

                        }
                    };
                    Handler h = new Handler();
                    h.postDelayed(r, 3000);

                } else {
                    Toast.makeText(MainActivity.this, "No Read Perm granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case PERM_WRITE_EXT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    swtData.setChecked(swtData.isChecked());
                } else {
                    Toast.makeText(MainActivity.this, "No Write Perm granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
