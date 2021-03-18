package com.xylope.betriot.layer.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class OnTimeEvent {
    @Getter
    long currentTimeMills;
    @Getter
    long getCount;
}
