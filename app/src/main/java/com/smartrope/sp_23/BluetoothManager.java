package com.smartrope.sp_23;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {
    static final String TAG = "myLog";
    static public final int REQUEST_CODE_ENABLE = 10;
    static final int LENGTH_MSG = 5;
    static final byte END_MSG = 10;

    static private BluetoothAdapter adapter;
    static private Context context;
    static private ArrayList<BluetoothDevice> devicesArray = new ArrayList<>();
    static private ArrayList<BluetoothDevice> devicesBoundArray = new ArrayList<>();

    static UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
    static InputStream inputStream;
    static Thread thread;

    public static ArrayList<BluetoothDevice> getDevicesArray() {
        return devicesArray;
    }

    public static ArrayList<BluetoothDevice> getDevicesBoundArray() {
        return devicesBoundArray;
    }

    static void initAdapter() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            showToast(context.getString(R.string.adapter_not_supported));
        }
    }

    @SuppressLint("MissingPermission")
    static ArrayList<BluetoothDevice> prepareBoundDevices() {
        Set<BluetoothDevice> set = adapter.getBondedDevices();
        for (BluetoothDevice device : set) {
            if (device != null) {
                devicesBoundArray.add(device);
            }
        }
        Log.d(TAG, "prepareBoundDevices: " + devicesBoundArray.size());
        return devicesBoundArray;
    }

    static boolean isEnableAdapter() {
        if (adapter.isEnabled()) return true;
        else return false;
    }

    @SuppressLint("MissingPermission")
    static boolean isDiscovery() {
        if (adapter.isDiscovering()) return true;
        else return false;
    }

    @SuppressLint("MissingPermission")
    public static Intent enableAdapter() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        return intent;
    }

    @SuppressLint("MissingPermission")
    public static void disableAdapter() {
        adapter.disable();
        showToast("Bluetooth is disabled");
    }

    @SuppressLint("MissingPermission")
    public static void startDiscovery() {
        adapter.startDiscovery();
        showToast("Start search devices");
    }

    @SuppressLint("MissingPermission")
    public static void cancelDiscovery() {
        adapter.cancelDiscovery();
        showToast("Cancel search devices");
    }

    static void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    static void setContext(Context _context) {
        context = _context;
    }

    @SuppressLint("MissingPermission")
    public static void connectDevice(BluetoothDevice device) {
        if (device != null) {
            try {
                @SuppressLint("MissingPermission")
                BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                inputStream = socket.getInputStream();
                socket.connect();

//                getData();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

  static void getData(Fragment fragment) {
        Handler handler = new Handler();
//        byte[] buffer = new byte[LENGTH_MSG];
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!thread.isInterrupted()) {
                    try {
                        if (inputStream != null) {
                            int data = inputStream.available();
                            if (data >= 0) {
                                byte[] bytes = new byte[inputStream.available()];
                                inputStream.read(bytes);
                                for (int i = 0; i < bytes.length; i++) {
                                    if (bytes[i] == END_MSG) {

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                              //   вывести в главный поток

                                            }
                                        });
                                    }// else buffer[i] = bytes[i];
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();
    }

}

