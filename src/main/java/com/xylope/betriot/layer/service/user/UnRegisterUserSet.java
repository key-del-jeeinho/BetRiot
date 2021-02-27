package com.xylope.betriot.layer.service.user;

import com.xylope.betriot.exception.NotExistIDException;
import com.xylope.betriot.exception.WrongRegisterProgressException;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class UnRegisterUserSet extends HashSet<UnRegisterUser> {
    public boolean isMessageTerms(long termsMessageId) {
        AtomicBoolean isMessageTerms = new AtomicBoolean(false);

        forEach(unRegisterUser -> {
            if(unRegisterUser.getProgress().equals(RegisterProgress.CHECK_TERMS)) {
                try {
                    if (unRegisterUser.getTermsMessageId() == termsMessageId) {
                        isMessageTerms.set(true);
                    }
                } catch (WrongRegisterProgressException e) {
                    e.printStackTrace();
                }
            }
        });
        return isMessageTerms.get();
    }

    public UnRegisterUser getUserByTermsMessageId(long termsMessageId) throws NotExistIDException {
        if (isMessageTerms(termsMessageId)) {
            AtomicReference<UnRegisterUser> unRegisterUserAtomic = new AtomicReference<>();
            forEach(unRegisterUser -> {
                if(unRegisterUser.getProgress().equals(RegisterProgress.CHECK_TERMS)) {
                    try {
                        if (unRegisterUser.getTermsMessageId() == termsMessageId) {
                            unRegisterUserAtomic.set(unRegisterUser);
                        }
                    } catch (WrongRegisterProgressException e) {
                        e.printStackTrace();
                    }
                }
            });
            return unRegisterUserAtomic.get();
        } else throw new NotExistIDException("User with the TermsMessageId does not exist\n termsMessageId : " + termsMessageId);
    }
}
