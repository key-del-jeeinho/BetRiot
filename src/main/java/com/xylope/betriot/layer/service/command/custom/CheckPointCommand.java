package com.xylope.betriot.layer.service.command.custom;

import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import com.xylope.betriot.layer.service.util.userpoint.UserPointChecker;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class CheckPointCommand extends LeafCommand {
    public CheckPointCommand(UserPointChecker userPointChecker) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                TextChannel tc = channel.getGuild().getTextChannelById(channel.getId());
                assert tc != null;

                userPointChecker.checkUserPoint(sender.getIdLong());
            }
        });
    }
}
