package com.xylope.betriot.layer.dataaccess;

public interface RiotDao {
    String rootUrl = "https://kr.api.riotgames.com//";
    String getRiotApiKey();
    void setRiotApiKey(String riotApiKey);
}
