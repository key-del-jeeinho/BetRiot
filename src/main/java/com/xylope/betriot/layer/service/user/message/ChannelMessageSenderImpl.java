package com.xylope.betriot.layer.service.user.message;

import net.dv8tion.jda.api.entities.TextChannel;

public class ChannelMessageSenderImpl implements ChannelMessageSender<String> {
    @Override
    public void sendMessage(TextChannel channel, String msg) {
        channel.sendMessage(msg).queue();
    }
}
