package com.xylope.betriot.layer.dataaccess.riotdata;

import lombok.Data;

@Data
public class PlayerDto {
    private int profileIcon;
    private String accountId;
    private String matchHistoryUri;
    private String currentAccountId;
    private String currentPlatformId;
    private String summonerName;
    private String summonerId;
    private String platformId;
}
