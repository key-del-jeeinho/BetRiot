package com.xylope.betriot.data.riotdata;

import lombok.Data;

import java.util.List;

@Data
public class CurrentGameParticipant {
    private long championId;
    private Perks perks;
    private long profileIconId;
    private  boolean bot;
    private long teamId;
    private String summonerName;
    private  String summonerId;
    private long spell1Id;
    private long spell2Id;
    private List<GameCustomizationObject> gameCustomizationObjects;
}
