package com.xylope.betriot.manager;

public class TimeCounter implements Runnable{
    long startTimeMills;
    long currentTimeMills;
    long runningTimeSecond;
    int runningTimeMinute;
    int runningTimeHour;
    int runningTimeDay;

    @Override
    public void run() {
        startTimeMills = System.currentTimeMillis();
        while (true) {
            currentTimeMills = System.currentTimeMillis();
            if((startTimeMills - currentTimeMills) % 1000 == 0) {
                secondLogic();
                if(runningTimeSecond % 60 == 0) {
                    minuteLogic();
                    if(runningTimeMinute % 60 == 0) {
                        hourLogic();
                        if(runningTimeHour % 24 == 0) {
                            dayLogic();
                        }
                    }
                }
            }
        }
    }

    private void secondLogic() {
        runningTimeSecond++;
    }

    private void minuteLogic() {
        runningTimeMinute++;
    }

    private void hourLogic() {
        runningTimeHour++;
    }

    private void dayLogic() {
        runningTimeDay++;
    }

}
