package com.xylope.betriot.layer.dataaccess;

import com.xylope.betriot.data.riotdata.SummonerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SummonerV4SummonerDao implements SummonerDao{
    @Setter @Getter
    private String riotApiKey;
    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getByName(String name) {
        return null;
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getById(String id) {
        return null;
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getByPuuid(String puuid) {
        return null;
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getByAccountId(String accountId) {
        return RiotAPITemplate.getData("https://kr.api.riotgames.com//lol/summoner/v4/summoners/by-account/" + accountId + "?api_key=" + riotApiKey,
                k-> SummonerDto.builder()
                .profileIconId(k.get("profileIconId").getAsInt())
                .name(k.get("name").getAsString())
                .puuid(k.get("puuid").getAsString())
                .summonerLevel(k.get("summonerLevel").getAsLong())
                .revisionDate(k.get("revisionDate").getAsLong())
                .id(k.get("id").getAsString())
                .accountId(k.get("accountId").getAsString()).build());
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public List<SummonerDto> getByMatch(String matchId해당변수이름수정바람) {
        return null;
    }
}
