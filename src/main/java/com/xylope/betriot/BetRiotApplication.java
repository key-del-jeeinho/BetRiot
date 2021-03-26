package com.xylope.betriot;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.xylope.betriot.manager.CommandManager;
import com.xylope.betriot.manager.JDAEventManager;
import com.xylope.betriot.manager.TimeCounter;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class BetRiotApplication {
    @Getter
    private final JDA jda;
    private final JDAEventManager eventManager;
    private final CommandManager commandManager;
    private final TimeCounter counter;

    public BetRiotApplication(String token, String riotApiKey, JDAEventManager eventManager, CommandManager commandManager, TimeCounter counter) throws LoginException, InterruptedException {
        Orianna.setRiotAPIKey(riotApiKey);
        Orianna.setDefaultRegion(Region.KOREA);
        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        this.eventManager = eventManager;
        this.eventManager.setJda(jda);
        this.commandManager = commandManager;
        this.counter = counter;
        jda.awaitReady();

        this.counter.run();
    }

    public void start() throws InterruptedException {
        jda.awaitStatus(JDA.Status.CONNECTED);
        eventManager.manage();
        commandManager.manage();
    }
}
