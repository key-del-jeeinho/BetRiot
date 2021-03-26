package com.xylope.betriot.layer.dataaccess.apis.riot;

import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Team;
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;

public interface MatchAPI {
    Match getByMatchId(long matchId);
    MatchHistory getMatchListBySummoner(String summonerId);
    CurrentMatch getCurrentMatch(String summonerId);
    boolean isCurrentMatchExist(String summonerId);

    boolean isSummonerWin(Match match, String summonerId);
    boolean isSummonerInTeam(Team team, String summonerId);
}
