package com.xylope.betriot.layer.dataaccess.apis.discord;

import com.xylope.betriot.ApplicationBooter;
import com.xylope.betriot.BetRiotApplication;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

public class JdaAPI {
    private JDA jda;

    public PrivateChannel getPrivateChannelByUserId(long discordId) {
        initJda();
        return Objects.requireNonNull(getUserById(discordId)).openPrivateChannel().complete();
    }

    public User getUserById(long discordId) {
        initJda();
        User user = jda.getUserById(discordId);
        if(user == null)
            user = jda.retrieveUserById(discordId).complete();
        return user;
    }

    public User getUserById(String discordId) {
        initJda();
        User user = jda.getUserById(discordId);
        if(user == null)
            user = jda.retrieveUserById(discordId).complete();
        return user;
    }

    public TextChannel getTextChannelById(long channelId) {
        initJda();
        return jda.getTextChannelById(channelId);
    }

    private void initJda() {
        if(jda == null)
            jda = ApplicationBooter.CONTEXT.getBean(BetRiotApplication.class).getJda();
    }
}
