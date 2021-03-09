package com.xylope.betriot.layer.service.command;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.User;

@RequiredArgsConstructor
public class LeafCommand extends AbstractCommand {
    private final AbstractCommand command;

    @Override
    public void execute(User sender, String... args) {
        command.execute(sender, args);
    }
}
