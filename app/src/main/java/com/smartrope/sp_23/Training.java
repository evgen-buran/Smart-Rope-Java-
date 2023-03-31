package com.smartrope.sp_23;

import java.util.Date;

public class Training {
    final int JUST_MULTIPLY = 1;
    final int DOUBLE_MULTIPLY = 2;
    final boolean START_TRAINING = true;
    final boolean FINISH_TRAINING = false;
    int countJumps;
    int rpm;

    private boolean isStarting;

    private int min, sec, ms; //минуты, секунды, миллисекунды
    private long currentTime, startTime;
    private boolean isSignal;


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
        if (isStarting)
            return multiply * countJumps++;
        else
            return countJumps;
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
    public void stopwatch(long currentTime) {
        long over;
        //время для отображения: millis - время уже прошедшее на момент срабатывания прерывания(startTime) и разницу раскидываем по минутам, секундам и т.д.

        over = currentTime % 3600000;
        min = (int) (over / 60000);
        over = over % 60000;
        sec = (int) (over / 1000);
        ms = (int) (over % 1000) / 10; //оставить две цифры
    }
}
