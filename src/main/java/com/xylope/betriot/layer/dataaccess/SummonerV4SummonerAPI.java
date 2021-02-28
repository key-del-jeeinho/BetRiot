package com.xylope.betriot.layer.dataaccess;

import com.google.gson.JsonObject;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class SummonerV4SummonerAPI implements SummonerAPI {
    @Setter
    private SummonerAPICallback callback;
    @Setter @Getter //Override
    private String riotApiKey;

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getByName(String name) {
        return RiotAPITemplate.getData( RiotAPI.riotRootUrl + "/lol/summoner/v4/summoners/by-name/" + name + "?api_key=" + riotApiKey, callback);
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getById(String id) {
        return RiotAPITemplate.getData( RiotAPI.riotRootUrl + "/lol/summoner/v4/summoners/" + id + "?api_key=" + riotApiKey, callback);
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getByPuuid(String puuid) {
        return RiotAPITemplate.getData( RiotAPI.riotRootUrl + "/lol/summoner/v4/summoners/by-puuid/" + puuid + "?api_key=" + riotApiKey, callback);
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getByAccountId(String accountId) {
        return RiotAPITemplate.getData( RiotAPI.riotRootUrl + "/lol/summoner/v4/summoners/by-account/" + accountId + "?api_key=" + riotApiKey, callback);
    }


    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public List<SummonerDto> getByMatch(String matchId) {
        return null;
    }

    static class SummonerAPICallback implements RiotAPICallback.JsonObjectCallback<SummonerDto> {
        @Override
        public SummonerDto getRiotDataObject(JsonObject k) {
            return SummonerDto.builder()
                    .profileIconId(k.get("profileIconId").getAsInt())
                    .name(k.get("name").getAsString())
                    .puuid(k.get("puuid").getAsString())
                    .summonerLevel(k.get("summonerLevel").getAsLong())
                    .revisionDate(k.get("revisionDate").getAsLong())
                    .id(k.get("id").getAsString())
                    .accountId(k.get("accountId").getAsString()).build();
        }
    }
}
