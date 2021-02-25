package com.xylope.betriot.layer.domain.vo;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Server {
    @Getter @NonNull
    private final int id;
    @Getter @NonNull
    private final String serverId;
    @Getter @Setter
    private int premiumLevel;
}
