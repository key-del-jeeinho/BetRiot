package com.xylope.betriot.layer.dataaccess.apis.riot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xylope.betriot.exception.ZeroChampionMasteryPointException;
import com.xylope.betriot.layer.dataaccess.RiotAPI;
import com.xylope.betriot.layer.dataaccess.RiotAPICallback;
import com.xylope.betriot.layer.dataaccess.RiotAPITemplate;
import com.xylope.betriot.layer.dataaccess.riotdata.ChampionMasteryDto;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ChampionMasteryAPIImpl implements ChampionMasteryAPI{
    @Setter
    private String riotApiKey;
    private final ChampionMasteryCallback championMasteryCallback = new ChampionMasteryCallback();

    @Override
    public List<ChampionMasteryDto> getChampionMasteryListById(String summonerId) {
         return RiotAPITemplate.getData( RiotAPI.RIOT_ROOT_URL + "/lol/champion-mastery/v4/champion-masteries/by-summoner/" + summonerId + "?api_key=" + riotApiKey, (JsonArray k) -> {
             List<ChampionMasteryDto> championMasteryDtoList = new ArrayList<>();
             for(JsonElement o : k) {
                 ChampionMasteryDto dto = championMasteryCallback.getRiotDataObject(o.getAsJsonObject());
                 championMasteryDtoList.add(dto);
             }

             return championMasteryDtoList;
         } );
    }

    @Override
    public ChampionMasteryDto getChampionMasteryTopById(String summonerId) {
        List<ChampionMasteryDto> list =  getChampionMasteryListById(summonerId);
        if(list.size() != 0)
            return list.get(0);
        throw new ZeroChampionMasteryPointException("champion isn't have any mastery point");
    }

    @Override
    public ChampionMasteryDto getChampionMasteryById(String summonerId, long championId) {
        return RiotAPITemplate.getData(RiotAPI.RIOT_ROOT_URL + "/lol/champion-mastery/v4/champion-masteries/by-summoner/" + summonerId + "/by-champion/" + championId + "?api_key=" + riotApiKey, championMasteryCallback);
    }

    @Override
    public int getChampionMasterySum(String summonerId) {
        return RiotAPITemplate.getData(RiotAPI.RIOT_ROOT_URL + "/lol/champion-mastery/v4/scores/by-summoner/" + summonerId, (RiotAPICallback.StringCallback<Integer>) Integer::parseInt);
    }

    private static class ChampionMasteryCallback implements RiotAPICallback.JsonObjectCallback<ChampionMasteryDto>{
        @Override
        public ChampionMasteryDto getRiotDataObject(JsonObject k) {
            JsonObject obj = k.getAsJsonObject();
            return new ChampionMasteryDto(
                    obj.get("championPointsUntilNextLevel").getAsLong(),
                    obj.get("chestGranted").getAsBoolean(),
                    obj.get("championId").getAsLong(),
                    obj.get("lastPlayTime").getAsLong(),
                    obj.get("championLevel").getAsInt(),
                    obj.get("summonerId").getAsString(),
                    obj.get("championPoints").getAsInt(),
                    obj.get("championPointsSinceLastLevel").getAsLong(),
                    obj.get("tokensEarned").getAsInt()
            );
        }
    }
}
