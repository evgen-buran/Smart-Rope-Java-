package com.smartrope.sp_23;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_just_jump, container, false);
        tvCounter = view.findViewById(R.id.tvCounter);
        tvKcal = view.findViewById(R.id.tvKcal);
        tvRPM = view.findViewById(R.id.tvRPM);
        tvTimer = view.findViewById(R.id.tvTimer);
        training = new Training();
//        Handler handler = new Handler();

    /*    thread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (!thread.isInterrupted()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (training.isStarting()) {
                                Log.d(TAG, "run: dfdf");
                                tvCounter.setText(training.jumpsIncrement(1));
                            }
                        }
                    });


                }
            }
        });*/


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
       /* training.jumpsIncrement(1);
        tvCounter.setText(training.getCountJumps());*/
    }


}