package com.xylope.betriot.layer.domain.event;

public class OnSecondEvent extends OnTimeEvent {
    public OnSecondEvent(long currentTimeMills, long getCount) {
        super(currentTimeMills, getCount);
    }
}
