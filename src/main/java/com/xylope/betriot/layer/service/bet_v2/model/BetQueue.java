package com.xylope.betriot.layer.service.bet_v2.model;

import com.xylope.betriot.exception.bet.UnknownBetIdException;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet_v2.BetView;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BetQueue {
    private final List<Bet> bets;

    public BetQueue() {
        bets = new ArrayList<>();
    }

    public void addBet(Bet bet) {
        bets.add(bet);
    }


    public void addParticipant(int betId, UserVO user, WinAndLose wal) {
        getById(betId).addParticipant(user, wal);
    }

    public Set<UserVO> getParticipants(int betId) {
        return getById(betId).getParticipants().keySet();
    }

    public Set<UserVO> getParticipants(int betId, WinAndLose sortMethod) {
        return getById(betId).getParticipants(sortMethod);
    }

    private @Nonnull Bet getById(int betId) {
        for(Bet bet : bets)
            if(bet.getId() == betId)
                return bet;
        throw new UnknownBetIdException("unknown bet Id : " + betId);
    }

    public Bet remove(int betId) {
        Bet bet = getById(betId);
        bet.close();

        return bet;
    }
}
