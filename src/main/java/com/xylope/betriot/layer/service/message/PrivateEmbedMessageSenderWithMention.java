package com.xylope.betriot.layer.service.message;

import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public class PrivateEmbedMessageSenderWithMention implements PrivateMessageSender<MessageEmbed> {
    @Setter
    private User user;

    @Override
    public void sendMessage(PrivateChannel channel, MessageEmbed msg) {
        channel.sendMessage(user.getAsMention()).queue();
        channel.sendMessage(msg).queue();
    }
}
