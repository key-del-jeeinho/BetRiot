package com.xylope.betriot.layer.dataaccess;

public interface RiotAPI {
    String riotRootUrl = "https://kr.api.riotgames.com";
    String dataDragonRootUrl = "https://ddragon.leagueoflegends.com";
    String getRiotApiKey();
    void setRiotApiKey(String riotApiKey);
}
