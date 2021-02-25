package com.xylope.betriot.layer.service.user;

import com.xylope.betriot.exception.WrongRegisterProgressException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UnRegisterUser {
    @Getter
    private RegisterProgress progress;
    @Getter @NonNull
    private final long discordId;
    private long termsMessageId;
    private boolean isTermsAccept;
    private String riotName;

    private <R> R doSomethingAt(DoSomethingCallback<R> callback, RegisterProgress progress) throws WrongRegisterProgressException {
        if(checkProgress(progress))
            return callback.doSomething();
        throw new WrongRegisterProgressException(String.format("Wrong RegisterProgress \"%s\"", progress.toString()));
    }

    public long getTermsMessageId() throws WrongRegisterProgressException {
        return doSomethingAt(()->termsMessageId, RegisterProgress.TERMS);
    }

    public void setTermsMessageId(long termsMessageId) throws WrongRegisterProgressException {
        doSomethingAt(()->this.termsMessageId = termsMessageId, RegisterProgress.TERMS);
    }

    public boolean isTermsAccept() throws WrongRegisterProgressException {
        return doSomethingAt(()->isTermsAccept, RegisterProgress.TERMS);
    }

    public void setTermsAccept(boolean isTermsAccept) throws WrongRegisterProgressException {
        doSomethingAt(()->this.isTermsAccept = isTermsAccept, RegisterProgress.TERMS);
    }

    public String getRiotName() throws WrongRegisterProgressException {
        return doSomethingAt(()->this.riotName, RegisterProgress.RIOT_AUTHORIZE);
    }

    public void setRiotName(String riotName) throws WrongRegisterProgressException {
        doSomethingAt(()->this.riotName = riotName, RegisterProgress.RIOT_AUTHORIZE);
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