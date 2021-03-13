package com.xylope.betriot.layer.service.command;

import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

public interface Executable {
    void execute(GuildChannel channel, User sender, String... args);
}
