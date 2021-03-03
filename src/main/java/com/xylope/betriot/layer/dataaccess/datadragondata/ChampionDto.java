package com.xylope.betriot.layer.dataaccess.datadragondata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder @ToString
public class ChampionDto {
    @Getter @Setter
    String version;
    @Getter @Setter
    String id;
    @Getter @Setter
    int key;
    @Getter @Setter
    String name;
    @Getter @Setter
    String title;
    @Getter @Setter
    String blurb;
    @Getter @Setter
    ChampionImage image;
    @Getter @Setter
    ChampionInfo info;
    @Getter @Setter
    ChampionTag tag;
}
