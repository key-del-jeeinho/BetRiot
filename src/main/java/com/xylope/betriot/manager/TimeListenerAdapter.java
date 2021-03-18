package com.xylope.betriot.manager;

import com.xylope.betriot.layer.domain.event.OnDayEvent;
import com.xylope.betriot.layer.domain.event.OnHourEvent;
import com.xylope.betriot.layer.domain.event.OnMinuteEvent;
import com.xylope.betriot.layer.domain.event.OnSecondEvent;

public abstract class TimeListenerAdapter implements TimeListener{
    @Override
    public void onTimeSecond(OnSecondEvent e) {}
    @Override
    public void onTImeMinute(OnMinuteEvent e) {}
    @Override
    public void onTimeHour(OnHourEvent e) {}
    @Override
    public void onTImeDay(OnDayEvent e) {}
}
