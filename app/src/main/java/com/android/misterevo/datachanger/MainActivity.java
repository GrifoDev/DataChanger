package com.android.misterevo.datachanger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //Getting interface
    Button btnWrite, btnRead;
    TextView Output;
    EditText Input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWrite = (Button) findViewById(R.id.btnchangeData);
        btnRead = (Button) findViewById(R.id.readFile);
        Output = (TextView) findViewById(R.id.textViewOutout);
        Input = (EditText) findViewById(R.id.editTextInput) ;
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Output.setText(FileHelper.ReadFile(MainActivity.this));
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FileHelper.saveToFile(Input.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Saved to file", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"Error save file!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
