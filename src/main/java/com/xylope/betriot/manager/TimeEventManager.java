package com.xylope.betriot.manager;

import lombok.Setter;

public class TimeEventManager implements Manager{
    @Setter
    private TimeCounter basicListeners;
    @Setter
    private TimeListenerAdapter[] listeners;

    @Override
    public void manage() {

    }
}
