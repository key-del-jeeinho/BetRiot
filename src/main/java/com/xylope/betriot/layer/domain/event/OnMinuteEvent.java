package com.xylope.betriot.layer.domain.event;

public class OnMinuteEvent extends OnTimeEvent {
    public OnMinuteEvent(long currentTimeMills, long getCount) {
        super(currentTimeMills, getCount);
    }
}
