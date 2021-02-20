package com.xylope.betriot.layer.dataaccess.riotdata;

import java.util.List;

public class FeaturedGameInfo {
    String gameMode;
    long gameLength;
    long mapId;
    String gameType;
    List<BannedChampion> bannedChampions;
    long gameId;
    Observer observers;
    long gameQueueConfigId;
    long gameStartTime;
    List<Participant> participants;
    String platformId;
}
