package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class MessageReceivedListener extends EventRepeaterImpl<GuildMessageReceivedEvent>{
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        repeatEvent(event);
    }
}
