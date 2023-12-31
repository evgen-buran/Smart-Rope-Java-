package com.smartrope.sp_23;
//todo нельзя переключаться на другие вкладки если не закончена тренировка (isStarting): спрашивать об окончании тренировки
// или вставить кнопку завершения тренировки

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    final String TAG = "myLog";

    MenuItem onAdapterItem;
    BluetoothReceiver receiver;
    ListView listDevices;
    BtDeviceAdapter adapterSearchedDevices, adapterBoundDevices;
    AlertDialog.Builder dialog;
    ProgressDialog progressDialog;

    JustJumpFragment justJumpFragment;
    DoubleJumpFragment doubleJumpFragment;
    HIITFragment hiitFragment;

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    int currentItem;


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
        BluetoothManager.prepareBoundDevices();

        justJumpFragment = new JustJumpFragment();
        doubleJumpFragment = new DoubleJumpFragment();
        hiitFragment = new HIITFragment();

        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.nav_bottom);
        replaceFragment(justJumpFragment, fragmentManager, "1");
        bottomNavigationView.setOnItemSelectedListener(onBottomClicker);
        currentItem = R.id.justJumpNavigation;
    }

    NavigationBarView.OnItemSelectedListener onBottomClicker = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.justJumpNavigation:
                    replaceFragment(justJumpFragment, fragmentManager, "1");
                    currentItem = R.id.justJumpNavigation;
                    break;
                case R.id.doubleJumpNavigation:
                    replaceFragment(doubleJumpFragment, fragmentManager, "2");
                    currentItem = R.id.doubleJumpNavigation;
                    break;
                case R.id.hiitNavigation:
                    replaceFragment(hiitFragment, fragmentManager, "3");
                    currentItem = R.id.hiitNavigation;
                    break;
            }
            Fragment fragment = getCurrentFragment(currentItem);
            Log.d(TAG, "onNavigationItemSelected: " + fragment.getTag());
            return true;
        }
    };

    public void replaceFragment(Fragment fragment, @NonNull FragmentManager fragmentManager, String item) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment, item);
        fragmentTransaction.commit();
    }


    public Fragment getCurrentFragment(int idItem) {
        switch (idItem) {
            case (R.id.justJumpNavigation):
                Log.d(TAG, "getCurrentFragment: " + justJumpFragment);
                return justJumpFragment;
            case (R.id.doubleJumpNavigation):
                Log.d(TAG, "getCurrentFragment: " + doubleJumpFragment);
                return doubleJumpFragment;
            case (R.id.hiitNavigation):
                Log.d(TAG, "getCurrentFragment: " + hiitFragment);
                return hiitFragment;
        }
        return null;
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
                //включить блютуз, если выключен.
                if (!BluetoothManager.isEnableAdapter()) {
                    //noinspection deprecation
                    startActivityForResult
                            (BluetoothManager.enableAdapter(), BluetoothManager.REQUEST_CODE_ENABLE);

                } else {
                    BluetoothManager.disableAdapter();
                    onAdapterItem.setTitle(R.string.turnOnAdapter);
                    onAdapterItem.setIcon(R.drawable.ic_baseline_bluetooth_disabled_24);
                }
                Log.d(TAG, "getDevicesBoundArray: "+ BluetoothManager.getDevicesBoundArray().size());
                //проверить список сопряженных устройств и включить устройство
//                if (BluetoothManager.getDevicesBoundArray().size() > 0) {
//                    for (BluetoothDevice device : BluetoothManager.getDevicesBoundArray()) {
//                        if (device.getAddress().equals(BluetoothManager.ADRESS_DEVICE)) {
//                            BluetoothManager.connectDevice(device);
//                            BluetoothManager.getData(getCurrentFragment(currentItem));
//                            break;
//                        }
//                    }
//                    //если нет в сопряженных, начать поиск. Найти устройство с адресом и его включить.
//                } else {
                   /* if (!BluetoothManager.isDiscovery()) {
                        BluetoothManager.startDiscovery();
                    } else BluetoothManager.cancelDiscovery();
                    receiver = new BluetoothReceiver();
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                    filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(receiver, filter);*/
//                }

                Log.d(TAG, "getDevicesArray: "+ BluetoothManager.getDevicesArray().size());
//                if(BluetoothManager.getDevicesArray().size()>0) {
//                    for (BluetoothDevice device : BluetoothManager.getDevicesArray()) {
//                        if (device.getAddress().equals(BluetoothManager.ADRESS_DEVICE)) {
//                            BluetoothManager.connectDevice(device);
//                            BluetoothManager.getData(getCurrentFragment(currentItem));
//                            break;
//                        }
//                    }
//                }
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

    void showListDevices() {
        View view = setShowList(adapterSearchedDevices);
        setAlertDialog(view);
    }

    void showListBoundedDevices() {
        View view = setShowBoundedList(adapterBoundDevices);
        setAlertDialog(view);

    }

    //подготовить список найденных и связанных устройств (2 метода)
    @NonNull
    private View setShowList(BtDeviceAdapter adapter) {
        View view = getLayoutInflater().inflate(R.layout.list_devices, null);
        listDevices = view.findViewById(R.id.listDevices);
        listDevices.setAdapter(adapter);
        listDevices.setOnItemClickListener(onClickerItemListDevices);
        return view;
    }

    @NonNull
    private View setShowBoundedList(BtDeviceAdapter adapter) {
        View view = getLayoutInflater().inflate(R.layout.list_devices, null);
        listDevices = view.findViewById(R.id.listDevices);
        listDevices.setAdapter(adapter);
        listDevices.setOnItemClickListener(onClickerItemBoundedListDevices);
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

    // слушатель кликов найденных и связанных устройств (2 метода)
    private AdapterView.OnItemClickListener onClickerItemListDevices = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BluetoothDevice device = BluetoothManager.getDevicesArray().get(i);
            BluetoothManager.connectDevice(device);
            BluetoothManager.getData(getCurrentFragment(currentItem));
        }
    };
    private AdapterView.OnItemClickListener onClickerItemBoundedListDevices = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BluetoothDevice device = BluetoothManager.getDevicesBoundArray().get(i);
            BluetoothManager.connectDevice(device);
            BluetoothManager.getData(getCurrentFragment(currentItem));
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
                case (BluetoothDevice.ACTION_FOUND):
                    Log.d(TAG, "onReceive: ACTION_FOUND");
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getAddress().equals(BluetoothManager.ADRESS_DEVICE)) {
                        BluetoothManager.getDevicesArray().add(device);
                        Log.d(TAG, "onReceive: device added");
                    }
                    break;
                case (BluetoothAdapter.ACTION_DISCOVERY_FINISHED):
                    Log.d(TAG, "onReceive: ACTION_DISCOVERY_FINISHED");
                    progressDialog.dismiss();
                    showListDevices();
                    break;
            }

        }
    }

}