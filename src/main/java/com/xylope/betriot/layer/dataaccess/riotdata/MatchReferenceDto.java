package com.xylope.betriot.layer.dataaccess.riotdata;

import lombok.Data;

@Data
public class MatchReferenceDto {
    private long gameId;
    private String role;
    private int season;
    private String platformId;
    private int champion;
    private int queue;
    private String lane;
    private long timestamp;
}
