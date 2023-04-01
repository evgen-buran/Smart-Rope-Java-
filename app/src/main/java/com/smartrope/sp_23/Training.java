package com.smartrope.sp_23;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Training {
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


    public Training() {
        this.countJumps = 0;
        this.rpm = 0;
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
    public void Chronometer() {
//        isStarting = !isStarting;
        if(threadChrono == null) threadChrono = new Thread(new Runnable() {

            @Override

            public void run() {
                while (isStarting) {
                    try {
                        TimeUnit.MICROSECONDS.sleep(680);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isStarting) timeOver++;
                    over = (int) timeOver % 3600000;
                    min = over / 60000;
                    over = over % 60000;
                    sec = over / 1000;
                    ms = (over % 1000) / 10;
                    liveDataChrono.postValue(String.format("%02d:%02d.%02d", min, sec, ms));

                }
                if (!isStarting) threadChrono.interrupt();
            }
        });
        threadChrono.start();
    }

    public LiveData<String> getLiveDataChrono() {
        return liveDataChrono;
    }
}
