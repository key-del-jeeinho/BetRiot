package com.xylope.betriot.layer.domain.event;

public class OnHourEvent extends OnTimeEvent {
    public OnHourEvent(long currentTimeMills, long getCount) {
        super(currentTimeMills, getCount);
    }
}
