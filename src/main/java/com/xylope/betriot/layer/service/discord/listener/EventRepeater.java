package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.GenericEvent;

import java.util.List;

public interface EventRepeater<T extends GenericEvent> {
    default void repeatEvent(List<RepeatListener<T>> listeners, T event) {
        listeners.forEach(listener-> listener.onEvent(event));
    }

    void addListener(RepeatListener<T> listener);
}