package com.xylope.betriot.layer.logic.discord.message;

import net.dv8tion.jda.api.entities.PrivateChannel;

public class PrivateMessageSenderImpl implements PrivateMessageSender<String>{
    @Override
    public void sendMessage(PrivateChannel channel, String msg) {
        channel.sendMessage(msg).queue();
    }
}
