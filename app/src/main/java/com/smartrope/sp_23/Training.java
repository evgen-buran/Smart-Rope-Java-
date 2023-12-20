package com.smartrope.sp_23;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Training {
    final String TAG = "myLog";
    final int JUST_MULTIPLY = 1;
    final int DOUBLE_MULTIPLY = 2;
    final boolean START_TRAINING = true;
    final boolean FINISH_TRAINING = false;
    int countJumps;
    int rpm;


    private int min, sec, ms; //минуты, секунды, миллисекунды
    private long currentTime, startTime;
    private boolean isSignal;
    private Thread threadChrono;
    private boolean isStarting = false;
    private long timeOver = 0;
    private int over;
    MutableLiveData<String> liveDataChrono = new MutableLiveData<>();
    MutableLiveData<Integer> liveDataSignal = new MutableLiveData<>();


    public Training() {
        this.countJumps = 0;
        this.rpm = 0;
        isStarting = false;
    }

    public void resetTraining() {
        isStarting = false;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setStarting(boolean starting) {
        isStarting = starting;
    }

    //множитель для режимов прыжков: 1 - обычные прыжки, 2 - двойные прыжки
    public int jumpsIncrement(int multiply) {

        return ++countJumps * multiply;

    }

    public void setCountJumps(int countJumps) {
        this.countJumps = countJumps;
    }

    public int getCountJumps() {
        return countJumps;
    }

    long getCurrentTime() {
        currentTime = millis();
        return currentTime;
    }

    long millis() {
        return new Date().getTime();
    }

    //----------------------СЕКУНДОМЕР-----------------------------------------
    public void chronometer() {
//        isStarting = !isStarting;
        if (threadChrono == null) {

            threadChrono = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isStarting) {
                        try {
                            TimeUnit.MICROSECONDS.sleep(680);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        timeOver++;
                        over = (int) timeOver % 3600000;
                        min = over / 60000;
                        over = over % 60000;
                        sec = over / 1000;
                        ms = (over % 1000) / 10;
                        Log.d(TAG, "state: " + threadChrono.getState());
                        liveDataChrono.postValue(String.format("%02d:%02d.%02d", min, sec, ms));

                    }
                    if (!isStarting) {
                        min = sec = ms = 0;
                        liveDataChrono.postValue(String.format("%02d:%02d.%02d", min, sec, ms));
                        threadChrono.interrupt();
                        Log.d(TAG, "state: " + threadChrono.getState());
                    }
                }
            });
        }

        Log.d(TAG, "FINISH: " + threadChrono.getState());
        if (!threadChrono.isAlive()) threadChrono.start();
    }

    public LiveData<String> getLiveDataChrono() {
        return liveDataChrono;
    }






}

