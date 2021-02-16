package com.xylope.betriot.data.vo;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Getter @NonNull
    private final int id;
    @Getter @NonNull
    private final String discordId;
    @Getter @Setter
    private String riotId;
    @Getter @Setter
    private int money;
}
