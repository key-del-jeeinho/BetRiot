package com.xylope.betriot.layer.service.bet_v2.model;

import com.xylope.betriot.exception.bet.WrongDisplayStatusException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WinOrLose {
    WIN("승리"), LOSE("패배");

    @Getter
    private String displayStatus;

    public static WinOrLose getByDisplayStatus(String displayStatus) {
        for (WinOrLose value : values()) {
            if (value.displayStatus.equals(displayStatus)) {
                return value;
            }
        }

        throw new WrongDisplayStatusException("unknown display status : " + displayStatus);
    }
}
