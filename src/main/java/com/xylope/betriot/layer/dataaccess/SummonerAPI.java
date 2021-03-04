package com.xylope.betriot.layer.dataaccess;

import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import java.util.List;

public interface SummonerAPI extends RiotAPI {
    void setRiotApiKey(String riotKey);
    SummonerDto getByName(String name) ;
    SummonerDto getById(String id);
    SummonerDto getByPuuid(String puuid);
    SummonerDto getByAccountId(String accountId);
    List<SummonerDto> getByMatch(String matchId);
}
