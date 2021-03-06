package com.xylope.betriot.layer.service.bet.model;

import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BetUserVO {
    @Getter
    private final UserVO user;
    @Getter
    private final int money;
}
