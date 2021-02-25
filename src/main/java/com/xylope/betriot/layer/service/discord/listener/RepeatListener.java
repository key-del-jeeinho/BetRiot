package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.GenericEvent;

@FunctionalInterface
public interface RepeatListener<T extends GenericEvent>{
    void onEvent(T event);
}
