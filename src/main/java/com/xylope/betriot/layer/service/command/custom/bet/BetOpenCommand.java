package com.xylope.betriot.layer.service.command.custom.bet;

import com.xylope.betriot.layer.service.bet_v2.BetService;
import com.xylope.betriot.layer.service.bet_v2.handler.DiscordBetReaderExceptionHandler;
import com.xylope.betriot.layer.service.bet_v2.reader.Action;
import com.xylope.betriot.layer.service.bet_v2.reader.BetReader;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import net.dv8tion.jda.api.entities.*;

public class BetOpenCommand extends LeafCommand {
    public BetOpenCommand(BetService service, BetReader<String[]> reader, DiscordBetReaderExceptionHandler handler) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                TextChannel textChannel = channel.getGuild().getTextChannelById(channel.getId());
                handler.setTextChannel(textChannel);
                handler.handling(reader, service, Action.BET_OPEN, sender.getId());
            }
        });
    }
}
