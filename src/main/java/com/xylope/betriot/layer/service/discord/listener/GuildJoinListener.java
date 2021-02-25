package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuildJoinListener extends ListenerAdapter implements EventRepeater<GuildJoinEvent>{
    private final List<RepeatListener<GuildJoinEvent>> listeners;

    public GuildJoinListener() {
        super();
        listeners = new ArrayList<>();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        repeatEvent(listeners, event);
    }

    @Override
    public void addListener(RepeatListener<GuildJoinEvent> listener) {
        listeners.add(listener);
    }
}
