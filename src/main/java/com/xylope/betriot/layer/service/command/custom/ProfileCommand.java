package com.xylope.betriot.layer.service.command.custom;

import com.xylope.betriot.manager.CommandManager;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import com.xylope.betriot.layer.service.user.apis.UserSummonerAPI;
import com.xylope.betriot.layer.service.message.ChannelMessageSender;
import net.dv8tion.jda.api.entities.*;
import org.springframework.dao.EmptyResultDataAccessException;

import java.awt.*;
import java.util.Objects;

//Betriot Member Command
public class ProfileCommand extends LeafCommand {
    public ProfileCommand(UserDao dao,
                          ChannelMessageSender<String> messageSender,
                          ChannelMessageSender<MessageEmbed> embedMessageSender,
                          UserSummonerAPI userSummonerAPI) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                String channelId = channel.getId();
                Guild guild = channel.getGuild();
                TextChannel sendChannel = guild.getTextChannelById(channelId);

                UserVO user;

                if (args.length <= 0) {
                    if (!dao.isUserExist(sender.getIdLong())) {
                        messageSender.sendMessage(Objects.requireNonNull(guild.getTextChannelById(channelId)),
                                String.format("뱃라이엇 회원 전용 명령어입니다! (%s 회원가입)", CommandManager.ROOT_CMD_STR));
                        return;
                    }

                    user = dao.get(sender.getIdLong());
                } else {
                    String discordId = args[0].replaceAll("\\p{Punct}", "");
                    try {
                        user = dao.get(Long.parseLong(discordId));
                    } catch (EmptyResultDataAccessException e) {
                        messageSender.sendMessage(Objects.requireNonNull(guild.getTextChannelById(channelId)), "해당 회원을 찾을 수 없습니다!");
                        return;
                    }
                }
                SummonerDto summoner = userSummonerAPI.getById(user.getRiotId());
                MessageEmbed message = userSummonerAPI.getUserProfileMessage(summoner, new Color(108, 255, 245));

                embedMessageSender.sendMessage(sendChannel, message);
            }});
    }
}
