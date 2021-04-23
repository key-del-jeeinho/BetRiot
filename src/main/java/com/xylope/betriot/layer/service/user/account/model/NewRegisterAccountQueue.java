package com.xylope.betriot.layer.service.user.account.model;

import com.xylope.betriot.exception.DataNotFoundException;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.logic.discord.listener.EventRepeater;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;

import java.util.ArrayList;
import java.util.List;

public class NewRegisterAccountQueue {
    private final List<NewRegisterAccount> accounts;
    private final EventRepeater<PrivateMessageReactionAddEvent> privateMessageReactionAddEventRepeater;
    private final EventRepeater<PrivateMessageReceivedEvent> privateMessageReceivedEventRepeater;

    public NewRegisterAccountQueue(EventRepeater<PrivateMessageReactionAddEvent> privateMessageReactionAddEventRepeater,
                                   EventRepeater<PrivateMessageReceivedEvent> privateMessageReceivedEventRepeater) {
        this.accounts = new ArrayList<>();
        this.privateMessageReactionAddEventRepeater = privateMessageReactionAddEventRepeater;
        this.privateMessageReceivedEventRepeater = privateMessageReceivedEventRepeater;

        this.privateMessageReactionAddEventRepeater.addListener(this::onPrivateMessageReactionAddEvent);
        this.privateMessageReceivedEventRepeater.addListener(this::onPrivateMessageReceivedEvent);
    }

    public void addAccount(NewRegisterAccount account) {
        accounts.add(account);
    }

    public NewRegisterAccountDto getAccount(long accountId) {
        return getById(accountId).convertToDto();
    }

    private NewRegisterAccount getById(long accountId) {
        for (NewRegisterAccount account : accounts) {
            if(account.getDiscordId() == accountId)
                return account;
        }
        throw new DataNotFoundException("unknown account id : " + accountId);
    }


    public boolean checkIsPolicyChecked(long accountId) {
        return getById(accountId).checkIsPolicyChecked();
    }

    public boolean getIsPolicyAccept(long accountId) {
        return getById(accountId).getIsPolicyAccept();
    }

    public void setPolicyMessageId(long accountId, long policyMessageId) {
        getById(accountId).setPolicyMessageId(policyMessageId);
    }

    public void setRiotNameAuthorizeMessageId(long accountId, long riotAuthorizeMessageId) {
        getById(accountId).setRiotNameAuthorizeMessageId(riotAuthorizeMessageId);
    }

    public void setRiotAccountAuthorizeMessageId(long accountId, long riotAuthorizeMessageId) {
        getById(accountId).setRiotAccountAuthorizeMessageId(riotAuthorizeMessageId);
    }

    public void nextStep(long accountId) {
        getById(accountId).nextStep();
    }

    public boolean checkProgress(long accountId, NewRegisterAccountProgress progress) {
        return getById(accountId).checkProgress(progress);
    }

    public boolean checkAuthorizeRiotAccount(long accountId) {
        return getById(accountId).checkIsRiotAuthorized();
    }

    public void onPrivateMessageReactionAddEvent(PrivateMessageReactionAddEvent event) {
        long discordId = event.getUserIdLong();
        long messageId = event.getMessageIdLong();
        String emote = event.getReactionEmote().getName();

        accounts.forEach(
                account -> {
                    if(account.getDiscordId() == discordId) {
                        if (messageId == account.getPolicyMessageId()) {
                            account.checkIsPolicyChecked(emote);
                        }
                        if (messageId == account.getRiotAccountAuthorizeMessageId()) {
                            account.authorizeRiotAccount(emote);
                        }
                    }
                }
        );
    }

    private void onPrivateMessageReceivedEvent(PrivateMessageReceivedEvent event) {
        long discordId = event.getAuthor().getIdLong();
        accounts.forEach(
                account -> {
                    if(account.getDiscordId() == discordId) {
                        if (account.checkProgress(NewRegisterAccountProgress.RIOT_AUTHORIZE_NAME)) {
                            account.setRiotName(event.getMessage().getContentRaw());
                        }
                    }
                }
        );
    }

    public void close(long accountId) {
        accounts.remove(getById(accountId));
    }

    public void setAuthorizeIconId(long accountId, int authorizeIconId) {
        getById(accountId).setAuthorizeIconId(authorizeIconId);
    }

    public void removeAccount(long discordId, UserDao dao) {
        dao.remove(discordId);
    }
}


