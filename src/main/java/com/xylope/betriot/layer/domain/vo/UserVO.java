package com.xylope.betriot.layer.domain.vo;

import com.xylope.betriot.exception.WrongRegisterProgressException;
import com.xylope.betriot.layer.service.user.register.UnRegisterUser;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class UserVO {
    public static final int START_MONEY = 10000;
    @Getter @NonNull
    private final long discordId;
    @Getter @Setter
    private String riotId;
    @Getter @Setter
    private int money;

    public UserVO(UnRegisterUser unRegisterUser) {
        this.discordId = unRegisterUser.getDiscordId();
        try {
            this.riotId = unRegisterUser.getRiotId();
        } catch (WrongRegisterProgressException e) {
            e.printStackTrace();
        }
        this.money = START_MONEY;
    }
}
