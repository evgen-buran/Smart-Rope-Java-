package com.smartrope.sp_23;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothManager {
    static public final int REQUEST_CODE_ENABLE = 10;
    static final int LENGTH_MSG = 5;
    static final byte END_MSG = 10;

    static private BluetoothAdapter adapter;
    static private Context context;
    static private ArrayList<BluetoothDevice> devicesArray = new ArrayList<>();
    static UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
    static InputStream inputStream;
    static Thread thread;


    public static ArrayList<BluetoothDevice> getDevicesArray() {
        return devicesArray;
    }

    static void initAdapter() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            showToast(context.getString(R.string.adapter_not_supported));
        }
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

    public static void connectDevice(BluetoothDevice device) {
        if (device != null) {
            try {
                @SuppressLint("MissingPermission")
                BluetoothSocket socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
                inputStream = socket.getInputStream();
                getData();

            } catch (IOException e) {
                e.printStackTrace();}

        }
    }

    private static void getData() {
        Handler handler = new Handler();
        byte[] buffer = new byte[LENGTH_MSG];
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!thread.isInterrupted()) {
                    try {
                        if (inputStream != null) {
                            if (inputStream.available() >= 0) {
                                byte[] bytes = new byte[inputStream.available()];
                                inputStream.read(bytes);
                                for (int i = 0; i < bytes.length; i++) {
                                    if (bytes[i] == END_MSG) {
                                        String msg = new String(buffer);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                showToast("Jump");
                                            }
                                        });
                                    } else buffer[i] = bytes[i];
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

