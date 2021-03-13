package com.xylope.betriot.layer.service.command.custom;

import com.xylope.betriot.manager.CommandManager;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import com.xylope.betriot.layer.service.user.account.remove.RemoveAccountService;
import com.xylope.betriot.layer.service.user.message.ChannelMessageSender;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class RemoveAccountCommand extends LeafCommand {
    public RemoveAccountCommand(UserDao dao, RemoveAccountService removeAccountService, ChannelMessageSender<String> channelMessageSender) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                if(sender.isBot()) return; //bot can't use command

                if(dao.isUserExist(sender.getIdLong())) {
                    removeAccountService.removeUserAccount(sender);
                } else {
                    Guild guild = channel.getGuild();
                    String channelId = channel.getId();
                    TextChannel guildChannel = guild.getTextChannelById(channelId);

                    assert guildChannel != null;
                    channelMessageSender.sendMessage(guildChannel, String.format("뱃라이엇에 가입하지 않으셧습니다! (%s 회원가입)", CommandManager.ROOT_CMD_STR));
                }
            }
        });
    }
}
