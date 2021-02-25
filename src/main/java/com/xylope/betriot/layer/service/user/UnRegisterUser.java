package com.xylope.betriot.layer.service.user;

import com.xylope.betriot.exception.WrongRegisterProgressException;
import lombok.Getter;

public class UnRegisterUser {
    @Getter
    private RegisterProgress progress;
    private long termsMessageId;
    private boolean isTermsAccept;

    private <R> R doSomethingAtTerms(DoSomethingCallback<R> callback) throws WrongRegisterProgressException {
        if(checkProgress(RegisterProgress.TERMS))
            return callback.doSomething();
        throw new WrongRegisterProgressException(String.format("Wrong RegisterProgress \"%s\"", progress.toString()));
    }

    public long getTermsMessageId() throws WrongRegisterProgressException {
        return doSomethingAtTerms(()->termsMessageId);
    }

    public void setTermsMessageId(long termsMessageId) throws WrongRegisterProgressException {
        doSomethingAtTerms(()->this.termsMessageId = termsMessageId);
    }

    public boolean isTermsAccept() throws WrongRegisterProgressException {
        return doSomethingAtTerms(()->isTermsAccept);
    }

    public void setTermsAccept(boolean isTermsAccept) throws WrongRegisterProgressException {
        doSomethingAtTerms(()->this.isTermsAccept = isTermsAccept);
    }

    public boolean checkProgress(RegisterProgress progress) {
        return this.progress.equals(progress);
    }

    public void nextStep() {
        progress = progress.getNextStep();
    }

    private interface DoSomethingCallback<R> {
        R doSomething();
    }
}