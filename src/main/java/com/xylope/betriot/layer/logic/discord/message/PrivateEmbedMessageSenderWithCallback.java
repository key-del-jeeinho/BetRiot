package com.xylope.betriot.layer.logic.discord.message;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class PrivateEmbedMessageSenderWithCallback extends PrivateEmbedMessageSender{
    public long sendMessage(PrivateChannel channel, MessageEmbed msg, Consumer<Message> consumer) {
        AtomicLong messageId = new AtomicLong();
        channel.sendMessage(msg).queue(consumer);
        return messageId.get();
    }
}
