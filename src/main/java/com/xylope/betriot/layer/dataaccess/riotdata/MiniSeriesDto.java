package com.xylope.betriot.layer.dataaccess.riotdata;

import lombok.Data;

@Data
public class MiniSeriesDto {
    private int losses;
    private String progress;
    private int target;
    private int wins;
}
