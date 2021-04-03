package com.xylope.betriot.layer.service.command.custom.bet;

import com.xylope.betriot.layer.service.bet_v2.BetService;
import com.xylope.betriot.layer.service.bet_v2.handler.DiscordBetReaderExceptionHandler;
import com.xylope.betriot.layer.service.bet_v2.reader.Action;
import com.xylope.betriot.layer.service.bet_v2.reader.BetReader;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BettingParticipationCommand extends LeafCommand {
    public BettingParticipationCommand(BetService service, BetReader<String[]> reader, DiscordBetReaderExceptionHandler handler) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                TextChannel textChannel = channel.getGuild().getTextChannelById(channel.getId());
                handler.setTextChannel(textChannel);
                handler.handling(reader, service, Action.BETTING_PARTICIPATION,
                        sender.getId(), args[0], args[1], args[2]);
            }
        });
    }
}

