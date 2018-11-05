package com.example.mauri.musclehealth;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.darken.myolib.Myo;
import eu.darken.myolib.MyoConnector;
import eu.darken.myolib.processor.emg.EmgData;
import eu.darken.myolib.processor.emg.EmgProcessor;

import java.util.Arrays;
import java.util.List;

public class MuscleHealthActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_health);
        Button button = findViewById(R.id.stupidButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView text = findViewById(R.id.helloTextView);
                text.setText(getString(R.string.test_text));
            }
        });
        MyoConnector connector = new MyoConnector(getApplicationContext());
        connector.scan(5000, new MyoConnector.ScannerCallback(){
            @Override
            public void onScanFinished(List<Myo> myos) {
                Myo myo = myos.get(0);
                myo.connect();
                EmgProcessor emgProcessor = new EmgProcessor();
                myo.addProcessor(emgProcessor);
                emgProcessor.addListener(new EmgProcessor.EmgDataListener() {
                    @Override
                    public void onNewEmgData(EmgData emgData) {
                        Log.i("EMG-Data", Arrays.toString(emgData.getData()));
                    }
                });
            }
        });
        Handler handler = new Handler();
        private Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                Log.d("Handlers", "Called on main thread");
                handler.postDelayed(this, 2000);
            }
        };
        handler.post(runnableCode);
    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        //outState.putString("theWord", theWord); // Saving the Variable theWord
        //outState.putStringArrayList("fiveDefns", fiveDefns); // Saving the ArrayList fiveDefns
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        //theWord = savedInstanceState.getString("theWord"); // Restoring theWord
        //fiveDefns = savedInstanceState.getStringArrayList("fiveDefns"); //Restoring fiveDefns
    }
}
