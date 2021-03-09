package com.xylope.betriot.layer.service.user.message;

import net.dv8tion.jda.api.entities.MessageChannel;

public interface MessageSender <T1 extends MessageChannel, T2>{
    void sendMessage(T1 channel, T2 msg);
}
