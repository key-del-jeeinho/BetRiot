package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class EventRepeaterImpl <T extends GenericEvent> extends ListenerAdapter implements EventRepeater<T> {
    private final List<RepeatListener<T>> listeners;

    public EventRepeaterImpl() {
        super();
        listeners = new ArrayList<>();
    }

    @Override
    public void addListener(RepeatListener<T> listener) {
        listeners.add(listener);
    }

    public void repeatEvent(T event) {
        repeatEvent(listeners, event);
    }
}
