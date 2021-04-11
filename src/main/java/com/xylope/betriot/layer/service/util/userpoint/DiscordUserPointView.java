package com.xylope.betriot.layer.service.util.userpoint;

import com.xylope.betriot.layer.dataaccess.apis.discord.JdaAPI;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.logic.discord.message.PrivateMessageSenderImpl;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.User;

@AllArgsConstructor
public class DiscordUserPointView implements UserPointView{
    private static final String MESSAGE_FORMAT = "%s님의 소지 포인트 : %s원";

    private final PrivateMessageSenderImpl privateMessageSender;
    private final JdaAPI jdaAPI;

    @Override
    public void sendCheckUserPointView(UserVO user) {
        User discordUser = jdaAPI.getUserById(user.getDiscordId());
        privateMessageSender.sendMessage(discordUser.openPrivateChannel().complete(), String.format(MESSAGE_FORMAT, discordUser.getAsMention(), user.getMoney()));
    }
}
