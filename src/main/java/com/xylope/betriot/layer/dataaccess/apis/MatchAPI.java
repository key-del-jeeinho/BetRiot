package com.xylope.betriot.layer.dataaccess.apis;

import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;

public interface MatchAPI {
    Match getByMatchId(long matchId);
    MatchHistory getMatchListBySummoner(String summonerId);
}
