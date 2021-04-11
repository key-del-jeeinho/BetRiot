package com.xylope.betriot.layer.logic.discord.message;

import net.dv8tion.jda.api.entities.TextChannel;

public interface ChannelMessageSender<T> extends MessageSender<TextChannel, T> {
}
