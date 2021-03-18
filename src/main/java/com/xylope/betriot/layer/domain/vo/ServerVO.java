package com.xylope.betriot.layer.domain.vo;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class ServerVO {
    @Getter @NonNull
    private final int id;
    @Getter @NonNull
    private final long serverId;
    @Getter @Setter @NonNull
    private long noticeChannelId;
    @Getter @Setter
    private int premiumLevel;

    @AllArgsConstructor
    public enum Premium_Level {
        BASIC(0), BASIC_PLUS(1), SPECIAL(2);

        @Getter
        int id;
    }
}
