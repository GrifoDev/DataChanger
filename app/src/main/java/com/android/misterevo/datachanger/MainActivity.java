package com.android.misterevo.datachanger;

import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //Getting interface
    Button btnWrite, btnRead;
    TextView Output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Connect elements
        btnWrite = (Button) findViewById(R.id.btnSaveFile);
        btnRead = (Button) findViewById(R.id.btnReadFile);
        Output = (TextView) findViewById(R.id.tvCSC);

        //Read OnClickListener

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reading the content of the XMLFile
                Output.setText(FileHelper.readFile());
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xmlResult = FileHelper.readFile();
                String saved = FileHelper.investInput(xmlResult);

                Toast.makeText(MainActivity.this, saved, Toast.LENGTH_SHORT).show();


            }
        });

    }


}
