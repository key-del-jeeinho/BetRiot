package com.xylope.betriot.layer.service.user.register;

import com.xylope.betriot.exception.WrongRegisterProgressException;
import lombok.Getter;
import lombok.NonNull;

public class UnRegisterUser {
    @Getter
    private RegisterProgress progress;
    @Getter @NonNull
    private final long discordId;
    private long termsMessageId;
    private boolean isTermsAgree;
    private long riotId;

    public UnRegisterUser(long discordId) {
        this.discordId = discordId;
        this.progress = RegisterProgress.UNREGISTERED;
    }

    private <R> R doSomethingAt(DoSomethingCallback<R> callback, RegisterProgress progress) throws WrongRegisterProgressException {
        if(checkProgress(progress))
            return callback.doSomething();
        throw new WrongRegisterProgressException(String.format("Wrong RegisterProgress \"%s\"", progress.toString()));
    }

    public long getTermsMessageId() throws WrongRegisterProgressException {
        return doSomethingAt(()->termsMessageId, RegisterProgress.CHECK_TERMS);
    }

    public void setTermsMessageId(long termsMessageId) throws WrongRegisterProgressException {
        doSomethingAt(()->this.termsMessageId = termsMessageId, RegisterProgress.CHECK_TERMS);
    }

    public boolean isTermsAgree() throws WrongRegisterProgressException {
        return doSomethingAt(()->isTermsAgree, RegisterProgress.CHECK_TERMS);
    }

    public void setTermsAgree(boolean isTermsAgree) throws WrongRegisterProgressException {
        doSomethingAt(()->this.isTermsAgree = isTermsAgree, RegisterProgress.CHECK_TERMS);
    }

    public long getRiotId() throws WrongRegisterProgressException {
        return doSomethingAt(()->this.riotId, RegisterProgress.RIOT_AUTHORIZE);
    }

    public void setRiotId(long riotId) throws WrongRegisterProgressException {
        doSomethingAt(()->this.riotId = riotId, RegisterProgress.RIOT_AUTHORIZE);
    }

    public boolean checkProgress(RegisterProgress progress) {
        return this.progress.equals(progress);
    }

    public void nextStep() {
        progress = progress.getNextStep();
    }

    @FunctionalInterface
    private interface DoSomethingCallback<R> {
        R doSomething();
    }
}