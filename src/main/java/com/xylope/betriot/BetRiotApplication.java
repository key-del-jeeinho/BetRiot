package com.xylope.betriot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.security.auth.login.LoginException;

@ContextConfiguration("/applicationContext.xml")
public class BetRiotApplication {
    private JDA jda;
    @Autowired
    private JDAEventManager mng;

    public BetRiotApplication(String token) throws LoginException {
        jda = JDABuilder.createDefault(token).build();
        mng.setJda(jda);
    }

    public void start() {
    }
}
