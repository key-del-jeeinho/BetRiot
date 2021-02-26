package com.xylope.betriot.layer.service.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RegisterProgress {
    UNREGISTERED(0), CHECK_TERMS(1), RIOT_AUTHORIZE(2), REGISTERED(3);

    @Getter
    private final int id;

    public RegisterProgress getNextStep() {
        return values()[getId()+1];
    }
}
