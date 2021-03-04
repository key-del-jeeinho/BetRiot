package com.xylope.betriot.layer.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SpecialEmote {
    TERMS_AGREE("✅"), TERMS_DISAGREE("❎"), RIOT_CHANGE_ICON_DONE("✅");

    @Getter
    private final String emote;
}
