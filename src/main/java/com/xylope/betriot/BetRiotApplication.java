package com.xylope.betriot;


import com.xylope.betriot.layer.service.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class BetRiotApplication {
    public static void main(String[] args) throws LoginException, InterruptedException {
        JDA jda = JDABuilder.createDefault(args[0]).build();
        jda.addEventListener(new MessageListener());
    }
}
