package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessageReceivedListener extends ListenerAdapter implements EventRepeater<GuildMessageReceivedEvent>{
    private final List<RepeatListener<GuildMessageReceivedEvent>> listeners;

    public MessageReceivedListener() {
        super();
        listeners = new ArrayList<>();
    }

    @Override
    public void addListener(RepeatListener<GuildMessageReceivedEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        repeatEvent(listeners, event);
    }
}
