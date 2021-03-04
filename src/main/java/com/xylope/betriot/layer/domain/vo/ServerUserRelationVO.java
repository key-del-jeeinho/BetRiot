package com.xylope.betriot.layer.domain.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ServerUserRelationVO {
    @Getter
    private final int serverId;
    @Getter
    private final int userId;
}
