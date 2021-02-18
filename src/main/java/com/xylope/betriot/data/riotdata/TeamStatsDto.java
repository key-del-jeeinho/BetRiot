package com.xylope.betriot.data.riotdata;

import lombok.Data;

import java.util.List;

@Data
public class TeamStatsDto {
    private int towerKills;
    private int riftHeraldKills;
    private boolean firstBlood;
    private int inhibitorKills;
    private List<TeamBansDto> bans;
    private boolean firstBaron;
    private boolean firstDragon;
    private int dominionVictoryScore;
    private int dragonKills;
    private int baronKills;
    private boolean firstInhibitor;
    private boolean firstTower;
    private int vilemawKills;
    private boolean firstRiftHerald;
    private int teamId;
    private String win;
}
