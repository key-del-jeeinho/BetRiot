package com.xylope.betriot.layer.service.command.custom;

import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class KillCodeCommand extends LeafCommand {
    public KillCodeCommand() {
        super(
                new AbstractCommand() {
                    @Override
                    public void execute(GuildChannel channel, User sender, String... args) {
                        Guild guild = channel.getGuild();
                        TextChannel textChannel = guild.getTextChannelById(channel.getId());
                        assert textChannel != null;
                        System.out.println("KillCode 명령어에 의해 시스템을 강제종료합니다!");
                        System.exit(0);
                    }
                }
        );
    }
}
