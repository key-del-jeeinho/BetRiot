package com.xylope.betriot.layer.service.user;

import lombok.Getter;

import java.util.List;

public class UserRegisterService {

    @Getter
    private List<UnRegisterUser> unRegisterUserQueue;

    public void addUnRegisterUser(long discordId) {
        this.unRegisterUserQueue.add(new UnRegisterUser(discordId));
    }

    public void registerUser(UnRegisterUser user) {

    }
}
