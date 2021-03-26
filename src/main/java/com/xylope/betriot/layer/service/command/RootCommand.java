package com.xylope.betriot.layer.service.command;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

public class RootCommand extends CommandImpl {
    @Override
    public void execute(GuildChannel channel, User sender, String... args) {
        super.execute(channel, sender, args);
        for(AbstractCommand cmd : children) {
            System.out.println(cmd);
        }
    }
}
