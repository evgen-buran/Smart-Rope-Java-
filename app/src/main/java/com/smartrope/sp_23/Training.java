package com.smartrope.sp_23;

public class Training {
    final int JUST_MULTIPLY = 1;
    final int DOUBLE_MULTIPLY = 2;
    int countJumps;
    int rpm;
    boolean isStarting;

    public Training() {
        this.countJumps = 0;
        this.rpm = 0;
        isStarting = false;
    }
//множитель для режимов прыжков: 1 - обычные прыжки, 2 - двойные прыжки
    public int countJumpsIncrement(int multiply) {
        return multiply * countJumps++;
    }

    public void setCountJumps(int countJumps) {
        this.countJumps = countJumps;
    }

    public int getCountJumps() {
        return countJumps;
    }
}
