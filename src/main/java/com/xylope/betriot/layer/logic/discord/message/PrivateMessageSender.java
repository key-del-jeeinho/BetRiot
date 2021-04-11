package com.xylope.betriot.layer.logic.discord.message;

import net.dv8tion.jda.api.entities.PrivateChannel;

public interface PrivateMessageSender<T> extends MessageSender<PrivateChannel, T> {
}
