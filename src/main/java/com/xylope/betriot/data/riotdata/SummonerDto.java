package com.xylope.betriot.data.riotdata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public class SummonerDto {
    @Getter @Setter
    private String accountId;
    @Getter @Setter
    private int profileIconId;
    @Getter @Setter
    private long revisionDate;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String id;
    @Getter @Setter
    private String puuid;
    @Getter @Setter
    private long summonerLevel;
}
