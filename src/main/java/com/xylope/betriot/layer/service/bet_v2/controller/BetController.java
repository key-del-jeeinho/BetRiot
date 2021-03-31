package com.xylope.betriot.layer.service.bet_v2.controller;

import com.xylope.betriot.exception.bet.WrongBetProgressException;
import com.xylope.betriot.layer.dataaccess.apis.riot.OriannaMatchAPI;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.model.*;
import com.xylope.betriot.layer.service.bet_v2.view.BetView;
import com.xylope.betriot.layer.service.user.dao.BankUserDao;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class BetController {
    public static final float DIVIDEND_YIELD = 2F;

    private final BetQueue model;
    private final BetView view;
    private final OriannaMatchAPI matchAPI;
    private final BankUserDao bankUserDao;

    public boolean checkMatchEnd(int betId) {
        long matchId = model.getBet(betId).getMatchId();
        return matchAPI.getByMatchId(matchId).exists();
    }

    public void nextStep(int betId) {
        model.nextStep(betId);
    }

    public boolean checkProgress(int betId, BetProgress progress) {
        return model.getBet(betId).getProgress() == progress;
    }

    public long getMatchId(int betId) {
        return model.getBet(betId).getMatchId();
    }

    //LifeCycle
    public int createBet(UserVO user) {
        if (model.isBetExist(user.getDiscordId())) { //이미 해당 유저 명의로 배팅이 예약되어있거나 개설된경우
            throw new BetAlreadyCreatedException("bet is Already Created");
        }

        Bet bet = new Bet(user);
        model.addBet(bet);

        return bet.getId();
    }

    public void reserveBet(int betId) {
        BetDto bet = model.getBet(betId);

        if(!(bet.getProgress() == BetProgress.BET_RESERVE))
            throw new WrongBetProgressException("progress isn't BET_RESERVE", BetProgress.BET_RESERVE);

        view.sendReserveBetView(bet.getPublisher());
    }

    public void startBet(int betId) {
        BetDto dto = model.getBet(betId);
        if(!dto.getProgress().equals(BetProgress.BET_START))
            throw new WrongBetProgressException("progress isn't BET_START", BetProgress.BET_START);

        view.sendStartBetView(dto);
    }

    public void openBetParticipation(int betId) {
        BetDto dto = model.getBet(betId);
        if(!dto.getProgress().equals(BetProgress.BET_PARTICIPATION_OPEN))
            throw new WrongBetProgressException("progress isn't BET_PARTICIPATION_OPEN", BetProgress.BET_PARTICIPATION_OPEN);

        view.sendOpenBetParticipationView();
    }

    public void closeBetParticipation(int betId) {
        BetDto dto = model.getBet(betId);
        if(!dto.getProgress().equals(BetProgress.BET_PARTICIPATION_CLOSE))
            throw new WrongBetProgressException("progress isn't BET_PARTICIPATION_CLOSE", BetProgress.BET_PARTICIPATION_CLOSE);

        view.sendCloseBetParticipationView();
    }

    public void giveRewardToWinners(int betId, WinOrLose isPublisherWinOrLose) {
        BetDto dto = model.getBet(betId);
        if(!dto.getProgress().equals(BetProgress.BET_GIVE_REWARD))
            throw new WrongBetProgressException("progress isn't BET_GIVE_REWARD", BetProgress.BET_GIVE_REWARD);

        Set<BetUserVO> winners = model.getParticipants(betId, isPublisherWinOrLose);
        winners.forEach(betUserVO -> bankUserDao.addMoney(betUserVO.getUser(), (int) (betUserVO.getMoney() * DIVIDEND_YIELD)));

        view.sendGiveRewardToWinnersView(dto, isPublisherWinOrLose);
    }

    public void endBet(int betId) {
        BetDto dto = model.getBet(betId);
        if(!dto.getProgress().equals(BetProgress.BET_END))
            throw new WrongBetProgressException("progress isn't BET_END", BetProgress.BET_END);

        model.remove(betId);
        view.sendEndBetView(betId);
    }
}
