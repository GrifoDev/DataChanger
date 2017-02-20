package com.android.misterevo.datachanger;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;



public class MainActivity extends AppCompatActivity {

    //Getting interface
    Button btnWrite;
    TextView Output;
    Switch swtData;
    private static final int PERM_READ_EXT = 111;
    private static final int PERM_WRITE_EXT = 222;
    private final String TEMP_FILE_NAME = "other_temp.xml";
    String cmd;
    String Result;
    File tempFile = new File("/sdcard/download/" + TEMP_FILE_NAME);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect elements
        btnWrite = (Button) findViewById(R.id.btnSaveFile);
        Output = (TextView) findViewById(R.id.tvCSC);
        swtData = (Switch) findViewById(R.id.swtdata);

        //Acquire Permissions
        final int permissionsCheckRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        final int permissionsCheckWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);


        //Loading the original xml into temp
        cmd = "cat /system/csc/others.xml > /sdcard/download/" + TEMP_FILE_NAME + "\n";

        //Checking ReadStoragePermission
        if(permissionsCheckRead != PackageManager.PERMISSION_GRANTED) {
            //No access --> OpenRequestDialog
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERM_READ_EXT);

        } else {
            //Permissions is ok

            //Copying File from Root to temp
            FileHelper.copyFileToTemp(cmd);
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Output.setText(FileHelper.readFile(tempFile));
                    //Check if LTE is toggled
                    if (FileHelper.Toggled(Output.getText().toString())) {
                        swtData.setChecked(true);

                    }else {
                        swtData.setChecked(false);
                    }
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 2000);
        }

        swtData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Result = FileHelper.readFile(tempFile);
                    Result = Result.replace("<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>", "<CscFeature_SystemUI_ConfigOverrideDataIcon>LTE</CscFeature_SystemUI_ConfigOverrideDataIcon>");
                    FileHelper.saveFile(Result, tempFile);
                    cmd = "cat /sdcard/download/" + TEMP_FILE_NAME+ " > /system/csc/others.xml\n";
                    FileHelper.copyFileToRoot(cmd);
                }else {
                    Result = FileHelper.readFile(tempFile);
                    Result = Result.replace("<CscFeature_SystemUI_ConfigOverrideDataIcon>LTE</CscFeature_SystemUI_ConfigOverrideDataIcon>", "<CscFeature_SystemUI_ConfigOverrideDataIcon>4G</CscFeature_SystemUI_ConfigOverrideDataIcon>");
                    FileHelper.saveFile(Result, tempFile);
                    cmd = "cat /sdcard/download/" + TEMP_FILE_NAME+ " > /system/csc/others.xml\n";
                    FileHelper.copyFileToRoot(cmd);
                }
            }
        });




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERM_READ_EXT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileHelper.copyFileToTemp(cmd);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Output.setText(FileHelper.readFile(tempFile));
                            //Check if LTE is toggled
                            if (FileHelper.Toggled(Output.getText().toString())) {
                                swtData.setChecked(true);

                            }else {
                                swtData.setChecked(false);
                            }
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
                   // String xmlResult = FileHelper.readFile();
                   // String saved = FileHelper.investInput(xmlResult);

                  //  Toast.makeText(MainActivity.this, saved, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No Write Perm granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
