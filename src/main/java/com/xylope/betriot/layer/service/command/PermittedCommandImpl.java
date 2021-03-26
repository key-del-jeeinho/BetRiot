package com.xylope.betriot.layer.service.command;

import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

@NoArgsConstructor
public class PermittedCommandImpl extends AbstractCommand{
    @Setter
    private AbstractCommand command;
    @Setter
    private UserDao dao;
    @Setter
    int permissionLevel;

    @Override
    public void execute(GuildChannel channel, User sender, String... args) {
        if(sender.isBot()) return; //bot can't use command

        Guild guild = channel.getGuild();
        TextChannel textChannel = guild .getTextChannelById(channel.getId());
        long discordId = sender.getIdLong();
        assert textChannel != null;
        if(!dao.isUserExist(discordId)) {
            //비회원 유저일경우
            textChannel.sendMessage("회원전용 명령어입니다!").queue();
            return;
        }
        else if(!dao.checkPermission(discordId, UserVO.Permission.getById(permissionLevel))) {
            //회원이지만, 권한이 없을경우
            textChannel.sendMessage("해당 명령어를 사용할 권한이 없습니다!").queue();
            return;
        }

        if(command == null)
            checkChildExecute(channel, sender, args);
        else
            command.execute(channel, sender, args);
    }
}
