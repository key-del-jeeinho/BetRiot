package com.xylope.betriot.layer.dataaccess.riotdata;

import lombok.Data;

@Data
public class BannedChampion {
    private int pickTurn;
    private long championId;
    private long teamId;
}
