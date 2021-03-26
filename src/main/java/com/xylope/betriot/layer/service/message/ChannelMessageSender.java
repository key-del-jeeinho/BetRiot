package com.xylope.betriot.layer.service.message;

import net.dv8tion.jda.api.entities.TextChannel;

public interface ChannelMessageSender<T> extends MessageSender<TextChannel, T> {
}
