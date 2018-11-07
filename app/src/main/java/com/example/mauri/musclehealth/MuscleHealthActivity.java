package com.example.mauri.musclehealth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import eu.darken.myolib.Myo;
import eu.darken.myolib.processor.emg.EmgData;
import eu.darken.myolib.MyoCmds;
import eu.darken.myolib.processor.emg.EmgProcessor;

public class MuscleHealthActivity extends Activity implements EmgProcessor.EmgDataListener, View.OnClickListener{

    private ArrayList<Long> dataTimes;
    private ArrayList<String> dataPoints;

    private Myo mMyo;
    private boolean mMyoConnected = false;
    private EmgProcessor emgProcessor = new EmgProcessor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscle_health);

        Button button = findViewById(R.id.stupidButton);
        button.setOnClickListener(this);

    }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putSerializable("dataTimes", dataTimes.toArray(new Long[dataTimes.size()]));
        outState.putSerializable("dataPoints", dataPoints.toArray(new String[dataPoints.size()]));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        dataTimes = new ArrayList<>(Arrays.asList((Long[])savedInstanceState.getSerializable("dataTimes")));
        dataPoints = new ArrayList<>(Arrays.asList((String[])savedInstanceState.getSerializable("dataPoints")));
    }

    private long mLastEmgUpdate = 0;

    @Override
    public void onNewEmgData(final EmgData emgData) {
        if (System.currentTimeMillis() - mLastEmgUpdate > 500) {
            Log.d("MLog: DATA", emgData.toString());
            mLastEmgUpdate = System.currentTimeMillis();
        }
    }

    @Override
    public void onClick(View v) {
        TextView text = findViewById(R.id.helloTextView);
        if (mMyoConnected){
            text.setText(getString(R.string.disconnected_text));
            mMyo.writeMode(MyoCmds.EmgMode.NONE, MyoCmds.ImuMode.ALL, MyoCmds.ClassifierMode.DISABLED, null);
            emgProcessor.removeDataListener(this);
            mMyo.removeProcessor(emgProcessor);
            mMyo.writeSleepMode(MyoCmds.SleepMode.NORMAL, null);
            mMyo.disconnect();
            mMyo = null;
            mMyoConnected = false;
        } else {
            text.setText(getString(R.string.connected_text));
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() == 1) {
                for (BluetoothDevice device : pairedDevices) {
                    Log.d("MLog: Connected", device.getName());
                    mMyo = new Myo(getApplicationContext(), device);
                    mMyo.connect();
                    mMyo.writeMode(MyoCmds.EmgMode.RAW, MyoCmds.ImuMode.NONE, MyoCmds.ClassifierMode.DISABLED, null);
                    mMyo.writeSleepMode(MyoCmds.SleepMode.NEVER, null);
                    mMyo.addProcessor(emgProcessor);
                    emgProcessor.addListener(this);
                }
            }
            mMyoConnected = true;
        }

    }

    public Myo getMyo() {
        return mMyo;
    }

    public void setMyo(Myo myo) {
        mMyo = myo;
    }
}
