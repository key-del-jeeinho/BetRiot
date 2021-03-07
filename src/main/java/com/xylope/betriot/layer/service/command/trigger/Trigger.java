package com.xylope.betriot.layer.service.command.trigger;

public interface Trigger<T> {
    //check is trigger
    boolean checkTrigger(T arg);
}
