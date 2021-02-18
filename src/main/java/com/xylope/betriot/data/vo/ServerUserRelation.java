package com.xylope.betriot.data.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ServerUserRelation {
    @Getter
    private final int serverId;
    @Getter
    private final int userId;
}
