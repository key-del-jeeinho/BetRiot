package com.xylope.betriot.layer.service.command;

import com.xylope.betriot.exception.TreeException;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

@RequiredArgsConstructor
public class LeafCommand extends AbstractCommand {
    private final AbstractCommand command;

    @Override
    public void execute(GuildChannel channel, User sender, String... args) {
        command.execute(channel, sender, args);
    }

    @Override
    public void addChildCommand(AbstractCommand cmd) {
        throw new TreeException("that is leaf command");
    }
}
