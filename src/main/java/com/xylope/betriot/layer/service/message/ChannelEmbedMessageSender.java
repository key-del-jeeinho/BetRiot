package com.xylope.betriot.layer.service.message;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class ChannelEmbedMessageSender implements ChannelMessageSender<MessageEmbed> {
    @Override
    public void sendMessage(TextChannel channel, MessageEmbed msg) {
        channel.sendMessage(msg).queue();
    }
}
