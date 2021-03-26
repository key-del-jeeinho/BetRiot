package com.xylope.betriot.layer.service.command.custom.bet;

import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.service.bet.BetService;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import com.xylope.betriot.layer.service.message.ChannelMessageSender;
import com.xylope.betriot.manager.CommandManager;
import net.dv8tion.jda.api.entities.*;

import java.util.Objects;

public class BetOpenCommand extends LeafCommand {
    public BetOpenCommand(BetService betService, UserDao userDao,
                          ChannelMessageSender<String> stringChannelMessageSender) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                Guild guild = channel.getGuild();
                long channelId = channel.getIdLong();

                if(!userDao.isUserExist(sender.getIdLong()))
                    stringChannelMessageSender.sendMessage(Objects.requireNonNull(guild.getTextChannelById(channelId)),
                            String.format("뱃라이엇 회원 전용 명령어입니다! (%s 회원가입)", CommandManager.ROOT_CMD_STR));

                TextChannel textChannel = guild.getTextChannelById(channelId);
                PrivateChannel privateChannel = sender.openPrivateChannel().complete();
                betService.openPrivateBet(userDao.get(sender.getIdLong()), textChannel, privateChannel);
                privateChannel.close().complete();
            }
        });
    }
}
