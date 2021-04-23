package com.xylope.betriot.layer.domain.vo;

import com.xylope.betriot.exception.DataNotFoundException;
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
    @Getter @Setter
    private Permission permission;

    public UserVO(@NonNull long discordId, String riotId, int money) {
        this.discordId = discordId;
        this.riotId = riotId;
        this.money = money;
        this.permission = Permission.IRON;
    }

    @AllArgsConstructor
    public enum Permission {
        /*
        Iron : 처음 회원가입을 하였을시 부여받는 칭호
        Bronze :
        Silver :
        Gold :
        Platinum :
        Diamond :
        Master :
        GrandMaster :
        Challenger : 봇 관리자만 가지고있는 칭호
        Administrator : 봇 소유자만 가지고있는 칭호
         */
        IRON(0), BRONZE(1), SILVER(2), GOLD(3), PLATINUM(4), DIAMOND(5), MASTER(6), GRAND_MASTER(7), CHALLENGER(8), ADMINISTRATOR(9);

        @Getter
        private final int id;

        public static Permission getById(int id) {
            for(Permission permission : Permission.values()) {
                if(permission.getId() == id) return permission;
            }
            throw new DataNotFoundException("unknown permission id : " + id);
        }
    }
}
