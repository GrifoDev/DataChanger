package com.android.misterevo.datachanger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    //Getting interface
    Button b1;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.btnchangeData);
        t1 = (TextView) findViewById(R.id.editText);
        b1.setOnClickListener(myHandler1);

    }

    View.OnClickListener myHandler1 = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //Button Clicked

        }
    };

}
