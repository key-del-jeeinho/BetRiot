package com.xylope.betriot.layer.dataaccess;

import com.xylope.betriot.data.riotdata.SummonerDto;
import java.util.List;

public interface SummonerDao extends RiotDao {
    SummonerDto getByName(String name);
    SummonerDto getById(String id);
    SummonerDto getByPuuid(String puuid);
    SummonerDto getByAccountId(String accountId);
    List<SummonerDto> getByMatch(String matchId);
}
