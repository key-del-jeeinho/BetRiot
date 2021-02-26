package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PrivateMessageReactionAddListener extends ListenerAdapter implements EventRepeater<PrivateMessageReactionAddEvent> {
    private final List<RepeatListener<PrivateMessageReactionAddEvent>> listeners;

    public PrivateMessageReactionAddListener() {
        super();
        listeners = new ArrayList<>();
    }

    @Override
    public void addListener(RepeatListener<PrivateMessageReactionAddEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void onPrivateMessageReactionAdd(@NotNull PrivateMessageReactionAddEvent event) {
        repeatEvent(listeners, event);
    }
}
