package com.xylope.betriot.layer.service.bet_v2.model;

import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
public class BetDto {
    @Getter
    private final int id;
    @Getter
    private final long matchId;
    @Getter
    private final BetProgress progress;
    @Getter
    private final UserVO publisher;
    @Getter
    private final Map<BetUserVO, WinOrLose> participants;
}
