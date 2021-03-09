package com.xylope.betriot.layer.service.command;

import net.dv8tion.jda.api.entities.User;

public interface Executable {
    void execute(User sender, String... args);
}
