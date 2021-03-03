package com.xylope.betriot;

import com.xylope.betriot.layer.service.user.register.UserRegisterService;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.context.support.GenericXmlApplicationContext;

import javax.security.auth.login.LoginException;

public class BetRiotApplication {
    @Getter
    private static GenericXmlApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml", "/secretContext.xml");
    @Getter
    private final JDA jda;
    private final JDAEventManager eventManager;
    //private final CommandManager commandManager;
    private final UserRegisterService registerService;

    public BetRiotApplication(String token) throws LoginException {
        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        //init Managers
        eventManager = context.getBean("jdaEventManager", JDAEventManager.class);
        eventManager.setJda(jda);
        //commandManager = context.getBean("commandManager", CommandManager.class);

        registerService = context.getBean(UserRegisterService.class);
    }

    public void start() {
        eventManager.manage();
        //commandManager.manage();
    }
}
