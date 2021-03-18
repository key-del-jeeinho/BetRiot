package com.xylope.betriot.layer.domain.event;

public class OnDayEvent extends OnTimeEvent {
    public OnDayEvent(long currentTimeMills, long getCount) {
        super(currentTimeMills, getCount);
    }
}
