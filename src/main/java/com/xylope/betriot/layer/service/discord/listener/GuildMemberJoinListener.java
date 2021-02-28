package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import org.jetbrains.annotations.NotNull;

public class GuildMemberJoinListener extends EventRepeaterImpl<GuildMemberJoinEvent>{
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        repeatEvent(event);
    }
}
