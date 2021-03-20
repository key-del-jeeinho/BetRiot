package com.xylope.betriot.layer.service.bet;

import com.xylope.betriot.layer.domain.vo.UserVO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BetUser {
    public static final int DEFAULT_SECOND_COUNT = 180; //3분
    private final UserVO user;
    private int secondCount; //0이될 경우 해당 배팅이 유효(매치성사)한지 검사한다
    private final BetService.Action action; //BetSummoner 의 매치 확인 쿨다운이 0일때 실행되는 콜백

    public void countdown() {
        countdown(1);
    }
    public void countdown(int count) {
        secondCount -= count;
        if(count <= 0)
            action.doSomething(user);

    }
}
