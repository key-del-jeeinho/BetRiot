package com.xylope.betriot.layer.service.bet_v2;

import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.model.Bet;
import com.xylope.betriot.layer.service.bet_v2.model.BetQueue;

public class BetController {
    private BetQueue betQueue;
    private final BetView view;

    public BetController(BetQueue betQueue, BetView view) {
        this.betQueue = betQueue;
        this.view = view;
    }

    public void reserveBet(UserVO user) {
        Bet bet = new Bet(user);
        betQueue.addBet(bet);
    }

    public void startBet(int betId) {

    }

    public void openBetParticipation(int betId) {

    }

    public void closeBetParticipation(int betId) {

    }

    public boolean checkMatchEnd(int betId) {
        return false; //TODO Dummy 소스 제거
    }

    public void giveRewardToWinners(int betId) {

    }

    public void endBet(int betId) {
        betQueue.remove(betId);
    }
}
