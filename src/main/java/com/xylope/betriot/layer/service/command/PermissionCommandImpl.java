package com.xylope.betriot.layer.service.command;

import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.command.trigger.CommandTrigger;
import lombok.Setter;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

public class PermissionCommandImpl extends AbstractCommand{
    @Setter
    private UserDao dao;
    @Setter
    UserVO.Permission permission;
    @Override
    public void execute(GuildChannel channel, User sender, String... args) {
        if(sender.isBot()) return; //bot can't use command
        long discordId = sender.getIdLong();
        if(!dao.isUserExist(discordId)) return; //비회원 유저일경우
        else if(!dao.checkPermission(discordId, permission)) return; //회원이지만, 권한이 없을경우

        if(args.length > 0) {
            children.forEach((child) -> {
                        CommandTrigger trigger = child.trigger;
                        if (trigger.checkTrigger(args[0])) {
                            String[] childArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, childArgs, 0, childArgs.length);
                            child.execute(channel, sender, childArgs);
                        }
                    }
            );
        }
    }
}
