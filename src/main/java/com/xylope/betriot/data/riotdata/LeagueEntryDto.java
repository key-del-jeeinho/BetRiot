package com.xylope.betriot.data.riotdata;

import lombok.Data;

@Data
public class LeagueEntryDto {
    private String leagueId;
    private String summonerId;
    private String summonerName;
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private boolean hotStreak;
    private boolean freshBlood;
    private boolean inactive;
    private MiniSeriesDto miniSirise;
}
