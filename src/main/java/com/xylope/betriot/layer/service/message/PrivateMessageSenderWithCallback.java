package com.xylope.betriot.layer.service.message;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;

import java.util.function.Consumer;

public interface PrivateMessageSenderWithCallback extends
        PrivateMessageSender<String> {
    void sendMessage(PrivateChannel channel, String msg, Consumer<Message> consumer);
}
