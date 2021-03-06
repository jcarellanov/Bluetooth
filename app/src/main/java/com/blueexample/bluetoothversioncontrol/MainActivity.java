package com.blueexample.bluetoothversioncontrol;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private String LOG_TAG;
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 99;
    private int x;
    public int MY_PERMISSIONS_ACCESS_FINE = 1;

    public static final String[] runtimePermissions = {
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH
            , Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_ACCESS_FINE);


        LOG_TAG = getResources().getString(R.string.app_name);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Button startBluetooth = (Button) findViewById(R.id.startBluetooth);
        Button alreadyPaired = (Button) findViewById(R.id.getArray);
        Button findBluetooth = (Button) findViewById(R.id.scanDevices);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableBluetoothOnDevice();
            }
        };

        View.OnClickListener pairedDevices = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getArrayOfAlreadyPairedBluetoothDevices() != null) {
                    Intent intent = new Intent(MainActivity.this, AlreadyPairedList.class);
                    intent.putParcelableArrayListExtra("arrayOfPairedDevices", getArrayOfAlreadyPairedBluetoothDevices());
                    startActivity(intent);
                }
            }
        };

        View.OnClickListener findDevices = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter != null)
                    scanForBluetoothDevices();
            }
        };

        startBluetooth.setOnClickListener(listener);
        alreadyPaired.setOnClickListener(pairedDevices);
        findBluetooth.setOnClickListener(findDevices);


    }

    private void enableBluetoothOnDevice() {
        if (mBluetoothAdapter == null) {
            Log.e(LOG_TAG, "This device does not have a bluetooth adapter");
            finish();
            // If the android device does not have bluetooth, just return and get out.
            // There's nothing the app can do in this case. Closing app.
        }

        // Check to see if bluetooth is enabled. Prompt to enable it
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == 0) {
                // If the resultCode is 0, the user selected "No" when prompt to
                // allow the app to enable bluetooth.
                // You may want to display a dialog explaining what would happen if
                // the user doesn't enable bluetooth.
                Toast.makeText(this, "The user decided to deny bluetooth access", Toast.LENGTH_LONG).show();
            } else
                Log.i(LOG_TAG, "User allowed bluetooth access!");
        }

    }

    private ArrayList getArrayOfAlreadyPairedBluetoothDevices() {
        ArrayList<BluetoothObject> arrayOfAlreadyPairedBTDevices = null;

        // Query paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are any paired devices
        if (pairedDevices.size() > 0) {
            arrayOfAlreadyPairedBTDevices = new ArrayList<>();

            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Create the device object and add it to the arrayList of devices
                BluetoothObject bluetoothObject = new BluetoothObject();
                bluetoothObject.setBluetooth_name(device.getName());
                bluetoothObject.setBluetooth_address(device.getAddress());
                bluetoothObject.setBluetooth_state(device.getBondState());
                bluetoothObject.setBluetooth_type(device.getType());    // requires API 18 or higher
                bluetoothObject.setBluetooth_uuids(device.getUuids());

                arrayOfAlreadyPairedBTDevices.add(bluetoothObject);
            }
        }

        return arrayOfAlreadyPairedBTDevices;
    }

    private void scanForBluetoothDevices() {
        // Start this on a new activity without passing any data to it
        Intent intent = new Intent(this, FoundBTDevices.class);
        startActivity(intent);
    }
}
