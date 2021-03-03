package com.xylope.betriot.layer.service.user.register;

import com.xylope.betriot.exception.NotExistIDException;
import com.xylope.betriot.exception.WrongRegisterProgressException;

import java.util.HashSet;

public class UnRegisterUserSet extends HashSet<UnRegisterUser> {
    public boolean isMessageTerms(long termsMessageId) {
        boolean isMessageTerms = false;

        for(UnRegisterUser unRegisterUser : this) {
            if(unRegisterUser.getProgress().equals(RegisterProgress.CHECK_TERMS)) {
                try {
                    if (unRegisterUser.getTermsMessageId() == termsMessageId) {
                        isMessageTerms = true;
                    }
                } catch (WrongRegisterProgressException e) {
                    e.printStackTrace();
                }
            }
        }
        return isMessageTerms;
    }

    public UnRegisterUser getUserByTermsMessageId(long termsMessageId) throws NotExistIDException {
        if (isMessageTerms(termsMessageId)) {
            UnRegisterUser result = null;
            for(UnRegisterUser unRegisterUser : this) {
                if(unRegisterUser.getProgress().equals(RegisterProgress.CHECK_TERMS)) {
                    try {
                        if (unRegisterUser.getTermsMessageId() == termsMessageId) {
                            result = unRegisterUser;
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
        for(UnRegisterUser unRegisterUser : this)
            if(unRegisterUser.getDiscordId() == discordId)
                return true;
        return false;
    }

    public UnRegisterUser getUserByDiscordId(long discordId) {
        if(isUserExistByDiscordId(discordId)) {
            for (UnRegisterUser unRegisterUser : this)
                if (unRegisterUser.getDiscordId() == discordId)
                    return unRegisterUser;
        }
        throw new NotExistIDException("User with the discordId does not exist\n discordId : " + discordId);
    }
}
