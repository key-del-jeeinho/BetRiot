package com.xylope.betriot.layer.service.bet;

import com.merakianalytics.orianna.Orianna;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import com.xylope.betriot.layer.domain.vo.UserVO;

import java.util.List;

public class BetService {
    //3분내로 게임시작
    //틱 이벤트 사용해서 3분 재기
    List<BetUser> betUserQueue;

    public BetService() {
        new Action() {
            @Override
            public void doSomething(UserVO user) {
                Orianna.currentMatchForSummoner(Orianna.summonerWithId(user.getRiotId()).get()).get();
            }
        };
    }

    public void countdown() {
        betUserQueue.forEach(BetUser::countdown);
    }

    @FunctionalInterface
    public interface Action {
        void doSomething(UserVO user);
    }
}
