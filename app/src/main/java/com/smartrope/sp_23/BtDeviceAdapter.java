package com.smartrope.sp_23;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BtDeviceAdapter extends ArrayAdapter<BluetoothDevice> {
    LayoutInflater inflater;
    int resource;
    ArrayList<BluetoothDevice> listDevices;


    public BtDeviceAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BluetoothDevice> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater) context.getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        listDevices = objects;
    }

    @SuppressLint("MissingPermission")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_list_devices, null);
        TextView tvNameDevice = convertView.findViewById(R.id.tvNameDevice);
        TextView tvAddressDevice = convertView.findViewById(R.id.tvAddressDevice);
        tvNameDevice.setText(listDevices.get(position).getName());
        tvAddressDevice.setText(listDevices.get(position).getAddress());
        return convertView;
    }
}
