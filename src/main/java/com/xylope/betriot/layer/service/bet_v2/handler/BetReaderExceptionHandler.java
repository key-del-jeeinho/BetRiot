package com.xylope.betriot.layer.service.bet_v2.handler;

import com.xylope.betriot.layer.service.bet_v2.reader.BetReader;

public abstract class BetReaderExceptionHandler<T> {
    protected void read(BetReader<T> reader, T input) {
        reader.read(input);
    }

    abstract void handling(BetReader<T> reader, T input);
}
