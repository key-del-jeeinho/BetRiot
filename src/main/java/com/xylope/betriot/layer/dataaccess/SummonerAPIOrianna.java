package com.xylope.betriot.layer.dataaccess;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SummonerAPIOrianna implements SummonerAPI{
    @Getter @Setter
    private String riotApiKey;
    @Setter
    private SummonerAPI spareAPI;

    @Override
    public SummonerDto getByName(String name) {
        return convertToStandardSummoner(Orianna.summonerNamed(name).get());
    }

    @Override
    public SummonerDto getById(String id) {
        return convertToStandardSummoner(Orianna.summonerWithId(id).get());
    }

    @Override
    public SummonerDto getByPuuid(String puuid) {
        return convertToStandardSummoner(Orianna.summonerWithPuuid(puuid).get());
    }

    @Override
    public SummonerDto getByAccountId(String accountId) {
        return convertToStandardSummoner(Orianna.summonerWithAccountId(accountId).get());
    }

    @Override
    public List<SummonerDto> getByMatch(String matchId) {
        return spareAPI.getByMatch(matchId);
    }

    private SummonerDto convertToStandardSummoner(Summoner summoner) {
        return SummonerDto.builder()
                .summonerLevel(summoner.getLevel())
                .profileIconId(summoner.getProfileIcon().getId())
                .id(summoner.getId())
                .accountId(summoner.getAccountId())
                .name(summoner.getName())
                .puuid(summoner.getPuuid())
                .build();
    }
}
