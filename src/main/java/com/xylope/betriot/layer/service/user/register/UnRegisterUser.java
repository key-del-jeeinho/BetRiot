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
    private int authorizeIconId;
    private String riotId;

    public UnRegisterUser(long discordId) {
        this.discordId = discordId;
        this.progress = RegisterProgress.UNREGISTERED;
    }

    private <R> R doSomethingAt(DoSomethingCallback<R> callback, RegisterProgress... progresses) throws WrongRegisterProgressException {
        for(RegisterProgress progress : progresses)
            if(checkProgress(progress))
                return callback.doSomething();
        throw new WrongRegisterProgressException(String.format("Wrong RegisterProgress \"%s\"", progress.toString()));
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

    public String getRiotId() throws WrongRegisterProgressException {
        return doSomethingAt(()->this.riotId, RegisterProgress.RIOT_AUTHORIZE, RegisterProgress.REGISTERED);
    }

    public void setRiotId(String riotId) throws WrongRegisterProgressException {
        doSomethingAt(()->this.riotId = riotId, RegisterProgress.RIOT_NAME);
    }

    public int getAuthorizeIconId() throws WrongRegisterProgressException {
        return doSomethingAt(()->this.authorizeIconId, RegisterProgress.RIOT_AUTHORIZE);
    }

    public void setAuthorizeIconId(int authorizeIconId) throws WrongRegisterProgressException {
        doSomethingAt(()->this.authorizeIconId = authorizeIconId, RegisterProgress.RIOT_NAME);
    }

    public boolean checkProgress(RegisterProgress progress) {
        return this.progress.equals(progress);
    }

    public void nextStep() {
        progress = progress.getNextStep();
    }
    public void prevStep() {
        progress = progress.getPrevStep();
    }
    public void cancelStep() throws WrongRegisterProgressException {
        switch (progress) {
            case UNREGISTERED:
                throw new WrongRegisterProgressException("first progress isn't cancelable");
            case CHECK_TERMS:
                termsMessageId = 0;
                isTermsAgree = false;
                prevStep();
                break;
            case RIOT_NAME:
            case RIOT_AUTHORIZE:
                authorizeIconId = 0;
                riotId = null;
                prevStep();
                break;
            case REGISTERED:
                throw new WrongRegisterProgressException("user's progress already complete! not cancelable");
        }
    }

    @FunctionalInterface
    private interface DoSomethingCallback<R> {
        R doSomething();
    }
}