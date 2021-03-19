package com.xylope.betriot.manager;

import com.xylope.betriot.layer.domain.event.OnDayEvent;
import com.xylope.betriot.layer.domain.event.OnHourEvent;
import com.xylope.betriot.layer.domain.event.OnMinuteEvent;
import com.xylope.betriot.layer.domain.event.OnSecondEvent;

public interface TimeListener {
    void onTimeSecond(OnSecondEvent e);
    void onTimeMinute(OnMinuteEvent e);
    void onTimeHour(OnHourEvent e);
    void onTImeDay(OnDayEvent e);
}
