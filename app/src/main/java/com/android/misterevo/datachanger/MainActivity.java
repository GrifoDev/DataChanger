package com.android.misterevo.datachanger;


import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    //Getting interface
    Button btnWrite, btnRead;
    TextView Output;
    private static final int PERM_READ_EXT = 111;
    private static final int PERM_WRITE_EXT = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect elements
        btnWrite = (Button) findViewById(R.id.btnSaveFile);
        btnRead = (Button) findViewById(R.id.btnReadFile);
        Output = (TextView) findViewById(R.id.tvCSC);


        //Acquire Permissions
        final int permissionsCheckRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        final int permissionsCheckWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //Read OnClickListener

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(permissionsCheckRead != PackageManager.PERMISSION_GRANTED) {
                    //No access
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERM_READ_EXT);

                } else {
                    //Permissions is ok
                    //Reading the content of the XMLFile
                    Output.setText(FileHelper.readFile());
                }
            }
        });


        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(permissionsCheckWrite != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERM_WRITE_EXT);

                } else {
                    String xmlResult = FileHelper.readFile();
                    String saved = FileHelper.investInput(xmlResult);

                    Toast.makeText(MainActivity.this, saved, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERM_READ_EXT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Output.setText(FileHelper.readFile());
                } else {
                    Toast.makeText(MainActivity.this, "No Read Perm granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case PERM_WRITE_EXT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String xmlResult = FileHelper.readFile();
                    String saved = FileHelper.investInput(xmlResult);

                    Toast.makeText(MainActivity.this, saved, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No Write Perm granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
