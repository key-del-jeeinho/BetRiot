package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class PrivateMessageReceivedListener extends EventRepeaterImpl<PrivateMessageReceivedEvent>{
    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        repeatEvent(event);
    }
}
