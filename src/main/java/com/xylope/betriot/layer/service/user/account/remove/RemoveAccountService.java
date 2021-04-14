package com.xylope.betriot.layer.service.user.account.remove;

import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.logic.discord.SpecialEmote;
import com.xylope.betriot.layer.logic.discord.listener.PrivateMessageReactionAddListener;
import com.xylope.betriot.layer.logic.discord.message.PrivateMessageSenderWithCallback;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;

import java.util.ArrayList;
import java.util.List;

public class RemoveAccountService {
    private final List<Message> removeMessageQueue;
    @Setter
    private PrivateMessageSenderWithCallback<String> privateMessageSender;
    @Setter
    private UserDao dao;

    public RemoveAccountService(PrivateMessageReactionAddListener privateMessageReactionAddListener) {
        removeMessageQueue = new ArrayList<>();
        privateMessageReactionAddListener.addListener(this::checkRemove);
    }

    public void removeUserAccount(User user) {
        PrivateChannel pc = user.openPrivateChannel().complete();
        privateMessageSender.sendMessage(pc,
                String.format("회원탈퇴를 진행하시려면 %s 이모지를 눌러주세요 (회원 탈퇴시 회원님의 정보가 영구적으로 삭제처리됩니다)", SpecialEmote.ACCOUNT_REMOVE_ACCEPT.getEmote()),
                removeMessageQueue::add
        );
        pc.close().queue();
    }

    public void checkRemove(PrivateMessageReactionAddEvent event) {
        for (Message message : removeMessageQueue) {
            if(event.getMessageIdLong() == message.getIdLong()) {
                if (event.getReactionEmote().getName().equals(SpecialEmote.ACCOUNT_REMOVE_ACCEPT.getEmote())) {
                    doRemove(event.getUser());
                    privateMessageSender.sendMessage(event.getChannel(), "회원탈퇴가 성공적으로 완료되었습니다!");
                }
            }
        }
    }

    private void doRemove(User user) {
        dao.remove(user.getIdLong());

    }
}
