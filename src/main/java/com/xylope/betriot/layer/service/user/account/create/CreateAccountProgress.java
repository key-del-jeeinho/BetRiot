package com.xylope.betriot.layer.service.user.account.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CreateAccountProgress {
    UNREGISTERED(0), CHECK_TERMS(1), RIOT_NAME(2), RIOT_AUTHORIZE(3), REGISTERED(4);

    @Getter
    private final int id;

    public CreateAccountProgress getNextStep() {
        if(id < values().length)
            return values()[getId()+1];
        throw new IndexOutOfBoundsException("register process \"" + this + "\" is last step!");
    }
    public CreateAccountProgress getPrevStep() {
        if(id > 0)
            return values()[getId()-1];
        throw new IndexOutOfBoundsException("register process \"" + this + "\" is first step!");
    }
}