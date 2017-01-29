package com.blueexample.bluetoothversioncontrol;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by mariapb on 29/01/17.
 */

public class AlreadyPairedList extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 0. Get the arrayList from the Intent. This was sent from MainActivity.java
        Intent intent = getIntent();
        ArrayList<BluetoothObject> arrayOfPairedDevices = intent.getParcelableArrayListExtra("arrayOfPairedDevices");

        // 1. Pass context and data to the custom adapter
        AlreadyPairedAdapter myAdapter = new AlreadyPairedAdapter(getApplicationContext(), arrayOfPairedDevices);

        // 2. setListAdapter
        setListAdapter(myAdapter);

    }
}
