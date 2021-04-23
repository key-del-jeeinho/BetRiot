package com.xylope.betriot.layer.service.user.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NewRegisterAccountDto {
    @Getter
    private final NewRegisterAccountProgress progress;
    @Getter
    private final long discordId;
    @Getter
    private final String riotName;
    @Getter
    private final long policyMessageId;
    @Getter
    private final long riotNameAuthorizeMessageId;
    @Getter
    private final long riotAccountAuthorizeMessageId;
    @Getter
    private final int authorizeIconId;
}
