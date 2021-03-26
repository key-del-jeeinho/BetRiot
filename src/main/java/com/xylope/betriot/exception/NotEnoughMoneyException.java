package com.xylope.betriot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NotEnoughMoneyException extends RuntimeException {
    @Getter
    private final int possessionMoney;
    @Getter
    private final int requiredMoney;
}