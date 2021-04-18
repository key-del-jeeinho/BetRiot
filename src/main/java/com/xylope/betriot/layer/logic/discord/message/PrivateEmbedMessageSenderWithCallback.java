package com.xylope.betriot.layer.logic.discord.message;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;

import java.util.function.Consumer;

public class PrivateEmbedMessageSenderWithCallback extends PrivateEmbedMessageSender{
    public void sendMessage(PrivateChannel channel, MessageEmbed msg, Consumer<Message> consumer) {
        channel.sendMessage(msg).queue(consumer);
    }
}
