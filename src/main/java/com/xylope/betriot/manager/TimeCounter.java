package com.xylope.betriot.manager;

import com.xylope.betriot.layer.domain.event.OnDayEvent;
import com.xylope.betriot.layer.domain.event.OnHourEvent;
import com.xylope.betriot.layer.domain.event.OnMinuteEvent;
import com.xylope.betriot.layer.domain.event.OnSecondEvent;
import lombok.Setter;

import java.util.*;

public class TimeCounter{
    @Setter
    private boolean isRunning;

    private long startTimeMills; // 계속 변동함
    private long currentTimeMills;

    private long runningTimeSecond;
    private int runningTimeMinute;
    private int runningTimeHour;
    private int runningTimeDay;

    @Setter
    private List<TimeListener> listeners = new ArrayList<>();

    public void addTimeListener(TimeListener listener) {
        listeners.add(listener);
    }

    public void addTimeListener(TimeListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    public int getListenerCount() {
        return listeners.size();
    }

    public void run() {
        isRunning = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(isRunning)
                    loop();
            }
        }, 0L, 1000L);
    }

    public void loop() {
        startTimeMills = currentTimeMills;
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

    private void secondLogic() {
        runningTimeSecond++;
        listeners.forEach((listener -> listener.onTimeSecond(new OnSecondEvent(runningTimeSecond))));
    }

    private void minuteLogic() {
        runningTimeMinute++;
        listeners.forEach((listener -> listener.onTimeMinute(new OnMinuteEvent(runningTimeMinute))));
    }

    private void hourLogic() {
        runningTimeHour++;
        listeners.forEach((listener -> listener.onTimeHour(new OnHourEvent(runningTimeHour))));
    }

    private void dayLogic() {
        runningTimeDay++;
        listeners.forEach((listener -> listener.onTImeDay(new OnDayEvent(runningTimeDay))));
    }

}
