package com.xylope.betriot.layer.service.bet.handler;

import com.xylope.betriot.layer.service.bet.BetService;
import com.xylope.betriot.layer.service.bet.reader.Action;
import com.xylope.betriot.layer.service.bet.reader.BetReader;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class DiscordBetReaderExceptionHandler extends BetReaderExceptionHandler<String[]>{
    @Override
    public void handling(BetReader<String[]> reader, BetService service, Action action, String... input) {

    }

    public abstract void setTextChannel(TextChannel textChannel);
}
