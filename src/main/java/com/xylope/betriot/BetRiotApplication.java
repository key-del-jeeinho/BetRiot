package com.xylope.betriot;

import com.xylope.betriot.layer.service.user.UserRegisterService;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import javax.security.auth.login.LoginException;

@ContextConfiguration("/applicationContext.xml")
public class BetRiotApplication {
    @Getter
    private static GenericXmlApplicationContext context = new GenericXmlApplicationContext("/applicationContext.xml", "/secretContext.xml");
    @Getter
    private JDA jda;
    @Autowired
    private final JDAEventManager mng;
    private final UserRegisterService registerService;

    public BetRiotApplication(String token) throws LoginException {
        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
        mng = context.getBean("jdaEventManager", JDAEventManager.class);
        mng.setJda(jda);

        registerService = context.getBean(UserRegisterService.class);
    }

    public void start() {
        mng.loadListener();
    }
}
