package com.xylope.betriot.layer.logic.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
public class ServerUserRelation {
    @Getter
    private final int serverId;
    @Getter
    private final int userId;
}
