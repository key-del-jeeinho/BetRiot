package com.xylope.betriot.data.vo;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
public class Server {
    @Getter @NonNull
    private final int id;
    @Getter @NonNull
    private final String serverId;
    @Getter @Setter
    private int premiumLevel;
}
