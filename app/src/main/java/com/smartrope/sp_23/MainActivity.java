package com.smartrope.sp_23;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    MenuItem onAdapterItem;
    BluetoothReceiver receiver;
    ListView listDevices;
    BtDeviceAdapter adapter;
    AlertDialog.Builder dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothManager.setContext(this);
        BluetoothManager.initAdapter();
        adapter = new BtDeviceAdapter(this, R.layout.item_list_devices, BluetoothManager.getDevicesArray());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bluetooth_menu, menu);
        onAdapterItem = menu.findItem(R.id.onOffBtooth);
        if (BluetoothManager.isEnableAdapter()) {
            onAdapterItem.setIcon(R.drawable.ic_baseline_bluetooth_24).setTitle(R.string.turnOffAdapter);
        } else {
            onAdapterItem.setIcon(R.drawable.ic_baseline_bluetooth_disabled_24).setTitle(R.string.turnOnAdapter);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.onOffBtooth:
                if (!BluetoothManager.isEnableAdapter()) {
                    //noinspection deprecation
                    startActivityForResult
                            (BluetoothManager.enableAdapter(), BluetoothManager.REQUEST_CODE_ENABLE);

                } else {
                    BluetoothManager.disableAdapter();
                    onAdapterItem.setTitle(R.string.turnOnAdapter);
                    onAdapterItem.setIcon(R.drawable.ic_baseline_bluetooth_disabled_24);
                }
                break;
            case R.id.searchDevice:
                if (!BluetoothManager.isDiscovery()) {
                    BluetoothManager.startDiscovery();
                } else BluetoothManager.cancelDiscovery();

                receiver = new BluetoothReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                filter.addAction(BluetoothDevice.ACTION_FOUND);

                registerReceiver(receiver, filter);
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (BluetoothManager.REQUEST_CODE_ENABLE):
                if (resultCode == RESULT_OK) {
                    onAdapterItem.setTitle(R.string.turnOffAdapter);
                    onAdapterItem.setIcon(R.drawable.ic_baseline_bluetooth_24);
                }
                break;
        }
    }

    //TODO реализовать метод показа списка найденных устройств
     void showListDevices() {
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Found devices");
        View view = getLayoutInflater().inflate(R.layout.list_devices, null);
        listDevices = view.findViewById(R.id.listDevices);
        listDevices.setAdapter(adapter);
        listDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BluetoothDevice device = BluetoothManager.getDevicesArray().get(i);
                BluetoothManager.connectDevice(device);
            }
        });
    }

    public class BluetoothReceiver extends BroadcastReceiver {
        String action;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            android.app.AlertDialog dialog = new ProgressDialog(context);
            switch (action) {
                case (BluetoothAdapter.ACTION_DISCOVERY_STARTED):
                    dialog = ProgressDialog.show(context, "Start search", "Please wait");
                    break;
                case (BluetoothAdapter.ACTION_DISCOVERY_FINISHED):
                    dialog.dismiss();
                    showListDevices();
                    break;
                case (BluetoothDevice.ACTION_FOUND):
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null) {
                        BluetoothManager.getDevicesArray().add(device);
                    }
                    break;
            }

        }
    }

}