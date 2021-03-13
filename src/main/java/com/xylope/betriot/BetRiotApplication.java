package com.xylope.betriot;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.xylope.betriot.manager.CommandManager;
import com.xylope.betriot.manager.JDAEventManager;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class BetRiotApplication {
    @Getter
    private final JDA jda;
    @Setter
    private JDAEventManager eventManager;
    @Setter
    private CommandManager commandManager;

    public BetRiotApplication(String token, String riotApiKey, JDAEventManager eventManager, CommandManager commandManager) throws LoginException {
        Orianna.setRiotAPIKey(riotApiKey);
        Orianna.setDefaultRegion(Region.KOREA);
        jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        this.eventManager = eventManager;
        this.eventManager.setJda(jda);
        this.commandManager = commandManager;
    }

    public void start() {
        eventManager.manage();
        commandManager.manage();
    }
}
