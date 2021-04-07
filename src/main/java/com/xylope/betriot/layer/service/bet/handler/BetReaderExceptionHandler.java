package com.xylope.betriot.layer.service.bet.handler;
import com.xylope.betriot.layer.service.bet.BetService;
import com.xylope.betriot.layer.service.bet.reader.Action;
import com.xylope.betriot.layer.service.bet.reader.BetReader;

public abstract class BetReaderExceptionHandler<T> {
    protected void read(BetReader<T> reader, BetService service, Action action, T input) {
        reader.read(service, action, input);
    }

    public abstract void handling(BetReader<T> reader, BetService service, Action action, T input);
}
