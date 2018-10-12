package com.example.mauri.musclehealth;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MuscleHealthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_health);
        Button button = (Button) findViewById(R.id.stupidButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
            }
        });
    }
}
