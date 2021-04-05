package com.xylope.betriot.layer.service.bet_v2.model;

import com.xylope.betriot.exception.AlreadyInitializeValueException;
import com.xylope.betriot.exception.bet.UnknownBetIdException;
import com.xylope.betriot.layer.domain.vo.UserVO;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class BetQueue {
    private final List<Bet> bets;

    public BetQueue() {
        bets = new ArrayList<>();
    }

    public void nextStep(int betId) {
        getById(betId).nextStep();
    }

    public void addBet(Bet bet) {
        bets.add(bet);
    }

    public void addParticipant(int betId, BetUserVO user, WinOrLose wal) {
        getById(betId).addParticipant(user, wal);
    }

    public void setMatchId(int betId, long matchId) {
        Bet bet = getById(betId);

        if(bet.getMatchId() != -1L)
            throw new AlreadyInitializeValueException("The match ID for this bet has already been initialize");

        bet.setMatchId(matchId);
        System.out.println(matchId + "로 설정되었습니다\n" + "BetQueue 에서 실행됨");
    }

    public Set<BetUserVO> getParticipants(int betId) {
        return getById(betId).getParticipants().keySet();
    }

    public Set<BetUserVO> getParticipants(int betId, WinOrLose sortMethod) {
        return getById(betId).getParticipants(sortMethod);
    }

    public BetDto getBet(int betId) {
        return getById(betId).convertToDto();
    }

    private @Nonnull Bet getById(int betId) {
        for(Bet bet : bets)
            if(bet.getId() == betId)
                return bet;
        throw new UnknownBetIdException("unknown bet Id : " + betId);
    }

    public boolean isBetExist(long discordId) {
        AtomicBoolean betExist = new AtomicBoolean(false);
        bets.forEach(bet -> {
            if (discordId == bet.getPublisher().getDiscordId()) {
                betExist.set(true);
            }
        });

        return false;
    }

    public void remove(int betId) {
        Bet bet = getById(betId);
        bet.close();
    }
}
