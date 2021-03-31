package com.xylope.betriot.layer.service.bet_v2;

import com.merakianalytics.orianna.types.core.match.Match;
import com.xylope.betriot.exception.bet.MatchNotStartException;
import com.xylope.betriot.layer.dataaccess.apis.riot.OriannaMatchAPI;
import com.xylope.betriot.layer.domain.event.OnSecondEvent;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.controller.BetController;
import com.xylope.betriot.layer.service.bet_v2.handler.BetReaderExceptionHandler;
import com.xylope.betriot.layer.service.bet_v2.model.BetProgress;
import com.xylope.betriot.layer.service.bet_v2.model.WinOrLose;
import com.xylope.betriot.layer.service.bet_v2.reader.BetReader;
import com.xylope.betriot.layer.service.user.dao.BankUserDao;
import com.xylope.betriot.manager.TimeCounter;
import com.xylope.betriot.manager.TimeListenerAdapter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BetService {
    private final BetController betController;
    private final BetReader<String[]> betReader;
    private final BetReaderExceptionHandler<String[]> readerHandler;
    private final OriannaMatchAPI matchAPI;
    private final BankUserDao userDao;
    private final TimeCounter timeCounter;

    public void createNewBet(long discordId) {
        UserVO user = userDao.get(discordId);
        int betId = betController.createBet(user);

        BetLifeCycle betLifeCycle = new BetLifeCycle(betId, user, betController, matchAPI);
        timeCounter.addTimeListener(betLifeCycle);
    }


}

class BetLifeCycle extends TimeListenerAdapter {
    private static final int CHECK_MATCH_DELAY = 240; //단위 : 초
    private static final int CHECK_MATCH_END_CYCLE_DELAY = 300; //단위 : 초
    private static final int BET_PARTICIPATION_OPEN_TIME = 300; //단위 : 초

    private final int betId;
    private final UserVO user;
    private final BetController betController;
    private final OriannaMatchAPI matchAPI;

    private int count = 0;

    public BetLifeCycle(int betId, UserVO user, BetController betController, OriannaMatchAPI matchAPI) {
        this.betId = betId;
        this.user = user;
        this.betController = betController;
        this.matchAPI = matchAPI;

        betController.reserveBet(betId); //처음 1회만 실행
    }

    @Override
    public void onTimeSecond(OnSecondEvent e) {
        count++;
        if (count == CHECK_MATCH_DELAY && betController.checkProgress(betId, BetProgress.BET_RESERVE)) {
            String summonerId = user.getRiotId();
            boolean isMatchStart = matchAPI.getCurrentMatch(summonerId).exists();

            if (!isMatchStart)
                throw new MatchNotStartException("match isn't started!");

            betController.nextStep(betId); //MATCH_START
            betController.nextStep(betId); //BET_START
            betController.startBet(betId);
            betController.nextStep(betId); //BET_PARTICIPATION_OPEN
        }

        if (betController.checkProgress(betId, BetProgress.BET_PARTICIPATION_OPEN)) {
            betController.openBetParticipation(betId);
            betController.nextStep(betId); //BET_PARTICIPATION_CLOSE
            count = 0;
        }

        if (betController.checkProgress(betId, BetProgress.BET_PARTICIPATION_CLOSE) && count % BET_PARTICIPATION_OPEN_TIME == 0) {
            betController.closeBetParticipation(betId);
            betController.nextStep(betId); //MATCH_END
            count = 0;
        }

        if (betController.checkProgress(betId, BetProgress.MATCH_END) && count == CHECK_MATCH_END_CYCLE_DELAY) {
            if (betController.checkMatchEnd(betId))
                betController.nextStep(betId); //BET_GIVE_REWARD
            count = 0;
        }

        if (betController.checkProgress(betId, BetProgress.BET_GIVE_REWARD)) {
            Match match = matchAPI.getByMatchId(betController.getMatchId(betId));
            WinOrLose wol;
            if (matchAPI.isSummonerWin(match, user.getRiotId())) wol = WinOrLose.WIN;
            else wol = WinOrLose.LOSE;
            betController.giveRewardToWinners(betId, wol);
            betController.nextStep(betId); //BET_END;
            betController.endBet(betId);
        }
    }
}