package com.xylope.betriot.layer.logic.discord.message;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;

public class PrivateEmbedMessageSender implements PrivateMessageSender<MessageEmbed>{
    @Override
    public void sendMessage(PrivateChannel channel, MessageEmbed msg) {
        channel.sendMessage(msg).queue();
    }
}
