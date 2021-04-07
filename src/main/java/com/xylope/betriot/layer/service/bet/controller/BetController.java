package com.xylope.betriot.layer.service.bet.controller;

import com.xylope.betriot.exception.bet.BetAlreadyCreatedException;
import com.xylope.betriot.exception.bet.WrongBetProgressException;
import com.xylope.betriot.layer.dataaccess.apis.discord.JdaAPI;
import com.xylope.betriot.layer.dataaccess.apis.riot.OriannaMatchAPI;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet.model.*;
import com.xylope.betriot.layer.service.bet.view.BetView;
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
    private final JdaAPI jdaAPI;

    public boolean checkMatchEnd(int betId) {
        BetDto bet = model.getBet(betId);
        long currentMatchId = matchAPI.getCurrentMatch(bet.getPublisher().getRiotId()).getId();

        boolean isMatchEnd = false;

        if(currentMatchId == 0) isMatchEnd = true; //현재 진행중인 매치가 없을경우 ID로 0을 반환한다
        else if(currentMatchId != bet.getMatchId()) isMatchEnd = true;

        return isMatchEnd;
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

    public void addParticipant(int betId, BetUserVO betUser, WinOrLose betWhere) {
        if(!checkProgress(betId, BetProgress.BET_PARTICIPATION_CLOSE)) { //프로그레스는 라이프사이클 특성상 하나씩 밀린다
            view.sendParticipationAlreadyCloseView(model.getBet(betId));
            return;
        }
        if(model.isParticipationExist(betId, betUser.getUser().getDiscordId())) {
            view.sendDuplicateParticipationView(model.getBet(betId), betUser.getUser());
            return;
        }

        UserVO user = betUser.getUser();
        int betMoney = betUser.getMoney();

        if(betMoney <= user.getMoney()) {
            bankUserDao.addMoney(betUser.getUser(), -1 * betMoney);
            model.addParticipant(betId, betUser, betWhere);

            view.sendUserParticipationBettingView(model.getBet(betId), betUser);
        } else
            view.sendNotEnoughMoneyToParticipationView(model.getBet(betId), user);
    }

    //LifeCycle
    public int createBet(UserVO user, long relayChannel) {
        if (model.isBetExist(user.getDiscordId())) { //이미 해당 유저 명의로 배팅이 예약되어있거나 개설된경우
            throw new BetAlreadyCreatedException("bet is Already Created");
        }

        Bet bet = new Bet(user, jdaAPI.getTextChannelById(relayChannel));
        model.addBet(bet);

        return bet.getId();
    }

    public void reserveBet(int betId) {
        BetDto bet = model.getBet(betId);

        if(!(bet.getProgress() == BetProgress.BET_RESERVE))
            throw new WrongBetProgressException("progress isn't BET_RESERVE", BetProgress.BET_RESERVE);

        view.sendReserveBetView(bet.getPublisher());
    }

    public void startBet(int betId, long matchId) {
        BetDto dto = model.getBet(betId);
        if(!dto.getProgress().equals(BetProgress.BET_START))
            throw new WrongBetProgressException("progress isn't BET_START", BetProgress.BET_START);

        model.setMatchId(betId, matchId);
        view.sendStartBetView(dto);
    }

    public void openBetParticipation(int betId) {
        BetDto dto = model.getBet(betId);
        if(!dto.getProgress().equals(BetProgress.BET_PARTICIPATION_OPEN))
            throw new WrongBetProgressException("progress isn't BET_PARTICIPATION_OPEN", BetProgress.BET_PARTICIPATION_OPEN);

        view.sendOpenBetParticipationView(dto);
    }

    public void closeBetParticipation(int betId) {
        BetDto dto = model.getBet(betId);
        if(!dto.getProgress().equals(BetProgress.BET_PARTICIPATION_CLOSE))
            throw new WrongBetProgressException("progress isn't BET_PARTICIPATION_CLOSE", BetProgress.BET_PARTICIPATION_CLOSE);

        view.sendCloseBetParticipationView(dto);
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
        view.sendEndBetView(betId, dto);
    }

    public void matchNotFound(int betId) {
        view.sendMatchNotFoundView(model.getBet(betId));
    }

    public void matchExceedTimeLimit(int betId) {
        view.sendMatchExceedTimeLimitView(model.getBet(betId));
    }
}
