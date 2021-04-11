package com.xylope.betriot.layer.logic.discord.listener;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class PrivateMessageReceivedListener extends EventRepeaterImpl<PrivateMessageReceivedEvent>{
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        repeatEvent(event);
    }
}
