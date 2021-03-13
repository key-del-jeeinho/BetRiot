package com.xylope.betriot.layer.service.user.account.create;

import com.xylope.betriot.exception.NotExistIDException;
import com.xylope.betriot.exception.WrongRegisterProgressException;

import java.util.HashSet;

public class BeforeCreateAccountUserSet extends HashSet<BeforeCreateAccountUser> {
    public boolean isMessageTerms(long termsMessageId) {
        boolean isMessageTerms = false;

        for(BeforeCreateAccountUser beforeCreateAccountUser : this) {
            if(beforeCreateAccountUser.getProgress().equals(CreateAccountProgress.CHECK_TERMS)) {
                try {
                    if (beforeCreateAccountUser.getTermsMessageId() == termsMessageId) {
                        isMessageTerms = true;
                    }
                } catch (WrongRegisterProgressException e) {
                    e.printStackTrace();
                }
            }
        }

        return isMessageTerms;
    }

    public boolean isMessageRiot(long messageIdLong) {
        boolean isMessageRiot = false;

        for(BeforeCreateAccountUser beforeCreateAccountUser : this) {
            if(beforeCreateAccountUser.getProgress().equals(CreateAccountProgress.RIOT_AUTHORIZE)) {
                try {
                    if(beforeCreateAccountUser.getRiotMessageId() == messageIdLong) {
                        isMessageRiot = true;
                    }
                } catch (WrongRegisterProgressException e) {
                    e.printStackTrace();
                }
            }
        }

        return isMessageRiot;
    }

    public BeforeCreateAccountUser getUserByTermsMessageId(long termsMessageId) throws NotExistIDException {
        if (isMessageTerms(termsMessageId)) {
            BeforeCreateAccountUser result = null;
            for(BeforeCreateAccountUser beforeCreateAccountUser : this) {
                if(beforeCreateAccountUser.getProgress().equals(CreateAccountProgress.CHECK_TERMS)) {
                    try {
                        if (beforeCreateAccountUser.getTermsMessageId() == termsMessageId) {
                            result = beforeCreateAccountUser;
                        }
                    } catch (WrongRegisterProgressException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        } else throw new NotExistIDException("User with the TermsMessageId does not exist\n termsMessageId : " + termsMessageId);
    }

    public boolean isUserExistByDiscordId(long discordId) {
        for(BeforeCreateAccountUser beforeCreateAccountUser : this)
            if(beforeCreateAccountUser.getDiscordId() == discordId)
                return true;
        return false;
    }

    public BeforeCreateAccountUser getUserByDiscordId(long discordId) {
        if(isUserExistByDiscordId(discordId)) {
            for (BeforeCreateAccountUser beforeCreateAccountUser : this)
                if (beforeCreateAccountUser.getDiscordId() == discordId)
                    return beforeCreateAccountUser;
        }
        throw new NotExistIDException("User with the discordId does not exist\n discordId : " + discordId);
    }
}
