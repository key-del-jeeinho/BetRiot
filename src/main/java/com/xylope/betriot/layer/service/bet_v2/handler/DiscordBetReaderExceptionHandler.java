package com.xylope.betriot.layer.service.bet_v2.handler;

import com.xylope.betriot.layer.service.bet_v2.reader.BetReader;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.TextChannel;

@AllArgsConstructor
public class DiscordBetReaderExceptionHandler extends BetReaderExceptionHandler<String[]> {
    private final TextChannel textChannel;

    @Override
    void handling(BetReader<String[]> reader, String[] input) {
        try {
            super.read(reader, input);
        } catch (Exception e) {
            //TODO catch 구현 | 2021.03.29 | Xylope
        }
    }
}
