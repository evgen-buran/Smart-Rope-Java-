package com.smartrope.sp_23;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//todo нижнее меню с переходами. рабочий первый экран: счетчик, время, калории, РПМ (класс тренировка)
public class MainActivity extends AppCompatActivity {
    final String TAG = "myLog";
    MenuItem onAdapterItem;
    BluetoothReceiver receiver;
    ListView listDevices;
    BtDeviceAdapter adapterSearchedDevices, adapterBoundDevices;
    AlertDialog.Builder dialog;
    ProgressDialog progressDialog;

    NavController controller;
    NavHostFragment hostFragment;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothManager.setContext(this);
        BluetoothManager.initAdapter();
        progressDialog = new ProgressDialog(this);
        adapterSearchedDevices = new BtDeviceAdapter(this, R.layout.item_list_devices,
                BluetoothManager.getDevicesArray());
        adapterBoundDevices = new BtDeviceAdapter(this, R.layout.item_list_devices,
                BluetoothManager.getDevicesBoundArray());

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        AppBarConfiguration barConfiguration = new AppBarConfiguration.Builder(
                R.id.justJumpNavigation,
                R.id.doubleJumpNavigation,
                R.id.hiitNavigation,
                R.id.paramsNavigation,
                R.id.statisticNavigation).build();
        fragmentManager = getSupportFragmentManager();
        hostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.fragmentContainerView);
        controller = hostFragment.getNavController();
        bottomNavigationView = findViewById(R.id.nav_bottom);
        NavigationUI.setupWithNavController(bottomNavigationView, controller);


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
            case R.id.pairedDevice:
                BluetoothManager.prepareBoundDevices();
                showListBoundedDevices();
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
        View view = setShowList(adapterSearchedDevices);
        setAlertDialog(view);
    }

    void showListBoundedDevices() {
        View view = setShowList(adapterBoundDevices);
        setAlertDialog(view);

    }

    @NonNull
    private View setShowList(BtDeviceAdapter adapter) {
        View view = getLayoutInflater().inflate(R.layout.list_devices, null);
        listDevices = view.findViewById(R.id.listDevices);
        listDevices.setAdapter(adapter);
        listDevices.setOnItemClickListener(onClickerItemListDevices);
        return view;
    }

    private void setAlertDialog(View view) {
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Found devices");
        dialog.setView(view);
        dialog.setNegativeButton("OK", null);
        dialog.create();
        dialog.show();
    }


    private AdapterView.OnItemClickListener onClickerItemListDevices = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BluetoothDevice device = BluetoothManager.getDevicesArray().get(i);
            BluetoothManager.connectDevice(device);
        }
    };

    public class BluetoothReceiver extends BroadcastReceiver {
        String action;

        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            android.app.AlertDialog dialog = new ProgressDialog(context);
            switch (action) {
                case (BluetoothAdapter.ACTION_DISCOVERY_STARTED):
                    Log.d(TAG, "onReceive: ACTION_DISCOVERY_STARTED");
                    progressDialog = ProgressDialog.show(context, "Start search", "Please wait");
                    break;
                case (BluetoothAdapter.ACTION_DISCOVERY_FINISHED):
                    Log.d(TAG, "onReceive: ACTION_DISCOVERY_FINISHED");
                    progressDialog.dismiss();
                    showListDevices();
                    break;
                case (BluetoothDevice.ACTION_FOUND):
                    Log.d(TAG, "onReceive: ACTION_FOUND");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null) {
                        BluetoothManager.getDevicesArray().add(device);
                        Log.d(TAG, "onReceive: device added");
                    }
                    break;
            }

        }
    }

}