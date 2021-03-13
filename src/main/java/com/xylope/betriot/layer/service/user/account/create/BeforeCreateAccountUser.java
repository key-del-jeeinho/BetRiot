package com.xylope.betriot.layer.service.user.account.create;

import com.xylope.betriot.exception.WrongRegisterProgressException;
import lombok.Getter;
import lombok.NonNull;

public class BeforeCreateAccountUser {
    @Getter
    private CreateAccountProgress progress;
    @Getter @NonNull
    private final long discordId;
    private long termsMessageId;
    private boolean isTermsAgree;
    private long riotMessageId;
    private int authorizeIconId;
    private String riotId;

    public BeforeCreateAccountUser(long discordId) {
        this.discordId = discordId;
        this.progress = CreateAccountProgress.UNREGISTERED;
    }

    private <R> R doSomethingAt(DoSomethingCallback<R> callback, CreateAccountProgress... progresses) throws WrongRegisterProgressException {
        for(CreateAccountProgress progress : progresses)
            if(checkProgress(progress))
                return callback.doSomething();
        throw new WrongRegisterProgressException(String.format("Wrong RegisterProgress \"%s\"", progress.toString()));
    }

    private <R> R doSomethingAt(DoSomethingCallback<R> callback, CreateAccountProgress progress) throws WrongRegisterProgressException {
        if(checkProgress(progress))
            return callback.doSomething();
        throw new WrongRegisterProgressException(String.format("Wrong RegisterProgress \"%s\"", progress.toString()));
    }

    public long getTermsMessageId() throws WrongRegisterProgressException {
        return doSomethingAt(()->termsMessageId, CreateAccountProgress.CHECK_TERMS);
    }

    public void setTermsMessageId(long termsMessageId) throws WrongRegisterProgressException {
        doSomethingAt(()->this.termsMessageId = termsMessageId, CreateAccountProgress.CHECK_TERMS);
    }

    public boolean isTermsAgree() throws WrongRegisterProgressException {
        return doSomethingAt(()->isTermsAgree, CreateAccountProgress.CHECK_TERMS);
    }

    public void setTermsAgree(boolean isTermsAgree) throws WrongRegisterProgressException {
        doSomethingAt(()->this.isTermsAgree = isTermsAgree, CreateAccountProgress.CHECK_TERMS);
    }

    public String getRiotId() throws WrongRegisterProgressException {
        return doSomethingAt(()->this.riotId, CreateAccountProgress.RIOT_AUTHORIZE, CreateAccountProgress.REGISTERED);
    }

    public void setRiotId(String riotId) throws WrongRegisterProgressException {
        doSomethingAt(()->this.riotId = riotId, CreateAccountProgress.RIOT_NAME);
    }

    public int getAuthorizeIconId() throws WrongRegisterProgressException {
        return doSomethingAt(()->this.authorizeIconId, CreateAccountProgress.RIOT_AUTHORIZE);
    }

    public void setAuthorizeIconId(int authorizeIconId) throws WrongRegisterProgressException {
        doSomethingAt(()->this.authorizeIconId = authorizeIconId, CreateAccountProgress.RIOT_NAME);
    }

    public long getRiotMessageId() throws WrongRegisterProgressException {
        return doSomethingAt(()->this.riotMessageId, CreateAccountProgress.RIOT_AUTHORIZE);
    }

    public void setRiotMessageId(long riotMessageId) throws WrongRegisterProgressException {
        doSomethingAt(()->this.riotMessageId = riotMessageId, CreateAccountProgress.RIOT_AUTHORIZE);
    }

    public boolean checkProgress(CreateAccountProgress progress) {
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