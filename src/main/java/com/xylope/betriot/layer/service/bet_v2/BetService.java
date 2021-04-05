package com.xylope.betriot.layer.service.bet_v2;

import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;
import com.xylope.betriot.layer.dataaccess.apis.riot.OriannaMatchAPI;
import com.xylope.betriot.layer.domain.event.OnSecondEvent;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.controller.BetController;
import com.xylope.betriot.layer.service.bet_v2.model.BetProgress;
import com.xylope.betriot.layer.service.bet_v2.model.BetUserVO;
import com.xylope.betriot.layer.service.bet_v2.model.WinOrLose;
import com.xylope.betriot.layer.service.user.dao.BankUserDao;
import com.xylope.betriot.manager.TimeCounter;
import com.xylope.betriot.manager.TimeListenerAdapter;

public class BetService {
    private final BetController betController;
    private final OriannaMatchAPI matchAPI;
    private final BankUserDao userDao;
    private final TimeCounter timeCounter;

    public BetService(BetController betController, OriannaMatchAPI matchAPI, BankUserDao userDao, TimeCounter timeCounter) {
        this.betController = betController;
        this.matchAPI = matchAPI;
        this.userDao = userDao;
        this.timeCounter = timeCounter;
    }

    public void createNewBet(long discordId) {
        UserVO user = userDao.get(discordId);
        int betId = betController.createBet(user);

        new BetLifeCycle(betId, user, betController, matchAPI, timeCounter);
    }

    public void addParticipant(int betId, UserVO user, int money, WinOrLose betWhere) {
        betController.addParticipant(betId, new BetUserVO(user, money), betWhere);
    }

}

//단일 배팅에대해 1초마다 생명주기 확인 밑 변환 작업을 수행하는 TimeListener 구현클래스
class BetLifeCycle extends TimeListenerAdapter {
    private static final int CHECK_MATCH_DELAY = 2; //단위 : 초
    private static final int BET_PARTICIPATION_OPEN_TIME = 3; //단위 : 초
    private static final int CHECK_MATCH_END_CYCLE_DELAY = 60; //단위 : 초

    private final int betId;
    private final UserVO user;
    private final BetController betController;
    private final OriannaMatchAPI matchAPI;
    private final TimeCounter timeCounter;
    private int count = 0;

    public BetLifeCycle(int betId, UserVO user, BetController betController, OriannaMatchAPI matchAPI, TimeCounter timeCounter) {
        this.betId = betId;
        this.user = user;
        this.betController = betController;
        this.matchAPI = matchAPI;
        this.timeCounter = timeCounter;

        timeCounter.addTimeListener(this);

        betController.nextStep(betId);
        betController.reserveBet(betId); //처음 1회만 실행
    }

    @Override
    public void onTimeSecond(OnSecondEvent e) {
        count++;
        if (count == CHECK_MATCH_DELAY && betController.checkProgress(betId, BetProgress.BET_RESERVE)) {
            String summonerId = user.getRiotId();
            CurrentMatch currentMatch = matchAPI.getCurrentMatch(summonerId);
            boolean isMatchStart = currentMatch.exists();

            if (!isMatchStart) {
                betController.matchNotFound(betId);
                timeCounter.removeTimeListener(this);
                return;
            }

            betController.nextStep(betId); //MATCH_START
            betController.nextStep(betId); //BET_START
            betController.startBet(betId, currentMatch.getId());
            betController.nextStep(betId); //BET_PARTICIPATION_OPEN
        }

        if (betController.checkProgress(betId, BetProgress.BET_PARTICIPATION_OPEN)) {
            betController.openBetParticipation(betId);
            betController.nextStep(betId); //BET_PARTICIPATION_CLOSE
            count = 0;
            return;
        }

        if (betController.checkProgress(betId, BetProgress.BET_PARTICIPATION_CLOSE) && (count % BET_PARTICIPATION_OPEN_TIME == 0)) {
            betController.closeBetParticipation(betId);
            betController.nextStep(betId); //MATCH_END
            count = 0;
            return;
        }

        if (betController.checkProgress(betId, BetProgress.MATCH_END) && (count == CHECK_MATCH_END_CYCLE_DELAY)) {
            if (betController.checkMatchEnd(betId)) {
                betController.nextStep(betId); //BET_GIVE_REWARD
            }

            count = 0;
            return;
        }

        if (betController.checkProgress(betId, BetProgress.BET_GIVE_REWARD)) {
            Match match = matchAPI.getByMatchId(betController.getMatchId(betId));
            WinOrLose wol;
            if (matchAPI.isSummonerWin(match, user.getRiotId())) wol = WinOrLose.WIN;
            else wol = WinOrLose.LOSE;
            betController.giveRewardToWinners(betId, wol);
            betController.nextStep(betId); //BET_END;
            betController.endBet(betId);
            timeCounter.removeTimeListener(this);
        }
    }
}