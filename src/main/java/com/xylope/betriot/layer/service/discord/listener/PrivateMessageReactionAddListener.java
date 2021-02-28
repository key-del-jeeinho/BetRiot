package com.xylope.betriot.layer.service.discord.listener;

import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import org.jetbrains.annotations.NotNull;

public class PrivateMessageReactionAddListener extends EventRepeaterImpl<PrivateMessageReactionAddEvent> {
    @Override
    public void onPrivateMessageReactionAdd(@NotNull PrivateMessageReactionAddEvent event) {
        repeatEvent(event);
    }
}
