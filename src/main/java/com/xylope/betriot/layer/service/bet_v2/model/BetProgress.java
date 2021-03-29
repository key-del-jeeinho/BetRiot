package com.xylope.betriot.layer.service.bet_v2.model;

import com.xylope.betriot.exception.bet.WrongBetProgressException;

public enum BetProgress {
    UN_ACTIVE, BET_RESERVE, MATCH_START, BET_START, BET_PARTICIPATION_OPEN, BET_PARTICIPATION_CLOSE, MATCH_END, BET_GIVE_REWARD, BET_END;

    public BetProgress nextStep() {
        BetProgress[] values = values();
        int ordinal = ordinal();
        if(values.length <= ordinal)
            throw new WrongBetProgressException("progress %s is last progress");
        return values[ordinal+1];
    }
}
