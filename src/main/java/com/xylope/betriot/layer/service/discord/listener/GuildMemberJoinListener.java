package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuildMemberJoinListener extends ListenerAdapter implements EventRepeater<GuildMemberJoinEvent>{
    private final List<RepeatListener<GuildMemberJoinEvent>> listeners;

    public GuildMemberJoinListener() {
        super();
        listeners = new ArrayList<>();
    }

    @Override
    public void addListener(RepeatListener<GuildMemberJoinEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        repeatEvent(listeners, event);
    }
}
