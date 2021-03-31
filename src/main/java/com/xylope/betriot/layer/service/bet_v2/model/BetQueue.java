package com.xylope.betriot.layer.service.bet_v2.model;

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

    public void addBet(Bet bet) {
        bets.add(bet);
    }

    public void nextStep(int betId) {
        getById(betId).nextStep();
    }

    public void addParticipant(int betId, BetUserVO user, WinOrLose wal) {
        getById(betId).addParticipant(user, wal);
    }

    public Set<BetUserVO> getParticipants(int betId) {
        return getById(betId).getParticipants().keySet();
    }

    public Set<BetUserVO> getParticipants(int betId, WinOrLose sortMethod) {
        return getById(betId).getParticipants(sortMethod);
    }

    public void remove(int betId) {
        Bet bet = getById(betId);
        bet.close();
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

    public BetDto getBet(int betId) {
        return getById(betId).convertToDto();
    }

    private @Nonnull Bet getById(int betId) {
        for(Bet bet : bets)
            if(bet.getId() == betId)
                return bet;
        throw new UnknownBetIdException("unknown bet Id : " + betId);
    }
}
