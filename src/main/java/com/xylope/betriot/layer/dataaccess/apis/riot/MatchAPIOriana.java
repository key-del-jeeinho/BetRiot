package com.xylope.betriot.layer.dataaccess.apis.riot;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.match.Match;
import com.merakianalytics.orianna.types.core.match.MatchHistory;
import com.merakianalytics.orianna.types.core.match.Participant;
import com.merakianalytics.orianna.types.core.match.Team;
import com.merakianalytics.orianna.types.core.spectator.CurrentMatch;
import com.merakianalytics.orianna.types.core.summoner.Summoner;

public class MatchAPIOriana implements MatchAPI{

    @Override
    public Match getByMatchId(long matchId) {
        return Orianna.matchWithId(matchId).get();
    }

    @Override
    public MatchHistory getMatchListBySummoner(String summonerId) {
        return Orianna.matchHistoryForSummoner(Orianna.summonerWithId(summonerId).get()).get();
    }

    @Override
    public CurrentMatch getCurrentMatch(String summonerId) {
        Summoner summoner = Orianna.summonerWithId(summonerId).get();
        return Orianna.currentMatchForSummoner(summoner).get();
    }

    @Override
    public boolean isCurrentMatchExist(String summonerId) {
        return getCurrentMatch(summonerId).exists();
    }

    @Override
    public boolean isSummonerWin(Match match, String summonerId) {
        boolean isSummonerWin = false;
        Team red = match.getRedTeam();
        if(red.isWinner()) { //if red win
            //어짜피 레드팀 내에 퍼블리셔가 존재하기만 한다면 퍼블리셔는 이긴다. (레드팀이 이겼다는것을 이미 입증하였으므로)
            return isSummonerInTeam(red, summonerId);
        } else { //or blue win
            //어짜피 블루팀 내에 퍼블리셔가 존재하기만 한다면 퍼블리셔는 이긴다. (블루팀이 이겼다는것을 이미 입증하였으므로)
            return isSummonerInTeam(match.getBlueTeam(), summonerId);
        }
    }

    @Override
    public boolean isSummonerInTeam(Team team, String summonerId) {
        for(Participant participant : team.getParticipants()) {
            if(participant.getSummoner().getId().equals(summonerId))
                return true;
        }
        return false;
    }
}
