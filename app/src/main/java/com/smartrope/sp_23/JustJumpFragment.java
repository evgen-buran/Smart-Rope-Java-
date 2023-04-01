package com.smartrope.sp_23;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//todo  getData устанавливает сигнал true при срабатывании магнита. И тут же его устанавливает в False
// до следующего срабатывания
// необходимо при true вызывать метод (например увеличения счетчика)
// и отобразить новое значение счетчика на экране
// как реализовать этого "слушателя"?
public class JustJumpFragment extends Fragment {
    final String TAG = "myLog";
    Training training;
    TextView
            tvCounter,
            tvTimer,
            tvKcal,
            tvRPM;
    Thread thread;
    LiveData<Boolean> liveDataSignal;
    LiveData<String> liveDataChrono;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_just_jump, container, false);
        tvCounter = view.findViewById(R.id.tvCounter);
        tvKcal = view.findViewById(R.id.tvKcal);
        tvRPM = view.findViewById(R.id.tvRPM);
        tvTimer = view.findViewById(R.id.tvTimer);
        training = new Training();
        liveDataSignal = BluetoothManager.getLiveDataSignal();
        liveDataChrono = training.getLiveDataChrono();

        liveDataSignal.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) training.jumpsIncrement(training.JUST_MULTIPLY);
                tvCounter.setText(String.valueOf(training.getCountJumps()));
                Log.d(TAG, "onChanged: " + training.getCountJumps());
            }
        });

        liveDataChrono.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String time) {
                tvTimer.setText(time);
                Log.d(TAG, "onChanged: " + time);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
       /* training.jumpsIncrement(1);
        tvCounter.setText(training.getCountJumps());*/
    }


}