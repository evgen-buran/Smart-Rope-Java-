package com.smartrope.sp_23;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    MenuItem onAdapterItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BluetoothManager.setContext(this);
        BluetoothManager.initAdapter();

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
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case (BluetoothManager.REQUEST_CODE_ENABLE):
                if (resultCode == RESULT_OK) {
                    onAdapterItem.setTitle(R.string.turnOffAdapter);
                    onAdapterItem.setIcon(R.drawable.ic_baseline_bluetooth_24);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}