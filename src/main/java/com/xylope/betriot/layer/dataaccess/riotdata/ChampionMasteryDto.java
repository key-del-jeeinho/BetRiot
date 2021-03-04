package com.xylope.betriot.layer.dataaccess.riotdata;

import lombok.*;

@Builder @ToString @AllArgsConstructor
public class ChampionMasteryDto {
    @Getter @Setter
    long ChampionPointsUtilNextLevel;
    @Getter @Setter
    boolean chestGranted;
    @Getter @Setter
    long championId;
    @Getter @Setter
    long lastPlayTime;
    @Getter @Setter
    int championLevel;
    @Getter @Setter
    String summonerId;
    @Getter @Setter
    int championPoints;
    @Getter @Setter
    long championPointsSinceLastLevel;
    @Getter @Setter
    int tokensEarned;
}
