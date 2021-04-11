package com.xylope.betriot.layer.logic.discord.listener;

import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PrivateMessageReactionAddListener extends EventRepeaterImpl<PrivateMessageReactionAddEvent> {
    @Override
    public void onPrivateMessageReactionAdd(@NotNull PrivateMessageReactionAddEvent event) {
        if(Objects.requireNonNull(event.getUser()).isBot()) return;
        repeatEvent(event);
    }
}
