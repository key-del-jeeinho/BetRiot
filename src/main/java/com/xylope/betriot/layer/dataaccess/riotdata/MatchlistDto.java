package com.xylope.betriot.layer.dataaccess.riotdata;

import lombok.Data;

import java.util.List;

@Data
public class MatchlistDto {
    private int startIndex;
    private int totalGames;
    private int endIndex;
    private List<MatchReferenceDto> matches;
}
