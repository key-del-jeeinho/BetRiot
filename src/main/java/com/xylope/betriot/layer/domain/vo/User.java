package com.xylope.betriot.layer.domain.vo;

import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class User {
    @Getter @NonNull
    private final long discordId;
    @Getter @Setter
    private String riotId;
    @Getter @Setter
    private int money;
}
