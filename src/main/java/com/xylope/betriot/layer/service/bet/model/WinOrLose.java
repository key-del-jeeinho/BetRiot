package com.xylope.betriot.layer.service.bet.model;

import com.xylope.betriot.exception.MatchConditionNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WinOrLose {
    LOSE("패배", 0), WIN("승리", 1);

    @Getter
    private final String displayStatus;
    @Getter
    private final int id;

    public static WinOrLose getByDisplayStatus(String displayStatus) {
        return getByCondition(value -> value.displayStatus.equals(displayStatus));
    }

    public static WinOrLose getById(int id) {
        return getByCondition(value -> value.id == id);
    }

    public static WinOrLose getByCondition(Checker checker) {
        for(WinOrLose value : values()) {
            if(checker.check(value)) {
                return value;
            }
        }

        throw new MatchConditionNotFoundException("Could not find WinOrLose matching condition");
    }

    @FunctionalInterface
    private interface Checker {
        boolean check(WinOrLose value);
    }
}
