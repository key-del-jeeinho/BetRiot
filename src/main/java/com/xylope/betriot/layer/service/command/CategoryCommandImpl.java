package com.xylope.betriot.layer.service.command;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

public class CategoryCommandImpl extends AbstractCommand{
    @Override
    public final void execute(GuildChannel channel, User sender, String... args) {
        checkChildExecute(channel, sender, args);
    }
}
