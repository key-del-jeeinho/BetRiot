package com.xylope.betriot.layer.dataaccess.riotdata;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchDto {
    private long gameId;
    private List<ParticipantIdentityDto> participantIdentities;
    private int queueId;
    private String gameType;
    private long gameDuration;
    private List<TeamStatsDto> teams;
    private String platformId;
    private long gameCreation;
    private int seasonId;
    private String gameVersion;
    private int mapId;
    private String gameMode;
    private List<ParticipantDto> participants;
}
