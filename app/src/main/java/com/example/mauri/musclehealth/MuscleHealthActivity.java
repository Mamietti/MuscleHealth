package com.example.mauri.musclehealth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thalmic.myo.Hub;

import static android.content.ContentValues.TAG;


public class MuscleHealthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_health);
        Button button = (Button) findViewById(R.id.stupidButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView text = (TextView) findViewById(R.id.helloTextView);
                text.setText(getString(R.string.test_text));
            }
        });
        Hub hub = Hub.getInstance();
        if (!hub.init(this)) {
            Log.e(TAG, "Could not initialize the Hub.");
            finish();
            return;
        }
    }
}
