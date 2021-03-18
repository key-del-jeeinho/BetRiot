package com.xylope.betriot.layer.dataaccess.apis;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;

public class MatchAPIOriana implements MatchAPI{

    @Override
    public Match getByMatchId(long matchId) {
        return Orianna.matchWithId(matchId).get();
    }

    @Override
    public MatchHistory getMatchListBySummoner(String summonerId) {
        return Orianna.matchHistoryForSummoner(Orianna.summonerWithId(summonerId).get()).get();
    }
}
