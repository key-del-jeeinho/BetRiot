package com.xylope.betriot.data.riotdata;

import lombok.Data;

import java.util.List;

@Data
public class ParticipantDto {
    private int participantId;
    private int championId;
    private List<RuneDto> runes;
    private ParticipantStatsDto stats;
    private int teamId;
    private ParticipantTimelineDto timeline;
    private int spell1Id;
    private int spell2Id;
    private String highestAchievedSeasonTier; //(Legal values: CHALLENGER, MASTER, DIAMOND, PLATINUM, GOLD, SILVER, BRONZE, UNRANKED)
    private List<MasteryDto> masteries;
}
