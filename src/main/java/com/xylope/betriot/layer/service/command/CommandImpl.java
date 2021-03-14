package com.xylope.betriot.layer.service.command;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandImpl extends AbstractCommand{
    @Override
    public void execute(GuildChannel channel, User sender, String... args) {
        if(sender.isBot()) return; //bot can't use command

        checkChildExecute(channel, sender, args);
    }
}
