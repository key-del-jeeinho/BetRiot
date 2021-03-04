package com.xylope.betriot.layer.dataaccess;

import com.xylope.betriot.layer.dataaccess.riotdata.ChampionMasteryDto;

import java.util.List;

public interface ChampionMasteryAPI {
    List<ChampionMasteryDto> getChampionMasteryListById(String summonerId);
    ChampionMasteryDto getChampionMasteryTopById(String summonerId);
    ChampionMasteryDto getChampionMasteryById(String summonerId, long championId);
    int getChampionMasterySum(String summonerId);
}
