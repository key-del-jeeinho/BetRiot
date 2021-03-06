package com.xylope.betriot.layer.logic.discord.message;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

import java.util.function.Consumer;

public class PrivateMessageSenderWithCallbackImpl implements PrivateMessageSenderWithCallback<String>{
    @Override
    public void sendMessage(PrivateChannel channel, String msg) {
        channel.sendMessage(msg).queue();
    }

    @Override
    public void sendMessage(PrivateChannel channel, String msg, Consumer<Message> consumer) {
        channel.sendMessage(msg).queue(consumer);
    }
}
