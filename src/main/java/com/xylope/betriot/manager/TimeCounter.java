package com.xylope.betriot.manager;

import com.xylope.betriot.layer.domain.event.OnDayEvent;
import com.xylope.betriot.layer.domain.event.OnHourEvent;
import com.xylope.betriot.layer.domain.event.OnMinuteEvent;
import com.xylope.betriot.layer.domain.event.OnSecondEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class TimeCounter{
    @Setter @Getter
    private boolean isRunning;

    private long runningTimeSecond;
    private int runningTimeMinute;
    private int runningTimeHour;
    private int runningTimeDay;

    @Setter
    private List<TimeListener> listeners = new CopyOnWriteArrayList<>();

    public void addTimeListener(TimeListener listener) {
        listeners.add(listener);
    }

    public void addTimeListener(TimeListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    public void removeTimeListener(TimeListener listener) {
        this.listeners.remove(listener);
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
