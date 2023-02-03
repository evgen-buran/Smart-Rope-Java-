package com.smartrope.sp_23;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

public class BluetoothManager {
    static public final int REQUEST_CODE_ENABLE = 10;

    static private BluetoothAdapter adapter;
    static private Context context;

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
    public static Intent enableAdapter() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        return intent;
    }

    @SuppressLint("MissingPermission")
    public static void disableAdapter() {
        adapter.disable();
    }

    static void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }


    static void setContext(Context _context) {
        context = _context;
    }
}
