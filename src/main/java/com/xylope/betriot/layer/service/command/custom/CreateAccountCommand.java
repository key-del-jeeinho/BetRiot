package com.xylope.betriot.layer.service.command.custom;

import com.xylope.betriot.manager.CommandManager;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import com.xylope.betriot.layer.logic.discord.message.ChannelMessageSenderImpl;
import com.xylope.betriot.layer.service.user.account.create.CreateAccountService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

public class CreateAccountCommand extends LeafCommand {
    public CreateAccountCommand(CreateAccountService createAccountService, UserDao dao, ChannelMessageSenderImpl messageSender) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                Guild guild = channel.getGuild();
                String channelId = channel.getId();
                if(dao.isUserExist(sender.getIdLong())) {
                    messageSender.sendMessage(Objects.requireNonNull(guild.getTextChannelById(channelId)), String.format("이미 회원이십니다! (%s 회원탈퇴)", CommandManager.ROOT_CMD_STR));
                    return;
                }
                createAccountService.createUserAccount(guild, sender);
            }
        });
    }
}
