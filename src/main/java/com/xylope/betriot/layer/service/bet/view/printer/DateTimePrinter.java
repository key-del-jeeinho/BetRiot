package com.xylope.betriot.layer.service.bet.view.printer;

import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@AllArgsConstructor
public class DateTimePrinter implements Printer{
    private final String timeFormat;

    @Override
    public String print(String s) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat (timeFormat);
        String print = "[BETRIOT] [" + format.format(timestamp) + "] | " + s;
        System.out.println(print);
        return print;
    }
}
