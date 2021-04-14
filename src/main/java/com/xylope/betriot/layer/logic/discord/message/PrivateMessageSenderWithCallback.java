package com.xylope.betriot.layer.logic.discord.message;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

import java.util.function.Consumer;

public interface PrivateMessageSenderWithCallback<T> extends
        PrivateMessageSender<T> {
    void sendMessage(PrivateChannel channel, T msg, Consumer<Message> consumer);
}
