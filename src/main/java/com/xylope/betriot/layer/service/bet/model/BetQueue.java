package com.xylope.betriot.layer.service.bet.model;

import com.xylope.betriot.exception.AlreadyInitializeValueException;
import com.xylope.betriot.exception.bet.UnknownBetIdException;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BetQueue {
    private final List<Bet> bets;

    public BetQueue() {
        bets = new CopyOnWriteArrayList<>();
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

        return betExist.get();
    }

    public boolean isParticipationExist(int betId, long discordId) {
        AtomicBoolean isParticipationExist = new AtomicBoolean(false);
        BetDto bet = getBet(betId);

        bet.getParticipants().forEach(
                (user, betWhere) -> {
                    if(user.getUser().getDiscordId() == discordId)
                        isParticipationExist.set(true);
                }
        );

        return isParticipationExist.get();
    }

    public void remove(int betId) {
        Bet bet = getById(betId);
        bet.close();
    }

    public List<BetDto> getBetList() {
        return getBetList(bet -> true);
    }

    public List<BetDto> getBetList(Checker checker) {
        List<BetDto> result = new ArrayList<>();
        for (Bet bet : bets) {
            if(checker.check(bet)) {
                result.add(bet.convertToDto());
            }
        }

        return result;
    }

    public void removeCloseBets() {
        bets.forEach((bet -> {
            if(bet.getId() == -1) {
                bets.remove(bet);
            }
        }));
    }

    public void cancelBet(int betId) {
        getById(betId).close();
    }

    @FunctionalInterface
    interface Checker {
        boolean check(Bet bet);
    }
}
