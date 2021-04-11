package com.xylope.betriot.layer.domain.vo;

import com.xylope.betriot.layer.service.bet.model.BetUserVO;
import com.xylope.betriot.layer.service.bet.model.WinOrLose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class BetParticipantVO {
    @Getter @Setter
    long participantId;
    @Getter @Setter
    int betMoney;
    @Getter @Setter
    WinOrLose betWhere;

    public static BetParticipantVO convertBetUserVOToThis(BetUserVO betUser, WinOrLose wol) {
        return new BetParticipantVO(
                betUser.getUser().getDiscordId(),
                betUser.getMoney(),
                wol
        );
    }
}
